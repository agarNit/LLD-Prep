public interface PaymentStrategy {
    public PaymentStatus payAmount(double amount);
    public PaymentStatus refund(double amount);
}

enum PaymentStatus {
    SUCCESS,
    FAILED,
    PENDING
}