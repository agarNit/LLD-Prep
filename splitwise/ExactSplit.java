package splitwise;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExactSplit implements SplitStrategy {
    private Map<String, Double> exactAmounts;

    public ExactSplit(Map<String, Double> exactAmounts) {
        validateAmounts(exactAmounts);
        this.exactAmounts = exactAmounts;
    }

    private void validateAmounts(Map<String, Double> exactAmounts) {
        if (exactAmounts.values().stream().anyMatch(a -> a < 0)) {
            throw new IllegalArgumentException( "Exact amounts cannot be negative.");
        }
    }

    @Override
    public Map<String, Double> calculateSplit(double totalAmount, List<User> members) {
        validateTotal(totalAmount, members);
        Map<String, Double> split = new HashMap<>();
        for (User member : members) {
            split.put(member.getId(), exactAmounts.getOrDefault(member.getId(), 0.0));
        }
        return split;
    }

    private void validateTotal(double totalAmount, List<User> members) {
        double sum = members.stream().mapToDouble(m -> exactAmounts.getOrDefault(m.getId(), 0.0)).sum();
        if (Math.abs(sum - totalAmount) > 0.01) {
            throw new IllegalArgumentException("Exact amounts must sum to total. Expected: " + totalAmount + " Got: " + sum);
        }
    }
}