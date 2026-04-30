public class EmailStrategy implements NotificationStrategy {
    public void notify(Subscriber subscriber, String message) {
        System.out.println("Sending Email to " + subscriber.getEmail() + ": " + message);
    }
}