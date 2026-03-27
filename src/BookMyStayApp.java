import java.util.*;

// -------------------- Booking Request --------------------

class BookingRequest {
    private String requestId;
    private String guestName;
    private String roomType;

    public BookingRequest(String requestId, String guestName, String roomType) {
        this.requestId = requestId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getRequestId() {
        return requestId;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }
}

// -------------------- Thread-Safe Inventory --------------------

class InventoryManager {

    private Map<String, Integer> inventory;

    public InventoryManager() {
        inventory = new HashMap<>();
        inventory.put("Standard", 2);
        inventory.put("Deluxe", 1);
        inventory.put("Suite", 1);
    }

    // Critical section (synchronized)
    public synchronized boolean allocateRoom(String roomType) {

        int available = inventory.getOrDefault(roomType, 0);

        if (available > 0) {
            inventory.put(roomType, available - 1);
            return true;
        }

        return false;
    }

    public synchronized void displayInventory() {
        System.out.println("\nFinal Inventory:");
        for (String type : inventory.keySet()) {
            System.out.println(type + ": " + inventory.get(type));
        }
    }
}

// -------------------- Shared Booking Queue --------------------

class BookingQueue {

    private Queue<BookingRequest> queue = new LinkedList<>();

    // synchronized add
    public synchronized void addRequest(BookingRequest request) {
        queue.add(request);
    }

    // synchronized retrieval
    public synchronized BookingRequest getRequest() {
        return queue.poll();
    }
}

// -------------------- Worker Thread --------------------

class BookingProcessor extends Thread {

    private BookingQueue queue;
    private InventoryManager inventoryManager;

    public BookingProcessor(String name, BookingQueue queue, InventoryManager inventoryManager) {
        super(name);
        this.queue = queue;
        this.inventoryManager = inventoryManager;
    }

    @Override
    public void run() {

        while (true) {

            BookingRequest request;

            // synchronized retrieval
            synchronized (queue) {
                request = queue.getRequest();
            }

            if (request == null) break;

            boolean success = inventoryManager.allocateRoom(request.getRoomType());

            if (success) {
                System.out.println(getName() + " allocated room for " +
                        request.getGuestName() + " (" + request.getRoomType() + ")");
            } else {
                System.out.println(getName() + " FAILED for " +
                        request.getGuestName() + " (" + request.getRoomType() + ")");
            }
        }
    }
}

// -------------------- Main --------------------

public class BookMyStayApp {

    public static void main(String[] args) {

        InventoryManager inventoryManager = new InventoryManager();
        BookingQueue queue = new BookingQueue();

        // Simulate concurrent booking requests
        queue.addRequest(new BookingRequest("REQ1", "Rakshit", "Standard"));
        queue.addRequest(new BookingRequest("REQ2", "Amit", "Standard"));
        queue.addRequest(new BookingRequest("REQ3", "Neha", "Standard")); // exceeds capacity
        queue.addRequest(new BookingRequest("REQ4", "Priya", "Deluxe"));
        queue.addRequest(new BookingRequest("REQ5", "Rahul", "Suite"));

        // Multiple threads (simulating concurrent users)
        Thread t1 = new BookingProcessor("Thread-1", queue, inventoryManager);
        Thread t2 = new BookingProcessor("Thread-2", queue, inventoryManager);
        Thread t3 = new BookingProcessor("Thread-3", queue, inventoryManager);

        // Start threads
        t1.start();
        t2.start();
        t3.start();

        // Wait for completion
        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Final state
        inventoryManager.displayInventory();
    }
}