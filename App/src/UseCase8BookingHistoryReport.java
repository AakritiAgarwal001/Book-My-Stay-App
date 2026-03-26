import java.util.*;

// Reservation class
class Reservation {
    private String reservationId;
    private String customerName;
    private String roomType;
    private int nights;
    private double pricePerNight;

    public Reservation(String reservationId, String customerName, String roomType, int nights, double pricePerNight) {
        this.reservationId = reservationId;
        this.customerName = customerName;
        this.roomType = roomType;
        this.nights = nights;
        this.pricePerNight = pricePerNight;
    }

    public double getTotalCost() {
        return nights * pricePerNight;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getRoomType() {
        return roomType;
    }

    public int getNights() {
        return nights;
    }

    @Override
    public String toString() {
        return "Reservation ID: " + reservationId +
                ", Customer: " + customerName +
                ", Room Type: " + roomType +
                ", Nights: " + nights +
                ", Total Cost: $" + getTotalCost();
    }
}

// Booking History (Storage Layer)
class BookingHistory {
    private List<Reservation> reservations;

    public BookingHistory() {
        reservations = new ArrayList<>();
    }

    // Store confirmed reservation
    public void addReservation(Reservation reservation) {
        reservations.add(reservation);
    }

    // Retrieve all reservations (read-only copy)
    public List<Reservation> getAllReservations() {
        return Collections.unmodifiableList(reservations);
    }
}

// Reporting Service (Business Logic Layer)
class BookingReportService {

    // Display all reservations
    public void printAllBookings(List<Reservation> reservations) {
        System.out.println("\n--- Booking History ---");
        for (Reservation r : reservations) {
            System.out.println(r);
        }
    }

    // Generate summary report
    public void generateSummaryReport(List<Reservation> reservations) {
        int totalBookings = reservations.size();
        double totalRevenue = 0;

        for (Reservation r : reservations) {
            totalRevenue += r.getTotalCost();
        }

        System.out.println("\n--- Booking Summary Report ---");
        System.out.println("Total Bookings: " + totalBookings);
        System.out.println("Total Revenue: $" + totalRevenue);
    }
}

// Main Class
public class UseCase8BookingHistoryReport {

    public static void main(String[] args) {

        // Initialize components
        BookingHistory bookingHistory = new BookingHistory();
        BookingReportService reportService = new BookingReportService();

        // Simulate confirmed bookings
        Reservation r1 = new Reservation("R001", "Alice", "Deluxe", 3, 100);
        Reservation r2 = new Reservation("R002", "Bob", "Suite", 2, 150);
        Reservation r3 = new Reservation("R003", "Charlie", "Standard", 4, 80);

        // Add to booking history (in insertion order)
        bookingHistory.addReservation(r1);
        bookingHistory.addReservation(r2);
        bookingHistory.addReservation(r3);

        // Admin retrieves booking history
        List<Reservation> storedReservations = bookingHistory.getAllReservations();

        // Display all bookings
        reportService.printAllBookings(storedReservations);

        // Generate summary report
        reportService.generateSummaryReport(storedReservations);
    }
}