package javax.xml.stream;

public abstract class XMLOutputFactory {
	public static final String IS_REPAIRING_NAMESPACES = "javax.xml.stream.isRepairingNamespaces";

	public static XMLOutputFactory newInstance()
			throws FactoryConfigurationError {
		return (XMLOutputFactory) FactoryLocator.locate(
				"javax.xml.stream.XMLOutputFactory",
				"com.bea.xml.stream.MXParserFactory");
	}

	public static XMLOutputFactory newInstance(String factoryId,
			java.lang.ClassLoader classLoader) throws FactoryConfigurationError {
		return (XMLOutputFactory) FactoryLocator.locate(factoryId,
				"com.bea.xml.stream.MXParserFactory", classLoader);
	}

	public abstract XMLStreamWriter createXMLStreamWriter(java.io.Writer stream)
			throws XMLStreamException;

	public abstract XMLStreamWriter createXMLStreamWriter(
			java.io.OutputStream stream) throws XMLStreamException;

	public abstract XMLStreamWriter createXMLStreamWriter(
			java.io.OutputStream stream, String encoding)
			throws XMLStreamException;

	public abstract XMLStreamWriter createXMLStreamWriter(
			javax.xml.transform.Result result) throws XMLStreamException;

	public abstract XMLEventWriter createXMLEventWriter(
			javax.xml.transform.Result result) throws XMLStreamException;

	public abstract XMLEventWriter createXMLEventWriter(
			java.io.OutputStream stream) throws XMLStreamException;

	public abstract XMLEventWriter createXMLEventWriter(
			java.io.OutputStream stream, String encoding)
			throws XMLStreamException;

	public abstract XMLEventWriter createXMLEventWriter(java.io.Writer stream)
			throws XMLStreamException;

	public abstract void setProperty(String name, Object value)
			throws IllegalArgumentException;

	public abstract Object getProperty(String name)
			throws IllegalArgumentException;

	public abstract boolean isPropertySupported(String name);
}
