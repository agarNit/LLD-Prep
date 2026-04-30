public class DeliveredState implements OrderState {

    @Override
    public void confirm(Order order) {
        System.out.println("Order already delivered.");
    }

    @Override
    public void ship(Order order) {
        System.out.println("Order already delivered.");
    }

    @Override
    public void deliver(Order order) {
        System.out.println("Order already delivered.");
    }

    @Override
    public void cancel(Order order) {
        System.out.println("Cannot cancel — order already delivered.");
    }

    @Override
    public void paymentFailed(Order order) {
        System.out.println("Order already delivered.");
    }
}