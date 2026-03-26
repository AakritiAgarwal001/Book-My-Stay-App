import java.util.*;

// Booking Request
class BookingRequest {
    String customerName;
    String roomType;

    public BookingRequest(String customerName, String roomType) {
        this.customerName = customerName;
        this.roomType = roomType;
    }
}

// Thread-safe Booking Queue
class BookingQueue {
    private Queue<BookingRequest> queue = new LinkedList<>();

    public synchronized void addRequest(BookingRequest request) {
        queue.add(request);
        notify(); // Notify waiting threads
    }

    public synchronized BookingRequest getRequest() {
        while (queue.isEmpty()) {
            try {
                wait(); // Wait until request is available
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        return queue.poll();
    }
}

// Shared Inventory (Critical Resource)
class RoomInventory {
    private Map<String, Integer> inventory = new HashMap<>();

    public RoomInventory() {
        inventory.put("Standard", 2);
        inventory.put("Deluxe", 2);
        inventory.put("Suite", 1);
    }

    // Critical Section (Thread-Safe)
    public synchronized boolean allocateRoom(String roomType) {
        int available = inventory.getOrDefault(roomType, 0);

        if (available > 0) {
            // Simulate delay (to expose race condition if unsynchronized)
            try { Thread.sleep(100); } catch (InterruptedException e) {}

            inventory.put(roomType, available - 1);
            return true;
        }
        return false;
    }

    public synchronized void displayInventory() {
        System.out.println("Final Inventory: " + inventory);
    }
}

// Worker Thread (Concurrent Booking Processor)
class BookingProcessor extends Thread {
    private BookingQueue queue;
    private RoomInventory inventory;

    public BookingProcessor(String name, BookingQueue queue, RoomInventory inventory) {
        super(name);
        this.queue = queue;
        this.inventory = inventory;
    }

    @Override
    public void run() {
        // Process limited number of requests for demo
        for (int i = 0; i < 3; i++) {
            BookingRequest request = queue.getRequest();

            boolean success = inventory.allocateRoom(request.roomType);

            if (success) {
                System.out.println(Thread.currentThread().getName() +
                        " SUCCESS: Booked " + request.roomType +
                        " for " + request.customerName);
            } else {
                System.out.println(Thread.currentThread().getName() +
                        " FAILED: No " + request.roomType +
                        " available for " + request.customerName);
            }
        }
    }
}

// Main Class
public class UseCase11ConcurrentBookingSimulation {

    public static void main(String[] args) {

        BookingQueue queue = new BookingQueue();
        RoomInventory inventory = new RoomInventory();

        // Add booking requests (simulating multiple guests)
        queue.addRequest(new BookingRequest("Alice", "Deluxe"));
        queue.addRequest(new BookingRequest("Bob", "Deluxe"));
        queue.addRequest(new BookingRequest("Charlie", "Deluxe")); // should fail

        queue.addRequest(new BookingRequest("David", "Suite"));
        queue.addRequest(new BookingRequest("Eve", "Suite")); // should fail

        queue.addRequest(new BookingRequest("Frank", "Standard"));
        queue.addRequest(new BookingRequest("Grace", "Standard"));
        queue.addRequest(new BookingRequest("Hannah", "Standard")); // should fail

        // Create multiple threads (simulating concurrent users)
        BookingProcessor t1 = new BookingProcessor("Thread-1", queue, inventory);
        BookingProcessor t2 = new BookingProcessor("Thread-2", queue, inventory);
        BookingProcessor t3 = new BookingProcessor("Thread-3", queue, inventory);

        // Start threads
        t1.start();
        t2.start();
        t3.start();

        // Wait for all threads to complete
        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Final inventory state
        inventory.displayInventory();
    }
}