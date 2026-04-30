public class WhatsappStrategy implements NotificationStrategy {
    public void notify(Subscriber subscriber, String message) {
        System.out.println("Sending WhatsApp message to " + subscriber.getWhatsappId() + ": " + message);
    }
}