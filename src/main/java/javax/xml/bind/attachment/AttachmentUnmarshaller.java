package javax.xml.bind.attachment;

import javax.activation.DataHandler;

/**
 * Created by IntelliJ IDEA.
 * User: gnodet
 * Date: Apr 17, 2008
 * Time: 9:23:53 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class AttachmentUnmarshaller {

    public abstract byte[] getAttachmentAsByteArray(String cid);

    public abstract DataHandler getAttachmentAsDataHandler(String cid);

    public boolean isXOPPackage() {
        return false;
    }
}
