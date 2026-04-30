public class CartItem {
    private Variant variant;
    private Product product;
    private int quantity;

    public CartItem(Variant variant, Product product, int quantity) {
        this.variant = variant;
        this.product = product;
        this.quantity = quantity;
    }

    public Variant getVariant() {
        return variant;
    }

    public Product getProduct() {
        return product;
    } 

    public int getQuantity() {
        return quantity;
    }

    public void updateQuantity(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }
        this.quantity = quantity;
    }
}