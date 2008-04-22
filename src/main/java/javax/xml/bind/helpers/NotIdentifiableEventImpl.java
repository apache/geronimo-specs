package javax.xml.bind.helpers;

import javax.xml.bind.NotIdentifiableEvent;
import javax.xml.bind.ValidationEventLocator;

/**
 * Created by IntelliJ IDEA.
 * User: gnodet
 * Date: Apr 17, 2008
 * Time: 1:20:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class NotIdentifiableEventImpl extends ValidationEventImpl implements NotIdentifiableEvent {

    public NotIdentifiableEventImpl(int severity, String message, ValidationEventLocator locator) {
        super(severity, message, locator);
    }

    public NotIdentifiableEventImpl(int severity, String message, ValidationEventLocator locator, Throwable linkedException) {
        super(severity, message, locator, linkedException);
    }
}
