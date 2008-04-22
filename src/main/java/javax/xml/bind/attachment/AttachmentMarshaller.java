package javax.xml.bind.attachment;

import javax.activation.DataHandler;

/**
 * Created by IntelliJ IDEA.
 * User: gnodet
 * Date: Apr 17, 2008
 * Time: 9:18:43 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class AttachmentMarshaller {

    public abstract String addMtomAttachment(byte[] data, int offset, int length, String mimeType, String elementNamespace, String elementLocalName);

    public abstract String addMtomAttachment(DataHandler data, String elementNamespace, String elementLocalName);

    public abstract String addSwaRefAttachment(DataHandler data);

    public boolean isXOPPackage() {
        return false;
    }
}
