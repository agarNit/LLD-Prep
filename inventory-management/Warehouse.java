import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Warehouse {
    private final String id;
    private final Map<String, Integer> inventory;
    private final Map<String, List<AlertConfig>> alertConfigs;

    public Warehouse(String id) {
        this.id = id;
        this.inventory = new HashMap<>();
        this.alertConfigs = new HashMap<>();
    }

    public String getId() {
        return id;
    }

    public void addStock(String productId, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }

        List<AlertsToFire> alertsToFire = null;

        synchronized (this) {
            int currQuantity = inventory.getOrDefault(productId, 0);
            int newQuantity = currQuantity + quantity;
            inventory.put(productId, newQuantity);

            alertsToFire = getAlertsToFire(productId, currQuantity, newQuantity);
        }

        if (alertsToFire != null) {
            fireAlerts(alertsToFire);
        }
    }

    public boolean removeStock(String productId, int quantity) {
        List<AlertsToFire> alertsToFire = null;

        synchronized (this) {
            if (quantity <= 0) {
                return false;
            }

            int currQuantity = inventory.getOrDefault(productId, 0);
            if (currQuantity - quantity < 0) {
                return false;
            }
            int newQuantity = currQuantity - quantity;
            inventory.put(productId, newQuantity);

            alertsToFire = getAlertsToFire(productId, currQuantity, newQuantity);
        }

        if (alertsToFire != null) {
            fireAlerts(alertsToFire);
        }

        return true;
    }

    public synchronized void setLowStockAlert(String productId, int threshold, AlertListener listener) {
        if (threshold <=0 ) {
            throw new IllegalArgumentException("Threshold must be positive");
        }
        if (listener == null) {
            throw new IllegalArgumentException("Listener cannot be null");
        }
        alertConfigs.computeIfAbsent(productId, k -> new ArrayList<>());
        alertConfigs.get(productId).add(new AlertConfig(threshold, listener));
    }

    private List<AlertsToFire> getAlertsToFire(String productId, int prevQuantity, int newQuantity) {
        List<AlertConfig> configs = alertConfigs.get(productId);
        if (configs == null) {
            return null;
        }

        List<AlertsToFire> alertsToFire = new ArrayList<>();

        for (AlertConfig config: configs) {
            if (prevQuantity >= config.getThreshold() && newQuantity < config.getThreshold()) {
                alertsToFire.add(new AlertsToFire(config.getListener(), productId, newQuantity));
            }
        }

        return alertsToFire.isEmpty() ? null : alertsToFire;
    }

    private void fireAlerts(List<AlertsToFire> alerts) {
        for (AlertsToFire alert: alerts) {
            alert.listener.onLowStock(id, alert.productId, alert.quantity);
        }
    }

    private static class AlertsToFire {
        final AlertListener listener;
        final String productId;
        final int quantity;

        AlertsToFire(AlertListener listener, String productId, int quantity) {
            this.listener = listener;
            this.productId = productId;
            this.quantity = quantity;
        }
    }

}