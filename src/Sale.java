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
        double effectivePrice = unitPrice;
        if (quantity == 1) {
            effectivePrice = effectivePrice * 0.9;
        }
        return effectivePrice * quantity;
    }

    public String getReceiptLine() {
        double effectivePrice = unitPrice;
        String discountText = "";
        if (quantity == 1) {
            effectivePrice = unitPrice * 0.9;
            discountText = " (-10%)";
        }
        return String.format("%-3d x %-20s%s @ R%-6.2f = R%-7.2f",
                quantity, productName, discountText, effectivePrice, effectivePrice * quantity);
    }

    @Override
    public String toString() {
        return String.format("[%s] %s - %s",
                timestamp.format(DateTimeFormatter.ISO_LOCAL_TIME),
                productId, getReceiptLine());
    }


}