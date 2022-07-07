package errors;

/**
 * @author jrodriguez
 */
public class DeletionInvalidException extends RuntimeException {
    public DeletionInvalidException(Object o) {
        super("resource: ".concat(o.toString()).concat(" cannot be deleted, change status first"));
    }
}
