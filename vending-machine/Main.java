public class Main {
    public static void main(String[] args) {
        VendingMachine machine = new VendingMachine();
        Product coke = new Product("Coke", 20);
        Product chips = new Product("Chips", 30);
        Product chocolate = new Product("Chocolate", 45);

        machine.restock(coke, 1);
        machine.restock(chips, 1);
        machine.restock(chocolate, 2);

        System.out.println("\n--- Scenario 1: Happy path with change ---");
        machine.insertMoney(50);
        machine.selectProduct(coke); // costs $20, change $30 returned

        System.out.println("\n--- Scenario 2: Insufficient balance ---");
        machine.insertMoney(10);
        machine.selectProduct(chocolate); // costs $45, balance only $10

        System.out.println("\n--- Scenario 3: Top up and buy ---");
        machine.insertMoney(40); // total now $50
        machine.selectProduct(chocolate); // costs $45, change $5 returned

        System.out.println("\n--- Scenario 4: Refund ---");
        machine.insertMoney(30);
        machine.refund();

        System.out.println("\n--- Scenario 5: Machine goes fully out of stock ---");
        machine.insertMoney(45);
        machine.selectProduct(chocolate); // buys last chocolate → coke=0, chips=1, chocolate=0
        machine.insertMoney(30);
        machine.selectProduct(chips);    // buys last chips → all stock = 0 → OutOfStockState
        machine.insertMoney(30);         // machine is OutOfStockState, returns money immediately

        System.out.println("\n--- Scenario 6: Restock brings machine back ---");
        machine.restock(chips, 3);       // onRestock: OutOfStockState → IdleState
        machine.insertMoney(30);
        machine.selectProduct(chips);    // works now
    }
}
