import java.util.HashMap;
import java.util.Map;

public class NotificationService {
    private Map<String, Channel> channels;
    private Map<String, Subscriber> subscribers;
    private Publisher publisher;
    private StrategyRegistry registry;

    public NotificationService() {
        channels = new HashMap<>();
        subscribers = new HashMap<>();
        registry = new StrategyRegistry();
        publisher = new Publisher(registry);
    }

    public Channel getChannel(String channelId) {
        return channels.get(channelId);
    }

    public Subscriber getSubscriber(String subscriberId) {
        return subscribers.get(subscriberId);
    }

    public String addChannel(String name) {
        Channel channel = new Channel(name);
        channels.put(channel.getId(), channel);
        return channel.getId();
    }

    public void removeChannel(String channelId) {
        if (!channels.containsKey(channelId)) {
            throw new IllegalArgumentException("Channel not found: " + channelId);
        }
        channels.remove(channelId);
    }

    public String addSubscriber(String email, String phone, String whatsappId) {
        Subscriber subscriber = new Subscriber(email, phone, whatsappId);
        subscribers.put(subscriber.getId(), subscriber);
        return subscriber.getId();
    }

    public void removeSubscriber(String subscriberId) {
        if (!subscribers.containsKey(subscriberId)) {
            throw new IllegalArgumentException("Subscriber not found: " + subscriberId);
        }
        subscribers.remove(subscriberId);
    }

    public void subscribeToChannel(String subscriberId, String channelId) {
        Channel channel = channels.get(channelId);
        channel.addSubscriber(subscribers.get(subscriberId));
    }

    public void unsubscribeFromChannel(String subscriberId, String channelId) {
        Channel channel = channels.get(channelId);
        channel.removeSubscriber(subscribers.get(subscriberId));
    }

    public void publishToChannel(String channelId, String message) {
        if (!channels.containsKey(channelId)) {
            throw new IllegalArgumentException("Channel does not exist.");
        }
        publisher.publishToChannel(channels.get(channelId), message);
    }

    public void registerStrategy(NotificationType type, NotificationStrategy strategy) {
        registry.registerStrategy(type, strategy);
    }
}