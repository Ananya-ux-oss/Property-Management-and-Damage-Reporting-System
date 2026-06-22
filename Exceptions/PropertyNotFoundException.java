public class PropertyNotFoundException extends SystemException {
    public PropertyNotFoundException(String propertyId) {
        super("Property not found with ID: " + propertyId);
    }
}