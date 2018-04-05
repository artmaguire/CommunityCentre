package Project2;

import java.util.ArrayList;

import static Project2.RandomPassword.randomPassword;

public class User {
    private  int userID;
    private String userEmail;
    private String userPass;
    private boolean isAdmin;
    private ArrayList<Booking> bookings = new ArrayList<>();

    private static int recentID = 1;

    public User(String userEmail) {
        this.userEmail = userEmail;
        this.userPass = randomPassword();
        this.isAdmin = false;
        this.userID = recentID;
        recentID++;
    }

    public User(int userId, String userEmail, String userPass, boolean isAdmin) {
        this.userID = userId;
        this.userEmail = userEmail;
        this.userPass = userPass;
        this.isAdmin = isAdmin;
    }

    public static void setRecentID(int recentID) {
        User.recentID = recentID;
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

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public ArrayList<Booking> getBookings() {
        return bookings;
    }

    public void addBooking(Booking booking) {
        bookings.add(booking);
    }

    public boolean check(String username, String password) {
        return this.userEmail.equals(username) && this.userPass.equals(password);
    }
    
    public boolean checkEmail(String email) {
        return this.userEmail.equals(email);
    }

    public String getCSV() {
        return userID + "," + userEmail + "," + userPass + "," + (isAdmin ? 1 : 2);
    }
}