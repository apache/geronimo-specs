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

// http://www.faqs.org/rfcs/rfc2183.html

/**
 * @version $Rev$ $Date$
 */
public class ContentDisposition {
    private String _disposition;
    private ParameterList _list;

    public ContentDisposition() {
        setDisposition(null);
        setParameterList(null);
    }

    public ContentDisposition(final String disposition) throws ParseException {
        // get a token parser for the type information
        final HeaderTokenizer tokenizer = new HeaderTokenizer(disposition, HeaderTokenizer.MIME);

        // get the first token, which must be an ATOM
        final HeaderTokenizer.Token token = tokenizer.next();
        if (token.getType() != HeaderTokenizer.Token.ATOM) {
            throw new ParseException("Invalid content disposition");
        }

        _disposition = token.getValue();

        // the remainder is parameters, which ParameterList will take care of parsing.
        final String remainder = tokenizer.getRemainder();
        if (remainder != null) {
            _list = new ParameterList(remainder);
        }
    }

    public ContentDisposition(final String disposition, final ParameterList list) {
        setDisposition(disposition);
        setParameterList(list);
    }

    public String getDisposition() {
        return _disposition;
    }

    public String getParameter(final String name) {
        if (_list == null) {
            return null;
        } else {
            return _list.get(name);
        }
    }

    public ParameterList getParameterList() {
        return _list;
    }

    public void setDisposition(final String string) {
        _disposition = string;
    }

    public void setParameter(final String name, final String value) {
        if (_list == null) {
            _list = new ParameterList();
        }
        _list.set(name, value);
    }

    public void setParameterList(final ParameterList list) {
        if (list == null) {
            _list = new ParameterList();
        } else {
            _list = list;
        }
    }

    /**
     * Retrieve a RFC2045 style string representation of
     * this ContentDisposition. Returns an empty string if
     * the conversion failed.
     *
     * @return  RFC2045 style string
     * @since       JavaMail 1.2
     */
    @Override
    public String toString() {
        
        /* Since JavaMail 1.5:
           The general contract of Object.toString is that it never returns null.
           The toString methods of ContentType and ContentDisposition were defined
           to return null in certain error cases.  Given the general toString contract
           it seems unlikely that anyone ever depended on these special cases, and
           it would be more useful for these classes to obey the general contract.
           These methods have been changed to return an empty string in these error
           cases.
        */
        

        // it is possible we might have a parameter list, but this is meaningless if
        // there is no disposition string.  Return a failure.
        if (_disposition == null) {
            return "";
        }


        // no parameter list?  Just return the disposition string
        if (_list == null) {
            return _disposition;
        }

        // format this for use on a Content-Disposition header, which means we need to
        // account for the length of the header part too.
        return _disposition + _list.toString("Content-Disposition".length() + _disposition.length());
    }
}
