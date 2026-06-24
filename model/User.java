package model;

import exception.*;
import db.Database;


public abstract class User {

    //Attributes
    private final String userId;
    private final String name;
    private final String email;
    private final String passwordHash;   
    private boolean loggedIn;

    //Constructor 
    protected User(String userId, String name, String email, String password) {
        this.userId       = userId;
        this.name         = name;
        this.email        = email;
        this.passwordHash = hashPassword(password);
        this.loggedIn     = false;
    }

    //Getters
    public String  getUserId() { 
      return userId; 
    }
    public String  getName() { 
      return name; 
    }
    public String  getEmail() {
      return email; 
    }
    public boolean isLoggedIn() { 
      return loggedIn; 
    }
    public String  getPasswordHash() { 
      return passwordHash; 
    }

    //Methods
  
    public boolean login(String email, String password) {
        try {
            boolean valid = Database.getInstance().validateUser(email, hashPassword(password));
            if (!valid) {
                throw new AuthenticationException("Invalid email or password for: " + email);
            }
            this.loggedIn = true;
            System.out.println("[Login]  " + name + " (" + getRole() + ") logged in successfully.");
            return true;

        } catch (AuthenticationException e) {
            System.out.println("[Login]  " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.out.println("[Login]  Unexpected error: " + e.getMessage());
            return false;
        }
    }

    public void logout() {
        this.loggedIn = false;
        System.out.println("[Logout] " + name + " logged out.");
    }

    //Abstract Method
    public abstract String getRole();

    public static String hashPassword(String password) {
        return "HASH[" + password.hashCode() + "]";
    }

    @Override
    public String toString() {
        return getRole() + "| id=" + userId + ", name=" + name + ", email=" + email + "|";
    }
}
