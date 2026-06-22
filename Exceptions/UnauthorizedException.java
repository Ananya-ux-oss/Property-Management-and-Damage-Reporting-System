public class UnauthorizedException extends SystemException {
    public UnauthorizedException(String role, String action) {
        super("Role '" + role + "' is not authorized to: " + action);
    }
}