package javax.xml.bind;

/**
 * Created by IntelliJ IDEA.
 * User: gnodet
 * Date: Apr 17, 2008
 * Time: 10:16:01 AM
 * To change this template use File | Settings | File Templates.
 */
public class PropertyException extends JAXBException {

    public PropertyException(String message) {
        super(message);
    }

    public PropertyException(String message, String errorCode) {
        super(message, errorCode);
    }

    public PropertyException(Throwable cause) {
        super(cause);
    }

    public PropertyException(String message, Throwable cause) {
        super(message, cause);
    }

    public PropertyException(String message, String errorCode, Throwable cause) {
        super(message, errorCode, cause);
    }

    public PropertyException(String name, Object value) {
        super("name: " + name + ", value: " + value);
    }

}
