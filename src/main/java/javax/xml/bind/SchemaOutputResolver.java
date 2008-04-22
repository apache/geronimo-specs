package javax.xml.bind;

import java.io.IOException;

import javax.xml.transform.Result;

/**
 * Created by IntelliJ IDEA.
 * User: gnodet
 * Date: Apr 17, 2008
 * Time: 10:45:25 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class SchemaOutputResolver {

    public abstract Result createOutput(String namespaceUri, String suggestedFileName) throws IOException;
}
