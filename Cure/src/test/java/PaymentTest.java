import org.example.cure.model.Medicine;
import org.example.cure.service.Order;
import org.example.cure.service.Payment;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PaymentTest {
    @Test
    public void testCalculateTotal() {
        Order order = new Order("elena");
        order.addItem(new Medicine(1, "Paracetamol", 5.0, "Yes"));
        order.addItem(new Medicine(2, "Vitamin C", 7.0, "No"));

        Payment payment = new Payment(order);
        assertEquals(12.0, payment.calculateTotal(), 0.001);
    }
}
