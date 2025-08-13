import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Sale {
    private final String productId;
    private final String productName;
    private final int quantity;
    private final double unitPrice;
    private final LocalDateTime timestamp;

    public Sale(Product product, int quantity) {
        if (product == null) throw new IllegalArgumentException("Product cannot be null");
        if (quantity <= 0) throw new IllegalArgumentException("Quantity must be positive");

        this.productId = product.getProductId();
        this.productName = product.getName();
        this.quantity = quantity;
        this.unitPrice = product.getPrice();
        this.timestamp = LocalDateTime.now();
    }

    public double getTotal() {
        return unitPrice * quantity;
    }

    public String getReceiptLine() {
        return String.format("%-3d x %-20s @ R%-6.2f = R%-7.2f",
                quantity, productName, unitPrice, getTotal());
    }

    @Override
    public String toString() {
        return String.format("[%s] %s - %s",
                timestamp.format(DateTimeFormatter.ISO_LOCAL_TIME),
                productId, getReceiptLine());
    }
}