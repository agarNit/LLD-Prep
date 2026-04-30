public class ShippedState implements OrderState {

    @Override
    public void confirm(Order order) {
        System.out.println("Order already confirmed.");
    }

    @Override
    public void ship(Order order) {
        System.out.println("Order already shipped.");
    }

    @Override
    public void deliver(Order order) {
        System.out.println("Order delivered successfully.");
        order.setState(new DeliveredState());
    }

    @Override
    public void cancel(Order order) {
        System.out.println("Cannot cancel — order already shipped.");
    }

    @Override
    public void paymentFailed(Order order) {
        System.out.println("Payment already completed.");
    }
}