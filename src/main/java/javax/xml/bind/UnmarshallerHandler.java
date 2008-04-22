package javax.xml.bind;

import org.xml.sax.ContentHandler;

/**
 * Created by IntelliJ IDEA.
 * User: gnodet
 * Date: Apr 17, 2008
 * Time: 10:05:04 AM
 * To change this template use File | Settings | File Templates.
 */
public interface UnmarshallerHandler extends ContentHandler {

    Object getResult();

}
