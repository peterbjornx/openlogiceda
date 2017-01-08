package nl.peterbjornx.openlogiceda.util;

/**
 * Represents an error during circuit modification.
 */
public class ModificationException extends Exception {

    /**
     * Creates a new exception object with the given message
     */
    public ModificationException(String message) {
        super(message);
    }
}
