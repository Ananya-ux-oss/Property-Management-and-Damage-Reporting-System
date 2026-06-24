public class AuthenticationException extends SystemException {
    public AuthenticationException(String detail) {
        super("Authentication failed: " + detail);
    }
}