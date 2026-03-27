import java.util.*;

// Represents an optional service
class AddOnService {
    private String serviceName;
    private double cost;

    public AddOnService(String serviceName, double cost) {
        this.serviceName = serviceName;
        this.cost = cost;
    }

    public String getServiceName() {
        return serviceName;
    }

    public double getCost() {
        return cost;
    }

    @Override
    public String toString() {
        return serviceName + " (₹" + cost + ")";
    }
}

// Represents a reservation (core entity remains unchanged)
class Reservation {
    private String reservationId;
    private String guestName;

    public Reservation(String reservationId, String guestName) {
        this.reservationId = reservationId;
        this.guestName = guestName;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getGuestName() {
        return guestName;
    }
}

// Manages add-on services for reservations
class AddOnServiceManager {

    // Map: Reservation ID -> List of Services
    private Map<String, List<AddOnService>> serviceMap;

    public AddOnServiceManager() {
        serviceMap = new HashMap<>();
    }

    // Add service to reservation
    public void addService(String reservationId, AddOnService service) {
        serviceMap.putIfAbsent(reservationId, new ArrayList<>());
        serviceMap.get(reservationId).add(service);
    }

    // Get services for a reservation
    public List<AddOnService> getServices(String reservationId) {
        return serviceMap.getOrDefault(reservationId, new ArrayList<>());
    }

    // Calculate total add-on cost
    public double calculateTotalCost(String reservationId) {
        double total = 0.0;
        List<AddOnService> services = serviceMap.get(reservationId);

        if (services != null) {
            for (AddOnService service : services) {
                total += service.getCost();
            }
        }
        return total;
    }
}

// Main class
public class BookMyStayApp {

    public static void main(String[] args) {

        // Sample reservation (already created from previous use cases)
        Reservation reservation = new Reservation("R101", "Rakshit");

        // Add-on service manager
        AddOnServiceManager manager = new AddOnServiceManager();

        // Guest selects services
        manager.addService(reservation.getReservationId(), new AddOnService("Breakfast", 500));
        manager.addService(reservation.getReservationId(), new AddOnService("Airport Pickup", 1200));
        manager.addService(reservation.getReservationId(), new AddOnService("Extra Bed", 800));

        // Fetch and display services
        System.out.println("Reservation ID: " + reservation.getReservationId());
        System.out.println("Guest Name: " + reservation.getGuestName());

        System.out.println("\nSelected Add-On Services:");
        List<AddOnService> services = manager.getServices(reservation.getReservationId());

        for (AddOnService service : services) {
            System.out.println("- " + service);
        }

        // Calculate total cost
        double totalCost = manager.calculateTotalCost(reservation.getReservationId());
        System.out.println("\nTotal Add-On Cost: ₹" + totalCost);
    }
}