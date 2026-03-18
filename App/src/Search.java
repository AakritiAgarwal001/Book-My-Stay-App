public class Search{


    public void searchAvailableRooms(
            RoomInventory inventory,
            Room singleRoom,
            Room doubleRoom,
            Room suiteRoom) {

        System.out.println("--- Available Rooms ---");

        // Check Single Room Availability
        int singleAvailable = inventory.getAvailableCount("Single");
        if (singleAvailable > 0) {
            displayRoomDetails(singleRoom, singleAvailable);
        }

        // Check Double Room Availability
        int doubleAvailable = inventory.getAvailableCount("Double");
        if (doubleAvailable > 0) {
            displayRoomDetails(doubleRoom, doubleAvailable);
        }

        // Check Suite Room Availability
        int suiteAvailable = inventory.getAvailableCount("Suite");
        if (suiteAvailable > 0) {
            displayRoomDetails(suiteRoom, suiteAvailable);
        }

        System.out.println("-----------------------");
    }

    private void displayRoomDetails(Room room, int count) {
        System.out.println("Type: " + room.getType());
        System.out.println("Price: $" + room.getPrice());
        System.out.println("Amenities: " + room.getAmenities());
        System.out.println("Units Available: " + count);
        System.out.println();
    }
}