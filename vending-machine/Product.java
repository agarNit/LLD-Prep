public class Product {
    private String name;
    private int price;

    public Product(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return this.name;
    }
    
    public int getPrice() {
        return this.price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        Product other = (Product) o;
        return this.price == other.price && this.name.equals(other.name);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(name, price);
    }
}