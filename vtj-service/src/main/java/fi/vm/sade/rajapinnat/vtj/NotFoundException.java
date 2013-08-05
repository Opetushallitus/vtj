package fi.vm.sade.rajapinnat.vtj;

/**
 * User: tommiha
 * Date: 8/5/13
 * Time: 12:50 PM
 */
public class NotFoundException extends RuntimeException {
    public NotFoundException() {
    }

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundException(Throwable cause) {
        super(cause);
    }
}
