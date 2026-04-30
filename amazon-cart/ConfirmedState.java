public class ConfirmedState implements OrderState {

    @Override
    public void confirm(Order order) {
        System.out.println("Order already confirmed.");
    }

    @Override
    public void ship(Order order) {
        System.out.println("Order shipped successfully.");
        order.setState(new ShippedState());
    }

    @Override
    public void deliver(Order order) {
        System.out.println("Cannot deliver — order not shipped yet.");
    }

    @Override
    public void cancel(Order order) {
        for (CartItem item : order.getItems()) {
            order.getInventory().restoreStock(item.getVariant().getId(), item.getQuantity());
        }
        order.getPayment().refund();
        System.out.println("Order cancelled. Refund initiated.");
        order.setState(new CancelledState());
    }

    @Override
    public void paymentFailed(Order order) {
        System.out.println("Payment already completed.");
    }
}