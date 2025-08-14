import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import java.time.LocalDate;

public class SalesTest {
    private Inventory inventory;
    private Product laptop;
    private PerishableProduct milk;

    @Test
    void testProductCreation() {
        Product product = new Product("P001", "Laptop", 999.99, 10);

        assertEquals("P001", product.getProductId());
        assertEquals("Laptop", product.getName());
        assertEquals(999.99, product.getPrice(), 0.001);
        assertEquals(10, product.getQuantity());
    }

    @Test
    void testInvalidProductCreation() {
        assertThrows(IllegalArgumentException.class, () ->
                new Product("", "Laptop", 999.99, 10));

        assertThrows(IllegalArgumentException.class, () ->
                new Product("P001", "", 999.99, 10));

        assertThrows(IllegalArgumentException.class, () ->
                new Product("P001", "Laptop", -1, 10));

        assertThrows(IllegalArgumentException.class, () ->
                new Product("P001", "Laptop", 999.99, -1));
    }

    @Test
    void testUpdatePrice() {
        Product product = new Product("P001", "Laptop", 999.99, 10);
        product.updatePrice(899.99);

        assertEquals(899.99, product.getPrice(), 0.001);
        assertThrows(IllegalArgumentException.class, () -> product.updatePrice(-1));
    }

    @Test
    void testAdjustQuantity() {
        Product product = new Product("P001", "Laptop", 999.99, 10);
        product.adjustQuantity(5);
        assertEquals(15, product.getQuantity());

        product.adjustQuantity(-3);
        assertEquals(12, product.getQuantity());

        assertThrows(IllegalArgumentException.class, () -> product.adjustQuantity(-20));
    }

    @Test
    void testEqualsAndHashCode() {
        Product p1 = new Product("P001", "Laptop", 999.99, 10);
        Product p2 = new Product("P001", "Laptop Pro", 1299.99, 5);
        Product p3 = new Product("P002", "Mouse", 29.99, 50);

        assertEquals(p1, p2);
        assertNotEquals(p1, p3);
        assertEquals(p1.hashCode(), p2.hashCode());
        assertNotEquals(p1.hashCode(), p3.hashCode());
    }

    @Test
    void testPerishableProductCreation() {
        PerishableProduct product = new PerishableProduct("P001", "Milk", 3.99, 50, "2023-12-31");

        assertEquals("P001", product.getProductId());
        assertEquals("Milk", product.getName());
        assertEquals(3.99, product.getPrice(), 0.001);
        assertEquals(50, product.getQuantity());
        assertEquals(LocalDate.of(2023, 12, 31), product.getExpiryDate());
    }

    @Test
    void testInvalidExpiryDate() {
        assertThrows(IllegalArgumentException.class, () ->
                new PerishableProduct("P001", "Milk", 3.99, 50, "invalid-date"));

        assertThrows(IllegalArgumentException.class, () ->
                new PerishableProduct("P001", "Milk", 3.99, 50, ""));
    }

    @Test
    void testIsExpired() {
        PerishableProduct expired = new PerishableProduct("P001", "Milk", 3.99, 50, "2000-01-01");
        PerishableProduct notExpired = new PerishableProduct("P002", "Cheese", 5.99, 30,
                LocalDate.now().plusDays(10).toString());

        assertTrue(expired.isExpired());
        assertFalse(notExpired.isExpired());
    }

    @Test
    void testInvalidSaleCreation() {
        Product product = new Product("P001", "Laptop", 999.99, 10);

        assertThrows(IllegalArgumentException.class, () -> new Sale(null, 2));
        assertThrows(IllegalArgumentException.class, () -> new Sale(product, 0));
        assertThrows(IllegalArgumentException.class, () -> new Sale(product, -1));
    }

    @Test
    void testGetReceiptLine() {
        Product product = new Product("P001", "Laptop", 999.99, 10);

        Sale sale = new Sale(product, 2);
        Sale discountedSale = new Sale(product, 10);

        assertTrue(sale.getReceiptLine().contains("2 x Laptop")); //Whatever solution I try does not work.
        assertTrue(sale.getReceiptLine().contains("999.99"));
        assertTrue(sale.getReceiptLine().contains("1999.98"));
        assertTrue(discountedSale.getReceiptLine().contains("10 x Laptop"));
        assertTrue(discountedSale.getReceiptLine().contains("(-10%)"));
        assertTrue(discountedSale.getReceiptLine().contains("8999.91"));
    }

    @BeforeEach
    void setUp() {
        inventory = new Inventory();
        laptop = new Product("P001", "Laptop", 999.99, 10);
        milk = new PerishableProduct("P002", "Milk", 3.99, 50, "2023-12-31");

        inventory.addProduct(laptop);
        inventory.addProduct(milk);
    }

    @Test
    void testGetProduct() {
        assertEquals(laptop, inventory.getProduct("P001"));
        assertEquals(milk, inventory.getProduct("P002"));
        assertNull(inventory.getProduct("P999"));
    }

    @Test
    void testProcessSale() {
        inventory.processSale("P001", 2);
        assertEquals(8, laptop.getQuantity());
        assertEquals(1, inventory.generateReport().split("TOTAL SALES").length - 1);

        inventory.processSale("P002", 5);
        assertEquals(45, milk.getQuantity());
    }

    @Test
    void testInvalidProcessSale() {
        assertThrows(IllegalArgumentException.class, () -> inventory.processSale("P999", 1));
        assertThrows(IllegalArgumentException.class, () -> inventory.processSale("P001", 0));
        assertThrows(IllegalArgumentException.class, () -> inventory.processSale("P001", -1));
        assertThrows(IllegalArgumentException.class, () -> inventory.processSale("P001", 100));
    }
}
