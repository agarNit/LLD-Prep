public class CancelledState implements OrderState {

    @Override
    public void confirm(Order order) {
        System.out.println("Cannot confirm — order already cancelled.");
    }

    @Override
    public void ship(Order order) {
        System.out.println("Cannot ship — order already cancelled.");
    }

    @Override
    public void deliver(Order order) {
        System.out.println("Cannot deliver — order already cancelled.");
    }

    @Override
    public void cancel(Order order) {
        System.out.println("Order already cancelled.");
    }

    @Override
    public void paymentFailed(Order order) {
        System.out.println("Cannot process — order already cancelled.");
    }
}