/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package jakarta.xml.bind;

import java.util.Calendar;
import java.util.TimeZone;
import java.math.BigInteger;
import java.math.BigDecimal;

import javax.xml.namespace.QName;
import javax.xml.namespace.NamespaceContext;

/**
 * Wrapper for DatatypeConverterInterface that provides {@link #printDate(Calendar)} method that
 * outputs time zone information. All other methods are delegated to the original converter.
 * See {@link https://jaxb.dev.java.net/issues/show_bug.cgi?id=761} for more information.
 */
class DatatypeConverterHelper implements DatatypeConverterInterface {
           
    private final DatatypeConverterInterface delegate;

    DatatypeConverterHelper(DatatypeConverterInterface delegate) {
        this.delegate = delegate;
    }
        
    public String parseAnySimpleType(String lexicalXSDAnySimpleType) {
        return delegate.parseAnySimpleType(lexicalXSDAnySimpleType);
    }

    public byte[] parseBase64Binary(String lexicalXSDBase64Binary) {
        return delegate.parseBase64Binary(lexicalXSDBase64Binary);
    }

    public boolean parseBoolean(String lexicalXSDBoolean) {
        return delegate.parseBoolean(lexicalXSDBoolean);
    }

    public byte parseByte(String lexicalXSDByte) {
        return delegate.parseByte(lexicalXSDByte);
    }

    public Calendar parseDate(String lexicalXSDDate) {
        return delegate.parseDate(lexicalXSDDate);
    }

    public Calendar parseDateTime(String lexicalXSDDateTime) {
        return delegate.parseDateTime(lexicalXSDDateTime);
    }

    public BigDecimal parseDecimal(String lexicalXSDDecimal) {
        return delegate.parseDecimal(lexicalXSDDecimal);
    }

    public double parseDouble(String lexicalXSDDouble) {
        return delegate.parseDouble(lexicalXSDDouble);
    }

    public float parseFloat(String lexicalXSDFloat) {
        return delegate.parseFloat(lexicalXSDFloat);
    }

    public byte[] parseHexBinary(String lexicalXSDHexBinary) {
        return delegate.parseHexBinary(lexicalXSDHexBinary);
    }

    public int parseInt(String lexicalXSDInt) {
        return delegate.parseInt(lexicalXSDInt);
    }

    public BigInteger parseInteger(String lexicalXSDInteger) {
        return delegate.parseInteger(lexicalXSDInteger);
    }

    public long parseLong(String lexicalXSDLong) {
        return delegate.parseLong(lexicalXSDLong);
    }

    public QName parseQName(String lexicalXSDQName, NamespaceContext nsc) {
        return delegate.parseQName(lexicalXSDQName, nsc);
    }

    public short parseShort(String lexicalXSDShort) {
        return delegate.parseShort(lexicalXSDShort);
    }

    public String parseString(String lexicalXSDString) {
        return delegate.parseString(lexicalXSDString);
    }

    public Calendar parseTime(String lexicalXSDTime) {
        return delegate.parseTime(lexicalXSDTime);
    }

    public long parseUnsignedInt(String lexicalXSDUnsignedInt) {
        return delegate.parseUnsignedInt(lexicalXSDUnsignedInt);
    }

    public int parseUnsignedShort(String lexicalXSDUnsignedShort) {
        return delegate.parseUnsignedShort(lexicalXSDUnsignedShort);
    }

    public String printAnySimpleType(String val) {
        return delegate.printAnySimpleType(val);
    }

    public String printBase64Binary(byte[] val) {
        return delegate.printBase64Binary(val);
    }

    public String printBoolean(boolean val) {
        return delegate.printBoolean(val);
    }

    public String printByte(byte val) {
        return delegate.printByte(val);
    }

    public String printDate(Calendar cal) {
        StringBuilder buf = new StringBuilder();
        
        int year = cal.get(Calendar.YEAR);
        if (year < 0) {
            buf.append("-");
            year = -year;
        }
        append(buf, year, 4);
        buf.append('-');
        append(buf, cal.get(Calendar.MONTH) + 1, 2);
        buf.append('-');
        append(buf, cal.get(Calendar.DAY_OF_MONTH), 2);
        
        TimeZone tz = cal.getTimeZone();
        if (tz != null) {
            int offset = cal.get(Calendar.ZONE_OFFSET);
            if (tz.inDaylightTime(cal.getTime())) {
                offset += cal.get(Calendar.DST_OFFSET);
            }
            if (offset == 0) {
                buf.append('Z');
            } else {
                if (offset < 0) {
                    buf.append('-');
                    offset = -offset;
                } else {
                    buf.append('+');
                }
                offset /= 60 * 1000;
                append(buf, offset / 60, 2);
                buf.append(':');
                append(buf, offset % 60, 2);
            }
        }
        
        return buf.toString();
    }
    
    private static void append(StringBuilder buf, int value, int minLength) {
        String str = String.valueOf(value);
        for (int i = str.length(); i < minLength; i++) {
            buf.append('0');
        }
        buf.append(str);
    }

    public String printDateTime(Calendar val) {
        return delegate.printDateTime(val);
    }

    public String printDecimal(BigDecimal val) {
        return delegate.printDecimal(val);
    }

    public String printDouble(double val) {
        return delegate.printDouble(val);
    }

    public String printFloat(float val) {
        return delegate.printFloat(val);
    }

    public String printHexBinary(byte[] val) {
        return delegate.printHexBinary(val);
    }

    public String printInt(int val) {
        return delegate.printInt(val);
    }

    public String printInteger(BigInteger val) {
        return delegate.printInteger(val);
    }

    public String printLong(long val) {
        return delegate.printLong(val);
    }

    public String printQName(QName val, NamespaceContext nsc) {
        return delegate.printQName(val, nsc);
    }

    public String printShort(short val) {
        return delegate.printShort(val);
    }

    public String printString(String val) {
        return delegate.printString(val);
    }

    public String printTime(Calendar val) {
        return delegate.printTime(val);
    }

    public String printUnsignedInt(long val) {
        return delegate.printUnsignedInt(val);
    }

    public String printUnsignedShort(int val) {
        return delegate.printUnsignedShort(val);
    }
        
}
