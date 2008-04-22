package javax.xml.bind.util;

import java.util.ArrayList;

import javax.xml.bind.ValidationEventHandler;
import javax.xml.bind.ValidationEvent;

/**
 * Created by IntelliJ IDEA.
 * User: gnodet
 * Date: Apr 17, 2008
 * Time: 1:31:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class ValidationEventCollector implements ValidationEventHandler {

    private ArrayList<ValidationEvent> events = new ArrayList<ValidationEvent>();

    public ValidationEvent[] getEvents() {
        return events.toArray(new ValidationEvent[events.size()]);
    }

    public boolean handleEvent(ValidationEvent event) {
        events.add(event);
        return event.getSeverity() != ValidationEvent.FATAL_ERROR;
    }

    public boolean hasEvents() {
        return !events.isEmpty();
    }

    public void reset() {
        events.clear();
    }
}
