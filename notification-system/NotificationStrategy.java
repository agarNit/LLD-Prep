public interface NotificationStrategy {
    public void notify(Subscriber subscriber, String message);
}

enum NotificationType {
    EMAIL, SMS, WHATSAPP
}