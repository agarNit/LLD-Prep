import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductCatalog {
    private Map<String, Product> products;

    public ProductCatalog() {
        this.products = new HashMap<>();
    }

    public void addProduct(Product p) {
        products.put(p.getId(), p);
    }

    public void removeProduct(String productId) {
        products.remove(productId);
    }

    public Product getProductById(String id) {
        if (!products.containsKey(id)) {
            throw new IllegalArgumentException("Product not found: " + id);
        }
        return products.get(id);
    }

    public List<Product> getProductByName(String name) {
        List<Product> result = new ArrayList<>();
        for (Product product : products.values()) {
            if (product.getName().toLowerCase().contains(name.toLowerCase())) {
                result.add(product);
            }
        }
        return result;
    }

    public List<Product> getProductByCategory(String category) {
        List<Product> result = new ArrayList<>();
        for (Product product : products.values()) {
            if (product.getCategory().toLowerCase().contains(category.toLowerCase())) {
                result.add(product);
            }
        }
        return result;
    }

    public Collection<Product> getAllProducts() {
        return products.values();
    }
}