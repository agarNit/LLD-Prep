package splitwise;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EqualSplit implements SplitStrategy {

    public Map<String, Double> calculateSplit(double amount, List<User> members) {
        Map<String, Double> split = new HashMap<>();
        double share = Math.round((amount / members.size()) * 100.0) / 100.0;
        double remainder = Math.round((amount - share * members.size()) * 100.0) / 100.0;

        for (int i = 0; i < members.size(); i++) {
            if (i == 0) {
                split.put(members.get(i).getId(), share + remainder);
            } else {
                split.put(members.get(i).getId(), share);
            }
        }
        return split;
    }
}