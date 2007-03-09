package javax.xml.stream;

public class XMLStreamException extends Exception {
	protected Throwable nested;

	protected Location location;

	public XMLStreamException() {
	}

	public XMLStreamException(java.lang.String msg) {
		super(msg);
	}

	public XMLStreamException(java.lang.Throwable th) {
		this.nested = th;
	}

	public XMLStreamException(java.lang.String msg, java.lang.Throwable th) {
		super(msg);
		this.nested = th;
	}

	public XMLStreamException(java.lang.String msg, Location location,
			java.lang.Throwable th) {
		super("ParseError at [row,col]:[" + location.getLineNumber() + ","
				+ location.getColumnNumber() + "]\n" + "Message: " + msg);
		this.location = location;
		this.nested = th;
	}

	public XMLStreamException(java.lang.String msg, Location location) {
		super("ParseError at [row,col]:[" + location.getLineNumber() + ","
				+ location.getColumnNumber() + "]\n" + "Message: " + msg);
		this.location = location;
	}

	public java.lang.Throwable getNestedException() {
		return nested;
	}

	public Location getLocation() {
		return location;
	}
}
