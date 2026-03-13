import java.util.*;

public class ParkingLot {
    private final List<ParkingSpot> spots;
    private final long hourlyCents;
    private final Map<String, Ticket> activeTickets;
    private final Set<String> occupiedSpotIds;

    public ParkingLot(List<ParkingSpot> spots, long hourlyCents) {
        this.spots = spots;
        this.hourlyCents = hourlyCents;
        this.activeTickets = new HashMap<>();
        this.occupiedSpotIds = new HashSet<>();
    }

    public Ticket enter(VehicleType vehicleType) {
        ParkingSpot spot = getAvailableSpot(vehicleType);
        if (spot == null) {
            throw new RuntimeException("No available spots for vehicle type: " + vehicleType);
        }

        occupiedSpotIds.add(spot.getId());

        String ticketId = UUID.randomUUID().toString();
        long entryTime = System.currentTimeMillis();
        Ticket ticket = new Ticket(ticketId, spot.getId(), vehicleType, entryTime);

        activeTickets.put(ticketId, ticket);
        return ticket;
    }

    public long exit(String ticketId) {
        if (ticketId == null || ticketId.isEmpty()) {
            throw new RuntimeException("Invalid ticket Id");
        }

        Ticket ticket = activeTickets.get(ticketId);
        if (ticket == null) {
            throw new RuntimeException("Ticket not found or already used");
        }

        long exitTime = System.currentTimeMillis();
        long fee = computeFee(ticket.getEntryTime(), exitTime);

        occupiedSpotIds.remove(ticket.getSpotId());
        activeTickets.remove(ticketId);

        return fee;
    }

    private ParkingSpot getAvailableSpot(VehicleType vehicleType) {
        SpotType spotType = mapVehicleTypeToSpotType(vehicleType);
        for (ParkingSpot spot: spots) {
            if (!occupiedSpotIds.contains(spot.getId()) && spot.getSpotType() == spotType) {
                return spot;
            }
        }
        return null;
    }

    private SpotType mapVehicleTypeToSpotType(VehicleType vehicleType) {
        if (vehicleType == VehicleType.MOTORCYCLE) 
            return SpotType.MOTORCYCLE;
        if (vehicleType == VehicleType.CAR) 
            return SpotType.CAR;
        if (vehicleType == VehicleType.LARGE) 
            return SpotType.LARGE;
        throw new RuntimeException("Unknown vehicle type");
    }

    private long computeFee(long entryTime, long exitTime) {
        long timePassed = exitTime - entryTime;
        long durationInHours = timePassed/(1000*60*60);
        long remainingTime = timePassed%(1000*60*60);

        if (remainingTime > 0) {
            durationInHours++;
        }

        return hourlyCents*durationInHours;
    }


}