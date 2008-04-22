package javax.xml.bind;

/**
 * Created by IntelliJ IDEA.
 * User: gnodet
 * Date: Apr 17, 2008
 * Time: 10:20:29 AM
 * To change this template use File | Settings | File Templates.
 */
public class MarshalException extends JAXBException {

    public MarshalException(String message) {
        super(message);
    }

    public MarshalException(String message, String errorCode) {
        super(message, errorCode);
    }

    public MarshalException(String message, String errorCode, Throwable cause) {
        super(message, errorCode, cause);
    }

    public MarshalException(String message, Throwable cause) {
        super(message, cause);
    }

    public MarshalException(Throwable cause) {
        super(cause);
    }
}
