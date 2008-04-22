package javax.xml.bind;

/**
 * Created by IntelliJ IDEA.
 * User: gnodet
 * Date: Apr 17, 2008
 * Time: 10:23:28 AM
 * To change this template use File | Settings | File Templates.
 */
public class ValidationException extends JAXBException {

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, String errorCode) {
        super(message, errorCode);
    }

    public ValidationException(String message, String errorCode, Throwable cause) {
        super(message, errorCode, cause);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ValidationException(Throwable cause) {
        super(cause);
    }
}
