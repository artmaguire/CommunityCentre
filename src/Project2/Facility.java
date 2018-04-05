package Project2;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Facility {
    private int facilityID;
    private String facilityName;
    private double facilityPrice;
    private Date decommissionDate = null; //Decommission until date
    private ArrayList<Booking> bookings = new ArrayList<>();

    private static int recentID = 1;

    public Facility(String facilityName, double facilityPrice) {
        this.facilityName = facilityName;
        this.facilityPrice = facilityPrice;

        this.facilityID = recentID;
        recentID++;
    }

    public Facility(int facilityID, String facilityName, double facilityPrice, Date decommissionDate) {
        this.facilityID = facilityID;
        this.facilityName = facilityName;
        this.facilityPrice = facilityPrice;
        this.decommissionDate = decommissionDate;
    }

    public ArrayList<Booking> getBookings (Date date) {
        ArrayList<Booking> returnBookings = new ArrayList<>(9);

        for (int i = 0; i < 9; i++) {
            returnBookings.add(null); //Initialising List as null
        }

        for (Booking b : bookings) {
            SimpleDateFormat fmt = new SimpleDateFormat("ddMMyyyy");
            if (fmt.format(date).equals(fmt.format(b.getDate()))) {
                returnBookings.set(b.getSlotNo() - 1, b);
            }
        }
        return returnBookings;
    }

    public int getFacilityID() {
        return facilityID;
    }

    public void setFacilityID(int facilityID) {
        this.facilityID = facilityID;
    }

    public String getFacilityName() {
        return facilityName;
    }

    public void setFacilityName(String facilityName) {
        this.facilityName = facilityName;
    }

    public double getFacilityPrice() {
        return facilityPrice;
    }

    public void setFacilityPrice(double facilityPrice) {
        this.facilityPrice = facilityPrice;
    }

    public Date getDecommissionDate() {
        return decommissionDate;
    }

    public void setDecommissionDate(Date date) {
        this.decommissionDate = date;
    }

    public void recommission() {
        decommissionDate = null;
    }

    public ArrayList<Booking> getBookings() {
        return bookings;
    }

    public void addBooking(Booking booking) {
        bookings.add(booking);
    }

    public boolean isNotBooked () {
        return this.bookings.isEmpty();
    }

    public static void setRecentID(int recentID) {
        Facility.recentID = recentID;
    }

    public boolean checkName (String name) {
        return this.facilityName.equals(name);
    }

    public String getCSV() {
        String date = "null";
        if (decommissionDate != null) {
            SimpleDateFormat fmt = new SimpleDateFormat("ddMMyyyy");
            date = fmt.format(decommissionDate);
        }
        return facilityID + "," + facilityName + "," + facilityPrice + "," + date;
    }
}
