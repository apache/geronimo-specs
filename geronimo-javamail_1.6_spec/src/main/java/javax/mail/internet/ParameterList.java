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

import java.io.ByteArrayOutputStream;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.geronimo.mail.util.ASCIIUtil;
import org.apache.geronimo.mail.util.RFC2231Encoder;
import org.apache.geronimo.mail.util.SessionUtil;
// Represents lists in things like

// Content-Type: text/plain;charset=klingon
//
// The ;charset=klingon is the parameter list, may have more of them with ';'
//
// The string could also look like
//
// Content-Type: text/plain;para1*=val1; para2*=val2; title*=us-ascii'en-us'This%20is%20%2A%2A%2Afun%2A%2A%2A
//
// And this (multisegment parameter) is also possible (since JavaMail 1.5)
//
// Content-Type: message/external-body; access-type=URL;
// URL*0="ftp://";
// URL*1="cs.utk.edu/pub/moore/bulk-mailer/bulk-mailer.tar"
//
// which is the same as:
// Content-Type: message/external-body; access-type=URL;
//     URL="ftp://cs.utk.edu/pub/moore/bulk-mailer/bulk-mailer.tar"
/*
 * Content-Type: application/x-stuff
    title*0*=us-ascii'en'This%20is%20even%20more%20
    title*1*=%2A%2A%2Afun%2A%2A%2A%20
    title*2="isn't it!"
 */

/**
 * @version $Rev$ $Date$
 */
public class ParameterList {
    private static final String MIME_ENCODEPARAMETERS = "mail.mime.encodeparameters";
    private static final String MIME_DECODEPARAMETERS = "mail.mime.decodeparameters";
    private static final String MIME_DECODEPARAMETERS_STRICT = "mail.mime.decodeparameters.strict";

    private static final int HEADER_SIZE_LIMIT = 76;

    private final Map<String, ParameterValue> _parameters = new HashMap<String, ParameterValue>();

    /**
     * A set of names for multi-segment parameters that we
     * haven't processed yet.  Normally such names are accumulated
     * during the inital parse and processed at the end of the parse,
     * but such names can also be set via the set method when the
     * IMAP provider accumulates pre-parsed pieces of a parameter list.
     * (A special call to the set method tells us when the IMAP provider
     * is done setting parameters.)
     *
     * A multi-segment parameter is defined by RFC 2231.  For example,
     * "title*0=part1; title*1=part2", which represents a parameter
     * named "title" with value "part1part2".
     *
     * Note also that each segment of the value might or might not be
     * encoded, indicated by a trailing "*" on the parameter name.
     * If any segment is encoded, the first segment must be encoded.
     * Only the first segment contains the charset and language
     * information needed to decode any encoded segments.
     *
     * RFC 2231 introduces many possible failure modes, which we try
     * to handle as gracefully as possible.  Generally, a failure to
     * decode a parameter value causes the non-decoded parameter value
     * to be used instead.  Missing segments cause all later segments
     * to be appear as independent parameters with names that include
     * the segment number.  For example, "title*0=part1; title*1=part2;
     * title*3=part4" appears as two parameters named "title" and "title*3".
     */
    //private Set multisegmentNames = new HashSet();

    /**
     * A map containing the segments for all not-yet-processed
     * multi-segment parameters.  The map is indexed by "name*seg".
     * The value object is either a String or a Value object.
     * The Value object is not decoded during the initial parse
     * because the segments may appear in any order and until the
     * first segment appears we don't know what charset to use to
     * decode the encoded segments.  The segments are hex decoded
     * in order, combined into a single byte array, and converted
     * to a String using the specified charset in the
     * combineMultisegmentNames method.
     */
    private final Map<MultiSegmentEntry, ParameterValue> _multiSegmentParameters = new TreeMap<MultiSegmentEntry, ParameterValue>();
    
    private boolean encodeParameters = false;
    private boolean decodeParameters = false;
    private boolean decodeParametersStrict = false;

    public ParameterList() {
        // figure out how parameter handling is to be performed.
        getInitialProperties();
    }

    public ParameterList(final String list) throws ParseException {
        // figure out how parameter handling is to be performed.
        getInitialProperties();
        // get a token parser for the type information
        final HeaderTokenizer tokenizer = new HeaderTokenizer(list, HeaderTokenizer.MIME);
        while (true) {
            HeaderTokenizer.Token token = tokenizer.next();

            if (token.getType() == HeaderTokenizer.Token.EOF) {
                // the EOF token terminates parsing.
                break;
            } else if (token.getType() == ';') {
                // each new parameter is separated by a semicolon, including the
                // first, which separates
                // the parameters from the main part of the header.

                // the next token needs to be a parameter name
                token = tokenizer.next();
                // allow a trailing semicolon on the parameters.
                if (token.getType() == HeaderTokenizer.Token.EOF) {
                    break;
                }

                if (token.getType() != HeaderTokenizer.Token.ATOM) {
                    throw new ParseException("Invalid parameter name: " + token.getValue());
                }

                // get the parameter name as a lower case version for better
                // mapping.
                String name = token.getValue().toLowerCase();

                token = tokenizer.next();

                // parameters are name=value, so we must have the "=" here.
                if (token.getType() != '=') {
                    throw new ParseException("Missing '='");
                }

                // now the value, which may be an atom or a literal
                token = tokenizer.next();

                if (token.getType() != HeaderTokenizer.Token.ATOM && token.getType() != HeaderTokenizer.Token.QUOTEDSTRING) {
                    throw new ParseException("Invalid parameter value: " + token.getValue());
                }

                final String value = token.getValue();
                String decodedValue = null;

                // we might have to do some additional decoding. A name that
                // ends with "*"
                // is marked as being encoded, so if requested, we decode the
                // value.
                if (decodeParameters && name.endsWith("*") && !isMultiSegmentName(name)) {
                    // the name needs to be pruned of the marker, and we need to
                    // decode the value.
                    name = name.substring(0, name.length() - 1);
                    // get a new decoder
                    final RFC2231Encoder decoder = new RFC2231Encoder(HeaderTokenizer.MIME);

                    try {
                        // decode the value
                        decodedValue = decoder.decode(value);
                    } catch (final Exception e) {
                        // if we're doing things strictly, then raise a parsing
                        // exception for errors.
                        // otherwise, leave the value in its current state.
                        if (decodeParametersStrict) {
                            throw new ParseException("Invalid RFC2231 encoded parameter");
                        }
                    }
                    _parameters.put(name, new ParameterValue(name, decodedValue, value));
                } else if (isMultiSegmentName(name)) {
                    // multisegment parameter
                    _multiSegmentParameters.put(new MultiSegmentEntry(name), new ParameterValue(name, value));
                } else {
                    _parameters.put(name, new ParameterValue(name, value));
                }

            } else {

                throw new ParseException("Missing ';'");
            }

        }

        combineSegments();
    }
    
    private static boolean isMultiSegmentName(final String name) {
        
        if(name == null || name.length() == 0) {
			return false;
		}
        
        final int firstAsterixIndex = name.indexOf('*');
        
        if(firstAsterixIndex < 0) {
            return false; //no asterix at all
        }else {
            
            if(firstAsterixIndex == name.length()-1) {
                //first asterix is last char, so this is an encoded name/value pair but not a multisegment one
                return false;
            }
            
            final String restOfname = name.substring(firstAsterixIndex+1);
            
            if(Character.isDigit(restOfname.charAt(0))) {
                return true;
            }
            
            return false;
        }
    }
    
    /**
     * Normal users of this class will use simple parameter names.
     * In some cases, for example, when processing IMAP protocol
     * messages, individual segments of a multi-segment name
     * (specified by RFC 2231) will be encountered and passed to
     * the {@link #set} method.  After all these segments are added
     * to this ParameterList, they need to be combined to represent
     * the logical parameter name and value.  This method will combine
     * all segments of multi-segment names. 
     *
     * Normal users should never need to call this method.
     *
     * @since    JavaMail 1.5
     */ 
    public void combineSegments() {
       
        // title*0*=us-ascii'en'This%20is%20even%20more%20
        // title*1*=%2A%2A%2Afun%2A%2A%2A%20
        // title*2="isn't it!"

        if (_multiSegmentParameters.size() > 0) {

            final RFC2231Encoder decoder = new RFC2231Encoder(HeaderTokenizer.MIME);
            String lastName = null;
            int lastSegmentNumber = -1;
            final StringBuilder segmentValue = new StringBuilder();
            for (final Entry<MultiSegmentEntry, ParameterValue> entry : _multiSegmentParameters.entrySet()) {

                final MultiSegmentEntry currentMEntry = entry.getKey();

                if (lastName == null) {
                    lastName = currentMEntry.name;
                } else {

                    if (!lastName.equals(currentMEntry.name)) {

                        _parameters.put(lastName, new ParameterValue(lastName, segmentValue.toString()));
                        segmentValue.setLength(0);
                        lastName = currentMEntry.name;

                    }

                }

                if (lastSegmentNumber == -1) {
                    lastSegmentNumber = currentMEntry.range;

                    if (lastSegmentNumber != 0) {
                        // does not start with 0
                        // skip gracefully
                    }

                } else {
                    if (lastSegmentNumber + 1 != currentMEntry.range) {
                        // seems here is a gap
                        // skip gracefully
                    }
                }

                if (currentMEntry.encoded) {

                    try {
                        // decode the value
                        segmentValue.append(decoder.decode(entry.getValue().value));
                    } catch (final Exception e) {
                        segmentValue.append(entry.getValue().value);
                    }

                } else {

                    segmentValue.append(entry.getValue().value);

                }

            }

            _parameters.put(lastName, new ParameterValue(lastName, segmentValue.toString()));

        }

    }

    /**
     * Get the initial parameters that control parsing and values.
     * These parameters are controlled by System properties.
     */
    private void getInitialProperties() {
        decodeParameters = SessionUtil.getBooleanProperty(MIME_DECODEPARAMETERS, true); //since JavaMail 1.5 RFC 2231 support is enabled by default
        decodeParametersStrict = SessionUtil.getBooleanProperty(MIME_DECODEPARAMETERS_STRICT, false);
        encodeParameters = SessionUtil.getBooleanProperty(MIME_ENCODEPARAMETERS, true); //since JavaMail 1.5 RFC 2231 support is enabled by default
    }

    public int size() {
        return _parameters.size();
    }

    public String get(final String name) {
        final ParameterValue value = _parameters.get(name.toLowerCase());
        if (value != null) {
            return value.value;
        }
        return null;
    }

    public void set(String name, final String value) {
        name = name.toLowerCase();

        if (isMultiSegmentName(name)) {
            // multisegment parameter
            _multiSegmentParameters.put(new MultiSegmentEntry(name), new ParameterValue(name, value));
        } else {
            _parameters.put(name, new ParameterValue(name, value));
        }
    }

    public void set(String name, final String value, final String charset) {
        name = name.toLowerCase();
        // only encode if told to and this contains non-ASCII charactes.
        if (encodeParameters && !ASCIIUtil.isAscii(value)) {
            final ByteArrayOutputStream out = new ByteArrayOutputStream();

            try {
                final RFC2231Encoder encoder = new RFC2231Encoder(HeaderTokenizer.MIME);

                // extract the bytes using the given character set and encode
                final byte[] valueBytes = value.getBytes(MimeUtility.javaCharset(charset));

                // the string format is charset''data
                out.write(charset.getBytes("ISO8859-1"));
                out.write('\'');
                out.write('\'');
                encoder.encode(valueBytes, 0, valueBytes.length, out);

                
                if (isMultiSegmentName(name)) {
                    // multisegment parameter
                    _multiSegmentParameters.put(new MultiSegmentEntry(name), new ParameterValue(name, value, new String(out.toByteArray(), "ISO8859-1")));
                } else {
                    _parameters.put(name, new ParameterValue(name, value, new String(out.toByteArray(), "ISO8859-1")));
                }
                
                
                return;

            } catch (final Exception e) {
                // just fall through and set the value directly if there is an error
            }
        }
        // default in case there is an exception
        if (isMultiSegmentName(name)) {
            // multisegment parameter
            _multiSegmentParameters.put(new MultiSegmentEntry(name), new ParameterValue(name, value));
        } else {
            _parameters.put(name, new ParameterValue(name, value));
        }
    }

    public void remove(final String name) {
        _parameters.remove(name);
    }

    public Enumeration getNames() {
        return Collections.enumeration(_parameters.keySet());
    }

    @Override
    public String toString() {
        // we need to perform folding, but out starting point is 0.
        return toString(0);
    }

    public String toString(int used) {
        final StringBuffer stringValue = new StringBuffer();

        final Iterator values = _parameters.values().iterator();

        while (values.hasNext()) {
            final ParameterValue parm = (ParameterValue)values.next();
            // get the values we're going to encode in here.
            final String name = parm.getEncodedName();
            final String value = parm.toString();

            // add the semicolon separator.  We also add a blank so that folding/unfolding rules can be used.
            stringValue.append("; ");
            used += 2;

            // too big for the current header line?
            if ((used + name.length() + value.length() + 1) > HEADER_SIZE_LIMIT) {
                // and a CRLF-combo combo.
                stringValue.append("\r\n\t");
                // reset the counter for a fresh line
                // note we use use 8 because we're using a rather than a blank
                used = 8;
            }
            // now add the keyword/value pair.
            stringValue.append(name);
            stringValue.append("=");

            used += name.length() + 1;

            // we're not out of the woods yet.  It is possible that the keyword/value pair by itself might
            // be too long for a single line.  If that's the case, the we need to fold the value, if possible
            if (used + value.length() > HEADER_SIZE_LIMIT) {
                final String foldedValue = MimeUtility.fold(used, value);

                stringValue.append(foldedValue);

                // now we need to sort out how much of the current line is in use.
                final int lastLineBreak = foldedValue.lastIndexOf('\n');

                if (lastLineBreak != -1) {
                    used = foldedValue.length() - lastLineBreak + 1;
                }
                else {
                    used += foldedValue.length();
                }
            }
            else {
                // no folding required, just append.
                stringValue.append(value);
                used += value.length();
            }
        }

        return stringValue.toString();
    }


    /**
     * Utility class for representing parameter values in the list.
     */
    class ParameterValue {
        public String name;              // the name of the parameter
        public String value;             // the original set value
        public String encodedValue;      // an encoded value, if encoding is requested.

        public ParameterValue(final String name, final String value) {
            this.name = name;
            this.value = value;
            this.encodedValue = null;
        }

        public ParameterValue(final String name, final String value, final String encodedValue) {
            this.name = name;
            this.value = value;
            this.encodedValue = encodedValue;
        }

        @Override
        public String toString() {
            if (encodedValue != null) {
                return MimeUtility.quote(encodedValue, HeaderTokenizer.MIME);
            }
            return MimeUtility.quote(value, HeaderTokenizer.MIME);
        }

        public String getEncodedName() {
            if (encodedValue != null) {
                return name + "*";
            }
            return name;
        }
    }
    
    static class MultiSegmentEntry implements Comparable<MultiSegmentEntry>{
        final String original;
        final String normalized;
        final String name;
        final int range;
        final boolean encoded;
        
        public MultiSegmentEntry(final String original) {
            super();
            this.original = original;
        
            final int firstAsterixIndex1 = original.indexOf('*');
            encoded=original.endsWith("*");
            final int endIndex1 = encoded?original.length()-1:original.length();
            name = original.substring(0, firstAsterixIndex1);
            range = Integer.parseInt(original.substring(firstAsterixIndex1+1, endIndex1));
            normalized = original.substring(0, endIndex1);
        }
      
 
       @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((normalized == null) ? 0 : normalized.hashCode());
            return result;
        }

        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
				return true;
			}
            if (obj == null) {
				return false;
			}
            if (getClass() != obj.getClass()) {
				return false;
			}
            final MultiSegmentEntry other = (MultiSegmentEntry) obj;
            if (normalized == null) {
                if (other.normalized != null) {
					return false;
				}
            } else if (!normalized.equals(other.normalized)) {
				return false;
			}
            return true;
        }

        public int compareTo(final MultiSegmentEntry o) {
            
            if(this.equals(o)) {
				return 0;
			}
            
            if(name.equals(o.name)) {
                return range>o.range?1:-1;
            }else
            {
                return name.compareTo(o.name);
            }
            
            
            
        }


        @Override
        public String toString() {
            return "MultiSegmentEntry\n[original=" + original + ", name=" + name + ", range=" + range + "]\n";
        }
        
    }
    
    /*class MultiSegmentComparator implements Comparator<String> {

        public int compare(String o1, String o2) {
            
            if(o1.equals(o2)) return 0;
           
            int firstAsterixIndex1 = o1.indexOf('*');
            int firstAsterixIndex2 = o2.indexOf('*');
            String prefix1 = o1.substring(0, firstAsterixIndex1);
            String prefix2 = o2.substring(0, firstAsterixIndex2);
            
            if(!prefix1.equals(prefix2)) {
                return prefix1.compareTo(prefix2);
            }           
            
            int endIndex1 = o1.endsWith("*")?o1.length()-1:o1.length();           
            int endIndex2 = o2.endsWith("*")?o2.length()-1:o2.length();
            
            int num1 = Integer.parseInt(o1.substring(firstAsterixIndex1+1, endIndex1));
            int num2 = Integer.parseInt(o2.substring(firstAsterixIndex2+1, endIndex2));
            
            return num1>num2?1:-1;
           
        }
        
    }*/
}
