package splitwise;
import java.util.UUID;

public class Payment {
    private String id;
    private User paidBy;
    private User paidTo;
    private double amount;
    private String groupId;

    public Payment(User paidBy, User paidTo, double amount, String groupId) {
        this.id = UUID.randomUUID().toString();
        this.paidBy = paidBy;
        this.paidTo = paidTo;
        this.amount = amount;
        this.groupId = groupId;
    }

    public String getId() { 
        return id; 
    }

    public User getPaidBy() { 
        return paidBy; 
    }

    public User getPaidTo() { 
        return paidTo; 
    }
    
    public double getAmount() { 
        return amount; 
    }

    public String getGroupId() {
        return groupId;
    }
}