import java.util.HashMap;
import java.util.Map;

public class Inventory {
    private Map<Product, Integer> stock = new HashMap<>();

    public void addStock(Product p, int quantity) {
        stock.put(p, stock.getOrDefault(p, 0)+1);
    }

    public void reduceStock(Product p) {
        if (isAvailable(p)) {
            stock.put(p, stock.get(p)-1);
        }
    }

    public boolean isAvailable(Product p) {
        return stock.getOrDefault(p, 0) > 0;
    }

    public boolean isEmpty() {
        return stock.values().stream().allMatch(q -> q == 0);
    }
}