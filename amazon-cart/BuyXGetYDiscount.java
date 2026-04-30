public class BuyXGetYDiscount implements DiscountStrategy {
    private int x;
    private int y;
    private int totalItems;

    public BuyXGetYDiscount(int x, int y, int totalItems) {
        this.x = x;
        this.y = y;
        this.totalItems = totalItems;
    }

    public double applyDiscount(double amount) {
        validateDiscount(x, y, totalItems);
        return Math.max(0, amount - (double)y*amount/(x+y));
    }

    private void validateDiscount(int x, int y, int totalItems) {
        if (totalItems < x + y) {
            throw new IllegalArgumentException("Discount not applicable");
        }
    }
}