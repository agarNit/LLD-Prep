import java.util.ArrayList;
import java.util.List;

public class Cart {
    private String userId;
    private List<CartItem> items;
    private Coupon coupon;

    public Cart(String userId) {
        this.userId = userId;
        this.items = new ArrayList<>();
    }

    public String getUserId() {
        return userId;
    }

    public List<CartItem> getCartItems() {
        return items;
    } 

    public Coupon getCoupon() {
        return coupon;
    }

    public void addItem(CartItem item) {
        for (CartItem existing : items) {
            if (existing.getVariant().getId().equals(item.getVariant().getId())) {
                existing.updateQuantity(existing.getQuantity() + item.getQuantity());
                return;
            }
        }
        items.add(item);
    }

    public void removeItem(String variantId) {
        CartItem item = findItem(variantId);
        items.remove(item);
    }

    public void updateQuantity(String variantId, int quantity) {
        findItem(variantId).updateQuantity(quantity);
    }

    public void applyCoupon(Coupon coupon) {
        if (!coupon.isValid()) {
            throw new IllegalStateException("Coupon " + coupon.getCode() + " is not valid");
        }
        this.coupon = coupon;
    }

    public void removeCoupon() {
        this.coupon = null;
    }

    public double calculateTotal() {
        double total = 0;
        for (CartItem item : items) {
            total += item.getVariant().getPrice() * item.getQuantity();
        }
        return total;
    }

    public double calculateDiscountedTotal() {
        double total = calculateTotal();
        if (coupon != null && coupon.isValid()) {
            return coupon.apply(total);
        }
        return total;
    }

    private CartItem findItem(String variantId) {
        for (CartItem item : items) {
            if (item.getVariant().getId().equals(variantId)) {
                return item;
            }
        }
        throw new IllegalArgumentException("Item not found in cart: " + variantId);
    }
}