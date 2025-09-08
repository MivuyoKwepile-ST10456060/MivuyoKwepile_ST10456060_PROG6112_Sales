import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

class InventoryTest {

    private Inventory inventory;

    @BeforeEach
    void setUp() {
        inventory = new Inventory();
    }

    @Test
    void testAddProduct() {
        System.out.println("AddProduct - Test");

        Product product = new Product("P001", "Test Product", 10.99, 50);
        inventory.addProduct(product);

        Product result = inventory.getProduct("P001");
        assertNotNull(result);
        assertEquals("Test Product", result.getName());
        assertEquals(10.99, result.getPrice(), 0.001);
        assertEquals(50, result.getQuantity());
    }

    @Test
    void testAddPerishableProduct() {
        System.out.println("AddPerishableProduct - Test");

        String expiryDate = LocalDate.now().plusDays(30).format(DateTimeFormatter.ISO_DATE);
        PerishableProduct product = new PerishableProduct("P002", "Perishable Product", 15.99, 25, expiryDate);
        inventory.addProduct(product);

        Product result = inventory.getProduct("P002");
        assertNotNull(result);
        Assertions.assertEquals("Perishable Product", result.getName());
        assertInstanceOf(PerishableProduct.class, result);

        PerishableProduct perishable = (PerishableProduct) result;
        Assertions.assertEquals(expiryDate, perishable.getExpiryDate().format(DateTimeFormatter.ISO_DATE));
    }

    @Test
    void testProcessSale() {
        System.out.println("ProcessSale - Test");

        Product product = new Product("P003", "Sale Product", 5.99, 100);
        inventory.addProduct(product);

        inventory.processSale("P003", 10);

        Product result = inventory.getProduct("P003");
        Assertions.assertNotNull(result);
        Assertions.assertEquals(90, result.getQuantity()); // 100 - 10 = 90
    }

    @Test
    void testProcessSaleInsufficientStock() {
        System.out.println("ProcessSale - InsufficientStock - Test");

        Product product = new Product("P004", "Low Stock Product", 7.99, 5);
        inventory.addProduct(product);

        Assertions.assertThrows(IllegalArgumentException.class, () -> inventory.processSale("P004", 10));
    }

    @Test
    void testProcessSaleProductNotFound() {
        System.out.println("ProcessSale - ProductNotFound - Test");

        Assertions.assertThrows(IllegalArgumentException.class, () -> inventory.processSale("NONEXISTENT", 1));
    }

    @Test
    void testUpdateProductPrice() {
        System.out.println("UpdateProductPrice - Test");

        Product product = new Product("P005", "Price Update Product", 20.00, 15);
        inventory.addProduct(product);

        product.updatePrice(25.50);

        Product result = inventory.getProduct("P005");
        Assertions.assertNotNull(result);
        Assertions.assertEquals(25.50, result.getPrice(), 0.001);
    }

    @Test
    void testGenerateReport() {
        System.out.println("GenerateReport - Test");

        Product product1 = new Product("P006", "Report Product 1", 10.00, 20);
        Product product2 = new Product("P007", "Report Product 2", 15.00, 10);
        inventory.addProduct(product1);
        inventory.addProduct(product2);

        inventory.processSale("P006", 5);
        inventory.processSale("P007", 3);

        String report = inventory.generateReport();
        Assertions.assertNotNull(report);
        Assertions.assertTrue(report.contains("INVENTORY REPORT"));
        Assertions.assertTrue(report.contains("SALES SUMMARY"));
        Assertions.assertTrue(report.contains("TOTAL SALES"));
    }

    @Test
    void testGetTotalSales() {
        System.out.println("GetTotalSales - Test");

        Product product = new Product("P008", "Sales Product", 10.00, 50);
        inventory.addProduct(product);

        inventory.processSale("P008", 5); // 5 * 10.00 = 50.00
        inventory.processSale("P008", 3); // 3 * 10.00 = 30.00

        double totalSales = inventory.getTotalSales();
        Assertions.assertEquals(80.00, totalSales, 0.001); // 50 + 30 = 80
    }

    @Test
    void testPerishableProductExpiry() {
        System.out.println("PerishableProductExpiry - Test");

        String pastDate = LocalDate.now().minusDays(1).format(DateTimeFormatter.ISO_DATE);
        String futureDate = LocalDate.now().plusDays(1).format(DateTimeFormatter.ISO_DATE);

        PerishableProduct expired = new PerishableProduct("P010", "Expired Product", 10.00, 10, pastDate);
        PerishableProduct notExpired = new PerishableProduct("P011", "Fresh Product", 15.00, 10, futureDate);

        Assertions.assertTrue(expired.isExpired());
        Assertions.assertFalse(notExpired.isExpired());
    }
}