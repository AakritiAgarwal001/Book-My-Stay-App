import java.util.*;

class BookingRequest {
    String guestName;
    String roomType;

    public BookingRequest(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }
}

public class Allocation {
    // Inventory: Room Type -> Available Count
    private static Map<String, Integer> inventory = new HashMap<>();

    // Tracking: Room Type -> Set of Allocated Room IDs (Uniqueness Enforcement)
    private static Map<String, Set<String>> allocatedRooms = new HashMap<>();

    // Request Queue (FIFO)
    private static Queue<BookingRequest> requestQueue = new LinkedList<>();

    public static void main(String[] args) {
        // 1. Initialize Inventory
        inventory.put("Single", 2);
        inventory.put("Suite", 1);

        // 2. Initialize Allocation Sets
        allocatedRooms.put("Single", new HashSet<>());
        allocatedRooms.put("Suite", new HashSet<>());

        // 3. Add Booking Requests (Simulating Dequeued Requests)
        requestQueue.add(new BookingRequest("Abhi", "Single"));
        requestQueue.add(new BookingRequest("Subha", "Single"));
        requestQueue.add(new BookingRequest("Vanmathi", "Suite"));
        requestQueue.add(new BookingRequest("John", "Single")); // This should fail (No inventory)

        System.out.println("Room Allocation Processing");
        processAllocations();
    }

    public static void processAllocations() {
        while (!requestQueue.isEmpty()) {
            BookingRequest request = requestQueue.poll();
            String type = request.roomType;

            // Check Availability
            if (inventory.containsKey(type) && inventory.get(type) > 0) {
                // Generate a Unique Room ID (e.g., Single-1, Single-2)
                int roomNumber = allocatedRooms.get(type).size() + 1;
                String roomId = type + "-" + roomNumber;

                // Atomic Logical Operations: Assign and Update Inventory
                allocatedRooms.get(type).add(roomId);
                inventory.put(type, inventory.get(type) - 1);

                System.out.println("Booking confirmed for Guest: " + request.guestName + ", Room ID: " + roomId);
            } else {
                System.out.println("Booking failed for Guest: " + request.guestName + ". No " + type + " rooms available.");
            }
        }
    }
}