package javax.xml.bind.helpers;

import java.net.URL;

import javax.xml.bind.ValidationEventHandler;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventLocator;

import org.w3c.dom.Node;

/**
 * Created by IntelliJ IDEA.
 * User: gnodet
 * Date: Apr 17, 2008
 * Time: 12:23:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class DefaultValidationEventHandler implements ValidationEventHandler {

    public boolean handleEvent(ValidationEvent event) {
        if (event == null) {
            throw new IllegalArgumentException();
        }
        String severity = null;
        boolean retVal = false;
        switch(event.getSeverity()) {
        case ValidationEvent.WARNING: // '\0'
            severity = "[WARNING]: ";
            retVal = true;
            break;

        case ValidationEvent.ERROR: // '\001'
            severity = "[ERROR]: ";
            retVal = false;
            break;

        case ValidationEvent.FATAL_ERROR: // '\002'
            severity = "[FATAL_ERROR]: ";
            retVal = false;
            break;
        }
        String location = getLocation(event);
        System.out.println("DefaultValidationEventHandler " + severity + " " + event.getMessage() + "\n     Location: " + location);
        return retVal;
    }

    private String getLocation(ValidationEvent event) {
        StringBuffer msg = new StringBuffer();
        ValidationEventLocator locator = event.getLocator();
        if (locator != null) {
            URL url = locator.getURL();
            Object obj = locator.getObject();
            Node node = locator.getNode();
            int line = locator.getLineNumber();
            if(url != null || line != -1) {
                msg.append("line ").append(line);
                if(url != null) {
                    msg.append(" of ").append(url);
                }
            } else {
                if (obj != null) {
                    msg.append(" obj: ").append(obj.toString());
                } else if(node != null) {
                    msg.append(" node: ").append(node.toString());
                }
            }
        } else {
            msg.append("unavailable");
        }
        return msg.toString();
    }

}
