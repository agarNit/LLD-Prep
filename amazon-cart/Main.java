import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        AmazonService service = new AmazonService();
        runHappyFlow(service);
        runEdgeCases(service);
    }

    private static void runHappyFlow(AmazonService service) {
        printHeader("HAPPY FLOW");

        User user = service.registerUser("Aman", "aman@example.com");

        Product tshirt = service.addProduct("Polo T-Shirt", 100, "Fashion", 999);
        Variant blackM = new Variant("Black", "M", 1199);
        Variant navyL = new Variant("Navy", "L", 1299);
        service.addVariantToProduct(tshirt.getId(), blackM, 10);
        service.addVariantToProduct(tshirt.getId(), navyL, 8);

        service.createCoupon("SAVE10", LocalDate.now().plusDays(7), 5, new PercentDiscount(10));
        service.addItemToCart(user.getId(), blackM.getId(), 2);
        service.addItemToCart(user.getId(), navyL.getId(), 1);
        service.applyCouponToCart(user.getId(), "SAVE10");

        System.out.println("Cart total: " + service.getCartTotal(user.getId()));
        System.out.println("Discounted total: " + service.getCartDiscountedTotal(user.getId()));

        Order order = service.placeOrder(user.getId(), new CardPayment("1234567812345678", "123"));
        printOrder("After placement", order);

        order.ship();
        printOrder("After shipping", order);

        order.deliver();
        printOrder("After delivery", order);
    }

    private static void runEdgeCases(AmazonService service) {
        printHeader("EDGE CASES");

        testInsufficientStock(service);
        testInvalidCoupon(service);
        testPaymentFailureAndRecovery(service);
        testInvalidStateTransitions(service);
    }

    private static void testInsufficientStock(AmazonService service) {
        printCase("Insufficient stock while adding to cart");
        User user = service.registerUser("Riya", "riya@example.com");
        Product shoes = service.addProduct("Running Shoes", 20, "Footwear", 2499);
        Variant size9 = new Variant("Blue", "9", 2499);
        service.addVariantToProduct(shoes.getId(), size9, 2);

        try {
            service.addItemToCart(user.getId(), size9.getId(), 3);
            System.out.println("Unexpected: item added even though stock is low");
        } catch (Exception ex) {
            System.out.println("Expected error: " + ex.getMessage());
        }
    }

    private static void testInvalidCoupon(AmazonService service) {
        printCase("Expired coupon should fail");

       
        User user = service.registerUser("Karan", "karan@example.com");
        Product bag = service.addProduct("Laptop Bag", 30, "Accessories", 1999);
        Variant grey = new Variant("Grey", "Standard", 1999);
        service.addVariantToProduct(bag.getId(), grey, 5);
        service.addItemToCart(user.getId(), grey.getId(), 1);
        service.createCoupon("OLD50", LocalDate.now().minusDays(1), 2, new FlatDiscount(500));

        try {
            service.applyCouponToCart(user.getId(), "OLD50");
            System.out.println("Unexpected: expired coupon applied");
        } catch (Exception ex) {
            System.out.println("Expected error: " + ex.getMessage());
        }
    }

    private static void testPaymentFailureAndRecovery(AmazonService service) {
        printCase("Payment failure then manual recovery");


        User user = service.registerUser("Neha", "neha@example.com");
        Product phone = service.addProduct("Smartphone", 15, "Electronics", 49999);
        Variant black = new Variant("Black", "128GB", 49999);
        service.addVariantToProduct(phone.getId(), black, 3);
        service.addItemToCart(user.getId(), black.getId(), 1);

        Order order = service.placeOrder(user.getId(), new UPIPayment("invalid-upi-id"));
        printOrder("After failed payment placement", order);

        order.ship();
        printOrder("After invalid ship attempt", order);

        order.confirm();
        printOrder("After recovery confirm", order);
    }

    private static void testInvalidStateTransitions(AmazonService service) {
        printCase("Invalid state transitions");

        User user = service.registerUser("Ishita", "ishita@example.com");
        Product watch = service.addProduct("Smart Watch", 12, "Electronics", 8999);
        Variant silver = new Variant("Silver", "Standard", 8999);
        service.addVariantToProduct(watch.getId(), silver, 4);
        service.addItemToCart(user.getId(), silver.getId(), 1);

        Order order = service.placeOrder(user.getId(), new CODPayment());
        printOrder("After COD placement", order);

        order.deliver();
        printOrder("After invalid deliver attempt", order);

        order.cancel();
        printOrder("After cancel", order);

        order.ship();
        printOrder("After invalid ship post-cancel", order);
    }

    private static void printOrder(String label, Order order) {
        System.out.println(label + " -> state: " + order.getCurrentState().getClass().getSimpleName()
                + ", payment: " + order.getPayment().getStatus()
                + ", total: " + order.getTotalAmount());
    }

    private static void printHeader(String title) {
        System.out.println();
        System.out.println("==================================================");
        System.out.println(title);
        System.out.println("==================================================");
    }

    private static void printCase(String title) {
        System.out.println();
        System.out.println("-- " + title + " --");
    }
}