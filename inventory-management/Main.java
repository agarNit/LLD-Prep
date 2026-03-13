public class Main {
    public static void main(String[] args) {
        InventoryManager manager = new InventoryManager(java.util.List.of("W1", "W2"));
        manager.addStock("W1", "P1", 10);
        manager.transfer("P1", 4, "W1", "W2");

        // You can temporarily add a helper in Warehouse to print or expose stock,
        // or just rely on a debugger / breakpoints in your IDE.
        System.out.println("Manual smoke test completed");
    }
}