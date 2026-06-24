package ui;

import java.util.*;
import model.*;
import db.Database;

public class InputHandler {

    private final Scanner sc;

    public InputHandler() {
        this.sc = new Scanner(System.in);
    }

    private String readLine(String prompt) {
        System.out.print(prompt);
        String line = sc.nextLine().trim();
        while (line.isEmpty()) {
            System.out.print("  (cannot be blank) " + prompt);
            line = sc.nextLine().trim();
        }
        return line;
    }

    private int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("  [!] Please enter a whole number.");
            }
        }
    }

    public void runStudentMenu(Student student) {
        
        System.out.println("        STUDENT LOGIN PORTAL        ");
        System.out.println("────────────────────────────────────");

        String email    = readLine("  Enter your email    : ");
        String password = readLine("  Enter your password : ");

        boolean loggedIn = student.login(email, password);
        if (!loggedIn) {
            System.out.println("  Login failed. Returning to main menu.");
            return;
        }

        boolean running = true;
        while (running) {
            System.out.println("\n  ───────── Student Menu ────────");
            System.out.println("  (1) Report Property Damage");
            System.out.println("  (2) Logout");
            System.out.println("  ─────────────────────────────────────");

            int choice = readInt("  Your choice: ");
            switch (choice) {
                case 1 -> {
                    String propertyId  = readLine("  Property ID to report (e.g. P001) : ");
                    String description = readLine("  Describe the damage                : ");
                    DamageReport report = student.reportDamage(propertyId, description);
                    if (report != null) {
                        System.out.println("  ✔ Report saved with ID: " + report.getReportId());
                    }
                }
                case 2 -> {
                    student.logout();
                    running = false;
                }
                default -> System.out.println("  [!] Invalid option — enter 1 or 2.");
            }
        }
    }

    public void runManagerMenu(Manager manager) {
        
        System.out.println("        MANAGER LOGIN PORTAL         ");
        System.out.println("─────────────────────────────────────");

        String email    = readLine("  Enter your email    : ");
        String password = readLine("  Enter your password : ");

        boolean loggedIn = manager.login(email, password);
        if (!loggedIn) {
            System.out.println("  Login failed. Returning to main menu.");
            return;
        }

        boolean running = true;
        while (running) {
            System.out.println("\n ──────────── Manager Menu ───────────");
            System.out.println("  (1) Register New Property");
            System.out.println("  (2) Assign Location to Property");
            System.out.println("  (3) Summarize Property Information");
            System.out.println("  (4) Review All Damage Reports");
            System.out.println("  (5) Logout");
            System.out.println("  ─────────────────────────────────────");

            int choice = readInt("  Your choice: ");
            switch (choice) {
                case 1 -> {
                    String propId   = readLine("  New Property ID   : ");
                    String propName = readLine("  Property name     : ");
                    String type     = readLine("  Type (e.g. Lab, Dorm, Office) : ");
                    Property newProp = manager.registerProperty(propId, propName, type);
                    if (newProp != null) {
                        System.out.println("  ✔ Property registered: " + newProp);
                        String assignNow = readLine("  Assign a location now? (y/n) : ");
                        if (assignNow.equalsIgnoreCase("y")) {
                            String location = readLine("  Location description : ");
                            manager.assignLocation(newProp, location);
                        }
                    }
                }
                case 2 -> {
                    String propId = readLine("  Property ID to update : ");
                    Property prop = Database.getInstance().findPropertyById(propId);
                    if (prop == null) {
                        System.out.println("  [!] Property '" + propId + "' not found.");
                    } else {
                        String location = readLine("  New location description : ");
                        manager.assignLocation(prop, location);
                    }
                }
                case 3 -> {
                    String propId = readLine("  Property ID to summarize : ");
                    Property prop = Database.getInstance().findPropertyById(propId);
                    if (prop == null) {
                        System.out.println("  [!] Property '" + propId + "' not found.");
                    } else {
                        manager.summarizePropertyInformation(prop);
                    }
                }
                case 4 -> manager.reviewDamageReports();
                case 5 -> {
                    manager.logout();
                    running = false;
                }
                default -> System.out.println("  [!] Invalid option — enter 1–5.");
            }
        }
    }

    public void runAdminMenu(Admin admin) {
        System.out.println("         ADMIN LOGIN PORTAL          ");
        System.out.println("└─────────────────────────────────────┘");

        String email    = readLine("  Enter your email    : ");
        String password = readLine("  Enter your password : ");

        boolean loggedIn = admin.login(email, password);
        if (!loggedIn) {
            System.out.println("  Login failed. Returning to main menu.");
            return;
        }

        boolean running = true;
        while (running) {
            System.out.println("\n  ────────── Admin Menu ────────────────");
            System.out.println("  [1] Register New Manager");
            System.out.println("  [2] Register New Student");
            System.out.println("  [3] Logout");
            System.out.println("  ───────────────────────────────────────");

            int choice = readInt("  Your choice: ");
            switch (choice) {
                case 1 -> {
                    String name     = readLine("  Manager name     : ");
                    String mgrEmail= readLine("  Manager email    : ");
                    String mgrPass = readLine("  Manager password : ");
                    String id      = nextId("M");

                    Manager newManager = admin.registerManager(id, name, mgrEmail, mgrPass);
                    if (newManager != null) {
                        System.out.println("  ✔ Manager registered with ID: " + newManager.getUserId());
                    }
                }
                case 2 -> {
                    String name      = readLine("  Student name     : ");
                    String stuEmail  = readLine("  Student email    : ");
                    String stuPass   = readLine("  Student password : ");
                    String id        = nextId("S");
                    Student newStudent = new Student(id, name, stuEmail, stuPass);
                    System.out.println("  ✔ Student registered with ID: " + newStudent.getUserId());
                }
                case 3 -> {
                    admin.logout();
                    running = false;
                }
                default -> System.out.println("  [!] Invalid option — enter 1-3.");
            }
        }
    }
    
    public void close() {
        sc.close();
    }
}
