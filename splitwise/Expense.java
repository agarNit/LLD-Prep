package splitwise;

import java.util.List;
import java.util.UUID;

public class Expense {
    private String id;
    private String description;
    private double amount;
    private User paidBy;
    private List<User> splitAmong;
    private SplitStrategy splitStrategy;
    private String groupId;

    public Expense(String description, double amount, User paidBy, String groupId, SplitStrategy splitStrategy, List<User> splitAmong) {
        this.id = UUID.randomUUID().toString();
        this.description = description;
        this.amount = amount;
        this.paidBy = paidBy;
        this.groupId = groupId;
        this.splitStrategy = splitStrategy;
        this.splitAmong = splitAmong;
    }

    public String getId() { 
        return id; 
    }

    public String getDescription() { 
        return description; 
    }

    public double getAmount() { 
        return amount; 
    }

    public User getPaidBy() { 
        return paidBy; 
    }

    public String getGroupId() { 
        return groupId; 
    }

    public SplitStrategy getSplitStrategy() { 
        return splitStrategy; 
    }

    public List<User> getSplitAmong() { 
        return splitAmong; 
    }
}