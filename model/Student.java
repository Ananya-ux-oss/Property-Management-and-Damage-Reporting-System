package model;

import db.Database;
import exception.*;

public class Student extends User {

    private String assignedRoomId;

    public Student(String userId, String name, String email, String password) {
        super(userId, name, email, password);
        Database.getInstance().saveUser(this, password);
    }

    @Override
    public String getRole() { 
        return "Student"; 
    }
    
    public String getAssignedRoomId() { 
        return assignedRoomId; 
    }
    public void setAssignedRoomId(String roomId) { 
        this.assignedRoomId = roomId; 
    }

    public DamageReport reportDamage(String propertyId, String description) {
        if (!isLoggedIn()) {
            System.out.println("[ReportDamage] Must be logged in first.");
            return null;
        }
        try {
            Property property = Database.getInstance().findPropertyById(propertyId);
            if (property == null) {
                throw new PropertyNotFoundException(propertyId);
            }
            String reportId = "DR" + System.currentTimeMillis(); //creates report id
            DamageReport report = new DamageReport(reportId, getUserId(), propertyId, description); //creats new damage report object
            Database.getInstance().saveDamageReport(report); //saves report
            System.out.println("[ReportDamage] Report submitted by " + getName() //success message
                             + " for property " + property.getPropertyName()
                             + ": \"" + description + "\"");
            return report;
        } catch (PropertyNotFoundException e) { //handles PropertyNotFoundException
            System.out.println("[ReportDamage] Error — " + e.getMessage());
            return null;
        } catch (Exception e) { //handels unexpected errors
            System.out.println("[ReportDamage] Unexpected error: " + e.getMessage());
            return null;
        }
    }
}
