package splitwise;
import java.util.Map;
import java.util.List;

public interface SplitStrategy {
    Map<String, Double> calculateSplit(double amount, List<User> members);
}