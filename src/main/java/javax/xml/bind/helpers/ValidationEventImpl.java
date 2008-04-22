package javax.xml.bind.helpers;

import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventLocator;

/**
 * Created by IntelliJ IDEA.
 * User: gnodet
 * Date: Apr 17, 2008
 * Time: 1:15:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class ValidationEventImpl implements ValidationEvent {

    private int severity;
    private String message;
    private Throwable linkedException;
    private ValidationEventLocator locator;

    public ValidationEventImpl(int severity, String message, ValidationEventLocator locator) {
        this(severity, message, locator, null);
    }

    public ValidationEventImpl(int severity, String message, ValidationEventLocator locator, Throwable linkedException) {
        setSeverity(severity);
        this.message = message;
        this.locator = locator;
        this.linkedException = linkedException;
    }

    public int getSeverity() {
        return severity;
    }

    public void setSeverity(int severity) {
        if (severity != 0 && severity != 1 && severity != 2) {
            throw new IllegalArgumentException("Illegal severity");
        }
        this.severity = severity;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Throwable getLinkedException() {
        return linkedException;
    }

    public void setLinkedException(Throwable linkedException) {
        this.linkedException = linkedException;
    }

    public ValidationEventLocator getLocator() {
        return locator;
    }

    public void setLocator(ValidationEventLocator locator) {
        this.locator = locator;
    }

    public String toString() {
        String s;
        switch (getSeverity()) {
            case WARNING:
                s = "WARNING";
                break;
            case ERROR:
                s = "ERROR";
                break;
            case FATAL_ERROR:
                s = "FATAL_ERROR";
                break;
            default:
                s = String.valueOf(getSeverity());
                break;
        }
        return "[severity=" + s + ", message=" + getMessage() + ", locator=" + getLocator() + "]";
    }

}
