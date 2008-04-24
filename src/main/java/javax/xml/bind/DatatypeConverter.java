/*
 **
 ** Licensed to the Apache Software Foundation (ASF) under one
 ** or more contributor license agreements.  See the NOTICE file
 ** distributed with this work for additional information
 ** regarding copyright ownership.  The ASF licenses this file
 ** to you under the Apache License, Version 2.0 (the
 ** "License"); you may not use this file except in compliance
 ** with the License.  You may obtain a copy of the License at
 **
 **  http://www.apache.org/licenses/LICENSE-2.0
 **
 ** Unless required by applicable law or agreed to in writing,
 ** software distributed under the License is distributed on an
 ** "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 ** KIND, either express or implied.  See the License for the
 ** specific language governing permissions and limitations
 ** under the License.
 */
package javax.xml.bind;

import java.util.Calendar;
import java.math.BigInteger;
import java.math.BigDecimal;

import javax.xml.namespace.QName;
import javax.xml.namespace.NamespaceContext;

public final class DatatypeConverter {

    private static DatatypeConverterInterface converter = null;

    private DatatypeConverter() {
    }

    public static void setDatatypeConverter(DatatypeConverterInterface converter) {
        if (converter == null) {
            throw new IllegalArgumentException("The DatatypeConverterInterface parameter must not be null");
        }
        if (DatatypeConverter.converter == null) {
            DatatypeConverter.converter = converter;
        }
    }

    public static String parseString(String lexicalXSDString) {
        return converter.parseString(lexicalXSDString);
    }

    public static BigInteger parseInteger(String lexicalXSDInteger) {
        return converter.parseInteger(lexicalXSDInteger);
    }

    public static int parseInt(String lexicalXSDInt) {
        return converter.parseInt(lexicalXSDInt);
    }

    public static long parseLong(String lexicalXSDLong) {
        return converter.parseLong(lexicalXSDLong);
    }

    public static short parseShort(String lexicalXSDShort) {
        return converter.parseShort(lexicalXSDShort);
    }

    public static BigDecimal parseDecimal(String lexicalXSDDecimal) {
        return converter.parseDecimal(lexicalXSDDecimal);
    }

    public static float parseFloat(String lexicalXSDFloat) {
        return converter.parseFloat(lexicalXSDFloat);
    }

    public static double parseDouble(String lexicalXSDDouble) {
        return converter.parseDouble(lexicalXSDDouble);
    }

    public static boolean parseBoolean(String lexicalXSDBoolean) {
        return converter.parseBoolean(lexicalXSDBoolean);
    }

    public static byte parseByte(String lexicalXSDByte) {
        return converter.parseByte(lexicalXSDByte);
    }

    public static QName parseQName(String lexicalXSDQName, NamespaceContext nsc) {
        return converter.parseQName(lexicalXSDQName, nsc);
    }

    public static Calendar parseDateTime(String lexicalXSDDateTime) {
        return converter.parseDateTime(lexicalXSDDateTime);
    }

    public static byte[] parseBase64Binary(String lexicalXSDBase64Binary) {
        return converter.parseBase64Binary(lexicalXSDBase64Binary);
    }

    public static byte[] parseHexBinary(String lexicalXSDHexBinary) {
        return converter.parseHexBinary(lexicalXSDHexBinary);
    }

    public static long parseUnsignedInt(String lexicalXSDUnsignedInt) {
        return converter.parseUnsignedInt(lexicalXSDUnsignedInt);
    }

    public static int parseUnsignedShort(String lexicalXSDUnsignedShort) {
        return converter.parseUnsignedShort(lexicalXSDUnsignedShort);
    }

    public static Calendar parseTime(String lexicalXSDTime) {
        return converter.parseTime(lexicalXSDTime);
    }

    public static Calendar parseDate(String lexicalXSDDate) {
        return converter.parseDate(lexicalXSDDate);
    }

    public static String parseAnySimpleType(String lexicalXSDAnySimpleType) {
        return converter.parseAnySimpleType(lexicalXSDAnySimpleType);
    }

    public static String printString(String val) {
        return converter.printString(val);
    }

    public static String printInteger(BigInteger val) {
        return converter.printInteger(val);
    }

    public static String printInt(int val) {
        return converter.printInt(val);
    }

    public static String printLong(long val) {
        return converter.printLong(val);
    }

    public static String printShort(short val) {
        return converter.printShort(val);
    }

    public static String printDecimal(BigDecimal val) {
        return converter.printDecimal(val);
    }

    public static String printFloat(float val) {
        return converter.printFloat(val);
    }

    public static String printDouble(double val) {
        return converter.printDouble(val);
    }

    public static String printBoolean(boolean val) {
        return converter.printBoolean(val);
    }

    public static String printByte(byte val) {
        return converter.printByte(val);
    }

    public static String printQName(QName val, NamespaceContext nsc) {
        return converter.printQName(val, nsc);
    }

    public static String printDateTime(Calendar val) {
        return converter.printDateTime(val);
    }

    public static String printBase64Binary(byte val[]) {
        return converter.printBase64Binary(val);
    }

    public static String printHexBinary(byte val[]) {
        return converter.printHexBinary(val);
    }

    public static String printUnsignedInt(long val) {
        return converter.printUnsignedInt(val);
    }

    public static String printUnsignedShort(int val) {
        return converter.printUnsignedShort(val);
    }

    public static String printTime(Calendar val) {
        return converter.printTime(val);
    }

    public static String printDate(Calendar val) {
        return converter.printDate(val);
    }

    public static String printAnySimpleType(String val) {
        return converter.printAnySimpleType(val);
    }

}
