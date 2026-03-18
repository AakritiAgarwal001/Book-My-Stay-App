import java.util.HashMap;
import java.util.Map;

/**
 * Use Case 3: Centralized Room Inventory Management
 * Version: 3.0
 */
public class Inventory {

    public static void main(String[] args) {
        // Initialize the inventory component
        RoomInventory inventory = new RoomInventory();

        // Register room types with their available counts
        inventory.addRoomType("Single", 10);
        inventory.addRoomType("Double", 5);
        inventory.addRoomType("Suite", 2);

        // Display the current inventory state
        System.out.println("--- Initial Room Inventory ---");
        inventory.displayInventory();

        // Demonstrate a controlled update (e.g., a booking occurred)
        System.out.println("\nUpdating inventory: Booking 1 Double room...");
        inventory.updateAvailability("Double", 4);

        // Display updated state
        inventory.displayInventory();
    }
}

class RoomInventory {
    // HashMap used to map room types to available counts (O(1) Lookup)
    private Map<String, Integer> inventory;

    public RoomInventory() {
        this.inventory = new HashMap<>();
    }

    // Method to add or initialize room types
    public void addRoomType(String roomType, int count) {
        inventory.put(roomType, count);
    }

    // Controlled update to room availability
    public void updateAvailability(String roomType, int newCount) {
        if (inventory.containsKey(roomType)) {
            inventory.put(roomType, newCount);
        } else {
            System.out.println("Error: Room type " + roomType + " does not exist.");
        }
    }

    // Retrieve current availability for a specific type
    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    // Display all room availability
    public void displayInventory() {
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println("Room Type: " + entry.getKey() + " | Available: " + entry.getValue());
        }
    }
}