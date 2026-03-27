import java.util.*;

// -------------------- Reservation --------------------

class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;
    private String roomId;
    private boolean isCancelled;

    public Reservation(String reservationId, String guestName, String roomType, String roomId) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.roomId = roomId;
        this.isCancelled = false;
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

    public String getRoomId() {
        return roomId;
    }

    public boolean isCancelled() {
        return isCancelled;
    }

    public void cancel() {
        this.isCancelled = true;
    }

    @Override
    public String toString() {
        return "Reservation ID: " + reservationId +
                ", Guest: " + guestName +
                ", Room Type: " + roomType +
                ", Room ID: " + roomId +
                ", Status: " + (isCancelled ? "Cancelled" : "Active");
    }
}

// -------------------- Inventory Manager --------------------

class InventoryManager {

    private Map<String, Integer> roomInventory;
    private Map<String, Stack<String>> availableRooms;

    public InventoryManager() {
        roomInventory = new HashMap<>();
        availableRooms = new HashMap<>();

        // Initialize inventory
        roomInventory.put("Standard", 2);
        roomInventory.put("Deluxe", 1);
        roomInventory.put("Suite", 1);

        availableRooms.put("Standard", new Stack<>());
        availableRooms.put("Deluxe", new Stack<>());
        availableRooms.put("Suite", new Stack<>());

        // Preload room IDs
        availableRooms.get("Standard").push("S1");
        availableRooms.get("Standard").push("S2");
        availableRooms.get("Deluxe").push("D1");
        availableRooms.get("Suite").push("SU1");
    }

    // Allocate room
    public String allocateRoom(String roomType) {
        if (!availableRooms.containsKey(roomType) || availableRooms.get(roomType).isEmpty()) {
            return null;
        }

        String roomId = availableRooms.get(roomType).pop();
        roomInventory.put(roomType, roomInventory.get(roomType) - 1);
        return roomId;
    }

    // Rollback (release room)
    public void releaseRoom(String roomType, String roomId) {
        availableRooms.get(roomType).push(roomId);
        roomInventory.put(roomType, roomInventory.get(roomType) + 1);
    }

    public void displayInventory() {
        System.out.println("\nCurrent Inventory:");
        for (String type : roomInventory.keySet()) {
            System.out.println(type + ": " + roomInventory.get(type));
        }
    }
}

// -------------------- Booking Store --------------------

class BookingStore {
    private Map<String, Reservation> reservations;

    public BookingStore() {
        reservations = new HashMap<>();
    }

    public void addReservation(Reservation r) {
        reservations.put(r.getReservationId(), r);
    }

    public Reservation getReservation(String id) {
        return reservations.get(id);
    }
}

// -------------------- Cancellation Service --------------------

class CancellationService {

    private InventoryManager inventoryManager;
    private BookingStore bookingStore;

    public CancellationService(InventoryManager inventoryManager, BookingStore bookingStore) {
        this.inventoryManager = inventoryManager;
        this.bookingStore = bookingStore;
    }

    public void cancelBooking(String reservationId) {

        Reservation reservation = bookingStore.getReservation(reservationId);

        // Validation
        if (reservation == null) {
            System.out.println("\nCancellation Failed: Reservation not found.");
            return;
        }

        if (reservation.isCancelled()) {
            System.out.println("\nCancellation Failed: Already cancelled.");
            return;
        }

        // LIFO rollback (release room)
        inventoryManager.releaseRoom(
                reservation.getRoomType(),
                reservation.getRoomId()
        );

        // Update state
        reservation.cancel();

        System.out.println("\nCancellation Successful:");
        System.out.println(reservation);
    }
}

// -------------------- Main --------------------

public class BookMyStayApp {

    public static void main(String[] args) {

        InventoryManager inventoryManager = new InventoryManager();
        BookingStore bookingStore = new BookingStore();

        // Simulate bookings
        String room1 = inventoryManager.allocateRoom("Standard");
        Reservation r1 = new Reservation("R301", "Rakshit", "Standard", room1);
        bookingStore.addReservation(r1);

        String room2 = inventoryManager.allocateRoom("Deluxe");
        Reservation r2 = new Reservation("R302", "Amit", "Deluxe", room2);
        bookingStore.addReservation(r2);

        System.out.println("Initial Bookings:");
        System.out.println(r1);
        System.out.println(r2);

        // Cancellation service
        CancellationService cancellationService =
                new CancellationService(inventoryManager, bookingStore);

        // Valid cancellation
        cancellationService.cancelBooking("R301");

        // Invalid cancellation (already cancelled)
        cancellationService.cancelBooking("R301");

        // Invalid cancellation (non-existent)
        cancellationService.cancelBooking("R999");

        // Final inventory
        inventoryManager.displayInventory();
    }
}