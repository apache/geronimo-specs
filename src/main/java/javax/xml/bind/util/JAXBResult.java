package javax.xml.bind.util;

import javax.xml.transform.Result;
import javax.xml.transform.sax.SAXResult;
import javax.xml.bind.UnmarshallerHandler;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

/**
 * Created by IntelliJ IDEA.
 * User: gnodet
 * Date: Apr 17, 2008
 * Time: 1:31:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class JAXBResult extends SAXResult {

    private final UnmarshallerHandler unmarshallerHandler;

    public JAXBResult(JAXBContext context) throws JAXBException {
        if (context == null) {
            throw new JAXBException("context must not be null");
        }
        unmarshallerHandler = context.createUnmarshaller().getUnmarshallerHandler();
        super.setHandler(unmarshallerHandler);
    }

    public JAXBResult(Unmarshaller unmarshaller) throws JAXBException {
        if (unmarshaller == null) {
            throw new JAXBException("unmarshaller must not be null");
        }
        unmarshallerHandler = unmarshaller.getUnmarshallerHandler();
        super.setHandler(unmarshallerHandler);
    }

    public Object getResult() throws JAXBException {
        return unmarshallerHandler.getResult();
    }

}
