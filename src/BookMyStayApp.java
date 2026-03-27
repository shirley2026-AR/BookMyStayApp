import java.util.*;

// Core Reservation class (unchanged from previous use cases)
class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;
    private double baseCost;

    public Reservation(String reservationId, String guestName, String roomType, double baseCost) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.baseCost = baseCost;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public double getBaseCost() {
        return baseCost;
    }

    @Override
    public String toString() {
        return "Reservation ID: " + reservationId +
                ", Guest: " + guestName +
                ", Room: " + roomType +
                ", Cost: ₹" + baseCost;
    }
}

// Booking History (stores confirmed reservations)
class BookingHistory {

    // List preserves insertion order (chronological tracking)
    private List<Reservation> history;

    public BookingHistory() {
        history = new ArrayList<>();
    }

    // Add confirmed reservation
    public void addReservation(Reservation reservation) {
        history.add(reservation);
    }

    // Retrieve all reservations
    public List<Reservation> getAllReservations() {
        return new ArrayList<>(history); // return copy (immutability principle)
    }
}

// Reporting Service (separate from storage)
class BookingReportService {

    // Display all bookings
    public void printAllBookings(List<Reservation> reservations) {
        System.out.println("\n--- Booking History ---");
        for (Reservation r : reservations) {
            System.out.println(r);
        }
    }

    // Generate summary report
    public void generateSummary(List<Reservation> reservations) {
        int totalBookings = reservations.size();
        double totalRevenue = 0.0;

        Map<String, Integer> roomTypeCount = new HashMap<>();

        for (Reservation r : reservations) {
            totalRevenue += r.getBaseCost();

            roomTypeCount.put(
                    r.getRoomType(),
                    roomTypeCount.getOrDefault(r.getRoomType(), 0) + 1
            );
        }

        System.out.println("\n--- Booking Summary Report ---");
        System.out.println("Total Bookings: " + totalBookings);
        System.out.println("Total Revenue: ₹" + totalRevenue);

        System.out.println("\nRoom Type Distribution:");
        for (String type : roomTypeCount.keySet()) {
            System.out.println(type + ": " + roomTypeCount.get(type));
        }
    }
}

// Main class
public class BookMyStayApp {

    public static void main(String[] args) {

        // Booking history storage
        BookingHistory bookingHistory = new BookingHistory();

        // Simulating confirmed bookings
        bookingHistory.addReservation(new Reservation("R101", "Rakshit", "Deluxe", 3000));
        bookingHistory.addReservation(new Reservation("R102", "Amit", "Standard", 2000));
        bookingHistory.addReservation(new Reservation("R103", "Neha", "Suite", 5000));
        bookingHistory.addReservation(new Reservation("R104", "Priya", "Deluxe", 3200));

        // Reporting service
        BookingReportService reportService = new BookingReportService();

        // Retrieve history (read-only usage)
        List<Reservation> reservations = bookingHistory.getAllReservations();

        // Display data
        reportService.printAllBookings(reservations);

        // Generate report
        reportService.generateSummary(reservations);
    }
}