import java.util.List;
import java.util.UUID;

public class Order {
    private String id;
    private String userId;
    private List<CartItem> items;
    private double totalAmount;
    private Payment payment;
    private Inventory inventory;
    private OrderState currentState;

    public Order(String userId, List<CartItem> items, double totalAmount, Payment payment, Inventory inventory) {
        this.id = UUID.randomUUID().toString();
        this.userId = userId;
        this.items = items;
        this.totalAmount = totalAmount;
        this.payment = payment;
        this.inventory = inventory;
        this.currentState = new PlacedState();  // always starts here
    }

    public void confirm() { currentState.confirm(this); }
    public void ship() { currentState.ship(this); }
    public void deliver() { currentState.deliver(this); }
    public void cancel() { currentState.cancel(this); }
    public void paymentFailed() { currentState.paymentFailed(this); }

    public String getId() { return id; }
    public String getUserId() { return userId; }
    public List<CartItem> getItems() { return items; }
    public double getTotalAmount() { return totalAmount; }
    public Payment getPayment() { return payment; }
    public Inventory getInventory() { return inventory; }
    public OrderState getCurrentState() { return currentState; }

    public void setState(OrderState state) {
        this.currentState = state;
    }
}