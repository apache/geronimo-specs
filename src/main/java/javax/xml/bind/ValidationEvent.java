package javax.xml.bind;

/**
 * Created by IntelliJ IDEA.
 * User: gnodet
 * Date: Apr 17, 2008
 * Time: 8:52:15 AM
 * To change this template use File | Settings | File Templates.
 */
public interface ValidationEvent {

    int ERROR = 1;
    int FATAL_ERROR = 2;
    int WARNING = 0;

    Throwable getLinkedException();

    ValidationEventLocator getLocator();

    String getMessage();

    int getSeverity();

}
