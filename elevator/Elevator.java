import java.util.HashSet;
import java.util.Set;

class Elevator {

    private int curFloor;
    private Direction direction;
    private Set<Request> requests;

    public Elevator() {
        this.curFloor = 0;
        this.direction = Direction.IDLE;
        this.requests = new HashSet<>();
    }

    public int getCurrentFloor() {
        return curFloor;
    }

    public Direction getDirection() {
        return direction;
    }

    public boolean addRequest(int floor, RequestType type) {
        if (floor < 0 || floor > 9) {
            return false;
        }
        if (floor == curFloor) {
            return true;
        }
        Request request = new Request(floor, type);
        if (requests.contains(request)) {
            return false;
        }
        return requests.add(request);
    }

    public void step() {
        if (requests.isEmpty()) {
            direction = Direction.IDLE;
            return;
        }

        if (direction == Direction.IDLE) {
            // Find nearest request to establish initial direction (deterministic)
            Request nearest = null;
            int minDistance = Integer.MAX_VALUE;
            for (Request req: requests) {
                int distance = Math.abs(req.getFloor() - curFloor);
                if (distance < minDistance || (distance == minDistance && (nearest == null || req.getFloor() < nearest.getFloor()))) {
                    minDistance = distance;
                    nearest = req;
                }
            }
            direction = (nearest.getFloor() > curFloor) ? Direction.UP : Direction.DOWN;
        }

        RequestType pickupType = (direction == Direction.UP) ? RequestType.PICKUP_UP : RequestType.PICKUP_DOWN;
        Request pickupRequest = new Request(curFloor, pickupType);
        Request destinationRequest = new Request(curFloor, RequestType.DESTINATION);

        if (requests.contains(pickupRequest) || requests.contains(destinationRequest)) {
            requests.remove(pickupRequest);
            requests.remove(destinationRequest);

            if (requests.isEmpty()) {
                direction = Direction.IDLE;
                return;
            }
            if (!hasRequestAhead(direction)) {
                direction = (direction == Direction.UP) ? Direction.DOWN : Direction.UP;
            }
            return;
        }

        if (!hasRequestAhead(direction)) {
            direction = (direction == Direction.UP) ? Direction.DOWN : Direction.UP;
            return;
        }

        if (direction == Direction.UP) {
            curFloor++;
        } else if (direction == Direction.DOWN) {
            curFloor--;
        }
    }

    public boolean hasRequestAhead(Direction dir) {
        for (Request request: requests) {
            if (dir == Direction.UP && request.getFloor() > curFloor) {
                return true;
            }
            if (dir == Direction.DOWN && request.getFloor() < curFloor) {
                return true;
            }
        }
        return false;
    }

    public boolean hasRequestsAtOrBeyond(int floor, Direction dir) {
        for (Request request : requests) {
            if (dir == Direction.UP && request.getFloor() >= floor) {
                if (request.getType() == RequestType.PICKUP_UP || request.getType() == RequestType.DESTINATION) {
                    return true;
                }
            }
            if (dir == Direction.DOWN && request.getFloor() <= floor) {
                if (request.getType() == RequestType.PICKUP_DOWN || request.getType() == RequestType.DESTINATION) {
                    return true;
                }
            }
        }
        return false;
    }

}