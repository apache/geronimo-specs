package javax.xml.bind;

import java.util.Calendar;
import java.math.BigDecimal;
import java.math.BigInteger;

import javax.xml.namespace.QName;
import javax.xml.namespace.NamespaceContext;

/**
 * Created by IntelliJ IDEA.
 * User: gnodet
 * Date: Apr 17, 2008
 * Time: 8:47:04 AM
 * To change this template use File | Settings | File Templates.
 */
public interface DatatypeConverterInterface {

    String parseAnySimpleType(String lexicalXSDAnySimpleType);

    byte[] parseBase64Binary(String lexicalXSDBase64Binary);

    boolean parseBoolean(String lexicalXSDBoolean);

    byte parseByte(String lexicalXSDByte);

    Calendar parseDate(String lexicalXSDDate);

    Calendar parseDateTime(String lexicalXSDDateTime);

    BigDecimal parseDecimal(String lexicalXSDDecimal);

    double parseDouble(String lexicalXSDDouble);

    float parseFloat(String lexicalXSDFloat);

    byte[] parseHexBinary(String lexicalXSDHexBinary);

    int parseInt(String lexicalXSDInt);

    BigInteger parseInteger(String lexicalXSDInteger);

    long parseLong(String lexicalXSDLong);

    QName parseQName(String lexicalXSDQName, NamespaceContext nsc);

    short parseShort(String lexicalXSDShort);

    String parseString(String lexicalXSDString);

    Calendar parseTime(String lexicalXSDTime);

    long parseUnsignedInt(String lexicalXSDUnsignedInt);

    int parseUnsignedShort(String lexicalXSDUnsignedShort);

    String printAnySimpleType(String val);

    String printBase64Binary(byte[] val);

    String printBoolean(boolean val);

    String printByte(byte val);

    String printDate(Calendar val);

    String printDateTime(Calendar val);

    String printDecimal(BigDecimal val);

    String printDouble(double val);

    String printFloat(float val);

    String printHexBinary(byte[] val);

    String printInt(int val);

    String printInteger(BigInteger val);

    String printLong(long val);

    String printQName(QName val, NamespaceContext nsc);

    String printShort(short val);

    String printString(String val);

    String printTime(Calendar val);

    String printUnsignedInt(long val);

    String printUnsignedShort(int val);

}
