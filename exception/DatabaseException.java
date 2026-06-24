package exception;

public class DatabaseException extends SystemException {
    public DatabaseException(String detail) {
        super("Database error: " + detail);
    }
}
