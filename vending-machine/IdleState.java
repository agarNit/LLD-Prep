public class IdleState implements State {

    public void insertMoney(VendingMachine machine, int amount) {
        machine.addBalance(amount);
        System.out.println("Money inserted: $" + amount);
        machine.setState(new HasMoneyState());
    }

    public void selectProduct(VendingMachine machine, Product p) {
        System.out.println("Please insert money first.");
    }

    public void dispense(VendingMachine machine, Product p) {
        System.out.println("Please insert money and select a product first.");
    }

    public void refund(VendingMachine machine) {
        System.out.println("No money inserted. Nothing to refund.");
    }
}