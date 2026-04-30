import java.util.UUID;

public class Variant {
    private String color;
    private String size;
    private String id;
    private double price;

    public Variant(String color, String size, double price) {
        this.id = UUID.randomUUID().toString();
        this.color = color;
        this.size = size;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public String getColor() {
        return color;
    }

    public String getSize() {
        return size;
    }

    public double getPrice() {
        return price;
    }
}