import java.io.*;
import java.util.*;

// -------------------- Reservation --------------------

class Reservation implements Serializable {
    private static final long serialVersionUID = 1L;

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
        return reservationId + " | " + guestName + " | " + roomType;
    }
}

// -------------------- System State --------------------

class SystemState implements Serializable {
    private static final long serialVersionUID = 1L;

    List<Reservation> bookingHistory;
    Map<String, Integer> inventory;

    public SystemState(List<Reservation> bookingHistory, Map<String, Integer> inventory) {
        this.bookingHistory = bookingHistory;
        this.inventory = inventory;
    }
}

// -------------------- Persistence Service --------------------

class PersistenceService {

    private static final String FILE_NAME = "system_state.ser";

    // Save state
    public void saveState(SystemState state) {
        try (ObjectOutputStream oos =
                     new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {

            oos.writeObject(state);
            System.out.println("\nSystem state saved successfully.");

        } catch (IOException e) {
            System.out.println("\nError saving state: " + e.getMessage());
        }
    }

    // Load state
    public SystemState loadState() {
        try (ObjectInputStream ois =
                     new ObjectInputStream(new FileInputStream(FILE_NAME))) {

            System.out.println("\nSystem state restored successfully.");
            return (SystemState) ois.readObject();

        } catch (FileNotFoundException e) {
            System.out.println("\nNo previous state found. Starting fresh.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("\nError loading state. Starting with safe defaults.");
        }

        // Safe fallback
        return new SystemState(new ArrayList<>(), getDefaultInventory());
    }

    private Map<String, Integer> getDefaultInventory() {
        Map<String, Integer> defaultInventory = new HashMap<>();
        defaultInventory.put("Standard", 2);
        defaultInventory.put("Deluxe", 1);
        defaultInventory.put("Suite", 1);
        return defaultInventory;
    }
}

// -------------------- Main --------------------

public class BookMyStayApp {

    public static void main(String[] args) {

        PersistenceService persistenceService = new PersistenceService();

        // Step 1: Load previous state
        SystemState state = persistenceService.loadState();

        List<Reservation> bookingHistory = state.bookingHistory;
        Map<String, Integer> inventory = state.inventory;

        // Step 2: Display restored data
        System.out.println("\n--- Restored Booking History ---");
        for (Reservation r : bookingHistory) {
            System.out.println(r);
        }

        System.out.println("\n--- Restored Inventory ---");
        for (String type : inventory.keySet()) {
            System.out.println(type + ": " + inventory.get(type));
        }

        // Step 3: Simulate new booking
        System.out.println("\nAdding new booking...");
        Reservation newReservation =
                new Reservation("R401", "Rakshit", "Standard");

        bookingHistory.add(newReservation);
        inventory.put("Standard", inventory.get("Standard") - 1);

        // Step 4: Save updated state
        SystemState updatedState = new SystemState(bookingHistory, inventory);
        persistenceService.saveState(updatedState);
    }
}