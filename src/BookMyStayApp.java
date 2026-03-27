import java.util.*;

// -------------------- Custom Exceptions --------------------

class InvalidRoomTypeException extends Exception {
    public InvalidRoomTypeException(String message) {
        super(message);
    }
}

class InsufficientRoomsException extends Exception {
    public InsufficientRoomsException(String message) {
        super(message);
    }
}

// -------------------- Reservation --------------------

class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
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

    @Override
    public String toString() {
        return "Reservation ID: " + reservationId +
                ", Guest: " + guestName +
                ", Room Type: " + roomType;
    }
}

// -------------------- Inventory Manager --------------------

class InventoryManager {

    private Map<String, Integer> roomInventory;

    public InventoryManager() {
        roomInventory = new HashMap<>();
        roomInventory.put("Standard", 2);
        roomInventory.put("Deluxe", 1);
        roomInventory.put("Suite", 1);
    }

    // Validate room type
    public void validateRoomType(String roomType) throws InvalidRoomTypeException {
        if (!roomInventory.containsKey(roomType)) {
            throw new InvalidRoomTypeException("Invalid room type: " + roomType);
        }
    }

    // Allocate room (with validation)
    public void allocateRoom(String roomType) throws InsufficientRoomsException {
        int available = roomInventory.get(roomType);

        if (available <= 0) {
            throw new InsufficientRoomsException("No rooms available for type: " + roomType);
        }

        roomInventory.put(roomType, available - 1);
    }

    public void displayInventory() {
        System.out.println("\nCurrent Inventory:");
        for (String type : roomInventory.keySet()) {
            System.out.println(type + ": " + roomInventory.get(type));
        }
    }
}

// -------------------- Validator (Fail-Fast) --------------------

class InvalidBookingValidator {

    private InventoryManager inventoryManager;

    public InvalidBookingValidator(InventoryManager inventoryManager) {
        this.inventoryManager = inventoryManager;
    }

    public void validate(String roomType)
            throws InvalidRoomTypeException, InsufficientRoomsException {

        // Step 1: Validate room type
        inventoryManager.validateRoomType(roomType);

        // Step 2: Validate availability
        int available = getAvailable(roomType);
        if (available <= 0) {
            throw new InsufficientRoomsException("Rooms not available for: " + roomType);
        }
    }

    private int getAvailable(String roomType) {
        try {
            // Safe access
            java.lang.reflect.Field field =
                    InventoryManager.class.getDeclaredField("roomInventory");
            field.setAccessible(true);
            Map<String, Integer> map =
                    (Map<String, Integer>) field.get(inventoryManager);
            return map.get(roomType);
        } catch (Exception e) {
            return 0;
        }
    }
}

// -------------------- Booking Service --------------------

class BookingService {

    private InventoryManager inventoryManager;
    private InvalidBookingValidator validator;

    public BookingService(InventoryManager inventoryManager) {
        this.inventoryManager = inventoryManager;
        this.validator = new InvalidBookingValidator(inventoryManager);
    }

    public void createBooking(String id, String name, String roomType) {
        try {
            // Fail-fast validation
            validator.validate(roomType);

            // Allocate room (safe state change)
            inventoryManager.allocateRoom(roomType);

            // Create reservation
            Reservation reservation = new Reservation(id, name, roomType);

            System.out.println("\nBooking Successful:");
            System.out.println(reservation);

        } catch (InvalidRoomTypeException | InsufficientRoomsException e) {
            // Graceful failure
            System.out.println("\nBooking Failed: " + e.getMessage());
        }
    }
}

// -------------------- Main --------------------

public class BookMyStayApp {

    public static void main(String[] args) {

        InventoryManager inventoryManager = new InventoryManager();
        BookingService bookingService = new BookingService(inventoryManager);

        // Valid booking
        bookingService.createBooking("R201", "Rakshit", "Deluxe");

        // Invalid room type
        bookingService.createBooking("R202", "Amit", "Premium");

        // Exhaust inventory
        bookingService.createBooking("R203", "Neha", "Suite");
        bookingService.createBooking("R204", "Priya", "Suite");

        // Final inventory state
        inventoryManager.displayInventory();
    }
}