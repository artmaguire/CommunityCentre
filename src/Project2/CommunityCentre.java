package Project2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class CommunityCentre {
    private static User loggedInAdmin = null;

    private static final String userFileName = "users.csv";
    private static final String facilityFilename = "facilities.csv";
    private static final String bookingFileNmae = "bookings.csv";

    private static ArrayList<User> users = new ArrayList<>();
    private static ArrayList<Facility> facilities = new ArrayList<>();

    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        loadAdministrators();

        createLoginUser();

        writeAdministrators();
    }

    /**
     * Program reads from a file and splits the values into an array and then adds values into the global ArrayList.
     * If the file does not exist, the program will create a new file with the userFileName.
     */
    private static void loadAdministrators() {
        File f = new File(userFileName);
        if(f.isFile()) {
            Scanner scanner = null;
            try {
                scanner = new Scanner(f);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            while(scanner.hasNext()) {
                String line = scanner.nextLine();
                String[] values = line.split(",");
                users.add(new User(values[0], values[1]));
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
    private static void writeAdministrators() {
        try {
            PrintWriter pw = new PrintWriter(userFileName);
            for(User a : users) {
                pw.println(a.getCSV());
            }
            pw.close();
        } catch (FileNotFoundException e) {
            System.out.println("Failed to create: " + userFileName + "\n" + e.getStackTrace());
        }
    }

    /**
     * Creates the interface for interacting with the program, Logging in, Creating a new user and exiting hte program.
     * The switch in this Method links the method between the loadAdmin Method and the createAdmin Method.
     */
    private static void createLoginUser() {
        boolean exit = false;
        while(!exit) {
            System.out.println("1) Login Existing User\n2) Create New User\n\n0) Exit");
            System.out.println("--------------------");
            System.out.print("?) ");

            int option = -1;

            option = Integer.parseInt(sc.nextLine());

            switch (option) {
                case 0: exit = true; break;
                case 1: loginAdmin(); break;
                case 2: createAdmin(); break;
            }
        }
    }

    /**
     * This Method allows the user to log in to an existing user using name and password.
     * It gives the user 3 attempts. If login is successful, the program will show the leagues associated with the user.
     */
    private static void loginAdmin() {
        int attempts = 3;
        while(attempts > 0) {
            System.out.println("\n====================");
            System.out.println("Attempts Remaining: " + attempts);
            System.out.println("\n====================");
            System.out.println("Username: ");
            String username = sc.nextLine();
            System.out.println("Password: ");
            String password = sc.nextLine();

            loggedInAdmin = checkAdmins(username, password);

            if(loggedInAdmin != null) {
                System.out.println("Successfully Logged In\n");
                attempts = -1;
                listLeagues();
            } else {
                System.out.println("No User Found\n");
                attempts--;
            }
        }
    }

    /**
     * This Method creates a new User and adds it to the users ArrayList.
     * The program will check the checkAdmins Method to see if the user exists,
     * if the user does exist, it will show an appropriate error message.
     */
    private static void createAdmin() {
        System.out.println("\n====================");
        System.out.println("Username: ");
        String username = sc.nextLine();
        System.out.println("Password: ");
        String password = sc.nextLine();

        if(checkAdmins(username, password) != null) {
            System.out.println("User Already Exists");
        } else {
            User newAdmin = new User(username, password); //create new administrator object from users inputs
            users.add(newAdmin); //add them to the array of users
            loggedInAdmin = newAdmin; //set the new user as the current logged in user
            listLeagues();
        }
    }

    /**
     * Shows a list of leagues administered by the logged in administrator
     */
    private static void listLeagues() {
        System.out.println("0) Logout");
        System.out.println("--------------------");
        System.out.print("?) ");

        int option = -1;
        option = Integer.parseInt(sc.nextLine());

        switch (option) {
            case 0: loggedInAdmin = null; return;
        }
    }

    /**
     * Checks all entries in the users ArrayList for a valid username and password and returns boolean value.
     */
    private static User checkAdmins(String username, String password) {
        for(User a : users) {
            if(a.check(username, password)) return a;
        }
        return null;
    }
}