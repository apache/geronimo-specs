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

import java.io.InputStream;
import java.util.Scanner;

import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import junit.framework.Assert;
import junit.framework.TestCase;

public class QuotedPrintableEncoderTest extends TestCase{

    public void testGERONIMO6165() throws Exception{
        MimeMessage msg = new MimeMessage((Session)null, QuotedPrintableEncoderTest.class.getResourceAsStream("/GERONIMO-6165.msg"));
        Assert.assertEquals("quoted-printable", msg.getEncoding());
        Assert.assertEquals("hello there!", msg.getContent().toString());
    }
    
    public void testLongMail() throws Exception{
        MimeMessage msg = new MimeMessage((Session)null, QuotedPrintableEncoderTest.class.getResourceAsStream("/quoted-printable-longmail.msg"));
        InputStream result = QuotedPrintableEncoderTest.class.getResourceAsStream("/quoted-printable-longmail-result.txt");
        Assert.assertEquals("quoted-printable", msg.getEncoding());
        Assert.assertEquals(new Scanner(result,"UTF-8").useDelimiter("\\A").next(), msg.getContent());
    }
}
