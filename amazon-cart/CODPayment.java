public class CODPayment implements PaymentStrategy {

    public CODPayment() {
    }

    public PaymentStatus payAmount(double amount) {
        return PaymentStatus.PENDING;
    }

    public PaymentStatus refund(double amount) {
        System.out.println("Order cancelled. Cash not taken. ₹" + amount);
        return PaymentStatus.SUCCESS;
    }
}