public class CardPayment implements PaymentStrategy {
    private String cardNumber;
    private String cvv;

    public CardPayment(String cardNumber, String cvv) {
        this.cardNumber = cardNumber;
        this.cvv = cvv;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getCvv() {
        return cvv;
    }    

    public PaymentStatus payAmount(double amount) {
        if (isValidCard()) {
            System.out.println("Card payment successful: ₹" + amount);
            return PaymentStatus.SUCCESS;
        }
        System.out.println("Card payment failed");
        return PaymentStatus.FAILED;
    }

    private boolean isValidCard() {
        return cardNumber != null && cardNumber.length() == 16 && cvv != null && cvv.length() == 3;
    }

    public PaymentStatus refund(double amount) {
        System.out.println("Refund processed: ₹" + amount);
        return PaymentStatus.SUCCESS;
    }
}