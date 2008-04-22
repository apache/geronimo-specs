package javax.xml.bind.annotation;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.bind.ValidationEventHandler;

/**
 * Created by IntelliJ IDEA.
 * User: gnodet
 * Date: Apr 17, 2008
 * Time: 8:49:41 AM
 * To change this template use File | Settings | File Templates.
 */
public interface DomHandler<ElementT, ResultT extends Result> {

    ResultT createUnmarshaler(ValidationEventHandler errorHandler);

    ElementT getElement(ResultT rt);

    Source marshal(ElementT n, ValidationEventHandler errorHandler);
}
