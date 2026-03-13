import java.util.HashMap;
import java.util.List;
import java.util.Map;

class InventoryManager {
    private final Map<String, Warehouse> warehouses;

    public InventoryManager(List<String> warehouseIds) {
        this.warehouses = new HashMap<>();
        for (String id : warehouseIds) {
            warehouses.put(id, new Warehouse(id));
        }
    }

    public void addStock(String warehouseId, String productId, int quantity) {
        Warehouse warehouse = this.warehouses.get(warehouseId);
        if (warehouse == null) {
            throw new IllegalArgumentException("Warehouse " + warehouseId + " not found");
        }
        warehouse.addStock(productId, quantity);
    }

    public boolean removeStock(String warehouseId, String productId, int quantity) {
        Warehouse warehouse = this.warehouses.get(warehouseId);
        if (warehouse == null) {
            throw new IllegalArgumentException("Warehouse " + warehouseId + " not found");
        }
        return warehouse.removeStock(productId, quantity);
    }

    public boolean transfer(String productId, int quantity, String fromWareHouseId, String toWareHouseId) {
        if (quantity <= 0) {
            return false;
        }

        if (toWareHouseId.equals(fromWareHouseId)) {
            return false;
        }

        Warehouse toWarehouse = warehouses.get(toWareHouseId);
        Warehouse fromWarehouse = warehouses.get(fromWareHouseId);

        if (fromWarehouse == null || toWarehouse == null) {
            return false;
        }

        String firstId = toWareHouseId.compareTo(fromWareHouseId) < 0 ? fromWareHouseId : toWareHouseId;
        String secondId = toWareHouseId.compareTo(fromWareHouseId) < 0 ? toWareHouseId : fromWareHouseId;

        Warehouse firstLock = warehouses.get(firstId);
        Warehouse secondLock = warehouses.get(secondId);

        synchronized (firstLock) {
            synchronized (secondLock) {
                if (!fromWarehouse.removeStock(productId, quantity)) {
                    return false;
                }
                toWarehouse.addStock(productId, quantity);
            }
        }
        return true;
    }

    public void setLowStockAlert(String warehouseId, String productId, int threshold, AlertListener listener) {
        Warehouse warehouse = warehouses.get(warehouseId);
        if (warehouse == null) {
            throw new IllegalArgumentException("Warehouse " + warehouseId + " not found");
        }
        warehouse.setLowStockAlert(productId, threshold, listener);
    }


}