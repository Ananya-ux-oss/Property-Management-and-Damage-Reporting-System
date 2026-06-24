public class Admin extends User {

    public Admin(String userId, String email, String password) {
        super(userId, "System Admin", email, password);
        Database.getInstance().saveUser(this, password);
    }

    @Override
    public String getRole() { return "Admin"; }

    public Manager registerManager(String mgrId, String name,
                                    String email, String password) {
        try {
            Manager manager = new Manager(mgrId, name, email, password);
            Database.getInstance().saveUser(manager, password);
            System.out.println("[RegisterManager] New manager created: " + manager);
            return manager;
        } catch (Exception e) {
            System.out.println("[RegisterManager] Error: " + e.getMessage());
            return null;
        }
    }
}