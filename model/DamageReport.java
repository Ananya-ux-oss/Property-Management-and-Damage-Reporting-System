package model;

import java.time.LocalDateTime;

public class DamageReport {

    public enum Status { PENDING, REVIEWED, RESOLVED }

    private final String reportId;
    private final String studentId;
    private final String propertyId;
    private String description;
    private Status status;
    private final LocalDateTime reportedAt;
    private String reviewNotes;

    public DamageReport(String reportId, String studentId, String propertyId, String description) {
        this.reportId    = reportId;
        this.studentId   = studentId;
        this.propertyId  = propertyId;
        this.description = description;
        this.status      = Status.PENDING;
        this.reportedAt  = LocalDateTime.now();
        this.reviewNotes = "";
    }

    //Getter
    public String  getReportId() {
         return reportId; }

    public String getStudentId() { 
        return studentId; }

    public String getPropertyId() { 
        return propertyId; }

    public String  getDescription() {
         return description; }

    public void setDescription(String d) { 
        this.description = d; }
    
    public Status getStatus() { 
        return status; }
    
    public void setStatus(Status s)  { 
        this.status = s; }
    
     public LocalDateTime getReportedAt() {
         return reportedAt; }

    public String getReviewNotes() { 
         return reviewNotes; }
   
    //Setter
    public void setReviewNotes(String n) { 
        this.reviewNotes = n; }

    @Override
    public String toString() {
        return String.format(
            "DamageReport |id=%s, property=%s, student=%s, status=%s, desc='%s'|",
            reportId, propertyId, studentId, status, description
        );
    }
}
