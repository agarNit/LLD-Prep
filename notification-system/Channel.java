import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Channel {
    private String id;
    private String name;
    private List<Subscriber> subscribers;

    public Channel(String name) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.subscribers = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Subscriber> getSubscribers() {
        return subscribers;
    }

    public void addSubscriber(Subscriber subscriber) {
        subscribers.add(subscriber);
    }

    public void removeSubscriber(Subscriber subscriber) {
        subscribers.remove(subscriber);
    }
    
}