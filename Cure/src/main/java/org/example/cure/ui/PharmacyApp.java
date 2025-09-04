package org.example.cure.ui;



import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.print.PrinterJob;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.example.cure.model.*;
import org.example.cure.service.Order;
import org.example.cure.service.Payment;


import java.io.InputStream;
import java.util.*;

public class PharmacyApp extends Application {
    private final Order order = new Order("John Doe");
    private final TableView<PharmacyItem> tableView = new TableView<>();
    private final Label statusLabel = new Label();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        BorderPane root = new BorderPane();
        VBox centerBox = new VBox(10, tableView);
        centerBox.setPadding(new Insets(10));

        TableColumn<PharmacyItem, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getId()).asObject());

        TableColumn<PharmacyItem, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getName()));

        TableColumn<PharmacyItem, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getClass().getSimpleName()));

        TableColumn<PharmacyItem, Double> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(data -> new javafx.beans.property.SimpleDoubleProperty(data.getValue().getPrice()).asObject());

        tableView.getColumns().addAll(idCol, nameCol, typeCol, priceCol);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);


        MenuBar menuBar = new MenuBar();
        Menu menu = new Menu("Manage");
        MenuItem addItem = new MenuItem("Add Medicine");
        MenuItem editItem = new MenuItem("Edit Selected");
        MenuItem deleteItem = new MenuItem("Delete Selected");
        MenuItem sortItem = new MenuItem("Sort by Price");
        MenuItem searchItem = new MenuItem("Search by Name");
        MenuItem printRecipe = new MenuItem("Print Recipe");
        MenuItem viewPayment = new MenuItem("View Payment");
        menu.getItems().addAll(addItem, editItem, deleteItem, sortItem, searchItem, printRecipe, viewPayment);
        menuBar.getMenus().add(menu);


        addItem.setOnAction(e -> openAddDialog());
        editItem.setOnAction(e -> editSelected());
        deleteItem.setOnAction(e -> deleteSelected());
        sortItem.setOnAction(e -> {
            order.sortItems();
            refreshTable();
        });
        searchItem.setOnAction(e -> searchByName());
        printRecipe.setOnAction(e -> printRecipe());
        viewPayment.setOnAction(e -> {
            Payment payment = new Payment(order);
            showAlert("Total Payment", "Total: $" + payment.calculateTotal());
        });

        root.setTop(menuBar);
        root.setCenter(centerBox);
        root.setBottom(statusLabel);

        refreshTable();


        root.setStyle("-fx-background-color: #f0f8ff;");
        stage.setTitle("Pharmacy Management System");
        try {
            InputStream iconStream = getClass().getResourceAsStream("/icons-pharmacy.png");
            if (iconStream == null) throw new NullPointerException("Icon not found");
            stage.getIcons().add(new Image(iconStream));
        } catch (Exception e) {
            System.out.println("Icon load error: " + e.getMessage());
        }

        stage.setScene(new Scene(root, 700, 500));
        stage.show();
    }

    private void refreshTable() {
        tableView.getItems().setAll(order.getItems());
        statusLabel.setText("Total Items: " + order.getItems().size());
    }

    private void openAddDialog() {
        Dialog<PharmacyItem> dialog = new Dialog<>();
        dialog.setTitle("Add New Item");


        ComboBox<String> typeCombo = new ComboBox<>();
        typeCombo.getItems().addAll("Medicine", "Supplement");
        typeCombo.setValue("Medicine");

        TextField idField = new TextField();
        TextField nameField = new TextField();
        TextField priceField = new TextField();
        TextField extraField = new TextField();

        Label extraLabel = new Label("Prescription (Yes/No):");


        typeCombo.setOnAction(e -> {
            if (typeCombo.getValue().equals("Medicine")) {
                extraLabel.setText("Prescription (Yes/No):");
            } else {
                extraLabel.setText("Description:");
            }
        });

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        grid.add(new Label("Type:"), 0, 0);
        grid.add(typeCombo, 1, 0);
        grid.add(new Label("ID:"), 0, 1);
        grid.add(idField, 1, 1);
        grid.add(new Label("Name:"), 0, 2);
        grid.add(nameField, 1, 2);
        grid.add(new Label("Price:"), 0, 3);
        grid.add(priceField, 1, 3);
        grid.add(extraLabel, 0, 4);
        grid.add(extraField, 1, 4);

        dialog.getDialogPane().setContent(grid);
        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                try {
                    int id = Integer.parseInt(idField.getText());
                    String name = nameField.getText();
                    double price = Double.parseDouble(priceField.getText());
                    String extra = extraField.getText();

                    PharmacyItem item;
                    if (typeCombo.getValue().equals("Medicine")) {
                        Medicine med = new Medicine(id, name, 0, extra); // prescription
                        med.setPrice(price);
                        item = med;
                    } else {
                        Supplement sup = new Supplement(id, name, 0, extra); // description
                        sup.setPrice(price);
                        item = sup;
                    }

                    order.addItem(item);
                    refreshTable();

                } catch (NumberFormatException ex) {
                    showAlert("Input Error", "ID and Price must be numeric.");
                } catch (InvalidPriceException ex) {
                    showAlert("Price Error", ex.getMessage());
                }
            }
            return null;
        });

        dialog.showAndWait();
    }


    private void editSelected() {
        PharmacyItem selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Edit Error", "No item selected.");
            return;
        }


        TextInputDialog nameDialog = new TextInputDialog(selected.getName());
        nameDialog.setTitle("Edit Name");
        nameDialog.setHeaderText("Enter new name:");
        Optional<String> nameResult = nameDialog.showAndWait();
        nameResult.ifPresent(selected::setName);


        TextInputDialog priceDialog = new TextInputDialog(String.valueOf(selected.getPrice()));
        priceDialog.setTitle("Edit Price");
        priceDialog.setHeaderText("Enter new price:");
        Optional<String> priceResult = priceDialog.showAndWait();
        priceResult.ifPresent(p -> {
            try {
                double price = Double.parseDouble(p);
                selected.setPrice(price);
            } catch (NumberFormatException ex) {
                showAlert("Input Error", "Price must be numeric.");
            } catch (InvalidPriceException ex) {
                showAlert("Invalid Price", ex.getMessage());
            }
        });


        if (selected instanceof Medicine) {
            Medicine med = (Medicine) selected;
            TextInputDialog prescDialog = new TextInputDialog(med.getPrescription());
            prescDialog.setTitle("Edit Prescription");
            prescDialog.setHeaderText("Enter prescription (Yes/No):");
            prescDialog.showAndWait().ifPresent(med::setPrescription);
        } else if (selected instanceof Supplement) {
            Supplement sup = (Supplement) selected;
            TextInputDialog descDialog = new TextInputDialog(sup.getDescription());
            descDialog.setTitle("Edit Description");
            descDialog.setHeaderText("Enter new description:");
            descDialog.showAndWait().ifPresent(sup::setDescription);
        }

        refreshTable();
    }


    private void deleteSelected() {
        PharmacyItem selected = tableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            order.removeItem(selected);
            refreshTable();
        }
    }

    private void searchByName() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Search Medicine");
        dialog.setHeaderText("Enter name to search:");
        dialog.showAndWait().ifPresent(name -> {
            PharmacyItem found = order.findItemByName(name);
            if (found != null) {
                showAlert("Search Result", found.toString());
            } else {
                showAlert("Search Result", "No item found.");
            }
        });
    }

    private void printRecipe() {
        Recipe recipe = new Recipe("John Doe", new ArrayList<>(order.getItems()));
        PrinterJob job = PrinterJob.createPrinterJob();
        if (job != null && job.showPrintDialog(tableView.getScene().getWindow())) {
            TextArea printContent = new TextArea(recipe.getDetails());
            printContent.setWrapText(true);
            boolean success = job.printPage(printContent);
            if (success) {
                job.endJob();
            }
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
