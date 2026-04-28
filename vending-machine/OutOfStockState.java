public class OutOfStockState implements State {
    
    public void insertMoney(VendingMachine machine, int amount) {
        System.out.println("Machine is out of stock. Returning your money: $" + amount);
    }

    public void selectProduct(VendingMachine machine, Product p) {
        System.out.println("Machine is out of stock. No products available.");
    }

    public void dispense(VendingMachine machine, Product p) {
        System.out.println("Machine is out of stock. Cannot dispense.");
    }

    public void refund(VendingMachine machine) {
        if (machine.getBalance() > 0) {
            machine.refundBalance();
        } else {
            System.out.println("No balance to refund.");
        }
    }

    public void onRestock(VendingMachine machine) {
        machine.setState(new IdleState());
    }
}