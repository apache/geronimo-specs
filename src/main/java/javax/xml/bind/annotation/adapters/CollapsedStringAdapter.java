package javax.xml.bind.annotation.adapters;

/**
 * Created by IntelliJ IDEA.
 * User: gnodet
 * Date: Apr 17, 2008
 * Time: 12:08:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class CollapsedStringAdapter extends XmlAdapter<String, String> {

    public String marshal(String v) {
        return v;
    }

    public String unmarshal(String v) {
        if(v == null) {
            return null;
        }
        int len = v.length();
        int s;
        for (s = 0; s < len && !isWhiteSpace(v.charAt(s)); s++);
        if (s == len) {
            return v;
        }
        StringBuffer result = new StringBuffer(len);
        if (s != 0) {
            for(int i = 0; i < s; i++) {
                result.append(v.charAt(i));
            }
            result.append(' ');
        }
        boolean inStripMode = true;
        for (int i = s + 1; i < len; i++) {
            char ch = v.charAt(i);
            boolean b = isWhiteSpace(ch);
            if (inStripMode && b) {
                continue;
            }
            inStripMode = b;
            result.append(inStripMode ? ' ' : ch);
        }
        len = result.length();
        if (len > 0 && result.charAt(len - 1) == ' ') {
            result.setLength(len - 1);
        }
        return result.toString();
    }

    protected static boolean isWhiteSpace(char ch) {
        if (ch > ' ') {
            return false;
        } else {
            return ch == '\t' || ch == '\n' || ch == '\r' || ch == ' ';
        }
    }

}
