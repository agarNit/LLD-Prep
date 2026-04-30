import java.util.List;
import java.util.Set;

public class Publisher {
    private StrategyRegistry registry;

    public Publisher(StrategyRegistry registry) {
        this.registry = registry;
    }

    public void publishToChannel(Channel channel, String message) {
        List<Subscriber> subscribers = channel.getSubscribers();
        if (subscribers.size() == 0) {
            throw new Error("No subscribers found for channel: " + channel.getName());
        }
        for (Subscriber subscriber: subscribers) {
            Set<NotificationType> notificationTypes = subscriber.getPreferredNotificationTypes();
            for (NotificationType type: notificationTypes) {
                NotificationStrategy strategy = registry.getStrategy(type);
                if (strategy != null) {
                    strategy.notify(subscriber, message);
                }
            }
        }
    }

}