import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Inventory {
    private final Map<String, Product> products = new HashMap<>();
    private final List<Sale> sales = new ArrayList<>();

    public void addProduct(Product product) {
        products.put(product.getProductId().toLowerCase(), product);
    }

    public Product getProduct(String productId) {
        return products.get(productId.toLowerCase());
    }

    public void processSale(String productId, int quantity) {
        Product product = getProduct(productId);
        if (product == null) throw new IllegalArgumentException("Product not found");
        if (product.getQuantity() < quantity) throw new IllegalArgumentException("Insufficient stock");

        product.adjustQuantity(-quantity);
        sales.add(new Sale(product, quantity));
    }

    public double getTotalSales() {
        return sales.stream().mapToDouble(Sale::getTotal).sum();
    }

    public String generateReport() {
        StringBuilder report = new StringBuilder();
        report.append("=== INVENTORY REPORT ===\n");
        products.values().forEach(p -> report.append(p).append("\n"));

        report.append("\n=== SALES SUMMARY ===\n");
        sales.forEach(s -> report.append(s).append("\n"));
        report.append(String.format("TOTAL SALES: R%.2f", getTotalSales()));

        return report.toString();
    }
}