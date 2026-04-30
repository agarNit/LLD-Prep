package splitwise;

public class BalanceEntry {
    private String owedById;
    private String owedToId;
    private double amount;
    private String groupId;

    public BalanceEntry(String owedById, String owedToId, double amount, String groupId) {
        this.owedById = owedById;
        this.owedToId = owedToId;
        this.amount = amount;
        this.groupId = groupId;
    }

    public String getOwedById() {
        return owedById;
    }

    public String getOwedToId() {
        return owedToId;
    }

    public double getAmount() {
        return amount;
    }

    public String getGroupId() {
        return groupId;
    }

    public void updateBalance(double delta) {
        this.amount += delta;
    }
}
