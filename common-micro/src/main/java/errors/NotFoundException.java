package errors;

/**
 * @author jrodriguez
 */
public class NotFoundException extends RuntimeException {
    public NotFoundException(Object o) {
        super("resource: ".concat(o.toString()).concat(" not found"));
    }
}
