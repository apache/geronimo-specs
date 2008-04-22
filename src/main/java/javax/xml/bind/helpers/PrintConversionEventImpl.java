package javax.xml.bind.helpers;

import javax.xml.bind.PrintConversionEvent;
import javax.xml.bind.ValidationEventLocator;

/**
 * Created by IntelliJ IDEA.
 * User: gnodet
 * Date: Apr 17, 2008
 * Time: 1:22:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class PrintConversionEventImpl extends ValidationEventImpl implements PrintConversionEvent {

    public PrintConversionEventImpl(int severity, String message, ValidationEventLocator locator) {
        super(severity, message, locator);
    }

    public PrintConversionEventImpl(int severity, String message, ValidationEventLocator locator, Throwable linkedException) {
        super(severity, message, locator, linkedException);
    }
}
