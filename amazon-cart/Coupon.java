import java.time.LocalDate;

public class Coupon {
    private DiscountStrategy strategy;
    private LocalDate expiry;
    private String code;
    private int usageLimit;
    private int usageCount = 0;

    public Coupon(String code, LocalDate expiry, int usageLimit, DiscountStrategy strategy) {
        this.code = code;
        this.expiry = expiry;
        this.usageLimit = usageLimit;
        this.strategy = strategy;
    }

    public String getCode() {
        return code;
    }

    public LocalDate getExpiryDate() {
        return expiry;
    }

    public int getUsageLimit() {
        return usageLimit;
    }

    public int getUsageCount() {
        return usageCount;
    }

    public DiscountStrategy getDiscountStrategy() {
        return strategy;
    }

    public boolean isValid() {
        return usageCount < usageLimit && LocalDate.now().isBefore(expiry);
    } 

    public void incrementUsage() {
        if (!isValid()) {
            throw new IllegalStateException("Coupon " + code + " is no longer valid");
        }
        usageCount++;
    }

    public double apply(double amount) {
        if (!isValid()) {
            throw new IllegalStateException("Coupon " + code + " is no longer valid");
        }
        incrementUsage();
        return strategy.applyDiscount(amount);
    }
}