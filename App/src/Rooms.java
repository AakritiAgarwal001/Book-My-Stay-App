// Abstract class defining the blueprint for all rooms
abstract class Room {
    protected String type;
    protected int beds;
    protected double price;

    public Room(String type, int beds, double price) {
        this.type = type;
        this.beds = beds;
        this.price = price;
    }

    // Common behavior to display room details
    public void displayDetails() {
        System.out.println("Room Type: " + type + " | Beds: " + beds + " | Price: $" + price);
    }
}

// Concrete implementation for a Single Room
class SingleRoom extends Room {
    public SingleRoom() {
        super("Single", 1, 100.0);
    }
}

// Concrete implementation for a Double Room
class DoubleRoom extends Room {
    public DoubleRoom() {
        super("Double", 2, 150.0);
    }
}

// Concrete implementation for a Suite Room
class SuiteRoom extends Room {
    public SuiteRoom() {
        super("Suite", 4, 300.0);
    }
}

// Main Application Class
public class Rooms {
    public static void main(String[] args) {
        System.out.println("--- Book My Stay App (Version 2.0) ---");
        System.out.println("Initializing Room Types and Availability...\n");

        // Polymorphism: Handling different room objects through the abstract Room reference
        Room single = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suite = new SuiteRoom();

        // Static Availability Representation (using simple variables as per requirements)
        int singleAvailable = 5;
        int doubleAvailable = 3;
        int suiteAvailable = 2;

        // Displaying results to console
        single.displayDetails();
        System.out.println("Availability: " + singleAvailable);
        System.out.println("-------------------------------------------");

        doubleRoom.displayDetails();
        System.out.println("Availability: " + doubleAvailable);
        System.out.println("-------------------------------------------");

        suite.displayDetails();
        System.out.println("Availability: " + suiteAvailable);
        System.out.println("-------------------------------------------");
    }