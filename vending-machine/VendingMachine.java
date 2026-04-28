public class VendingMachine {
    private State currentState;
    private Inventory inventory;
    private int balance;

    VendingMachine() {
        this.currentState = new IdleState();
        this.balance = 0;
        this.inventory = new Inventory();
    }

    public void setState(State state) {
        this.currentState = state;
    }

    public State getState() {
        return currentState;
    }

    public void addBalance(int amount) {
        this.balance += amount;
    }

    public int getBalance() {
        return balance;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void insertMoney(int amount) {
        currentState.insertMoney(this, amount);
    }

    public void selectProduct(Product p) {
        currentState.selectProduct(this, p);
    }

    public void dispense(Product p) {
        currentState.dispense(this, p);
    }

    public void refund() {
        currentState.refund(this);
    }

    public void restock(Product p, int quantity) {
        inventory.addStock(p, quantity);
        if (currentState instanceof OutOfStockState) {
            currentState = new IdleState();
        }
        System.out.println("Restocked product " + p.getName() + " with quantity " + quantity);
    }

    public void refundBalance() {
        System.out.println("Refunding: $" + balance);
        this.balance = 0;
    }

    public void resetBalance() {
        this.balance = 0;
    }

}