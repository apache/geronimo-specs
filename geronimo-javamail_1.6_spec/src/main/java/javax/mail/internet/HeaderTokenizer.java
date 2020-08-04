/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package javax.mail.internet;

/**
 * @version $Rev$ $Date$
 */
public class HeaderTokenizer {
    public static class Token {
        // Constant values from J2SE 1.4 API Docs (Constant values)
        public static final int ATOM = -1;
        public static final int COMMENT = -3;
        public static final int EOF = -4;
        public static final int QUOTEDSTRING = -2;
        private final int _type;
        private final String _value;

        public Token(final int type, final String value) {
            _type = type;
            _value = value;
        }

        public int getType() {
            return _type;
        }

        public String getValue() {
            return _value;
        }
    }

    private static final char NUL = '\0';
    private static final Token EOF = new Token(Token.EOF, null);
    // characters not allowed in MIME
    public static final String MIME = "()<>@,;:\\\"\t []/?=";
    // characters not allowed in RFC822
    public static final String RFC822 = "()<>@,;:\\\"\t .[]";
    private static final String WHITE = " \t\n\r";
    private final String _delimiters;
    private final String _header;
    private final int _headerLength;
    private final boolean _skip;
    private int pos;

    public HeaderTokenizer(final String header) {
        this(header, RFC822);
    }

    public HeaderTokenizer(final String header, final String delimiters) {
        this(header, delimiters, true);
    }

    public HeaderTokenizer(final String header,
                           final String delimiters,
                           final boolean skipComments) {
        _skip = skipComments;
        _header = header;
        _delimiters = delimiters;
        _headerLength=header.length();
    }

    //Return the rest of the Header.
    //null is returned if we are already at end of header
    public String getRemainder() {

        if(pos > _headerLength) {
            return null;
        }

        return _header.substring(pos);
    }

    public Token next() throws ParseException {
        return readToken(NUL, false);
    }

    /**
     * Parses the next token from this String.
     * If endOfAtom is not NUL, the token extends until the
     * endOfAtom character is seen, or to the end of the header.
     * This method is useful when parsing headers that don't
     * obey the MIME specification, e.g., by failing to quote
     * parameter values that contain spaces.
     *
     * @param   endOfAtom   if not NUL, character marking end of token
     * @return      the next Token
     * @exception   ParseException if the parse fails
     * @since       JavaMail 1.5
     */
    public Token next(final char endOfAtom) throws ParseException {
        return next(endOfAtom, false);
    }

    /**
     * Parses the next token from this String.
     * endOfAtom is handled as above.  If keepEscapes is true,
     * any backslash escapes are preserved in the returned string.
     * This method is useful when parsing headers that don't
     * obey the MIME specification, e.g., by failing to escape
     * backslashes in the filename parameter.
     *
     * @param   endOfAtom   if not NUL, character marking end of token
     * @param   keepEscapes keep all backslashes in returned string?
     * @return      the next Token
     * @exception   ParseException if the parse fails
     * @since       JavaMail 1.5
     */
    public Token next(final char endOfAtom, final boolean keepEscapes)
                throws ParseException {
        return readToken(endOfAtom, keepEscapes);
    }


    public Token peek() throws ParseException {
        final int start = pos;
        try {
            return readToken(NUL, false);
        } finally {
            pos = start;
        }
    }

    /**
     * Read an ATOM token from the parsed header.
     *
     * @return A token containing the value of the atom token.
     */
    private Token readAtomicToken() {
        // skip to next delimiter
        final int start = pos;
        final StringBuilder sb = new StringBuilder();
        sb.append(_header.charAt(pos));
        while (++pos < _headerLength) {
            // break on the first non-atom character.
            final char ch = _header.charAt(pos);

            if ((_delimiters.indexOf(_header.charAt(pos)) != -1 || ch < 32 || ch >= 127)) {
                break;
            }
        }

        return new Token(Token.ATOM, _header.substring(start, pos));
    }

    /**
     * Read the next token from the header.
     *
     * @return The next token from the header.  White space is skipped, and comment
     *         tokens are also skipped if indicated.
     * @exception ParseException
     */
    private Token readToken(final char endOfAtom, final boolean keepEscapes) throws ParseException {
        if (pos >= _headerLength) {
            return EOF;
        } else {
            final char c = _header.charAt(pos);
            // comment token...read and skip over this
            if (c == '(') {
                final Token comment = readComment(keepEscapes);
                if (_skip) {
                    return readToken(endOfAtom, keepEscapes);
                } else {
                    return comment;
                }

            // quoted literal
            } else if (c == '\"') {
                return readQuotedString('"', keepEscapes, 1);

            // white space, eat this and find a real token.
            } else if (WHITE.indexOf(c) != -1) {
                eatWhiteSpace();
                return readToken(endOfAtom, keepEscapes);

            // either a CTL or special.  These characters have a self-defining token type.
            } else if (c < 32 || c >= 127 || _delimiters.indexOf(c) != -1) {

                if (endOfAtom != NUL && c != endOfAtom) {
                    return readQuotedString(endOfAtom, keepEscapes, 0);
                }


                pos++;
                return new Token(c, String.valueOf(c));

            } else {
                // start of an atom, parse it off.
                if (endOfAtom != NUL && c != endOfAtom) {
                    return readQuotedString(endOfAtom, keepEscapes, 0);
                }

                return readAtomicToken();
            }
        }
    }

    /**
     * Extract a substring from the header string and apply any
     * escaping/folding rules to the string.
     *
     * @param start  The starting offset in the header.
     * @param end    The header end offset + 1.
     *
     * @return The processed string value.
     * @exception ParseException
     */
    private String getEscapedValue(final int start, final int end, final boolean keepEscapes) throws ParseException {
        final StringBuffer value = new StringBuffer();

        for (int i = start; i < end; i++) {
            final char ch = _header.charAt(i);
            // is this an escape character?
            if (ch == '\\') {
                i++;
                if (i == end) {
                    throw new ParseException("Invalid escape character");
                }

                if(keepEscapes) {
                    value.append("\\");
                }

                value.append(_header.charAt(i));
            }
            // line breaks are ignored, except for naked '\n' characters, which are consider
            // parts of linear whitespace.
            else if (ch == '\r') {
                // see if this is a CRLF sequence, and skip the second if it is.
                if (i < end - 1 && _header.charAt(i + 1) == '\n') {
                    i++;
                }
            }
            else {

                 // just append the ch value.
                value.append(ch);
            }
        }
        return value.toString();
    }

    /**
     * Read a comment from the header, applying nesting and escape
     * rules to the content.
     *
     * @return A comment token with the token value.
     * @exception ParseException
     */
    private Token readComment(final boolean keepEscapes) throws ParseException {
        final int start = pos + 1;
        int nesting = 1;

        boolean requiresEscaping = false;

        // skip to end of comment/string
        while (++pos < _headerLength) {
            final char ch = _header.charAt(pos);
            if (ch == ')') {
                nesting--;
                if (nesting == 0) {
                    break;
                }
            }
            else if (ch == '(') {
                nesting++;
            }
            else if (ch == '\\') {
                pos++;
                requiresEscaping = true;
            }
            // we need to process line breaks also
            else if (ch == '\r') {
                requiresEscaping = true;
            }
        }

        if (nesting != 0) {
            throw new ParseException("Unbalanced comments");
        }

        String value;
        if (requiresEscaping) {
            value = getEscapedValue(start, pos, keepEscapes);
        }
        else {
            value = _header.substring(start, pos++);
        }
        return new Token(Token.COMMENT, value);
    }

    /**
     * Parse out a quoted string from the header, applying escaping
     * rules to the value.
     *
     * @return The QUOTEDSTRING token with the value.
     * @exception ParseException
     */
    private Token readQuotedString(final char endChar, final boolean keepEscapes, final int offset) throws ParseException {
        final int start = pos+offset;
        boolean requiresEscaping = false;

        // skip to end of comment/string
        while (++pos < _headerLength) {
            final char ch = _header.charAt(pos);

            if (ch == endChar) {
                String value;
                if (requiresEscaping) {
                    value = getEscapedValue(start, pos++, keepEscapes);
                }
                else {
                    value = _header.substring(start, pos++);
                }
                return new Token(Token.QUOTEDSTRING, value);
            }
            else if (ch == '\\') {
                pos++;
                requiresEscaping = true;
            }
            // we need to process line breaks also
            else if (ch == '\r') {
                requiresEscaping = true;
            }
        }

        // we ran out of chars in the string. If the end char is a quote, then there
        // is a missing quote somewhere
        if (endChar == '"') {
            throw new ParseException("Missing '\"'");
        }

        // otherwise, we can just return whatever is left
        String value;
        if (requiresEscaping) {
            value = getEscapedValue(start, pos, keepEscapes);

        } else {
            value = _header.substring(start, pos);
        }
        return new Token(Token.QUOTEDSTRING, trimWhiteSpace(value));
    }

    /**
     * Skip white space in the token string.
     */
    private void eatWhiteSpace() {
        // skip to end of whitespace
        while (++pos < _headerLength
                && WHITE.indexOf(_header.charAt(pos)) != -1) {
            ;
        }
    }

    /**
     * linear white spaces must be removed from quoted text or text
     *
     LWSP-char   =  SPACE / HTAB                 ; semantics = SPACE

     linear-white-space =  1*([CRLF] LWSP-char)  ; semantics = SPACE
                                                 ; CRLF => folding

     text        =  <any CHAR, including bare    ; => atoms, specials,
                     CR & bare LF, but NOT       ;  comments and
                     including CRLF>             ;  quoted-strings are
                                                 ;  NOT recognized.

     atom        =  1*<any CHAR except specials, SPACE and CTLs>

     quoted-string = <"> *(qtext/quoted-pair) <">; Regular qtext or
                                                 ;   quoted chars.

     qtext       =  <any CHAR excepting <">,     ; => may be folded
                     "\" & CR, and including
                     linear-white-space>

     domain-literal =  "[" *(dtext / quoted-pair) "]"
     */
    private static String trimWhiteSpace(final String s) {
        char c;
        int i;
        for (i = s.length() - 1; i >= 0; i--) {
            if ((
                    (c = s.charAt(i)) != ' ') && // space
                    (c != '\t') &&              // tab
                    (c != '\r') &&              // CR
                    (c != '\n')) {              // LF

                break;
            }
        }

        if (i <= 0) {
            return "";

        } else {
            return s.substring(0, i + 1);
        }
    }

}
