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

package javax.mail.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.mail.internet.SharedInputStream;

import junit.framework.TestCase;

/**
 * @version $Rev$ $Date$
 */
public class SharedFileInputStreamTest extends TestCase {

    File basedir = new File(System.getProperty("basedir", "."));
    File testInput = new File(basedir, "src/test/resources/test.dat");

    public SharedFileInputStreamTest(String arg0) {
        super(arg0);
    }

    public void testInput() throws Exception {
        doTestInput(new SharedFileInputStream(testInput));
        doTestInput(new SharedFileInputStream(testInput.getPath()));

        doTestInput(new SharedFileInputStream(testInput, 16));
        doTestInput(new SharedFileInputStream(testInput.getPath(), 16));
    }


    public void doTestInput(SharedFileInputStream in) throws Exception {
        assertEquals(in.read(), '0');

        assertEquals(in.getPosition(), 1);

        byte[] bytes = new byte[10];

        assertEquals(in.read(bytes), 10);
        assertEquals(new String(bytes), "123456789a");
        assertEquals(in.getPosition(), 11);

        assertEquals(in.read(bytes, 5, 5), 5);
        assertEquals(new String(bytes), "12345bcdef");
        assertEquals(in.getPosition(), 16);

        assertEquals(in.skip(5), 5);
        assertEquals(in.getPosition(), 21);
        assertEquals(in.read(), 'l');

        while (in.read() != '\n' ) {
        }

        assertEquals(in.read(), -1);

        in.close();
    }


    public void testNewStream() throws Exception {
        SharedFileInputStream in = new SharedFileInputStream(testInput);

        SharedFileInputStream sub = (SharedFileInputStream)in.newStream(10, 10 + 26);

        assertEquals(sub.getPosition(), 0);

        assertEquals(in.read(), '0');
        assertEquals(sub.read(), 'a');

        sub.skip(1);
        assertEquals(sub.getPosition(), 2);

        while (sub.read() != 'z') {
        }

        assertEquals(sub.read(), -1);

        SharedFileInputStream sub2 = (SharedFileInputStream)sub.newStream(5, 10);

        sub.close();    // should not close in or sub2

        assertEquals(sub2.getPosition(), 0);
        assertEquals(sub2.read(), 'f');

        assertEquals(in.read(), '1');   // should still work

        sub2.close();

        assertEquals(in.read(), '2');   // should still work

        in.close();
    }


    public void testMark() throws Exception {
        doMarkTest(new SharedFileInputStream(testInput, 10));

        SharedFileInputStream in = new SharedFileInputStream(testInput, 10);

        SharedFileInputStream sub = (SharedFileInputStream)in.newStream(5, -1);
        doMarkTest(sub);
    }


    private void doMarkTest(SharedFileInputStream in) throws Exception {
         assertTrue(in.markSupported());

         byte[] buffer = new byte[60];

         in.read();
         in.read();
         in.mark(50);

         int markSpot = in.read();

         in.read(buffer, 0, 20);

         in.reset();

         assertEquals(markSpot, in.read());
         in.read(buffer, 0, 40);
         in.reset();
         assertEquals(markSpot, in.read());

         in.read(buffer, 0, 51);

         try {
             in.reset();
             fail();
         } catch (IOException e) {
         }
    }
}

