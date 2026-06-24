package db;

import java.sql.*;
import java.util.*;
import model.*;

public class Database {

    private static Database instance;
    private Database() {}
    public static Database getInstance() {
        if (instance == null) instance = new Database();
        return instance;
    }
    private final DatabaseConnection dbConnection = DatabaseConnection.getInstance();
    private final boolean usingJdbc = dbConnection.isAvailable();
    private Connection connection() { return dbConnection.getConnection(); }

    //User CRUD 
    public void saveUser(User user, String plainPassword) {
        String hash = User.hashPassword(plainPassword);
        if (usingJdbc) {
            try {
                PreparedStatement ps = connection().prepareStatement(
                    "INSERT IGNORE INTO users(user_id,name,email,password_hash,role) VALUES(?,?,?,?,?)");
                ps.setString(1, user.getUserId());
                ps.setString(2, user.getName());
                ps.setString(3, user.getEmail());
                ps.setString(4, hash);
                ps.setString(5, user.getRole());
                ps.executeUpdate();
                ps.close();
            } catch (SQLException e) {
                System.out.println("[DB] saveUser SQL error: " + e.getMessage());
            }
        } 
    }

    public boolean validateUser(String email, String passwordHash) {
        if (usingJdbc) {
            try {
                PreparedStatement ps = connection().prepareStatement(
                    "SELECT 1 FROM users WHERE email=? AND password_hash=?");
                ps.setString(1, email);
                ps.setString(2, passwordHash);
                boolean found = ps.executeQuery().next();
                ps.close();
                return found;
            } catch (SQLException e) {
                System.out.println("[DB] validateUser SQL error: " + e.getMessage());
                return false;
            } 
        } 
      else {return false}
    }

    //Property CRUD

    public void saveProperty(Property p) {
        if (usingJdbc) {
            try {
                PreparedStatement ps = connection().prepareStatement(
                    "INSERT IGNORE INTO properties(property_id,property_name,type,location,manager_id,active) VALUES(?,?,?,?,?,?)");
                ps.setString(1, p.getPropertyId());
                ps.setString(2, p.getPropertyName());
                ps.setString(3, p.getType());
                ps.setString(4, p.getLocation());
                ps.setString(5, p.getManagerId());
                ps.setBoolean(6, p.isActive());
                ps.executeUpdate();
                ps.close();
            } catch (SQLException e) {
                System.out.println("[DB] saveProperty SQL error: " + e.getMessage());
            }
        } 
        
    }

    public void updateProperty(Property p) {
        if (usingJdbc) {
            try {
                PreparedStatement ps = connection().prepareStatement(
                    "UPDATE properties SET location=?, active=? WHERE property_id=?");
                ps.setString (1, p.getLocation());
                ps.setBoolean(2, p.isActive());
                ps.setString (3, p.getPropertyId());
                ps.executeUpdate();
                ps.close();
            } catch (SQLException e) {
                System.out.println("[DB] updateProperty SQL error: " + e.getMessage());
            }
        } 
    }

    public Property findPropertyById(String propertyId) {
    if (usingJdbc) {
        try {
            PreparedStatement ps = connection().prepareStatement(
                "SELECT * FROM properties WHERE property_id=?");
            ps.setString(1, propertyId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Property p = new Property(
                    rs.getString("property_id"),
                    rs.getString("property_name"),
                    rs.getString("type"),
                    rs.getString("manager_id")
                );

                p.setLocation(rs.getString("location"));
                p.setActive(rs.getBoolean("active"));

                ps.close();
                return p;
            }

            ps.close();
        } catch (SQLException e) {
            System.out.println("[DB] findProperty SQL error: " + e.getMessage());
        }
    }
    return null;
}
    //DamageReport CRUD

    public void saveDamageReport(DamageReport r) {
        if (usingJdbc) {
            try {
                PreparedStatement ps = connection().prepareStatement(
                    "INSERT INTO damage_reports(report_id,student_id,property_id,description,status,review_notes) VALUES(?,?,?,?,?,?)");
                ps.setString(1, r.getReportId());
                ps.setString(2, r.getStudentId());
                ps.setString(3, r.getPropertyId());
                ps.setString(4, r.getDescription());
                ps.setString(5, r.getStatus().name());
                ps.setString(6, r.getReviewNotes());
                ps.executeUpdate();
                ps.close();
            } catch (SQLException e) {
                System.out.println("[DB] saveDamageReport SQL error: " + e.getMessage());
            }
        } 
    }

    public void updateDamageReport(DamageReport r) {
        if (usingJdbc) {
            try {
                PreparedStatement ps = connection().prepareStatement(
                    "UPDATE damage_reports SET status=?, review_notes=? WHERE report_id=?");
                ps.setString(1, r.getStatus().name());
                ps.setString(2, r.getReviewNotes());
                ps.setString(3, r.getReportId());
                ps.executeUpdate();
                ps.close();
            } catch (SQLException e) {
                System.out.println("[DB] updateDamageReport SQL error: " + e.getMessage());
            }
        } 
    }

    public List<DamageReport> getAllDamageReports() {
        List<DamageReport> list = new ArrayList<>();
        if (usingJdbc) {
            try (Statement stmt = connection().createStatement();
                 ResultSet rs   = stmt.executeQuery("SELECT * FROM damage_reports")) {
                while (rs.next()) {
                    DamageReport r = new DamageReport(
                        rs.getString("report_id"),
                        rs.getString("student_id"),
                        rs.getString("property_id"),
                        rs.getString("description"),
                        rs.getTimestamp("reported_at").toLocalDateTime()
                    );
                    r.setStatus(DamageReport.Status.valueOf(rs.getString("status")));
                    r.setReviewNotes(rs.getString("review_notes"));
                    list.add(r);
                }
            } catch (SQLException e) {
                System.out.println("[DB] getAllDamageReports SQL error: " + e.getMessage());
            }
        } 
        return list;
    }
}
