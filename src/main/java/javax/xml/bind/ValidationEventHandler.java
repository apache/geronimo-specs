package javax.xml.bind;

/**
 * Created by IntelliJ IDEA.
 * User: gnodet
 * Date: Apr 17, 2008
 * Time: 8:51:47 AM
 * To change this template use File | Settings | File Templates.
 */
public interface ValidationEventHandler {

    boolean handleEvent(ValidationEvent event);
}
