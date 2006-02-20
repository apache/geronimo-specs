/**
 *
 * Copyright 2003-2004 The Apache Software Foundation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package javax.mail.internet;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.MessagingException;

// encodings include "base64", "quoted-printable", "7bit", "8bit" and "binary".
// In addition, "uuencode" is also supported. The

/**
 * @version $Rev$ $Date$
 */
public class MimeUtility {

    private MimeUtility() {
    }

    public static final int ALL = -1;

    private static String defaultJavaCharset;
    private static String escapedChars = "\"\\\r\n";

    public static InputStream decode(InputStream in, String encoding) throws MessagingException {
        // TODO - take account of encoding
        return in;
    }

    public static String decodeText(String word) throws UnsupportedEncodingException {
        // TODO - take account of encoding
        return word;
    }

    public static String decodeWord(String word) throws ParseException, UnsupportedEncodingException {
        // TODO - take account of encoding
        return word;
    }

    public static OutputStream encode(OutputStream out, String encoding) throws MessagingException {
        // TODO - take account of encoding
        return out;
    }

    public static OutputStream encode(OutputStream out, String encoding, String filename) throws MessagingException {
        // TODO - take account of encoding
        return out;
    }

    public static String encodeText(String word) throws UnsupportedEncodingException {
        // TODO - take account of encoding
        return word;
    }

    public static String encodeText(String word, String characterset, String encoding) throws UnsupportedEncodingException {
        // TODO - take account of encoding
        return word;
    }

    public static String encodeWord(String word) throws UnsupportedEncodingException {
        // TODO - take account of encoding
        return word;
    }

    public static String encodeWord(String word, String characteset, String encoding) throws UnsupportedEncodingException {
        // TODO - take account of encoding
        return word;
    }

    public static String getEncoding(DataHandler handler) {
        // TODO figure what type of data it is
        return "binary";
    }

    public static String getEncoding(DataSource source) {
        // TODO figure what type of data it is
        return "binary";
    }

    /**
     * Quote a "word" value.  If the word contains any character from
     * the specified "specials" list, this value is returned as a
     * quoted strong.  Otherwise, it is returned unchanged (an "atom").
     *
     * @param word     The word requiring quoting.
     * @param specials The set of special characters that can't appear in an unquoted
     *                 string.
     *
     * @return The quoted value.  This will be unchanged if the word doesn't contain
     *         any of the designated special characters.
     */
    public static String quote(String word, String specials) {
        int wordLength = word.length();
        boolean requiresQuoting = false;
        // scan the string looking for problem characters
        for (int i =0; i < wordLength; i++) {
            char ch = word.charAt(i);
            // special escaped characters require escaping, which also implies quoting.
            if (escapedChars.indexOf(ch) >= 0) {
                return quoteAndEscapeString(word);
            }
            // now check for control characters or the designated special characters.
            if (ch < 32 || ch >= 127 || specials.indexOf(ch) >= 0) {
                // we know this requires quoting, but we still need to scan the entire string to
                // see if contains chars that require escaping.  Just go ahead and treat it as if it does.
                return quoteAndEscapeString(word);
            }
        }
        return word;
    }

    /**
     * Take a string and return it as a formatted quoted string, with
     * all characters requiring escaping handled properly.
     *
     * @param word   The string to quote.
     *
     * @return The quoted string.
     */
    private static String quoteAndEscapeString(String word) {
        int wordLength = word.length();
        // allocate at least enough for the string and two quotes plus a reasonable number of escaped chars.
        StringBuffer buffer = new StringBuffer(wordLength + 10);
        // add the leading quote.
        buffer.append('"');

        for (int i = 0; i < wordLength; i++) {
            char ch = word.charAt(i);
            // is this an escaped char?
            if (escapedChars.indexOf(ch) >= 0) {
                // add the escape marker before appending.
                buffer.append('\\');
            }
            buffer.append(ch);
        }
        // now the closing quote
        buffer.append('"');
        return buffer.toString();
    }

    public static String javaCharset(String charset) {
        // TODO Perform translations as appropriate
        return charset;
    }

    public static String mimeCharset(String charset) {
        // TODO Perform translations as appropriate
        return charset;
    }

    public static String getDefaultJavaCharset() {
        try {
            String charset = System.getProperty("mail.mime.charset");
            if (charset != null) {
                return javaCharset(charset);
            }
            charset = System.getProperty("file.encoding");
            if (charset != null) {
                return charset;
            }
        } catch (SecurityException e) {
                // ignore
        }
        return "utf-8";
    }
}
