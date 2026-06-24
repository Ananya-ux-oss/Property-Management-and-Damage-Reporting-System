import db.Database;
import db.DatabaseConnection;
import model.Admin;
import model.Manager;
import model.Student;
import model.User;
import ui.InputHandler;
import java.util.Scanner;

public class PropertyManagementSystem {

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════════════════╗");
        System.out.println("║   Property Management and Damage Reporting System    ║");
        System.out.println("╚══════════════════════════════════════════════════════╝\n");

        // 1. Core initialization - Hooks connection engine and creates tables
        Database db = Database.getInstance();

        // 2. Build default Admin profile if tables are empty
        if (db.isAvailable()) {
            User existingAdmin = db.findUserByEmail("admin@university.edu");
            if (existingAdmin == null) {
                System.out.println("[System] Seeding default credentials into database tables...");
                Admin defaultAdmin = new Admin("A001", "admin@university.edu", "adminSecret");
                db.saveUser(defaultAdmin);
            }
        }

        System.out.println("════ SYSTEM ONLINE ════");
        System.out.println("  Launch Credentials Available:");
        System.out.println("    Default Admin -> admin@university.edu / adminSecret");

        // 3. Launch UI Handler Loop
        InputHandler input = new InputHandler();
        Scanner sc = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n┌─────────────────────────────────────┐");
            System.out.println("│           MAIN MENU                 │");
            System.out.println("│  [1] Student Portal                 │");
            System.out.println("│  [2] Manager Portal                 │");
            System.out.println("│  [3] Admin Portal                   │");
            System.out.println("│  [4] Exit                           │");
            System.out.println("└─────────────────────────────────────┘");
            System.out.print("  Select: ");

            int choice = -1;
            try {
                choice = Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("  [!] Enter 1, 2, 3, or 4.");
                continue;
            }
            switch (choice) {
                case 1 -> {
                    System.out.println("\n--- Student Login Verification ---");
                    System.out.print("  Enter your registered email: ");
                    String email = sc.nextLine().trim();
                    
                    User foundUser = db.findUserByEmail(email);
                    if (foundUser instanceof Student) {
                        Student student = (Student) foundUser; 
                        input.runStudentMenu(student); // Safe, object is populated from DB!
                    } else {
                        System.out.println("  [!] Access Denied: No Student account found with that email.");
                    }
                }
                case 2 -> {
                    System.out.println("\n--- Manager Login Verification ---");
                    System.out.print("  Enter your registered email: ");
                    String email = sc.nextLine().trim();
                    
                    User foundUser = db.findUserByEmail(email);
                    if (foundUser instanceof Manager) {
                        Manager manager = (Manager) foundUser; 
                        input.runManagerMenu(manager); // Safe, object is populated from DB!
                    } else {
                        System.out.println("  [!] Access Denied: No Manager account found with that email.");
                    }
                }
                case 3 -> {
                    // Admin portal can be run directly with a verified setup or similar lookup
                    User foundUser = db.findUserByEmail("admin@university.edu");
                    if (foundUser instanceof Admin) {
                        input.runAdminMenu((Admin) foundUser);
                    }
                }
                case 4 -> {
                    System.out.println("\n  Thank you for using the system. Goodbye!");
                    running = false;
                }
                default -> System.out.println("  [!] Enter 1, 2, 3, or 4.");
            }
        }

        // Clean system termination
        input.close();
        sc.close();
        DatabaseConnection.getInstance().close(); 
        System.out.println("\n── System shutdown completed cleanly. ─────────────────");
    }
}
