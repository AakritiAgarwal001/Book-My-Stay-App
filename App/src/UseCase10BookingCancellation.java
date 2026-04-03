import java.util.*;

// Custom Exception
class BookingException extends Exception {
    public BookingException(String message) {
        super(message);
    }
}

// Reservation class
class Reservation {
    private String reservationId;
    private String customerName;
    private String roomType;
    private boolean isCancelled;

    public Reservation(String reservationId, String customerName, String roomType) {
        this.reservationId = reservationId;
        this.customerName = customerName;
        this.roomType = roomType;
        this.isCancelled = false;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getRoomType() {
        return roomType;
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
                ", Customer: " + customerName +
                ", Room Type: " + roomType +
                ", Status: " + (isCancelled ? "CANCELLED" : "CONFIRMED");
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

    public void allocateRoom(String roomType) throws BookingException {
        int count = inventory.getOrDefault(roomType, 0);
        if (count <= 0) {
            throw new BookingException("No rooms available for: " + roomType);
        }
        inventory.put(roomType, count - 1);
    }

    public void releaseRoom(String roomType) {
        int count = inventory.getOrDefault(roomType, 0);
        inventory.put(roomType, count + 1);
    }

    public void displayInventory() {
        System.out.println("Inventory: " + inventory);
    }
}

// Booking + Cancellation Service
class BookingService {
    private Map<String, Reservation> reservations;
    private RoomInventory inventory;

    // Stack for rollback tracking (LIFO)
    private Stack<String> rollbackStack;

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
        this.reservations = new HashMap<>();
        this.rollbackStack = new Stack<>();
    }

    // Create booking
    public void book(String id, String name, String roomType) {
        try {
            inventory.allocateRoom(roomType);

            Reservation res = new Reservation(id, name, roomType);
            reservations.put(id, res);

            // Track allocation for rollback
            rollbackStack.push(id);

            System.out.println("Booking successful: " + res);

        } catch (BookingException e) {
            System.out.println("Booking failed: " + e.getMessage());
        }
    }

    // Cancel booking with rollback
    public void cancel(String reservationId) {
        try {
            // Validate existence
            if (!reservations.containsKey(reservationId)) {
                throw new BookingException("Reservation not found.");
            }

            Reservation res = reservations.get(reservationId);

            // Prevent duplicate cancellation
            if (res.isCancelled()) {
                throw new BookingException("Reservation already cancelled.");
            }

            // Controlled rollback
            rollbackStack.push(reservationId);

            // Step 1: Restore inventory
            inventory.releaseRoom(res.getRoomType());

            // Step 2: Update reservation state
            res.cancel();

            System.out.println("Cancellation successful: " + res);

        } catch (BookingException e) {
            System.out.println("Cancellation failed: " + e.getMessage());
        }
    }

    public void displayReservations() {
        System.out.println("\n--- Reservations ---");
        for (Reservation r : reservations.values()) {
            System.out.println(r);
        }
    }

    public void displayRollbackStack() {
        System.out.println("\nRollback Stack (LIFO): " + rollbackStack);
    }
}

// Main class
public class UseCase10BookingCancellation {

    public static void main(String[] args) {

        RoomInventory inventory = new RoomInventory();
        BookingService service = new BookingService(inventory);

        // Create bookings
        service.book("R001", "Alice", "Deluxe");
        service.book("R002", "Bob", "Suite");

        // Cancel valid booking
        service.cancel("R001");

        // Attempt duplicate cancellation
        service.cancel("R001");

        // Attempt invalid cancellation
        service.cancel("R999");

        // Display system state
        service.displayReservations();
        inventory.displayInventory();
        service.displayRollbackStack();
    }
}