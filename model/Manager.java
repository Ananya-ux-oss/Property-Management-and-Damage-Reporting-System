package model;

import java.util.*;
import Interface.*;
import db.Database;

public class Manager extends User implements Reviewable, Summarizable {

    private String department;

    public Manager(String userId, String name, String email, String password) {
        super(userId, name, email, password);
        this.department = "Managament"; 
    }

    //Getter
    @Override
    public String getRole() { 
        return "Manager"; 
    }

    public String getDepartment() { 
        return department; 
    }

    //Setter
    public void setDepartment(String dept) { 
        this.department = dept; 
    }

    public Property registerProperty(String propId, String propName, String type, int capacity) {
        if (!isLoggedIn()) {
            System.out.println("[RegisterProperty] Must be logged in.");
            return null;
        }
        try {
            Property property = new Property(propId, propName, type, capacity, getUserId()); 
            Database.getInstance().saveProperty(property);
            System.out.println("[RegisterProperty] Registered: " + property);
            return property;
        } catch (Exception e) {
            System.out.println("[RegisterProperty] Error: " + e.getMessage());
            return null;
        }
    }

    public void assignLocation(Property property, String location) {
        if (!isLoggedIn()) {
            System.out.println("[AssignLocation] Must be logged in.");
            return;
        }
        if (property == null) {
            System.out.println("[AssignLocation] No property provided.");
            return;
        }
        
        property.setLocation(location);
        Database.getInstance().updateProperty(property); 
        System.out.println("[AssignLocation] \"" + location + "\" assigned to " + property.getPropertyName());
    }

    @Override
    public void summarizePropertyInformation(Property property) {
        if (!isLoggedIn()) {
            System.out.println("[Summarize] Must be logged in.");
            return;
        }
        System.out.println("[Summarize] Property Information:");
        System.out.println("  " + property.getSummary());
    }

    @Override
    public void reviewDamageReports() {
        if (!isLoggedIn()) {
            System.out.println("[ReviewDamage] Must be logged in.");
            return;
        }
        List<DamageReport> reports = Database.getInstance().getAllDamageReports();
        if (reports.isEmpty()) {
            System.out.println("[ReviewDamage] No reports to review.");
            return;
        }
        System.out.println("[ReviewDamage] Reviewing " + reports.size() + " report(s):");
        for (DamageReport r : reports) {
            System.out.println("  " + r);
            r.setStatus(DamageReport.Status.REVIEWED);//t changes the status of a single DamageReport object to REVIEWED.
            r.setReviewNotes("Reviewed by " + getName());
            Database.getInstance().updateDamageReport(r);//save changes
        }
        System.out.println("[ReviewDamage] All reports marked as REVIEWED.");
    }
}
