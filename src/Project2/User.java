package Project2;

import java.util.ArrayList;

public class User {
    private  int userID;
    private String userEmail;
    private String userPass;
    private ArrayList<Booking> bookings = new ArrayList<>();

    public User(String userEmail, String userPass) {
        this.userEmail = userEmail;
        this.userPass = userPass;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPass() {
        return userPass;
    }

    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }

    public ArrayList<Booking> getBookings() {
        return bookings;
    }

    public void addBoking(Booking booking) {
        bookings.add(booking);
    }

    public boolean check(String username, String password) {
        return this.userEmail.equals(username) && this.userPass.equals(password);
    }
    
    public boolean checkEmail(String email) {
        return this.userEmail.equals(email);
    }

    public String getCSV() {
        return userID + "," + userEmail + "," + userPass;
    }
}