public class SMSStrategy implements NotificationStrategy {
    public void notify(Subscriber subscriber, String message) {
        System.out.println("Sending SMS to " + subscriber.getPhone() + ": " + message);
    }
}