package javax.xml.bind.annotation.adapters;

import javax.xml.bind.DatatypeConverter;

/**
 * Created by IntelliJ IDEA.
 * User: gnodet
 * Date: Apr 17, 2008
 * Time: 11:38:25 AM
 * To change this template use File | Settings | File Templates.
 */
public class HexBinaryAdapter extends XmlAdapter<String, byte[]> {

    public String marshal(byte[] v) {
        return DatatypeConverter.printHexBinary(v);
    }

    public byte[] unmarshal(String v) {
        return DatatypeConverter.parseHexBinary(v);
    }
}
