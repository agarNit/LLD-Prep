import java.util.HashMap;
import java.util.Map;

public class StrategyRegistry {
    Map<NotificationType, NotificationStrategy> strategies;

    public StrategyRegistry() {
        strategies = new HashMap<>();
        registerStrategy(NotificationType.EMAIL, new EmailStrategy());
        registerStrategy(NotificationType.SMS, new SMSStrategy());
        registerStrategy(NotificationType.WHATSAPP, new WhatsappStrategy());
    }

    public NotificationStrategy getStrategy(NotificationType type) {
        return strategies.get(type);
    }

    public void registerStrategy(NotificationType type, NotificationStrategy strategy) {
        strategies.put(type, strategy);
    }

    public void removeStrategy(NotificationType type) {
        strategies.remove(type);
    }
}