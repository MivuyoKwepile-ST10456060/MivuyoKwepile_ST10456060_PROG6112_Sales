import java.util.Objects;

public class Product {
    private final String productId;
    private final String name;
    private double price;
    private int quantity;

    public Product(String productId, String name, double price, int quantity) {
        if (productId == null || productId.isBlank()) throw new IllegalArgumentException("Invalid product ID");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Invalid name");
        if (price <= 0) throw new IllegalArgumentException("Price must be positive");
        if (quantity < 0) throw new IllegalArgumentException("Quantity cannot be negative");

        this.productId = productId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public String getProductId() { return productId; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }


    public void updatePrice(double newPrice) {
        if (newPrice <= 0) throw new IllegalArgumentException("Price must be positive");
        this.price = newPrice;
    }

    public void adjustQuantity(int amount) {
        if (quantity + amount < 0) throw new IllegalArgumentException("Insufficient quantity");
        quantity += amount;
    }

    @Override
    public String toString() {
        return String.format("%s (%s) - R%.2f | Stock: %d",
                productId, name, price, quantity);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product product)) return false;
        return productId.equalsIgnoreCase(product.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId.toLowerCase());
    }
}