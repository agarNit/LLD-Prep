public class UPIPayment implements PaymentStrategy {
    private String upiId;

    public UPIPayment(String upiId) {
        this.upiId = upiId;
    }

    public String getUpiId() {
        return upiId;
    }

    public PaymentStatus payAmount(double amount) {
        if (isValidUpi()) {
            System.out.println("UPI payment successful: ₹" + amount);
            return PaymentStatus.SUCCESS;
        }
        System.out.println("UPI payment failed");
        return PaymentStatus.FAILED;
    }

    private boolean isValidUpi() {
        return upiId != null && upiId.contains("@");
    }

    public PaymentStatus refund(double amount) {
        System.out.println("Refund processed: ₹" + amount);
        return PaymentStatus.SUCCESS;
    }
}