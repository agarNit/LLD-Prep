public class DispensingState implements State {

    public void insertMoney(VendingMachine machine, int amount) {
        System.out.println("Please wait, dispensing is in progress.");
    }

    public void selectProduct(VendingMachine machine, Product p) {
        System.out.println("Please wait, dispensing is in progress.");
    }

    public void dispense(VendingMachine machine, Product p) {
        machine.getInventory().reduceStock(p);
        int change = machine.getBalance() - p.getPrice();
        if (change > 0) {
            System.out.println("Refunding change: $ " + change );
        }
        machine.resetBalance();
        if (machine.getInventory().isEmpty()) {
            System.out.println("Machine is now out of stock.");
            machine.setState(new OutOfStockState());
        } else {
            machine.setState(new IdleState());
        }
    }

    public void refund(VendingMachine machine) {
        System.out.println("Cannot refund while dispensing is in progress.");
    }

    public void onRestock(VendingMachine machine) {}
}