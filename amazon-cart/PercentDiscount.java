public class PercentDiscount implements DiscountStrategy {
    private double percent;

    public PercentDiscount(double percent) {
        validatePercentage(percent);
        this.percent = percent;
    }

    public double applyDiscount(double amount) {
        return Math.max(0, amount - (amount * percent / 100.0));
    }

    public double getDiscount() {
        return percent;
    }

    public void validatePercentage(double percent) {
        if (percent < 0.0 || percent > 100.0) {
            throw new IllegalArgumentException("Invalid discount percent");
        }
    }
}