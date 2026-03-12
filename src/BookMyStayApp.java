/**
 * Book My Stay App
 * Version 2.0
 * Demonstrates basic room types and static availability
 *
 * @author R. Shirley
 * @version 2.0
 */

// Abstract Room class
abstract class Room {

    protected String roomType;
    protected int beds;
    protected int size;
    protected double price;

    public Room(String roomType, int beds, int size, double price) {
        this.roomType = roomType;
        this.beds = beds;
        this.size = size;
        this.price = price;
    }

    public void displayRoomDetails() {
        System.out.println("Room Type : " + roomType);
        System.out.println("Beds      : " + beds);
        System.out.println("Size      : " + size + " sq.ft");
        System.out.println("Price     : ₹" + price);
    }
}

// Single Room class
class SingleRoom extends Room {

    public SingleRoom() {
        super("Single Room", 1, 200, 2500);
    }
}

// Double Room class
class DoubleRoom extends Room {

    public DoubleRoom() {
        super("Double Room", 2, 350, 4000);
    }
}

// Suite Room class
class SuiteRoom extends Room {

    public SuiteRoom() {
        super("Suite Room", 3, 600, 8000);
    }
}

// Main class
public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("=================================");
        System.out.println("Book My Stay - Hotel Booking App");
        System.out.println("Version 2.0");`
        System.out.println("=================================\n");

        // Static availability variables
        int singleRoomAvailable = 5;
        int doubleRoomAvailable = 3;
        int suiteRoomAvailable = 2;

        // Creating room objects (Polymorphism)
        Room single = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suite = new SuiteRoom();

        // Display details
        System.out.println("Single Room Details:");
        single.displayRoomDetails();
        System.out.println("Available: " + singleRoomAvailable + "\n");

        System.out.println("Double Room Details:");
        doubleRoom.displayRoomDetails();
        System.out.println("Available: " + doubleRoomAvailable + "\n");

        System.out.println("Suite Room Details:");
        suite.displayRoomDetails();
        System.out.println("Available: " + suiteRoomAvailable);
    }
}