package splitwise;
import java.util.HashMap;
import java.util.Map;

public class SplitwiseService {
    final static String PERSONAL = "personal";
    private Map<String, User> users = new HashMap<>();
    private Map<String, Group> groups = new HashMap<>();
    private Ledger ledger = new Ledger();

    public void addUser(User user) {
        users.put(user.getId(), user);
    }

    public void createGroup(Group group) {
        groups.put(group.getId(), group);
    }

    public void addExpense(Expense expense) {
        String groupId = expense.getGroupId() != null ? expense.getGroupId() : PERSONAL;

        if (!groupId.equals(PERSONAL)) {
            getGroup(groupId).addExpense(expense);
        }
        for (User member : expense.getSplitAmong()) {
            member.addExpense(expense);
        }
    
        Map<String, Double> splitResult = expense.getSplitStrategy().calculateSplit(expense.getAmount(), expense.getSplitAmong());
        for (User user : expense.getSplitAmong()) {
            if (user.getId().equals(expense.getPaidBy().getId())) 
                continue;
            ledger.addEntry(user.getId(), expense.getPaidBy().getId(),splitResult.get(user.getId()), groupId);
        }

        System.out.println("Expense added: ₹" + expense.getAmount() + " paid by " + expense.getPaidBy().getName()  + " [" + groupId + "]");
    }

    public void addMember(String groupId, User user) {
        Group group = getGroup(groupId);
        if (group == null) {
            throw new IllegalArgumentException("Group not found: " + groupId);
        }
        group.addMember(user);
    }

    public void removeMember(String groupId, User user) {
        Group group = groups.get(groupId);
        if (group == null) {
            throw new IllegalArgumentException("Group not found: " + groupId);
        }
        if (ledger.hasPendingBalance(user.getId(), groupId)) {
            throw new IllegalStateException(user.getName() + " has pending dues. Please settle first.");
        }
        group.removeMember(user);
    }

    public void settle(Payment payment) {
        // only validate group if it's a group payment
        if (!payment.getGroupId().equals(PERSONAL)) {
            getGroup(payment.getGroupId());  // validates group exists
        }
    
        ledger.settleEntry(payment.getPaidBy().getId(), payment.getPaidTo().getId(), payment.getAmount(), payment.getGroupId());
        System.out.println(payment.getPaidBy().getName() + " paid " + payment.getPaidTo().getName() + " ₹" + payment.getAmount() + " [" + payment.getGroupId() + "]");
    }

    public void showGlobalBalance(String userId) {
        ledger.showGlobalBalances(userId, users);
    }
    
    public void showGroupBalance(String userId, String groupId) {
        ledger.showScopedBalances(userId, groupId, users);
    }

    private Group getGroup(String groupId) {
        Group group = groups.get(groupId);
        if (group == null) {
            throw new IllegalArgumentException("Group not found: " + groupId);
        }
        return group;
    }
}