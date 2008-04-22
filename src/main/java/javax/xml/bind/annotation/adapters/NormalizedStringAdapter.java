package javax.xml.bind.annotation.adapters;

/**
 * Created by IntelliJ IDEA.
 * User: gnodet
 * Date: Apr 17, 2008
 * Time: 11:28:50 AM
 * To change this template use File | Settings | File Templates.
 */
public class NormalizedStringAdapter extends XmlAdapter<String, String> {

    public String marshal(String v) {
        return v;
    }

    public String unmarshal(String v) {
        if (v == null) {
            return null;
        }
        int i;
        for (i = v.length() - 1; i >= 0 && !isWhiteSpaceExceptSpace(v.charAt(i)); i--);
        if (i < 0) {
            return v;
        }
        char buf[] = v.toCharArray();
        buf[i--] = ' ';
        for(; i >= 0; i--) {
            if(isWhiteSpaceExceptSpace(buf[i])) {
                buf[i] = ' ';
            }
        }
        return new String(buf);
    }

    protected static boolean isWhiteSpaceExceptSpace(char ch) {
        if (ch >= ' ') {
            return false;
        } else {
            return ch == '\t' || ch == '\n' || ch == '\r';
        }
    }

}
