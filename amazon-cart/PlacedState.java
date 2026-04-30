public class PlacedState implements OrderState {

    public void confirm(Order order) {
        System.out.println("Payment successful. Order confirmed.");
        order.setState(new ConfirmedState());
    }

    public void ship(Order order) {
        System.out.println("Cannot ship — order not yet confirmed.");
    }

    public void deliver(Order order) {
        System.out.println("Cannot deliver — order not yet confirmed.");
    }

    public void cancel(Order order) {
        for (CartItem item : order.getItems()) {
            order.getInventory().restoreStock(item.getVariant().getId(), item.getQuantity());
        }
        System.out.println("Order cancelled successfully.");
        order.setState(new CancelledState());
    }

    public void paymentFailed(Order order) {
        System.out.println("Payment failed. Please retry.");
        order.setState(new PaymentFailedState());
    }
}