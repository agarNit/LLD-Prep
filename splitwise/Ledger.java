package splitwise;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Ledger {
    List<BalanceEntry> entries = new ArrayList<>();

    public void addEntry(String owedById, String owedToId, double amount, String groupId) {
        BalanceEntry existing = findEntry(owedById, owedToId, groupId);
        if (existing != null) {
            existing.updateBalance(amount);
        } else {
            entries.add(new BalanceEntry(owedById, owedToId, amount, groupId));
        }
    }

    public void settleEntry(String payerId, String payeeId, double amount, String groupId) {
        BalanceEntry entry = findEntry(payerId, payeeId, groupId);
        if (entry != null) {
            entry.updateBalance(-amount);
        }
    }

    public boolean hasPendingBalance(String userId, String groupId) {
        for (BalanceEntry entry: entries) {
            if (entry.getGroupId().equals(groupId)
                && (entry.getOwedById().equals(userId)
                || entry.getOwedToId().equals(userId))
                && Math.abs(entry.getAmount()) > 0.01) {
            return true;
        }
        }
        return false;
    }

    public void showGlobalBalances(String userId, Map<String, User> users) {
        System.out.println("\nGlobal balances for " + users.get(userId).getName() + ":");
        boolean hasBalance = false;

        for (BalanceEntry entry : entries) {
            if (Math.abs(entry.getAmount()) < 0.01) continue;

            if (entry.getOwedById().equals(userId)) {
                System.out.println("  Owes " + users.get(entry.getOwedToId()).getName() + ": ₹" + entry.getAmount());
                hasBalance = true;
            } else if (entry.getOwedToId().equals(userId)) {
                System.out.println("  Gets back from " + users.get(entry.getOwedById()).getName() + ": ₹" + entry.getAmount());
                hasBalance = true;
            }
        }
        if (!hasBalance) System.out.println("  All settled up!");
    }

    public void showScopedBalances(String userId, String groupId, Map<String, User> users) {
        System.out.println("\nBalances for " + users.get(userId).getName() + " in group [" + groupId + "]:");
        boolean hasBalance = false;

        for (BalanceEntry entry : entries) {
            if (!entry.getGroupId().equals(groupId)) continue;
            if (Math.abs(entry.getAmount()) < 0.01) continue;

            if (entry.getOwedById().equals(userId)) {
                System.out.println("  Owes " + users.get(entry.getOwedToId()).getName() + ": ₹" + entry.getAmount());
                hasBalance = true;
            } else if (entry.getOwedToId().equals(userId)) {
                System.out.println("  Gets back from " + users.get(entry.getOwedById()).getName() + ": ₹" + entry.getAmount());
                hasBalance = true;
            }
        }

        if (!hasBalance) System.out.println("  All settled up!");
    }

    private BalanceEntry findEntry(String owedById, String owedToId, String groupId) {
        for (BalanceEntry entry : entries) {
            if (entry.getOwedById().equals(owedById) && entry.getOwedToId().equals(owedToId) && entry.getGroupId().equals(groupId)) {
                return entry;
            }
        }
        return null;
    }
}