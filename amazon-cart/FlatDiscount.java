public class FlatDiscount implements DiscountStrategy {
    private double discount;

    public FlatDiscount(double discount) {
        this.discount = discount;
    }

    public double applyDiscount(double amount) {
        return Math.max(0, amount-discount);
    }

    public double getDiscount() {
        return discount;
    }
}