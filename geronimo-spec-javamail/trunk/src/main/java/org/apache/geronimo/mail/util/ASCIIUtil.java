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

package org.apache.geronimo.mail.util;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.IOException;


/**
 * Set of utility classes for handling common encoding-related
 * manipulations.
 */
public class ASCIIUtil {
    private static final String MIME_FOLDTEXT = "mail.mime.foldtext";
    private static final int FOLD_THRESHOLD = 76;

    /**
     * Test to see if this string contains only US-ASCII (i.e., 7-bit
     * ASCII) charactes.
     *
     * @param s      The test string.
     *
     * @return true if this is a valid 7-bit ASCII encoding, false if it
     *         contains any non-US ASCII characters.
     */
    static public boolean isAscii(String s) {
        for (int i = 0; i < s.length(); i++) {
            if (!isAscii(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Test to see if a given character can be considered "valid" ASCII.
     * The excluded characters are the control characters less than
     * 32, 8-bit characters greater than 127, EXCEPT the CR, LF and
     * tab characters ARE considered value (all less than 32).
     *
     * @param ch     The test character.
     *
     * @return true if this character meets the "ascii-ness" criteria, false
     *         otherwise.
     */
    static public boolean isAscii(int ch) {
        // these are explicitly considered valid.
        if (ch == '\r' || ch == '\n' || ch == '\t') {
            return true;
        }

        // anything else outside the range is just plain wrong.
        if (ch >= 127 || ch < 32) {
            return false;
        }
        return true;
    }


    /**
     * Examine a stream of text and make a judgement on what encoding
     * type should be used for the text.  Ideally, we want to use 7bit
     * encoding to determine this, but we may need to use either quoted-printable
     * or base64.  The choice is made on the ratio of 7-bit characters to non-7bit.
     *
     * @param content     An input stream for the content we're examining.
     *
     * @exception IOException
     */
    public static String getTextTransferEncoding(InputStream content) throws IOException {

        // for efficiency, we'll read in blocks.
        BufferedInputStream in = new BufferedInputStream(content, 4096);

        int span = 0;            // span of characters without a line break.
        boolean containsLongLines = false;
        int asciiChars = 0;
        int nonAsciiChars = 0;

        while (true) {
            int ch = in.read();
            // if we hit an EOF here, go decide what type we've actually found.
            if (ch == -1) {
                break;
            }

            // we found a linebreak.  Reset the line length counters on either one.  We don't
            // really need to validate here.
            if (ch == '\n' || ch == '\r') {
                // hit a line end, reset our line length counter
                span = 0;
            }
            else {
                span++;
                // the text has long lines, we can't transfer this as unencoded text.
                if (span > 998) {
                    containsLongLines = true;
                }

                // non-ascii character, we have to transfer this in binary.
                if (!isAscii(ch)) {
                    nonAsciiChars++;
                }
                else {
                    asciiChars++;
                }
            }
        }

        // looking good so far, only valid chars here.
        if (nonAsciiChars == 0) {
            // does this contain long text lines?  We need to use a Q-P encoding which will
            // be only slightly longer, but handles folding the longer lines.
            if (containsLongLines) {
                return "quoted-printable";
            }
            else {
                // ideal!  Easiest one to handle.
                return "7bit";
            }
        }
        else {
            // mostly characters requiring encoding?  Base64 is our best bet.
            if (nonAsciiChars > asciiChars) {
                return "base64";
            }
            else {
                // Q-P encoding will use fewer bytes than the full Base64.
                return "quoted-printable";
            }
        }
    }


    /**
     * Examine a stream of text and make a judgement on what encoding
     * type should be used for the text.  Ideally, we want to use 7bit
     * encoding to determine this, but we may need to use either quoted-printable
     * or base64.  The choice is made on the ratio of 7-bit characters to non-7bit.
     *
     * @param content     A string for the content we're examining.
     */
    public static String getTextTransferEncoding(String content) {

        int asciiChars = 0;
        int nonAsciiChars = 0;

        for (int i = 0; i < content.length(); i++) {
            int ch = content.charAt(i);

            // non-ascii character, we have to transfer this in binary.
            if (!isAscii(ch)) {
                nonAsciiChars++;
            }
            else {
                asciiChars++;
            }
        }

        // looking good so far, only valid chars here.
        if (nonAsciiChars == 0) {
            // ideal!  Easiest one to handle.
            return "7bit";
        }
        else {
            // mostly characters requiring encoding?  Base64 is our best bet.
            if (nonAsciiChars > asciiChars) {
                return "base64";
            }
            else {
                // Q-P encoding will use fewer bytes than the full Base64.
                return "quoted-printable";
            }
        }
    }


    /**
     * Determine if the transfer encoding looks like it might be
     * valid ascii text, and thus transferable as 7bit code.  In
     * order for this to be true, all characters must be valid
     * 7-bit ASCII code AND all line breaks must be properly formed
     * (JUST '\r\n' sequences).  7-bit transfers also
     * typically have a line limit of 1000 bytes (998 + the CRLF), so any
     * stretch of charactes longer than that will also force Base64 encoding.
     *
     * @param content     An input stream for the content we're examining.
     *
     * @exception IOException
     */
    public static String getBinaryTransferEncoding(InputStream content) throws IOException {

        // for efficiency, we'll read in blocks.
        BufferedInputStream in = new BufferedInputStream(content, 4096);

        int previousChar = 0;
        int span = 0;            // span of characters without a line break.

        while (true) {
            int ch = in.read();
            // if we hit an EOF here, we've only found valid text so far, so we can transfer this as
            // 7-bit ascii.
            if (ch == -1) {
                return "7bit";
            }

            // we found a newline, this is only valid if the previous char was the '\r'
            if (ch == '\n') {
                // malformed linebreak?  force this to base64 encoding.
                if (previousChar != '\r') {
                    return "base64";
                }
                // hit a line end, reset our line length counter
                span = 0;
            }
            else {
                span++;
                // the text has long lines, we can't transfer this as unencoded text.
                if (span > 998) {
                    return "base64";
                }

                // non-ascii character, we have to transfer this in binary.
                if (!isAscii(ch)) {
                    return "base64";
                }
            }
            previousChar = ch;
        }
    }


    /**
     * Perform RFC 2047 text folding on a string of text.
     *
     * @param used   The amount of text already "used up" on this line.  This is
     *               typically the length of a message header that this text
     *               get getting added to.
     * @param s      The text to fold.
     *
     * @return The input text, with linebreaks inserted at appropriate fold points.
     */
    public static String fold(int used, String s) {
        // if folding is disable, unfolding is also.  Return the string unchanged.
        if (!SessionUtil.getBooleanProperty(MIME_FOLDTEXT, true)) {
            return s;
        }

        int end;

        // now we need to strip off any trailing "whitespace", where whitespace is blanks, tabs,
        // and line break characters.
        for (end = s.length() - 1; end >= 0; end--) {
            int ch = s.charAt(end);
            if (ch != ' ' && ch != '\t' ) {
                break;
            }
        }

        // did we actually find something to remove?  Shorten the String to the trimmed length
        if (end != s.length() - 1) {
            s = s.substring(0, end + 1);
        }

        // does the string as it exists now not require folding?  We can just had that back right off.
        if (s.length() + used <= FOLD_THRESHOLD) {
            return s;
        }

        // get a buffer for the length of the string, plus room for a few line breaks.
        // these are soft line breaks, so we generally need more that just the line breaks (an escape +
        // CR + LF + leading space on next line);
        StringBuffer newString = new StringBuffer(s.length() + 8);


        // now keep chopping this down until we've accomplished what we need.
        while (used + s.length() > FOLD_THRESHOLD) {
            int breakPoint = -1;
            char breakChar = 0;

            // now scan for the next place where we can break.
            for (int i = 0; i < s.length(); i++) {
                // have we passed the fold limit?
                if (used + i > FOLD_THRESHOLD) {
                    // if we've already seen a blank, then stop now.  Otherwise
                    // we keep going until we hit a fold point.
                    if (breakPoint != -1) {
                        break;
                    }
                }
                char ch = s.charAt(i);

                // a white space character?
                if (ch == ' ' || ch == '\t') {
                    // this might be a run of white space, so skip over those now.
                    breakPoint = i;
                    // we need to maintain the same character type after the inserted linebreak.
                    breakChar = ch;
                    i++;
                    while (i < s.length()) {
                        ch = s.charAt(i);
                        if (ch != ' ' && ch != '\t') {
                            break;
                        }
                        i++;
                    }
                }
                // found an embedded new line.  Escape this so that the unfolding process preserves it.
                else if (ch == '\n') {
                    newString.append('\\');
                    newString.append('\n');
                }
                else if (ch == '\r') {
                    newString.append('\\');
                    newString.append('\n');
                    i++;
                    // if this is a CRLF pair, add the second char also
                    if (i < s.length() && s.charAt(i) == '\n') {
                        newString.append('\r');
                    }
                }

            }
            // no fold point found, we punt, append the remainder and leave.
            if (breakPoint == -1) {
                newString.append(s);
                return newString.toString();
            }
            newString.append(s.substring(0, breakPoint));
            newString.append("\r\n");
            newString.append(breakChar);
            // chop the string
            s = s.substring(breakPoint + 1);
            // start again, and we've used the first char of the limit already with the whitespace char.
            used = 1;
        }

        // add on the remainder, and return
        newString.append(s);
        return newString.toString();
    }

    /**
     * Unfold a folded string.  The unfolding process will remove
     * any line breaks that are not escaped and which are also followed
     * by whitespace characters.
     *
     * @param s      The folded string.
     *
     * @return A new string with unfolding rules applied.
     */
    public static String unfold(String s) {
        // if folding is disable, unfolding is also.  Return the string unchanged.
        if (!SessionUtil.getBooleanProperty(MIME_FOLDTEXT, true)) {
            return s;
        }

        // if there are no line break characters in the string, we can just return this.
        if (s.indexOf('\n') < 0 && s.indexOf('\r') < 0) {
            return s;
        }

        // we need to scan and fix things up.
        int length = s.length();

        StringBuffer newString = new StringBuffer(length);

        // scan the entire string
        for (int i = 0; i < length; i++) {
            int ch = s.charAt(i);

            // we have a backslash.  In folded strings, escape characters are only processed as such if
            // they preceed line breaks.  Otherwise, we leave it be.
            if (ch == '\\') {
                // escape at the very end?  Just add the character.
                if (i == length - 1) {
                    newString.append(ch);
                }
                else {
                    int nextChar = s.charAt(i + 1);

                    // naked newline?  Add the new line to the buffer, and skip the escape char.
                    if (nextChar == '\n') {
                        newString.append('\n');
                        i++;
                    }
                    else if (nextChar == '\r') {
                        // just the CR left?  Add it, removing the escape.
                        if (i == length - 2 || s.charAt(i + 2) != '\r') {
                            newString.append('\r');
                            i++;
                        }
                        else {
                            // toss the escape, add both parts of the CRLF, and skip over two chars.
                            newString.append('\r');
                            newString.append('\n');
                            i += 2;
                        }
                    }
                    else {
                        // an escape for another purpose, just copy it over.
                        newString.append(ch);
                    }
                }
            }
            // we have an unescaped line break
            else if (ch == '\n' || ch == '\r') {
                // remember the position in case we need to backtrack.
                int lineBreak = i;
                boolean CRLF = false;

                if (ch == '\r') {
                    // check to see if we need to step over this.
                    if (i < length - 1 && s.charAt(i + 1) == '\n') {
                        i++;
                        // flag the type so we know what we might need to preserve.
                        CRLF = true;
                    }
                }

                // get a temp position scanner.
                int scan = i + 1;

                // does a blank follow this new line?  we need to scrap the new line and reduce the leading blanks
                // down to a single blank.
                if (scan < length && s.charAt(scan) == ' ') {
                    // add the character
                    newString.append(' ');

                    // scan over the rest of the blanks
                    i = scan + 1;
                    while (i < length && s.charAt(i) == ' ') {
                        i++;
                    }
                    // we'll increment down below, so back up to the last blank as the current char.
                    i--;
                }
                else {
                    // we must keep this line break.  Append the appropriate style.
                    if (CRLF) {
                        newString.append("\r\n");
                    }
                    else {
                        newString.append(ch);
                    }
                }
            }
            else {
                // just a normal, ordinary character
                newString.append(ch);
            }
        }
        return newString.toString();
    }
}
