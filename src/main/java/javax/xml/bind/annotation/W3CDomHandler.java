package javax.xml.bind.annotation;

import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.Source;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.bind.ValidationEventHandler;

import org.w3c.dom.Element;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.DocumentFragment;

/**
 * Created by IntelliJ IDEA.
 * User: gnodet
 * Date: Apr 17, 2008
 * Time: 10:47:25 AM
 * To change this template use File | Settings | File Templates.
 */
public class W3CDomHandler implements DomHandler<Element, DOMResult> {

    private DocumentBuilder builder;

    public W3CDomHandler() {
    }

    public W3CDomHandler(DocumentBuilder builder) {
        if (builder == null) {
            throw new IllegalArgumentException();
        }
        this.builder = builder;
    }

    public DOMResult createUnmarshaler(ValidationEventHandler errorHandler) {
        if (builder == null) {
            return new DOMResult();
        } else {
            return new DOMResult(builder.newDocument());
        }
    }

    public DocumentBuilder getBuilder() {
        return builder;
    }

    public Element getElement(DOMResult rt) {
        Node n = rt.getNode();
        if (n instanceof Document) {
            return ((Document)n).getDocumentElement();
        }
        if (n instanceof Element) {
            return (Element)n;
        }
        if (n instanceof DocumentFragment) {
            return (Element)n.getChildNodes().item(0);
        } else {
            throw new IllegalStateException(n.toString());
        }
    }

    public Source marshal(Element n, ValidationEventHandler errorHandler) {
        return new DOMSource(n);
    }

    public void setBuilder(DocumentBuilder builder) {
        this.builder = builder;
    }
}
