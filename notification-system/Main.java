public class Main {
    public static void main(String[] args) {
        NotificationService service = new NotificationService();
        String channelId1 = service.addChannel("C1");
        String channelId2 = service.addChannel("C2");
        String subscriberId1 = service.addSubscriber("nitish@gmail.com", "123", "nitish12");
        String subscriberId2 = service.addSubscriber("aish@gmail.com", "345", "aish@20");
        service.subscribeToChannel(subscriberId1, channelId1);
        service.subscribeToChannel(subscriberId2, channelId1);
        service.getSubscriber(subscriberId1).addNotificationType(NotificationType.EMAIL);
        service.getSubscriber(subscriberId1).addNotificationType(NotificationType.SMS);
        service.getSubscriber(subscriberId2).addNotificationType(NotificationType.WHATSAPP);
        service.getSubscriber(subscriberId2).addNotificationType(NotificationType.EMAIL);
        service.publishToChannel(channelId1, "Hi, New Video Uploaded");
        service.publishToChannel(channelId1, "Hi, New Video Uploaded");
        
        service.publishToChannel(channelId2, "Hi, New video uploaded");
    }
}