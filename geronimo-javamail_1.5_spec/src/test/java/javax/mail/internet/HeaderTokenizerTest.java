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

import javax.mail.internet.HeaderTokenizer.Token;

import junit.framework.TestCase;

/**
 * @version $Rev$ $Date$
 */
public class HeaderTokenizerTest extends TestCase {
    public void testTokenizer() throws ParseException {
        final Token t;
        HeaderTokenizer ht;
        ht =
            new HeaderTokenizer("To: \"Geronimo List\" <geronimo-dev@apache.org>, \n\r Geronimo User <geronimo-user@apache.org>");
        validateToken(ht.peek(), Token.ATOM, "To");
        validateToken(ht.next(), Token.ATOM, "To");
        validateToken(ht.peek(), ':', ":");
        validateToken(ht.next(), ':', ":");
        validateToken(ht.next(), Token.QUOTEDSTRING, "Geronimo List");
        validateToken(ht.next(), '<', "<");
        validateToken(ht.next(), Token.ATOM, "geronimo-dev");
        validateToken(ht.next(), '@', "@");
        validateToken(ht.next(), Token.ATOM, "apache");
        validateToken(ht.next(), '.', ".");
        validateToken(ht.next(), Token.ATOM, "org");
        validateToken(ht.next(), '>', ">");
        validateToken(ht.next(), ',', ",");
        validateToken(ht.next(), Token.ATOM, "Geronimo");
        validateToken(ht.next(), Token.ATOM, "User");
        validateToken(ht.next(), '<', "<");
        validateToken(ht.next(), Token.ATOM, "geronimo-user");
        validateToken(ht.next(), '@', "@");
        validateToken(ht.next(), Token.ATOM, "apache");
        validateToken(ht.next(), '.', ".");
        assertEquals("org>", ht.getRemainder());
        validateToken(ht.peek(), Token.ATOM, "org");
        validateToken(ht.next(), Token.ATOM, "org");
        validateToken(ht.next(), '>', ">");
        assertEquals(Token.EOF, ht.next().getType());
        ht = new HeaderTokenizer("   ");
        assertEquals(Token.EOF, ht.next().getType());
        ht = new HeaderTokenizer("J2EE");
        validateToken(ht.next(), Token.ATOM, "J2EE");
        assertEquals(Token.EOF, ht.next().getType());
        // test comments
        doComment(true);
        doComment(false);
    }

    public void testErrors() throws ParseException {
        checkParseError("(Geronimo");
        checkParseError("((Geronimo)");
        checkParseError("\"Geronimo");
        checkParseError("\"Geronimo\\");
    }


    public void testQuotedLiteral() throws ParseException {
        checkTokenParse("\"\"", Token.QUOTEDSTRING, "");
        checkTokenParse("\"\\\"\"", Token.QUOTEDSTRING, "\"");
        checkTokenParse("\"\\\"\"", Token.QUOTEDSTRING, "\"");
        checkTokenParse("\"A\r\nB\"", Token.QUOTEDSTRING, "AB");
        checkTokenParse("\"A\nB\"", Token.QUOTEDSTRING, "A\nB");
    }


    public void testComment() throws ParseException {
        checkTokenParse("()", Token.COMMENT, "");
        checkTokenParse("(())", Token.COMMENT, "()");
        checkTokenParse("(Foo () Bar)", Token.COMMENT, "Foo () Bar");
        checkTokenParse("(\"Foo () Bar)", Token.COMMENT, "\"Foo () Bar");
        checkTokenParse("(\\()", Token.COMMENT, "(");
        checkTokenParse("(Foo \r\n Bar)", Token.COMMENT, "Foo  Bar");
        checkTokenParse("(Foo \n Bar)", Token.COMMENT, "Foo \n Bar");
    }
    
    public void testJavaMail15NextMethod() throws ParseException{
        HeaderTokenizer ht;
        ht =
            new HeaderTokenizer("To: \"Geronimo List\\\" <geronimo-dev@apache.org>, \n\r Geronimo User <geronimo-user@apache.org>");
        validateToken(ht.next('>', false), Token.QUOTEDSTRING, "To: \"Geronimo List\" <geronimo-dev@apache.org");
    }
    
    public void testJavaMail15NextMethodEscapes() throws ParseException{
        HeaderTokenizer ht;
        ht =
            new HeaderTokenizer("To: \"Geronimo List\\\" <geronimo-dev@apache.org>, \n\r Geronimo User <geronimo-user@apache.org>");
        validateToken(ht.next('<', true), Token.QUOTEDSTRING, "To: \"Geronimo List\\\" ");
    }
    
    public void testJavaMail15NextMethodEscapes2() throws ParseException{
        HeaderTokenizer ht;
        ht =
            new HeaderTokenizer("To: \"Geronimo List\" <geronimo-dev@apac\\he.org>, \n\r Geronimo User <geronimo-user@apache.org>");
        ht.next();
        ht.next();
        ht.next();
        validateToken(ht.next(',', false), Token.QUOTEDSTRING, "<geronimo-dev@apache.org>");
    }
    
    public void testJavaMail15NextMethodEscapes3() throws ParseException{
        HeaderTokenizer ht;
        ht =
            new HeaderTokenizer("To: \"Geronimo List\" <geronimo-dev@apac\\he.org>, \n\r Geronimo User <geronimo-user@apache.org>");
        ht.next();
        ht.next();
        ht.next();
        validateToken(ht.next(',', true), Token.QUOTEDSTRING, "<geronimo-dev@apac\\he.org>");
    }

    public void checkTokenParse(final String text, final int type, final String value) throws ParseException {
        HeaderTokenizer ht;
        ht = new HeaderTokenizer(text, HeaderTokenizer.RFC822, false);
        validateToken(ht.next(), type, value);
    }


    public void checkParseError(final String text) throws ParseException {
        final Token t;
        HeaderTokenizer ht;

        ht = new HeaderTokenizer(text);
        doNextError(ht);
        ht = new HeaderTokenizer(text);
        doPeekError(ht);
    }

    public void doNextError(final HeaderTokenizer ht) {
        try {
            ht.next();
            fail("Expected ParseException");
        } catch (final ParseException e) {
        }
    }

    public void doPeekError(final HeaderTokenizer ht) {
        try {
            ht.peek();
            fail("Expected ParseException");
        } catch (final ParseException e) {
        }
    }


    public void doComment(final boolean ignore) throws ParseException {
        HeaderTokenizer ht;
        final Token t;
        ht =
            new HeaderTokenizer(
                "Apache(Geronimo)J2EE",
                HeaderTokenizer.RFC822,
                ignore);
        validateToken(ht.next(), Token.ATOM, "Apache");
        if (!ignore) {
            validateToken(ht.next(), Token.COMMENT, "Geronimo");
        }
        validateToken(ht.next(), Token.ATOM, "J2EE");
        assertEquals(Token.EOF, ht.next().getType());

        ht =
            new HeaderTokenizer(
                "Apache(Geronimo (Project))J2EE",
                HeaderTokenizer.RFC822,
                ignore);
        validateToken(ht.next(), Token.ATOM, "Apache");
        if (!ignore) {
            validateToken(ht.next(), Token.COMMENT, "Geronimo (Project)");
        }
        validateToken(ht.next(), Token.ATOM, "J2EE");
        assertEquals(Token.EOF, ht.next().getType());
    }

    private void validateToken(final HeaderTokenizer.Token token, final int type, final String value) {
        assertEquals(type, token.getType());
        assertEquals(value, token.getValue());
    }
}
