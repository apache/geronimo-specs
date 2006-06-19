/**
 *
 * Copyright 2006 The Apache Software Foundation
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Session;

import junit.framework.TestCase;

public class MimeUtilityTest extends TestCase {

    private byte[] encodeBytes = new byte[] { 32, 104, -61, -87, 33, 32, -61, -96, -61, -88, -61, -76, 117, 32, 33, 33, 33 };

    public void testEncodeDecode() throws Exception {

        byte [] data = new byte[256];
        for (int i = 0; i < data.length; i++) {
            data[i] = (byte)i;
        }

        // different lengths test boundary conditions
        doEncodingTest(data, 256, "uuencode");
        doEncodingTest(data, 255, "uuencode");
        doEncodingTest(data, 254, "uuencode");

        doEncodingTest(data, 256, "binary");
        doEncodingTest(data, 256, "7bit");
        doEncodingTest(data, 256, "8bit");
        doEncodingTest(data, 256, "base64");
        doEncodingTest(data, 255, "base64");
        doEncodingTest(data, 254, "base64");

        doEncodingTest(data, 256, "x-uuencode");
        doEncodingTest(data, 256, "x-uue");
        doEncodingTest(data, 256, "quoted-printable");
        doEncodingTest(data, 255, "quoted-printable");
        doEncodingTest(data, 254, "quoted-printable");
    }


    public void doEncodingTest(byte[] data, int length, String encoding) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        OutputStream encoder = MimeUtility.encode(out, encoding);

        encoder.write(data, 0, length);
        encoder.flush();

        byte[] encodedData = out.toByteArray();

        ByteArrayInputStream in = new ByteArrayInputStream(encodedData);

        InputStream decoder = MimeUtility.decode(in, encoding);

        byte[] decodedData = new byte[length];

        int count = decoder.read(decodedData);

        assertEquals(length, count);

        for (int i = 0; i < length; i++) {
            assertEquals(data[i], decodedData[i]);
        }
    }


    public void testEncodeWord() throws Exception {
        assertEquals("abc", MimeUtility.encodeWord("abc"));

        String encodeString = new String(encodeBytes, "UTF-8");
        // default code page dependent, hard to directly test the encoded results
        assertEquals(encodeString, MimeUtility.decodeWord(MimeUtility.encodeWord(encodeString)));

        String encoded = MimeUtility.encodeWord(encodeString, "UTF-8", "Q");
        assertEquals("=?UTF-8?Q?_h=C3=A9!_=C3=A0=C3=A8=C3=B4u_!!!?=", encoded);
        assertEquals(encodeString, MimeUtility.decodeWord(encoded));

        encoded = MimeUtility.encodeWord(encodeString, "UTF-8", "B");
        assertEquals("=?UTF-8?B?IGjDqSEgw6DDqMO0dSAhISE=?=", encoded);
        assertEquals(encodeString, MimeUtility.decodeWord(encoded));
    }


    public void testEncodeText() throws Exception {
        assertEquals("abc", MimeUtility.encodeWord("abc"));

        String encodeString = new String(encodeBytes, "UTF-8");
        // default code page dependent, hard to directly test the encoded results
        assertEquals(encodeString, MimeUtility.decodeText(MimeUtility.encodeText(encodeString)));

        String encoded = MimeUtility.encodeText(encodeString, "UTF-8", "Q");
        assertEquals("=?UTF-8?Q?_h=C3=A9!_=C3=A0=C3=A8=C3=B4u_!!!?=", encoded);
        assertEquals(encodeString, MimeUtility.decodeText(encoded));

        encoded = MimeUtility.encodeText(encodeString, "UTF-8", "B");
        assertEquals("=?UTF-8?B?IGjDqSEgw6DDqMO0dSAhISE=?=", encoded);
        assertEquals(encodeString, MimeUtility.decodeText(encoded));
    }


    public void testQuote() throws Exception {
        assertEquals("abc", MimeUtility.quote("abc", "&*%"));
        assertEquals("\"abc&\"", MimeUtility.quote("abc&", "&*%"));
        assertEquals("\"abc\\\"\"", MimeUtility.quote("abc\"", "&*%"));
        assertEquals("\"abc\\\\\"", MimeUtility.quote("abc\\", "&*%"));
        assertEquals("\"abc\\\r\"", MimeUtility.quote("abc\r", "&*%"));
        assertEquals("\"abc\\\n\"", MimeUtility.quote("abc\n", "&*%"));
    }
}
