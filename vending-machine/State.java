public interface State {
    void insertMoney(VendingMachine machine, int money);
    void selectProduct(VendingMachine machine, Product p);
    void dispense(VendingMachine machine, Product p);
    void refund(VendingMachine machine);
}