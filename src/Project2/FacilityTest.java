package Project2;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.ArrayList;
import java.util.Date;

class FacilityTest {

    Facility facility;
    Booking b1;
    Booking b2;

    @BeforeEach
    void setUp() {
        facility = new Facility("IT", 60);
        b1 = new Booking(facility.getFacilityID(), 1, new Date(), 5);
        b2 = new Booking(facility.getFacilityID(), 1, new Date(), 2);
        facility.addBooking(b1);
        facility.addBooking(b2);
    }

    @Test
    void getBookings() {
        ArrayList<Booking> dayBookings = facility.getBookings(new Date());

        for(int i = 0; i < 9; i++) {
            System.out.print(i + 1 + ": ");
            if (i == b1.getSlotNo() - 1 || i == b2.getSlotNo() - 1) {
                System.out.println(dayBookings.get(i).getBookingID());
                assertEquals(i, dayBookings.get(i).getSlotNo() - 1);
            }
            else {
                System.out.println("-Available-");
                assertNull(dayBookings.get(0));
            }
        }
    }
}