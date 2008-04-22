package javax.xml.bind;

import java.net.URL;

import org.w3c.dom.Node;

/**
 * Created by IntelliJ IDEA.
 * User: gnodet
 * Date: Apr 17, 2008
 * Time: 8:53:24 AM
 * To change this template use File | Settings | File Templates.
 */
public interface ValidationEventLocator {

    int getColumnNumber();

    int getLineNumber();

    Node getNode();

    Object getObject();

    int getOffset();

    URL getURL();
}
