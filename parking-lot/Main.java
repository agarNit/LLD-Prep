import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        runAllTests();
    }

    private static void runAllTests() {
        System.out.println("Running ParkingLot tests...");
        testEnterAndExitCalculatesAtLeastOneHour();
        testExitWithInvalidTicketThrows();
        testFullLotForVehicleTypeThrows();
        System.out.println("All tests finished.");
    }

    private static void testEnterAndExitCalculatesAtLeastOneHour() {
        List<ParkingSpot> spots = Arrays.asList(
                new ParkingSpot("S1", SpotType.CAR)
        );
        long hourlyCents = 500;
        ParkingLot lot = new ParkingLot(spots, hourlyCents);

        Ticket ticket = lot.enter(VehicleType.CAR);

        // Ensure at least 1ms passes so remainingTime > 0
        try {
            Thread.sleep(2);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        long fee = lot.exit(ticket.getId());

        assertEquals("testEnterAndExitCalculatesAtLeastOneHour", hourlyCents, fee);
    }

    private static void testExitWithInvalidTicketThrows() {
        List<ParkingSpot> spots = Arrays.asList(
                new ParkingSpot("S1", SpotType.CAR)
        );
        ParkingLot lot = new ParkingLot(spots, 500);

        assertThrows("testExitWithInvalidTicketThrows (null id)",
                () -> lot.exit(null));

        assertThrows("testExitWithInvalidTicketThrows (empty id)",
                () -> lot.exit(""));

        assertThrows("testExitWithInvalidTicketThrows (unknown id)",
                () -> lot.exit("unknown-ticket-id"));
    }

    private static void testFullLotForVehicleTypeThrows() {
        List<ParkingSpot> spots = Arrays.asList(
                new ParkingSpot("S1", SpotType.CAR) // Only one car spot
        );
        ParkingLot lot = new ParkingLot(spots, 500);

        // First car should get the only spot
        lot.enter(VehicleType.CAR);

        // Second car should fail
        assertThrows("testFullLotForVehicleTypeThrows",
                () -> lot.enter(VehicleType.CAR));
    }

    private static void assertEquals(String testName, long expected, long actual) {
        if (expected != actual) {
            throw new AssertionError(testName + " failed: expected " + expected + " but got " + actual);
        }
        System.out.println(testName + " passed.");
    }

    private static void assertThrows(String testName, Runnable runnable) {
        try {
            runnable.run();
        } catch (RuntimeException e) {
            System.out.println(testName + " passed (caught expected RuntimeException: " + e.getMessage() + ").");
            return;
        }
        throw new AssertionError(testName + " failed: expected RuntimeException but none was thrown.");
    }
}
