import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PerishableProduct extends Product {
    private final LocalDate expiryDate;

    public PerishableProduct(String productId, String name, double price,
                             int quantity, String expiryDate) {
        super(productId, name, price, quantity);
        this.expiryDate = LocalDate.parse(expiryDate,
                DateTimeFormatter.ISO_LOCAL_DATE);
    }

    public boolean isExpired() {
        return LocalDate.now().isAfter(expiryDate);
    }

    @Override
    public String toString() {
        return super.toString() + " | Expires: " + expiryDate +
                (isExpired() ? " (EXPIRED!)" : "");
    }
}