import java.util.LinkedList;
import java.util.Queue;

/**
 * Represents a guest's intent to book a room.
 */
class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() { return guestName; }
    public String getRoomType() { return roomType; }

    @Override
    public String toString() {
        return "Guest: " + guestName + ", Room Type: " + roomType;
    }
}

/**
 * Manages and orders incoming booking requests using a FIFO Queue.
 */
class BookingRequestQueue {
    private Queue<Reservation> requestQueue;

    public BookingRequestQueue() {
        this.requestQueue = new LinkedList<>();
    }

    public void addRequest(Reservation reservation) {
        requestQueue.add(reservation);
    }

    public Reservation getNextRequest() {
        return requestQueue.poll();
    }

    public boolean hasPendingRequests() {
        return !requestQueue.isEmpty();
    }
}

/**
 * Main Driver Class
 */
public class Booking {
    public static void main(String[] args) {
        BookingRequestQueue bookingQueue = new BookingRequestQueue();

        // 1. Create booking requests as per the document snapshot
        Reservation r1 = new Reservation("Abhi", "Deluxe");
        Reservation r2 = new Reservation("Subha", "Standard");
        Reservation r3 = new Reservation("Vanmathi", "Suite");

        // 2. Add requests to the queue
        bookingQueue.addRequest(r1);
        bookingQueue.addRequest(r2);
        bookingQueue.addRequest(r3);

        System.out.println("Booking Request Queue");
        System.out.println("---------------------");

        // 3. Display and process queued booking requests in FIFO order
        while (bookingQueue.hasPendingRequests()) {
            Reservation current = bookingQueue.getNextRequest();
            System.out.println("Processing booking for " + current);
        }
    }
}