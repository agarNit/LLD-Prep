public class HasMoneyState implements State {
    
    public void insertMoney(VendingMachine machine, int amount) {
        machine.addBalance(amount);
        System.out.println("Additional money inserted: $" + amount + " | Total balance: $" + machine.getBalance());
    }

    public void selectProduct(VendingMachine machine, Product p) {
        if (!machine.getInventory().isAvailable(p)) {
            System.out.println("Sorry, " + p.getName() + " is out of stock. Refunding...");
            machine.refundBalance();
            machine.setState(new IdleState());
            return;
        }

        if (machine.getBalance() < p.getPrice()) {
            System.out.println("Insufficient balance. Product costs $" + p.getPrice() + " | Your balance: $" + machine.getBalance());
            return;
        }

        machine.setState(new DispensingState());
        machine.dispense(p);
    }

    public void dispense(VendingMachine machine, Product p) {
        System.out.println("Please select a product first.");
    }

    public void refund(VendingMachine machine) {
        machine.resetBalance();
        machine.setState(new IdleState());
    }   
}