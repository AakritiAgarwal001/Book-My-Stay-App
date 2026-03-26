import java.util.*;

// Custom Exception for invalid booking scenarios
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

// Reservation class
class Reservation {
    private String reservationId;
    private String customerName;
    private String roomType;
    private int nights;

    public Reservation(String reservationId, String customerName, String roomType, int nights) {
        this.reservationId = reservationId;
        this.customerName = customerName;
        this.roomType = roomType;
        this.nights = nights;
    }

    @Override
    public String toString() {
        return "Reservation ID: " + reservationId +
                ", Customer: " + customerName +
                ", Room Type: " + roomType +
                ", Nights: " + nights;
    }
}

// Inventory Management
class RoomInventory {
    private Map<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Standard", 2);
        inventory.put("Deluxe", 2);
        inventory.put("Suite", 1);
    }

    public boolean isValidRoomType(String roomType) {
        return inventory.containsKey(roomType);
    }

    public int getAvailableRooms(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public void reserveRoom(String roomType) throws InvalidBookingException {
        int available = getAvailableRooms(roomType);

        // Guard against invalid state
        if (available <= 0) {
            throw new InvalidBookingException("No rooms available for type: " + roomType);
        }

        inventory.put(roomType, available - 1);
    }

    public void displayInventory() {
        System.out.println("\nCurrent Inventory: " + inventory);
    }
}

// Validator class (Fail-Fast Design)
class BookingValidator {

    public static void validate(String customerName, String roomType, int nights, RoomInventory inventory)
            throws InvalidBookingException {

        if (customerName == null || customerName.trim().isEmpty()) {
            throw new InvalidBookingException("Customer name cannot be empty.");
        }

        if (!inventory.isValidRoomType(roomType)) {
            throw new InvalidBookingException("Invalid room type: " + roomType);
        }

        if (nights <= 0) {
            throw new InvalidBookingException("Number of nights must be greater than zero.");
        }

        if (inventory.getAvailableRooms(roomType) <= 0) {
            throw new InvalidBookingException("Selected room type is fully booked.");
        }
    }
}

// Booking Service
class BookingService {
    private RoomInventory inventory;
    private List<Reservation> bookingHistory;

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
        this.bookingHistory = new ArrayList<>();
    }

    public void createBooking(String id, String name, String roomType, int nights) {
        try {
            // Step 1: Validate (Fail Fast)
            BookingValidator.validate(name, roomType, nights, inventory);

            // Step 2: Reserve room (state change)
            inventory.reserveRoom(roomType);

            // Step 3: Create reservation
            Reservation reservation = new Reservation(id, name, roomType, nights);
            bookingHistory.add(reservation);

            System.out.println("Booking successful: " + reservation);

        } catch (InvalidBookingException e) {
            // Graceful failure handling
            System.out.println("Booking failed: " + e.getMessage());
        }
    }

    public void displayBookings() {
        System.out.println("\n--- Booking History ---");
        for (Reservation r : bookingHistory) {
            System.out.println(r);
        }
    }
}

// Main Class
public class UseCase9ErrorHandlingValidation {

    public static void main(String[] args) {

        RoomInventory inventory = new RoomInventory();
        BookingService bookingService = new BookingService(inventory);

        // Valid booking
        bookingService.createBooking("R001", "Alice", "Deluxe", 2);

        // Invalid room type
        bookingService.createBooking("R002", "Bob", "Premium", 1);

        // Invalid nights
        bookingService.createBooking("R003", "Charlie", "Standard", 0);

        // Overbooking scenario
        bookingService.createBooking("R004", "David", "Suite", 1);
        bookingService.createBooking("R005", "Eve", "Suite", 1); // Should fail

        // Empty customer name
        bookingService.createBooking("R006", "", "Standard", 1);

        // Display results
        bookingService.displayBookings();
        inventory.displayInventory();
    }
}