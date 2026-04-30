import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Subscriber {
    private String id;
    private String email;
    private String phone;
    private String whatsappId;
    private Set<NotificationType> preferredNotificationTypes;

    public Subscriber(String email, String phone, String whatsappId) {
        this.id = UUID.randomUUID().toString();
        this.email = email;
        this.phone = phone;
        this.whatsappId = whatsappId;
        this.preferredNotificationTypes = new HashSet<>();
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getWhatsappId() {
        return whatsappId;
    }

    public Set<NotificationType> getPreferredNotificationTypes() {
        return preferredNotificationTypes;
    }

    public void addNotificationType(NotificationType type) {
        preferredNotificationTypes.add(type);
    }

    public void removeNotificationType(NotificationType type) {
        preferredNotificationTypes.remove(type);
    }

    public boolean wantsNotificationVia(NotificationType type) {
        return preferredNotificationTypes.contains(type);
    }
}
