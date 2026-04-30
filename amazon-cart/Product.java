import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Product {
    private String id;
    private String name;
    private List<Variant> variants;
    private int quantity;
    private String category;
    private double basePrice;

    public Product(String name, int quantity, String category, double basePrice) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.variants = new ArrayList<>();
        this.quantity = quantity;
        this.category = category;
        this.basePrice = basePrice;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Variant> getVariants() {
        return variants;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getCategory() {
        return category;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public void addVariant(Variant variant) {
        variants.add(variant);
    }
}