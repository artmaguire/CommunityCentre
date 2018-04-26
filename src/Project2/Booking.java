package Project2;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class Booking {
    private int bookingID;
    private int facilityId;
    private int userId;
    private Date date;
    private int slotNo;
    private boolean payment = false; //Paid or not paid

    public Booking(int facilityId, int userId, Date date, int slotNo) {
        this.bookingID = Math.abs((int) UUID.randomUUID().getMostSignificantBits());
        this.facilityId = facilityId;
        this.userId = userId;
        this.date = date;
        this.slotNo = slotNo;
    }

    public Booking(int bookingID, int facilityId, int userId, Date date, int slotNo, boolean payment) {
        this.bookingID = bookingID;
        this.facilityId = facilityId;
        this.userId = userId;
        this.date = date;
        this.slotNo = slotNo;
        this.payment = payment;
    }

    public int getBookingID() {
        return bookingID;
    }

    public void setBookingID(int bookingID) {
        this.bookingID = bookingID;
    }

    public int getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(int facilityId) {
        this.facilityId = facilityId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getSlotNo() {
        return slotNo;
    }

    public void setSlotNo(int slotNo) {
        this.slotNo = slotNo;
    }

    public boolean isPaid() {
        return payment;
    }

    public void setPayment(boolean payment) {
        this.payment = payment;
    }

    public String getCSV() {
        SimpleDateFormat fmt = new SimpleDateFormat("ddMMyyyy");
        return bookingID + "," + facilityId + "," + userId + "," + fmt.format(date) + "," + slotNo + "," + (payment ? 1 : 2);
    }
}