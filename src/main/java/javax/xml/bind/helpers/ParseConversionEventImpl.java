package javax.xml.bind.helpers;

import javax.xml.bind.ParseConversionEvent;
import javax.xml.bind.ValidationEventLocator;

/**
 * Created by IntelliJ IDEA.
 * User: gnodet
 * Date: Apr 17, 2008
 * Time: 1:21:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class ParseConversionEventImpl extends ValidationEventImpl implements ParseConversionEvent {

    public ParseConversionEventImpl(int severity, String message, ValidationEventLocator locator) { 
        super(severity, message, locator);
    }

    public ParseConversionEventImpl(int severity, String message, ValidationEventLocator locator, Throwable linkedException) {
        super(severity, message, locator, linkedException);
    }
}
