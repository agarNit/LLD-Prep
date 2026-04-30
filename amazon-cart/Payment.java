import java.util.UUID;

public class Payment {
    private String id;
    private String orderId;
    private double amount;
    private PaymentStrategy strategy;
    private PaymentStatus status;

    public Payment(String orderId, double amount, PaymentStrategy strategy) {
        this.id = UUID.randomUUID().toString();
        this.orderId = orderId;
        this.amount = amount;
        this.strategy = strategy;
        this.status = PaymentStatus.PENDING;
    }

    public void pay() {
        this.status = strategy.payAmount(amount);
    }

    public void refund() {
        this.status = strategy.refund(amount);
    }

    public String getId() { return id; }
    public String getOrderId() { return orderId; }
    public double getAmount() { return amount; }
    public PaymentStatus getStatus() { return status; }
}