package javax.xml.bind;

/**
 * Created by IntelliJ IDEA.
 * User: gnodet
 * Date: Apr 17, 2008
 * Time: 10:07:57 AM
 * To change this template use File | Settings | File Templates.
 */
public class TypeConstraintException extends RuntimeException {

    private String errorCode;
    private Throwable linkedException;

    public TypeConstraintException(String message) {
        this(message, null, null);
    }

    public TypeConstraintException(String message, String errorCode) {
        this(message, errorCode, null);
    }

    public TypeConstraintException(String message, String errorCode, Throwable cause) {
        super(message);
        this.errorCode = errorCode;
        this.linkedException = cause;
    }

    public TypeConstraintException(String message, Throwable cause) {
        this(message, null, cause);
    }

    public TypeConstraintException(Throwable cause) {
        this(null, null, cause);
    }

    public String getErrorCode() {
        return errorCode;
    }

    public Throwable getLinkedException() {
        return getCause();
    }

    public synchronized void setLinkedException(Throwable linkedException) {
        this.linkedException = linkedException;
    }

    public String toString() {
        return linkedException != null ?
                super.toString() + "\n - with linked exception:\n[" + linkedException.toString() + "]" :
                super.toString();
    }

}