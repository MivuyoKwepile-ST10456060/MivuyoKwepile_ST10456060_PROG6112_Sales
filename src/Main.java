import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    private static final Inventory inventory = new Inventory();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        while (true) {
            printMenu();
            int choice = getIntInput("Enter choice: ");

            switch (choice) {
                case 1 -> addProduct();
                case 2 -> processSale();
                case 3 -> System.out.println(inventory.generateReport());
                case 4 -> updateProductPrice();
                case 5 -> System.out.println("Exiting inventory system...");
                default -> {
                    System.out.println("Invalid choice. Try again.");
                    scanner.close();
                    return;
                }
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n=== INVENTORY MANAGEMENT ===");
        System.out.println("1. Add Product");
        System.out.println("2. Sell Product");
        System.out.println("3. View Report");
        System.out.println("4. Update Product Price");
        System.out.println("5. Exit");
    }

    private static void addProduct() {
        System.out.println("\nADD NEW PRODUCT");
        String id = getStringInput("Product ID: ");
        String name = getStringInput("Name: ");
        double price = getDoubleInput();
        int quantity = getIntInput("Quantity: ");

        if (getYesNoInput()) {
            String expiry = getStringInput("Expiry date (YYYY-MM-DD): ");
            inventory.addProduct(new PerishableProduct(id, name, price, quantity, expiry));
        } else {
            inventory.addProduct(new Product(id, name, price, quantity));
        }
        System.out.println("Product added successfully!");
    }

    private static void processSale() {
        System.out.println("\nPROCESS SALE");
        String id = getStringInput("Product ID: ");
        int quantity = getIntInput("Quantity: ");

        try {
            inventory.processSale(id, quantity);
            System.out.println("Sale processed successfully!");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static String getStringInput(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                String input = scanner.nextLine().trim();
                if (!input.isEmpty()) return input;
                System.out.print("Input cannot be empty. " + prompt);
            } catch (Exception e) {
                System.out.print("Invalid input. " + prompt);
                scanner.nextLine(); // Clear buffer
            }
        }
    }

    private static int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                int value = scanner.nextInt();
                scanner.nextLine();
                return value;
            } catch (InputMismatchException e) {
                System.out.println("Please enter a valid integer.");
                scanner.nextLine();
            }
        }
    }

    private static double getDoubleInput() {
        while (true) {
            try {
                System.out.print("Price: ");
                double value = scanner.nextDouble();
                scanner.nextLine(); // Clear newline
                return value;
            } catch (InputMismatchException e) {
                System.out.println("Please enter a valid number.");
                scanner.nextLine(); // Clear invalid input
            }
        }
    }

    private static void updateProductPrice() {
        String id = getStringInput("Enter product ID: ");
        Product p = inventory.getProduct(id);
        if (p == null) {
            System.out.println("Product not found!");
            return;
        }
        double newPrice = getDoubleInput();
        p.updatePrice(newPrice);
        System.out.println("Price updated successfully!");
    }

    private static boolean getYesNoInput() {
        while (true) {
            String input = getStringInput("Is this perishable? (y/n): ").toLowerCase();
            if (input.equals("y")) return true;
            if (input.equals("n")) return false;
            System.out.println("Please enter 'y' or 'n'.");
        }
    }

}