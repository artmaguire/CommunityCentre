package Project2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class CommunityCentre {
    private static User loggedInUser = null;

    private static final String userFileName = "users.csv";
    private static final String facilityFileName = "facilities.csv";
    private static final String bookingFileName = "bookings.csv";

    private static ArrayList<User> users = new ArrayList<>();
    private static Map<Integer, Facility> facilityMap = new HashMap<>();

    private static Scanner sc = new Scanner(System.in);

    private static final String EQUALS_SEPARATOR = "====================";
    private static final String DASH_SEPARATOR = "--------------------";

    public static void main(String[] args) {

        loadUsers();
        loadFacilities();
        loadBookings();

        createLoginUser();

        writeUsers();
        writeFacilities();
        writeBookings();
    }

    /**
     * Creates the interface for interacting with the program, Logging in, Creating a new user and exiting hte program.
     * The switch in this Method links the method between the loadAdmin Method and the createAdmin Method.
     */
    private static void createLoginUser() {
        boolean exit = false;
        while (!exit) {
            System.out.println("0) Exit");
            if(users.isEmpty()) {
                System.out.println("1) Create Admin");
            } else {
                System.out.println("1) Login User");
            }
            System.out.println(DASH_SEPARATOR);
            System.out.print("?) ");

            int option;
            try {
                option = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid Input");
                continue;
            }

            switch (option) {
                case 0:
                    exit = true;
                    break;
                case 1:
                    if (users.isEmpty()) {
                        registerNewUser(true);
                    } else {
                        loginUser();
                    }
                    if (loggedInUser != null) userOptions();
                    break;
                default:
                    System.out.println("Invalid Input");
            }
        }
    }

    /**
     * This Method allows the user to log in to an existing user using name and password.
     * It gives the user 3 attempts. If login is successful, the program will show the leagues associated with the user.
     */
    private static void loginUser() {
        int attempts = 3;
        while (attempts > 0) {
            System.out.println("\n" + EQUALS_SEPARATOR);
            System.out.println("Attempts Remaining: " + attempts);
            System.out.println("\n" + EQUALS_SEPARATOR);
            System.out.println("Username: ");
            String username = sc.nextLine();
            System.out.println("Password: ");
            String password = sc.nextLine();

            for(User u: users) {
                if (u.check(username, password)) {
                    loggedInUser = u;
                    break;
                }
            }

            if (loggedInUser != null) {
                System.out.println("Successfully Logged In\n");
                attempts = -1;
            } else {
                System.out.println("No User Found\n");
                attempts--;
            }
        }
    }

    /**
     * This Method creates a new User and adds it to the users ArrayList.
     * The program will check to see if the user exists,
     * if the user does exist, it will show an appropriate error message.
     */
    private static void registerNewUser(boolean isAdmin) {
        String email;
        while (true) {
            System.out.println("\n" + EQUALS_SEPARATOR);
            System.out.println("Email: ");
            email = sc.nextLine();
            if (!email.contains("@") || !email.contains(".")) {
                System.out.println("Invalid Email.");
                continue;
            }
            break;
        }
        User newUser = new User(email);
        newUser.setAdmin(isAdmin);
        System.out.println("Your New Password: " + newUser.getUserPass());
        users.add(newUser); //add them to the array of users
        loggedInUser = newUser; //set the new user as the current logged in user

        writeUsers();
    }

    /**
     * This method displays all the options in the community centre, asks for an input from the user and uses a switch
     * to switch to the different methods.
     */
    private static void userOptions() {
        while (true) {
            System.out.println("\n" + EQUALS_SEPARATOR);
            System.out.println("0) Logout");
            System.out.println("1) View Bookings");
            System.out.println("2) View Availability");
            System.out.println("3) Book Facility");
            System.out.println("4) View Statements");
            if (loggedInUser.isAdmin()) {
                System.out.println(EQUALS_SEPARATOR + "\nAdmin Options:\n" + EQUALS_SEPARATOR);
                System.out.println("5) Register New User");
                System.out.println("6) Add New Facility");
                System.out.println("7) View Bookings For Facility");
                System.out.println("8) Remove Facility");
                System.out.println("9) Decommission Facility");
                System.out.println("10) Recommission Facility");
                System.out.println("11) Record Payment");
                System.out.println("12) View Account Statements");
            }
            System.out.print("?) ");
            int option;
            try {
                option = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid Input");
                continue;
            }
            switch (option) {
                case 0: loggedInUser = null; return;
                case 1: viewBookings(); break;
                case 2: viewAvailability(); break;
                case 3: bookFacility(); break;
                case 4: viewStatement(loggedInUser); break;
                case 5: registerNewUser(false); break;
                case 6: addNewFacility(); break;
                case 7: viewBookingsForFacility(); break;
                case 8: removeFacility(); break;
                case 9: decommissionFacility(); break;
                case 10: recommissionFacility(); break;
                case 11: recordPayment(); break;
                case 12: viewAccountStatements(); break;
                default:
                    System.out.println("Invalid Option.");
            }
        }
    }

    /**
     * This method check how many booking are inside the loggedIn users ArrayList 'bookings' and then displays all the bookings.
     */
    private static void viewBookings() {
        String tabNo;
        ArrayList<Booking> bookings = loggedInUser.getBookings();
        if (bookings.isEmpty()) {
            System.out.println("You have no bookings");
            return;
        }
        System.out.println("Date\t\tSlot\tFacility\tPaid?");
        for (Booking b : bookings) {
            SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");
            if (facilityMap.get(b.getFacilityId()).getFacilityName().length() < 4) {
                tabNo = "\t\t\t";
            } else if (facilityMap.get(b.getFacilityId()).getFacilityName().length() >= 4) {
                tabNo = "\t\t";
            } else {
                tabNo = "\t";
            }
            System.out.println(fmt.format(b.getDate()) + "\t" + (b.getSlotNo() + 8) + "\t\t" +
                    facilityMap.get(b.getFacilityId()).getFacilityName() + tabNo + b.isPaid());
        }
    }

    private static Facility chooseFacility() {
        System.out.println(EQUALS_SEPARATOR);
        if (facilityMap.values().isEmpty()) {
            System.out.println("No Facilities Exist.");
            return null;
        }
        System.out.println("Choose Facility:");
        System.out.println(Facility.facilityHeader);
        for (Facility f : facilityMap.values()) {
            System.out.println(f.toString());
        }
        System.out.println(DASH_SEPARATOR);
        System.out.println("0) Return");
        System.out.println(DASH_SEPARATOR);

        Facility f;
        while (true) {
            System.out.print("?) ");

            int option;
            try {
                option = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid Input");
                continue;
            }

            if (option == 0) return null;

            try {
                f = facilityMap.get(option);
            } catch (Exception e) {
                System.out.println("Invalid Option.");
                continue;
            }
            return f;
        }
    }

    private static Date chooseDate() {
        Date date;
        while (true) {
            System.out.println("Enter Date: (dd/mm/yyyy)");
            String input = sc.nextLine();
            try {
                date = new SimpleDateFormat("dd/MM/yyyy").parse(input);
                if (date.before(new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000L))) {
                    System.out.println("Date Cannot Be In the Past.");
                    continue;
                }
                break;
            } catch (ParseException e) {
                System.out.println("Invalid Entry");
            }
        }
        return date;
    }

    /**
     * Shows the availability of facilities after the present date and gives the option then to book the facility.
     */
    private static void viewAvailability() {
        Facility facility = chooseFacility();
        if (facility == null) return;

        Date date = chooseDate();
        ArrayList<Booking> bookings = facility.getBookingsDay(date);

        System.out.println(EQUALS_SEPARATOR);
        for (int i = 0; i < 9; i++) {
            Booking b = bookings.get(i);
            System.out.print(i + 9 + ") ");
            if (b == null) {
                System.out.println("Empty");
            } else {
                System.out.println("Booked");
            }
        }
        System.out.println(EQUALS_SEPARATOR);
    }

    /**
     * Allows the user to book facilities that are currently available at the hour.
     * It asks for an input in Date format which will then be parsed into a date.
     * The method will then provide a valid error message if the Date in invalid or it will show the times available for booking.
     */
    private static void bookFacility() {
        Facility facility = chooseFacility();
        if (facility == null) return;

        if (facility.getDecommissionDate() != null) {
            System.out.println("Facility is Decommissioned until: " + facility.getDecommissionDateString());
            return;
        }

        Date date = chooseDate();
        ArrayList<Booking> bookings = facility.getBookingsDay(date);

        System.out.println("Choose Time Slot:");
        System.out.println(EQUALS_SEPARATOR);
        for (int i = 0; i < 9; i++) {
            Booking b = bookings.get(i);
            System.out.print(i + 9 + ") ");
            if (b == null) {
                System.out.println("Empty");
            } else {
                System.out.println("Booked");
            }
        }
        System.out.println(EQUALS_SEPARATOR);
        int option;
        while (true) {
            System.out.print("?) ");
            try {
                option = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid Input");
                continue;
            }

            try {
                Booking b = bookings.get(option - 9);
                if (b != null) {
                    System.out.println("Please Choose Empty Time Slot.");
                    continue;
                }
                break;
            } catch (Exception e) {
                System.out.println("Invalid Input.");
            }
        }

        Booking b = new Booking(facility.getFacilityID(), loggedInUser.getUserID(), date, option - 8);
        facility.addBooking(b);
        loggedInUser.addBooking(b);
        writeBookings();
    }

    private static void viewStatement(User user) {
        double paidTotal = 0;
        double owedTotal = 0;
        System.out.println(EQUALS_SEPARATOR);
        System.out.println(Booking.bookingHeader + "\tPrice" + "\tFacility Name");
        for (Booking b : user.getBookings()) {
            if (b.isPaid()) {
                Facility f = facilityMap.get(b.getFacilityId());
                System.out.println(b.toString() + "\t" + f.getFacilityPrice() + "\t" + f.getFacilityName());
                paidTotal += f.getFacilityPrice();
            }
        }
        System.out.println("Paid Total: " + paidTotal);
        System.out.println(DASH_SEPARATOR);

        System.out.println(Booking.bookingHeader + "\tPrice" + "\tFacility Name");
        for (Booking b : user.getBookings()) {
            if (!b.isPaid()) {
                Facility f = facilityMap.get(b.getFacilityId());
                System.out.println(b.toString() + "\t" + f.getFacilityPrice() + "\t" + f.getFacilityName());
                owedTotal += f.getFacilityPrice();
            }
        }
        System.out.println("Owed Total: " + owedTotal);
        System.out.println(EQUALS_SEPARATOR);
    }

    /**
     * This method allows an Admin to add any facility they want to the list of Facilities and it will then be saved.
     */
    private static void addNewFacility() {
        System.out.println("\n====================");
        String name;
        do {
            System.out.println("Name: ");
            name = sc.nextLine();

            for (Facility f : facilityMap.values()) {
                if ((f.checkName(name))) {
                    System.out.println("Facility Already Exists");
                    name = "";
                    break;
                }
            }
        } while (name.isEmpty());

        System.out.println("Price Per Hour: ");
        double pph = Double.parseDouble(sc.nextLine());

        Facility f = new Facility(name, pph);
        facilityMap.put(f.getFacilityID(), f);
        writeFacilities();
    }

    private static void viewBookingsForFacility() {
        Facility facility = chooseFacility();
        if (facility == null) return;

        for (Facility f : facilityMap.values()) {
            if (facility.equals(f)) {
                System.out.println(Booking.bookingHeader);

                for (Booking b : f.getBookingsDay()) {
                    System.out.println(b.toString());
                }
                return;
            }
        }
    }

    private static void removeFacility() {
        Facility facility = chooseFacility();
        if (facility == null) return;

        for (Facility f : facilityMap.values()) {
            if (facility.equals(f)) {
                if (f.getBookingsDay().isEmpty()) {
                    facilityMap.remove(f.getFacilityID());
                    System.out.println(f.getFacilityName() + " Successfully Deleted");
                    writeFacilities();
                } else {
                    System.out.println(f.getFacilityName() + " has " + f.getBookingsDay().size() + " Booking(s) and Cannot be Deleted.");
                }
                return;
            }
        }
    }

    private static void decommissionFacility() {
        Facility facility = chooseFacility();
        if (facility == null) return;

        for (Facility f : facilityMap.values()) {
            if (facility.equals(f)) {
                if (!f.getBookingsDay().isEmpty()) {
                    System.out.println(f.getFacilityName() + " has " + f.getBookingsDay().size() + " Booking(s) and Cannot be Decommissioned.");
                    return;
                }

                System.out.println("Enter Date to Recommission the Facility on: (dd/mm/yyyy)");
                String dateAsString =  sc.nextLine();
                Date date;
                try {
                    date = new SimpleDateFormat("dd/MM/yyyy").parse(dateAsString);
                } catch (ParseException e) {
                    System.out.println("Error Parsing Date.");
                    return;
                }
                f.setDecommissionDate(date);
                writeFacilities();
                return;
            }
        }
    }

    private static void recommissionFacility() {
        Facility facility = chooseFacility();
        if (facility == null) return;

        for (Facility f : facilityMap.values()) {
            if (facility.equals(f)) {
                if (facility.getDecommissionDate() == null) {
                    System.out.println("'" + facility.getFacilityName() + "' is Currently not Decommissioned.");
                    return;
                }
                f.setDecommissionDate(null);
                writeFacilities();
                System.out.println("'" + facility.getFacilityName() + "' has been Recommissioned.");
                return;
            }
        }
    }

    private static User chooseUser() {
        System.out.println(EQUALS_SEPARATOR);

        System.out.println("Choose User:");
        for (int i = 0; i < users.size(); i++) {
            System.out.println(i + 1 + ") " + users.get(i).getUserEmail());
        }
        System.out.println(DASH_SEPARATOR);
        System.out.println("0) Return");

        System.out.print("?) ");

        int option;
        try {
            option = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid Input");
            return null;
        }

        if (option == 0) return null;

        User user;
        try {
            user = users.get(option - 1);
        } catch (Exception e) {
            System.out.println("Invalid Option.");
            return null;
        }
        return user;
    }

    private static void recordPayment() {
        User user = chooseUser();
        if (user == null) return;

        ArrayList<Booking> bookings = new ArrayList<>();

        for (Booking b : user.getBookings()) {
            if (!b.isPaid()) { //get all unpaid bookings
                bookings.add(b);
            }
        }

        if (bookings.isEmpty()) {
            System.out.println("This User has no Unpaid Bookings");
            return;
        }

        System.out.println("ID\t" + Booking.bookingHeader + "\tFacility Name");
        for (int i = 0; i < bookings.size(); i++) {
            Booking b = bookings.get(i);
            System.out.println(i + 1 + ")\t" + b.toString() + "\t" + facilityMap.get(b.getFacilityId()).getFacilityName());
        }

        System.out.println(DASH_SEPARATOR);
        System.out.println("0) Return");
        System.out.println(EQUALS_SEPARATOR);
        System.out.print("?) ");

        int option;
        try {
            option = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid Input");
            return;
        }

        if (option == 0) return;

        Booking payBooking;

        try {
            payBooking = bookings.get(option - 1);
        } catch (Exception e) {
            System.out.println("Invalid Option.");
            return;
        }

        payBooking.setPayment(true);
        writeBookings();
    }

    private static void viewAccountStatements() {
        User user = chooseUser();
        if (user == null) return;

        viewStatement(user);
    }

    /**
     * Program reads from a file and splits the values into an array and then adds values into the global ArrayList.
     * If the file does not exist, the program will create a new file with the userFileName.
     */
    private static void loadUsers() {
        File f = new File(userFileName);
        if (f.isFile()) {
            Scanner scanner = null;
            try {
                scanner = new Scanner(f);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                String[] values = line.split(",");
                int intType = Integer.parseInt(values[3]);
                boolean type;
                type = intType == 1;
                users.add(new User(Integer.parseInt(values[0]), values[1], values[2], type));
            }
            User.setRecentID(users.size() + 1);
            scanner.close();
        } else {
            try {
                f.createNewFile();
                System.out.println("Created new " + userFileName);
            } catch (IOException e) {
                System.out.println("Failed to create: " + userFileName + "\n" + e.getStackTrace());
            }
        }
    }

    /**
     * This method reads from the facilities file and add them to a Mapping.
     */
    private static void loadFacilities() {
        File f = new File(facilityFileName);
        if (f.isFile()) {
            Scanner scanner = null;
            try {
                scanner = new Scanner(f);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            int recentID = 0;
            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                String[] values = line.split(",");
                Date date = null;
                if (!values[3].equals("null")) {
                    try {
                        date = new SimpleDateFormat("ddMMyyyy").parse(values[3]);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                Facility facility = new Facility(Integer.parseInt(values[0]), values[1],
                        Double.parseDouble(values[2]), date);
                if (facility.getDecommissionDate() != null) {
                    Date now = new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000L);
                    if (facility.getDecommissionDate().before(now)) {
                        System.out.println("'" + facility.getFacilityName() + "'" + " was Decommissioned until: " + facility.getDecommissionDateString());
                        System.out.println("It has now been Recommissioned");
                        facility.setDecommissionDate(null);
                    }
                }

                facilityMap.put(facility.getFacilityID(), facility);
                recentID = Integer.parseInt(values[0]);
            }
            Facility.setRecentID(recentID + 1);
            scanner.close();
        } else {
            try {
                f.createNewFile();
                System.out.println("Created new " + userFileName);
            } catch (IOException e) {
                System.out.println("Failed to create: " + userFileName + "\n" + e.getStackTrace());
            }
        }
    }

    /**
     * Reads from the booking file and creates a new Booking object that is then added to the users Object.
     */
    private static void loadBookings() {
        File f = new File(bookingFileName);
        if (f.isFile()) {
            Scanner scanner = null;
            try {
                scanner = new Scanner(f);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                String[] values = line.split(",");
                Date date = null;
                try {
                    date = new SimpleDateFormat("ddMMyyyy").parse(values[3]);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                int intType = Integer.parseInt(values[5]);
                boolean paymentStatus;
                paymentStatus = intType == 1;
                Booking b = new Booking(Integer.parseInt(values[0]), Integer.parseInt(values[1]),
                        Integer.parseInt(values[2]), date, Integer.parseInt(values[4]), paymentStatus);
                int facilityID = Integer.parseInt(values[1]);
                for (Facility fac : facilityMap.values()) {
                    if (facilityID == fac.getFacilityID()) {
                        fac.addBooking(b);
                    }
                }
                int userID = Integer.parseInt(values[2]);
                for (User u : users) {
                    if (userID == u.getUserID()) {
                        u.addBooking(b);
                    }
                }
            }
            scanner.close();
        } else {
            try {
                f.createNewFile();
                System.out.println("Created new " + userFileName);
            } catch (IOException e) {
                System.out.println("Failed to create: " + userFileName + "\n" + e.getStackTrace());
            }
        }
    }

    /**
     * This method writes the username and password to the file using a PrintWriter.
     */
    private static void writeUsers() {
        try {
            PrintWriter pw = new PrintWriter(userFileName);
            for (User a : users) {
                pw.println(a.getCSV());
            }
            pw.close();
        } catch (FileNotFoundException e) {
            System.out.println("Failed to create: " + userFileName + "\n" + e.getStackTrace());
        }
    }

    /**
     This method writes the facilities to the file using a PrintWriter.
     */
    private static void writeFacilities() {
        PrintWriter pw;
        try {
            pw = new PrintWriter(facilityFileName);
            for (Facility f : facilityMap.values()) {
                pw.println(f.getCSV());
            }
            pw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("Failed to create: " + facilityFileName + "\n" + e.getStackTrace());
        }
    }

    /**
     * This method writes the bookings to the file using a PrintWriter
     */
    private static void writeBookings() {
        PrintWriter pw;
        try {
            pw = new PrintWriter(bookingFileName);
            for (User u : users) {
                ArrayList<Booking> bookings = u.getBookings();
                for (Booking b : bookings) {
                    pw.println(b.getCSV());
                }
            }
            pw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("Failed to create: " + bookingFileName + "\n" + e.getStackTrace());
        }
    }
}

