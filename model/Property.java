package model;
import java.time.LocalDateTime;

public class Property {

    private final String  propertyId;
    private       String  propertyName;
    private       String  type;
    private       String  location;
    private final String  managerId;
    private final LocalDateTime registeredAt;
    private       boolean   active;

    public Property(String propertyId, String propertyName,
        String type, String managerId) {
        this.propertyId   = propertyId;
        this.propertyName = propertyName;
        this.type         = type;
        this.managerId    = managerId;
        this.location     = "Unassigned";
        this.registeredAt = LocalDateTime.now();
        this.active       = true;
    }
    
    //Getter
    public String getPropertyId() {    
        return propertyId; 
    }
    public String getPropertyName() { 
        return propertyName;  
    }
   
    public String getType() {
        return type; 
    }

    public String getLocation() { 
        return location;
    }

    public String getManagerId() { 
        return managerId; 
    }

    public boolean isActive()  {
        return active; 
    }

    public LocalDateTime getRegisteredAt() {
        return registeredAt; 
    }
   
    public String getSummary() {
        return String.format(
            "ID=%-6s | %-22s | Type=%-12s | Cap=%-3d | Location=%-35s | Active=%s",
            propertyId, propertyName, type, capacity, location, active
        );
    }

    //Setter
    public void  setPropertyName(String n) {
        this.propertyName = n; 
    }

    public void setType(String t) { 
        this.type = t;
    }
    
    public void setLocation(String loc) {
         this.location = loc; 
    }
    
     public void setActive(boolean a) { 
        this.active = a; 
    }

    @Override
    public String toString() {
        return "Property |id=" + propertyId + ", name=" + propertyName
             + ", location=" + location + "|";
    }
}
