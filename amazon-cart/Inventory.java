import java.util.HashMap;
import java.util.Map;

public class Inventory {
    private Map<String, Integer> inventory;

    public Inventory() {
        this.inventory = new HashMap<>();
    }

    public void addStock(String variantId, int quantity) {
        inventory.put(variantId, inventory.getOrDefault(variantId, 0)+quantity);
    }

    public void reduceStock(String variantId, int quantity) {
        if (!isAvailable(variantId, quantity)) {
            throw new IllegalStateException("Insufficient stock");
        }
        inventory.put(variantId, inventory.get(variantId)-quantity);

    }

    public void restoreStock(String variantId, int quantity) {
        inventory.put(variantId, inventory.getOrDefault(variantId, 0)+quantity);
    }

    public boolean isAvailable(String variantId, int quantity) {
        return inventory.getOrDefault(variantId, 0) >= quantity;
    }
}