package javax.xml.bind;

/**
 * Created by IntelliJ IDEA.
 * User: gnodet
 * Date: Apr 17, 2008
 * Time: 10:22:55 AM
 * To change this template use File | Settings | File Templates.
 */
public class UnmarshalException extends JAXBException {

    public UnmarshalException(String message) {
        super(message);
    }

    public UnmarshalException(String message, String errorCode) {
        super(message, errorCode);
    }

    public UnmarshalException(String message, String errorCode, Throwable cause) {
        super(message, errorCode, cause);
    }

    public UnmarshalException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnmarshalException(Throwable cause) {
        super(cause);
    }
}
