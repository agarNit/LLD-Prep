public class Main {
    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        run("Request equality + hashCode", Main::testRequestEqualityAndHash);
        run("Elevator.addRequest validates floors + duplicates", Main::testElevatorAddRequestValidation);
        run("Elevator.step transitions IDLE->UP and moves toward request", Main::testElevatorStepMovesTowardRequest);
        run("Elevator.step with empty requests stays IDLE", Main::testElevatorStepEmptyStaysIdle);
        run("ElevatorController basic request validation smoke test", Main::testElevatorControllerSmoke);

        System.out.println();
        System.out.println("Passed: " + passed + ", Failed: " + failed);
        if (failed > 0) {
            System.exit(1);
        }
    }

    private static void run(String name, Runnable test) {
        try {
            test.run();
            passed++;
            System.out.println("[PASS] " + name);
        } catch (Throwable t) {
            failed++;
            System.out.println("[FAIL] " + name + " -> " + t.getClass().getSimpleName() + ": " + t.getMessage());
        }
    }

    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }

    private static void testRequestEqualityAndHash() {
        Request r1 = new Request(5, RequestType.PICKUP_UP);
        Request r2 = new Request(5, RequestType.PICKUP_UP);
        Request r3 = new Request(6, RequestType.PICKUP_UP);
        Request r4 = new Request(5, RequestType.PICKUP_DOWN);

        check(r1.equals(r2), "Requests with same floor/type should be equal");
        check(r1.hashCode() == r2.hashCode(), "Equal requests should have same hashCode");
        check(!r1.equals(r3), "Requests with different floor should not be equal");
        check(!r1.equals(r4), "Requests with different type should not be equal");
    }

    private static void testElevatorAddRequestValidation() {
        Elevator e = new Elevator();

        check(!e.addRequest(-1, RequestType.PICKUP_UP), "Should reject floors < 0");
        check(!e.addRequest(10, RequestType.PICKUP_UP), "Should reject floors > 9");

        // Elevator starts at floor 0; by design addRequest returns true when floor == curFloor.
        check(e.addRequest(0, RequestType.PICKUP_UP), "Should accept request at current floor");
        check(e.addRequest(0, RequestType.PICKUP_UP), "Should still return true for request at current floor");

        check(e.addRequest(3, RequestType.PICKUP_UP), "Should add new request");
        check(!e.addRequest(3, RequestType.PICKUP_UP), "Should reject duplicate request");
    }

    private static void testElevatorStepEmptyStaysIdle() {
        Elevator e = new Elevator();
        check(e.getDirection() == Direction.IDLE, "New elevator should start IDLE");
        e.step();
        check(e.getDirection() == Direction.IDLE, "Empty elevator should stay IDLE");
        check(e.getCurrentFloor() == 0, "Empty elevator should not move");
    }

    private static void testElevatorStepMovesTowardRequest() {
        Elevator e = new Elevator();
        check(e.getDirection() == Direction.IDLE, "New elevator should start IDLE");

        check(e.addRequest(2, RequestType.PICKUP_UP), "Should add request");
        check(e.getCurrentFloor() == 0, "Should start at floor 0");

        e.step(); // should establish UP and move one floor
        check(e.getDirection() == Direction.UP, "After first step with pending higher request, direction should be UP");
        check(e.getCurrentFloor() == 1, "After first step, elevator should move to floor 1");

        e.step();
        check(e.getDirection() == Direction.UP, "Second step should still be moving UP");
        check(e.getCurrentFloor() == 2, "Second step should move to floor 2");

        // At floor 2, the current pickupRequest matches and should clear all requests.
        e.step();
        check(e.getDirection() == Direction.IDLE, "After reaching request floor, elevator should become IDLE");
        check(e.getCurrentFloor() == 2, "Elevator should remain at the cleared request floor");
    }

    private static void testElevatorControllerSmoke() {
        ElevatorController controller = new ElevatorController();

        check(!controller.requestElevator(-1, Direction.UP), "Controller should reject invalid floors");
        check(!controller.requestElevator(5, Direction.IDLE), "Controller should reject Direction.IDLE requests");

        boolean accepted = controller.requestElevator(5, Direction.UP);
        check(accepted, "Controller should accept a valid request");

        // Just ensure stepping doesn't throw and system can accept more work afterward.
        for (int i = 0; i < 10; i++) {
            controller.step();
        }

        check(controller.requestElevator(1, Direction.DOWN), "Controller should accept another valid request after stepping");
    }
}

