public class Main {
    public static void main(String[] args) {
        VendingMachine machine = new VendingMachine();
        Product p1 = new Product("Coke", 20);
        Product p2 = new Product("Chocolate", 45);
        Product p3 = new Product("Chips", 30);
        Product p4 = new Product("Biscuit", 75);
        machine.restock(p1, 2);
        machine.restock(p2, 5);
        machine.restock(p3, 1);
        machine.restock(p4, 8);

        machine.insertMoney(30);
        machine.selectProduct(p1);
        machine.dispense(p1);
        machine.refund();

        machine.selectProduct(p3);

        machine.insertMoney(10);
        machine.dispense(p3);
        machine.selectProduct(p3);
        System.out.print(machine.getState());
        machine.refund();
        machine.dispense(p3);
    }   
}