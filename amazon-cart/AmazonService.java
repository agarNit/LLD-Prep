import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AmazonService {
    private final ProductCatalog catalog;
    private final Inventory inventory;
    private final Map<String, User> users;
    private final Map<String, Coupon> coupons;
    private final Map<String, Order> orders;

    public AmazonService() {
        this.catalog = new ProductCatalog();
        this.inventory = new Inventory();
        this.users = new HashMap<>();
        this.coupons = new HashMap<>();
        this.orders = new HashMap<>();
    }

    public User registerUser(String name, String email) {
        User user = new User(name, email);
        users.put(user.getId(), user);
        return user;
    }

    public Product addProduct(String name, int quantity, String category, double basePrice) {
        Product product = new Product(name, quantity, category, basePrice);
        catalog.addProduct(product);
        return product;
    }

    public void addVariantToProduct(String productId, Variant variant, int stockQuantity) {
        Product product = catalog.getProductById(productId);
        product.addVariant(variant);

        if (stockQuantity > 0) {
            inventory.addStock(variant.getId(), stockQuantity);
        }
    }

    public void addStock(String variantId, int quantity) {
        getVariantById(variantId);
        inventory.addStock(variantId, quantity);
    }

    public List<Product> searchProductsByName(String name) {
        return catalog.getProductByName(name);
    }

    public List<Product> searchProductsByCategory(String category) {
        return catalog.getProductByCategory(category);
    }

    public Coupon createCoupon(String code, LocalDate expiry, int usageLimit, DiscountStrategy strategy) {
        Coupon coupon = new Coupon(code, expiry, usageLimit, strategy);
        coupons.put(code, coupon);
        return coupon;
    }

    public void addItemToCart(String userId, String variantId, int quantity) {
        User user = getUser(userId);
        Variant variant = getVariantById(variantId);
        Product product = getProductByVariantId(variantId);

        if (!inventory.isAvailable(variantId, quantity)) {
            throw new IllegalStateException("Insufficient stock for variant: " + variantId);
        }

        user.getCart().addItem(new CartItem(variant, product, quantity));
    }

    public void updateCartItemQuantity(String userId, String variantId, int quantity) {
        User user = getUser(userId);
        user.getCart().updateQuantity(variantId, quantity);
    }

    public void removeItemFromCart(String userId, String variantId) {
        User user = getUser(userId);
        user.getCart().removeItem(variantId);
    }

    public void applyCouponToCart(String userId, String code) {
        User user = getUser(userId);
        Coupon coupon = getCoupon(code);
        user.getCart().applyCoupon(coupon);
    }

    public void removeCouponFromCart(String userId) {
        User user = getUser(userId);
        user.getCart().removeCoupon();
    }

    public double getCartTotal(String userId) {
        return getUser(userId).getCart().calculateTotal();
    }

    public double getCartDiscountedTotal(String userId) {
        return getUser(userId).getCart().calculateDiscountedTotal();
    }

    public Order placeOrder(String userId, PaymentStrategy paymentStrategy) {
        User user = getUser(userId);
        Cart cart = user.getCart();
        List<CartItem> cartItems = cart.getCartItems();

        if (cartItems.isEmpty()) {
            throw new IllegalStateException("Cannot place order with an empty cart");
        }

        for (CartItem item : cartItems) {
            if (!inventory.isAvailable(item.getVariant().getId(), item.getQuantity())) {
                throw new IllegalStateException("Insufficient stock for variant: " + item.getVariant().getId());
            }
        }

        List<CartItem> orderItems = new ArrayList<>();
        for (CartItem item : cartItems) {
            inventory.reduceStock(item.getVariant().getId(), item.getQuantity());
            orderItems.add(new CartItem(item.getVariant(), item.getProduct(), item.getQuantity()));
        }

        double payableAmount = cart.calculateDiscountedTotal();
        Payment payment = new Payment("PENDING_ORDER", payableAmount, paymentStrategy);
        payment.pay();

        Order order = new Order(user.getId(), orderItems, payableAmount, payment, inventory);

        if (payment.getStatus() == PaymentStatus.SUCCESS || payment.getStatus() == PaymentStatus.PENDING) {
            order.confirm();
        } else {
            order.paymentFailed();
        }

        orders.put(order.getId(), order);
        clearCart(cart);
        return order;
    }

    public Order getOrder(String orderId) {
        if (!orders.containsKey(orderId)) {
            throw new IllegalArgumentException("Order not found: " + orderId);
        }
        return orders.get(orderId);
    }

    private User getUser(String userId) {
        if (!users.containsKey(userId)) {
            throw new IllegalArgumentException("User not found: " + userId);
        }
        return users.get(userId);
    }

    private Coupon getCoupon(String code) {
        if (!coupons.containsKey(code)) {
            throw new IllegalArgumentException("Coupon not found: " + code);
        }
        return coupons.get(code);
    }

    private Variant getVariantById(String variantId) {
        for (Product product : catalog.getAllProducts()) {
            for (Variant variant : product.getVariants()) {
                if (variant.getId().equals(variantId)) {
                    return variant;
                }
            }
        }
        throw new IllegalArgumentException("Variant not found: " + variantId);
    }

    private Product getProductByVariantId(String variantId) {
        for (Product product : catalog.getAllProducts()) {
            for (Variant variant : product.getVariants()) {
                if (variant.getId().equals(variantId)) {
                    return product;
                }
            }
        }
        throw new IllegalArgumentException("Product for variant not found: " + variantId);
    }

    private void clearCart(Cart cart) {
        cart.getCartItems().clear();
        cart.removeCoupon();
    }
}