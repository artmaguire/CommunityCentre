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

    public ArrayList<Booking> getBookings (Date date) {
        ArrayList<Booking> returnBookings = new ArrayList<>();
        for (Booking b : bookings) {
            SimpleDateFormat fmt = new SimpleDateFormat("ddMMyyyy");
            if (fmt.format(date).equals(fmt.format(this.decommissionDate))) {
                returnBookings.add(b);
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

    public void addBoking(Booking booking) {
        bookings.add(booking);
    }

    public static void setRecentID(int recentID) {
        Facility.recentID = recentID;
    }

    public boolean checkName (String name) {
        return this.facilityName.equals(name);
    }

    public String getCSV() {
        return facilityID + "," + facilityName + "," + facilityPrice + "," + decommissionDate;
    }
}
