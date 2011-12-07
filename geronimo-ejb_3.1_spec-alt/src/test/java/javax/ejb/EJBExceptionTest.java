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
//
// This source code implements specifications defined by the Java
// Community Process. In order to remain compliant with the specification
// DO NOT add / change / or delete method signatures!
//
package javax.ejb;

import org.junit.Assert;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;

public class EJBExceptionTest {

    private static final EJBException exceptionDefaultConstructor = new EJBException();
    private static final EJBException exceptionWithNullMessage = new EJBException((String) null);
    private static final EJBException exceptionWithMessage = new EJBException("msg");

    private static final EJBException exceptionWithCausedBy = createException(true, false);
    private static final EJBException exceptionWithInitCause = createException(false, true);
    private static final EJBException exceptionWithCausedByAndInitCause = createException(true, true);
    private static final EJBException exceptionWithDifferentCausedByAndInitCause = (EJBException) createException(true, false).initCause(new RuntimeException("initCause"));

    private static EJBException createException(boolean causedBy, boolean initCause) {

        try {

            throw new Exception("cause");

        } catch (Exception ex) {

            EJBException ejbException = causedBy ? new EJBException("msg", ex) : new EJBException("msg");

            if (initCause) {

                ejbException.initCause(ex);

            }

            return ejbException;
        }
    }

    @Test
    public void testGetMessage() {

        Assert.assertEquals(null, exceptionDefaultConstructor.getMessage());

        Assert.assertEquals(null, exceptionWithNullMessage.getMessage());

        Assert.assertEquals("msg", exceptionWithMessage.getMessage());

        Assert.assertEquals("msg; nested exception is: java.lang.Exception: cause", exceptionWithCausedBy.getMessage());

        Assert.assertEquals("msg", exceptionWithInitCause.getMessage());

        Assert.assertEquals("msg; nested exception is: java.lang.Exception: cause", exceptionWithCausedByAndInitCause.getMessage());

        // TODO Define this behavior
//        Assert.assertEquals("msg; nested exception is: java.lang.RuntimeException: initCause", exceptionWithDifferentCausedByAndInitCause.getMessage());
    }

    @Test
    public void testGetCause() {

        Assert.assertNull(exceptionDefaultConstructor.getCause());

        Assert.assertNull(exceptionWithNullMessage.getCause());

        Assert.assertNull(exceptionWithMessage.getCause());

        Assert.assertNotNull(exceptionWithCausedBy.getCause());

        Assert.assertNotNull(exceptionWithInitCause.getCause());

        Assert.assertNotNull(exceptionWithCausedByAndInitCause.getCause());

        Assert.assertNotNull(exceptionWithDifferentCausedByAndInitCause.getCause());
    }

    @Test
    public void testGetCausedByException() {

        Assert.assertNull(exceptionDefaultConstructor.getCausedByException());

        Assert.assertNull(exceptionWithNullMessage.getCausedByException());

        Assert.assertNull(exceptionWithMessage.getCausedByException());

        Assert.assertSame(exceptionWithCausedBy.getCause(), exceptionWithCausedBy.getCausedByException());

        Assert.assertNull(exceptionWithInitCause.getCausedByException());

        Assert.assertNotNull(exceptionWithCausedByAndInitCause.getCausedByException());

        Assert.assertNotNull(exceptionWithDifferentCausedByAndInitCause.getCausedByException());
    }

    @Test
    public void testInitCause() {

        Exception cause1 = new Exception("cause1");

        Exception cause2 = new Exception("cause2");


        EJBException ejbEx = new EJBException(cause1);

        Assert.assertSame(cause1, ejbEx.getCausedByException());

        Assert.assertSame(cause1, ejbEx.getCause());

        ejbEx.initCause(cause2);

        Assert.assertSame(cause1, ejbEx.getCausedByException());

        Assert.assertSame(cause2, ejbEx.getCause());
    }

    private static final String[] printStackTraces(Throwable t) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        t.printStackTrace(new PrintStream(baos));


        StringWriter sw = new StringWriter();

        t.printStackTrace(new PrintWriter(sw));


        return new String[]{new String(baos.toByteArray()), sw.toString()};
    }

    private static String highlight(String s) {

        return ">>>" + s + "<<<";
    }

    private void assertStackTrace(String s, String... pieces) throws IOException {

        BufferedReader br = new BufferedReader(new StringReader(s));

        int pos = 0;


        for (String line; (line = br.readLine()) != null; ) {

            if (line.startsWith("\tat") && (pos > 0 && pieces[pos - 1].equals("\tat"))) {

                continue;

            }


            if (pos == pieces.length) {

                Assert.fail("unexpected lines" + System.getProperty("line.separator") + highlight(s));

            }


            if (!line.startsWith(pieces[pos])) {

                Assert.fail(highlight(line) + " does not start with piece " +

                        (pos + 1) + " " + highlight(pieces[pos]) +

                        System.getProperty("line.separator") + highlight(s));

            }


            pos++;
        }


        if (pos != pieces.length) {

            Assert.fail("expected piece " + (pos + 1) + System.getProperty("line.separator") + highlight(s));
        }
    }

    @Test
    public void testPrintStackTrace() throws Exception {

        for (String s : printStackTraces(exceptionDefaultConstructor)) {

            assertStackTrace(s, "javax.ejb.EJBException", "\tat");
        }


        for (String s : printStackTraces(exceptionWithNullMessage)) {

            assertStackTrace(s, "javax.ejb.EJBException", "\tat");
        }


        for (String s : printStackTraces(exceptionWithMessage)) {

            assertStackTrace(s, "javax.ejb.EJBException", "\tat");
        }


        for (String s : printStackTraces(exceptionWithCausedBy)) {

            assertStackTrace(s, "javax.ejb.EJBException: msg; nested exception",
                    "\tat",
                    "Caused by",
                    "\tat",
                    "\t...");
        }


        for (String s : printStackTraces(exceptionWithInitCause)) {

            assertStackTrace(s,
                    "javax.ejb.EJBException",
                    "\tat",
                    "Caused by",
                    "\tat",
                    "\t...");
        }


        for (String s : printStackTraces(exceptionWithCausedByAndInitCause)) {

            assertStackTrace(s,
                    "javax.ejb.EJBException: msg; nested exception",
                    "\tat",
                    "Caused by",
                    "\tat",
                    "\t...");
        }


        for (String s : printStackTraces(new EJBException(new Exception("cause1")).initCause(new Exception("cause2")))) {

            assertStackTrace(s,
                    "javax.ejb.EJBException: nested exception is: java.lang.Exception: cause1",
                    "java.lang.Exception: cause1",
                    "\tat",
                    "javax.ejb.EJBException: nested exception is: java.lang.Exception: cause1",
                    "\tat",
                    "Caused by: java.lang.Exception: cause2",
                    "\t...");
        }
    }
}