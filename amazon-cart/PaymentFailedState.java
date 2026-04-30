public class PaymentFailedState implements OrderState {

    @Override
    public void confirm(Order order) {
        // payment retry successful
        System.out.println("Payment retry successful. Order confirmed.");
        order.setState(new ConfirmedState());
    }

    @Override
    public void ship(Order order) {
        System.out.println("Cannot ship — payment failed.");
    }

    @Override
    public void deliver(Order order) {
        System.out.println("Cannot deliver — payment failed.");
    }

    @Override
    public void cancel(Order order) {
        System.out.println("Order cancelled after payment failure.");
        order.setState(new CancelledState());
    }

    @Override
    public void paymentFailed(Order order) {
        System.out.println("Payment already failed.");
    }
}