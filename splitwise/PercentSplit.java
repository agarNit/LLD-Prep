package splitwise;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PercentSplit implements SplitStrategy {
    private Map<String, Double> percentages;

    public PercentSplit(Map<String, Double> percentages) {
        validatePercentages(percentages);
        this.percentages = percentages;
    }

    private void validatePercentages(Map<String, Double> percentages) {
        double total = percentages.values().stream().mapToDouble(Double::doubleValue).sum();
        if (Math.abs(total - 100.0) > 0.01) {
            throw new IllegalArgumentException("Percentages must sum to 100. Current sum: " + total);
        }
    }

    public Map<String, Double> calculateSplit(double amount, List<User> members) {
        Map<String, Double> split = new HashMap<>();
        for (User member : members) {
            double percent = percentages.getOrDefault(member.getId(), 0.0);
            double share = Math.round((amount * percent / 100.0) * 100.0) / 100.0;
            split.put(member.getId(), share);
        }
        return split;
    }
}