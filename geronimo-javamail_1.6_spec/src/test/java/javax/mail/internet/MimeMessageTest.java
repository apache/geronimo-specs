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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.activation.CommandMap;
import javax.activation.MailcapCommandMap;
import javax.mail.Address;
import javax.mail.Flags.Flag;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Session;

import junit.framework.TestCase;

/**
 * @version $Rev$ $Date$
 */
public class MimeMessageTest extends TestCase {
    private CommandMap defaultMap;
    private Session session;

    public void testWriteTo() throws MessagingException, IOException {
        final MimeMessage msg = new MimeMessage(session);
        msg.setSender(new InternetAddress("foo"));
        msg.setHeader("foo", "bar");
        final MimeMultipart mp = new MimeMultipart();
        final MimeBodyPart part1 = new MimeBodyPart();
        part1.setHeader("foo", "bar");
        part1.setContent("Hello World", "text/plain");
        mp.addBodyPart(part1);
        final MimeBodyPart part2 = new MimeBodyPart();
        part2.setContent("Hello Again", "text/plain");
        mp.addBodyPart(part2);
        msg.setContent(mp);

        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        msg.writeTo(out);

        final InputStream in = new ByteArrayInputStream(out.toByteArray());

        MimeMessage newMessage = new MimeMessage(session, in);

        assertEquals(((InternetAddress)newMessage.getSender()).getAddress(), "foo");

        final String[] headers = newMessage.getHeader("foo");
        assertTrue(headers.length == 1);
        assertEquals(headers[0], "bar");


        newMessage = new MimeMessage(msg);

        assertEquals(((InternetAddress)newMessage.getSender()).getAddress(), "foo");
        assertEquals(newMessage.getHeader("foo")[0], "bar");
    }


    public void testFrom() throws MessagingException {
        final MimeMessage msg = new MimeMessage(session);

        final InternetAddress dev = new InternetAddress("geronimo-dev@apache.org");
        final InternetAddress user = new InternetAddress("geronimo-user@apache.org");

        msg.setSender(dev);

        Address[] from = msg.getFrom();
        assertTrue(from.length == 1);
        assertEquals(from[0], dev);

        msg.setFrom(user);
        from = msg.getFrom();
        assertTrue(from.length == 1);
        assertEquals(from[0], user);

        msg.addFrom(new Address[] { dev });
        from = msg.getFrom();
        assertTrue(from.length == 2);
        assertEquals(from[0], user);
        assertEquals(from[1], dev);

        msg.setFrom();
        final InternetAddress local = InternetAddress.getLocalAddress(session);
        from = msg.getFrom();

        assertTrue(from.length == 1);
        assertEquals(local, from[0]);

        msg.setFrom((Address) null);
        from = msg.getFrom();

        assertTrue(from.length == 1);
        assertEquals(dev, from[0]);

        msg.setSender(null);
        from = msg.getFrom();
        assertNull(from);
    }


    public void testSender() throws MessagingException {
        final MimeMessage msg = new MimeMessage(session);

        final InternetAddress dev = new InternetAddress("geronimo-dev@apache.org");
        final InternetAddress user = new InternetAddress("geronimo-user@apache.org");

        msg.setSender(dev);

        final Address[] from = msg.getFrom();
        assertTrue(from.length == 1);
        assertEquals(from[0], dev);

        assertEquals(msg.getSender(), dev);

        msg.setSender(null);
        assertNull(msg.getSender());
    }
    
    public void testJavaMail15GetSession() throws MessagingException {
        final MimeMessage msg = new MimeMessage(session);
        assertTrue(session == msg.getSession());
        
        final MimeMessage msg2 = new MimeMessage((Session) null);
        assertTrue(null == msg2.getSession());
    }
    
    public void testJava15From() throws MessagingException {
        final MimeMessage msg = new MimeMessage(session);
        
        final InternetAddress dev = new InternetAddress("geronimo-dev@apache.org");
        final InternetAddress user = new InternetAddress("geronimo-user@apache.org");

        
        msg.setFrom("geronimo-dev@apache.org,geronimo-user@apache.org");

        Address[] from = msg.getFrom();
        assertTrue(from.length == 2);
        assertEquals(from[0], dev);
        assertEquals(from[1], user);
        
        msg.setFrom("test@apache.org");
        
        from = msg.getFrom();
        assertTrue(from.length == 1);
        assertEquals(from[0], new InternetAddress("test@apache.org"));
    }

    public void testGetAllRecipients() throws MessagingException {
        final MimeMessage msg = new MimeMessage(session);

        final InternetAddress dev = new InternetAddress("geronimo-dev@apache.org");
        final InternetAddress user = new InternetAddress("geronimo-user@apache.org");
        final InternetAddress user1 = new InternetAddress("geronimo-user1@apache.org");
        final InternetAddress user2 = new InternetAddress("geronimo-user2@apache.org");
        final NewsAddress group = new NewsAddress("comp.lang.rexx");

        Address[] recipients = msg.getAllRecipients();
        assertNull(recipients);

        msg.setRecipients(Message.RecipientType.TO, new Address[] { dev });

        recipients = msg.getAllRecipients();
        assertTrue(recipients.length == 1);
        assertEquals(recipients[0], dev);

        msg.addRecipients(Message.RecipientType.BCC, new Address[] { user });

        recipients = msg.getAllRecipients();
        assertTrue(recipients.length == 2);
        assertEquals(recipients[0], dev);
        assertEquals(recipients[1], user);

        msg.addRecipients(Message.RecipientType.CC, new Address[] { user1, user2} );

        recipients = msg.getAllRecipients();
        assertTrue(recipients.length == 4);
        assertEquals(recipients[0], dev);
        assertEquals(recipients[1], user1);
        assertEquals(recipients[2], user2);
        assertEquals(recipients[3], user);


        msg.addRecipients(MimeMessage.RecipientType.NEWSGROUPS, new Address[] { group } );

        recipients = msg.getAllRecipients();
        assertTrue(recipients.length == 5);
        assertEquals(recipients[0], dev);
        assertEquals(recipients[1], user1);
        assertEquals(recipients[2], user2);
        assertEquals(recipients[3], user);
        assertEquals(recipients[4], group);

        msg.setRecipients(Message.RecipientType.CC, (String)null);

        recipients = msg.getAllRecipients();

        assertTrue(recipients.length == 3);
        assertEquals(recipients[0], dev);
        assertEquals(recipients[1], user);
        assertEquals(recipients[2], group);
    }

    public void testGetRecipients() throws MessagingException {
        doRecipientTest(Message.RecipientType.TO);
        doRecipientTest(Message.RecipientType.CC);
        doRecipientTest(Message.RecipientType.BCC);
        doNewsgroupRecipientTest(MimeMessage.RecipientType.NEWSGROUPS);
    }

    private void doRecipientTest(final Message.RecipientType type) throws MessagingException {
        final MimeMessage msg = new MimeMessage(session);

        final InternetAddress dev = new InternetAddress("geronimo-dev@apache.org");
        final InternetAddress user = new InternetAddress("geronimo-user@apache.org");

        Address[] recipients = msg.getRecipients(type);
        assertNull(recipients);

        msg.setRecipients(type, "geronimo-dev@apache.org");
        recipients = msg.getRecipients(type);
        assertTrue(recipients.length == 1);
        assertEquals(recipients[0], dev);

        msg.addRecipients(type, "geronimo-user@apache.org");

        recipients = msg.getRecipients(type);
        assertTrue(recipients.length == 2);
        assertEquals(recipients[0], dev);
        assertEquals(recipients[1], user);

        msg.setRecipients(type, (String)null);

        recipients = msg.getRecipients(type);
        assertNull(recipients);

        msg.setRecipients(type, new Address[] { dev });
        recipients = msg.getRecipients(type);
        assertTrue(recipients.length == 1);
        assertEquals(recipients[0], dev);

        msg.addRecipients(type, new Address[] { user });

        recipients = msg.getRecipients(type);
        assertTrue(recipients.length == 2);
        assertEquals(recipients[0], dev);
        assertEquals(recipients[1], user);

        msg.setRecipients(type, (Address[])null);

        recipients = msg.getRecipients(type);
        assertNull(recipients);

        msg.setRecipients(type, new Address[] { dev, user });

        recipients = msg.getRecipients(type);
        assertTrue(recipients.length == 2);
        assertEquals(recipients[0], dev);
        assertEquals(recipients[1], user);
    }


    private void doNewsgroupRecipientTest(final Message.RecipientType type) throws MessagingException {
        final MimeMessage msg = new MimeMessage(session);

        final Address dev = new NewsAddress("geronimo-dev");
        final Address user = new NewsAddress("geronimo-user");

        Address[] recipients = msg.getRecipients(type);
        assertNull(recipients);

        msg.setRecipients(type, "geronimo-dev");
        recipients = msg.getRecipients(type);
        assertTrue(recipients.length == 1);
        assertEquals(recipients[0], dev);

        msg.addRecipients(type, "geronimo-user");

        recipients = msg.getRecipients(type);
        assertTrue(recipients.length == 2);
        assertEquals(recipients[0], dev);
        assertEquals(recipients[1], user);

        msg.setRecipients(type, (String)null);

        recipients = msg.getRecipients(type);
        assertNull(recipients);

        msg.setRecipients(type, new Address[] { dev });
        recipients = msg.getRecipients(type);
        assertTrue(recipients.length == 1);
        assertEquals(recipients[0], dev);

        msg.addRecipients(type, new Address[] { user });

        recipients = msg.getRecipients(type);
        assertTrue(recipients.length == 2);
        assertEquals(recipients[0], dev);
        assertEquals(recipients[1], user);

        msg.setRecipients(type, (Address[])null);

        recipients = msg.getRecipients(type);
        assertNull(recipients);

        msg.setRecipients(type, new Address[] { dev, user });

        recipients = msg.getRecipients(type);
        assertTrue(recipients.length == 2);
        assertEquals(recipients[0], dev);
        assertEquals(recipients[1], user);
    }

    public void testReplyTo() throws MessagingException {
        final MimeMessage msg = new MimeMessage(session);

        final InternetAddress dev = new InternetAddress("geronimo-dev@apache.org");
        final InternetAddress user = new InternetAddress("geronimo-user@apache.org");

        msg.setReplyTo(new Address[] { dev });

        Address[] recipients = msg.getReplyTo();
        assertTrue(recipients.length == 1);
        assertEquals(recipients[0], dev);

        msg.setReplyTo(new Address[] { dev, user });

        recipients = msg.getReplyTo();
        assertTrue(recipients.length == 2);
        assertEquals(recipients[0], dev);
        assertEquals(recipients[1], user);

        msg.setReplyTo(null);

        recipients = msg.getReplyTo();
        assertNull(recipients);
    }
    
    public void testJavaMail15Reply() throws MessagingException {
        final MimeMessage msg = new MimeMessage(session);
        final InternetAddress dev = new InternetAddress("geronimo-dev@apache.org");
        
        msg.setFrom("test@apache.org");
        msg.setRecipient(RecipientType.TO, dev);

        final Message replyMsg = msg.reply(true, true);
        assertNotNull(replyMsg);
        assertTrue(msg.isSet(Flag.ANSWERED));
        assertEquals(new InternetAddress("test@apache.org"), replyMsg.getRecipients(RecipientType.TO)[0]);
    }
    
    public void testJavaMail15Reply2() throws MessagingException {
        final MimeMessage msg = new MimeMessage(session);
        final InternetAddress dev = new InternetAddress("geronimo-dev@apache.org");
        
        msg.setFrom("test@apache.org");
        msg.setRecipient(RecipientType.TO, dev);

        final Message replyMsg = msg.reply(false, false);
        assertNotNull(replyMsg);
        assertFalse(msg.isSet(Flag.ANSWERED));
        assertEquals(new InternetAddress("test@apache.org"), replyMsg.getRecipients(RecipientType.TO)[0]);
    }


    public void testSetSubject() throws MessagingException {
        final MimeMessage msg = new MimeMessage(session);

        final String simpleSubject = "Yada, yada";

        final String complexSubject = "Yada, yada\u0081";

        final String mungedSubject = "Yada, yada\u003F";

        msg.setSubject(simpleSubject);
        assertEquals(msg.getSubject(), simpleSubject);

        msg.setSubject(complexSubject, "UTF-8");
        assertEquals(msg.getSubject(), complexSubject);

        msg.setSubject(null);
        assertNull(msg.getSubject());
    }


    public void testSetDescription() throws MessagingException {
        final MimeMessage msg = new MimeMessage(session);

        final String simpleSubject = "Yada, yada";

        final String complexSubject = "Yada, yada\u0081";

        final String mungedSubject = "Yada, yada\u003F";

        msg.setDescription(simpleSubject);
        assertEquals(msg.getDescription(), simpleSubject);

        msg.setDescription(complexSubject, "UTF-8");
        assertEquals(msg.getDescription(), complexSubject);

        msg.setDescription(null);
        assertNull(msg.getDescription());
    }


    public void testGetContentType() throws MessagingException {
        final MimeMessage msg = new MimeMessage(session);
        assertEquals(msg.getContentType(), "text/plain");

        msg.setHeader("Content-Type", "text/xml");
        assertEquals(msg.getContentType(), "text/xml");
    }


    public void testSetText() throws MessagingException {
        MimeMessage msg = new MimeMessage(session);

        msg.setText("Yada, yada");
        msg.saveChanges();
        ContentType type = new ContentType(msg.getContentType());
        assertTrue(type.match("text/plain"));

        msg = new MimeMessage(session);
        msg.setText("Yada, yada", "UTF-8");
        msg.saveChanges();
        type = new ContentType(msg.getContentType());
        assertTrue(type.match("text/plain"));
        assertEquals(type.getParameter("charset"), "UTF-8");

        msg = new MimeMessage(session);
        msg.setText("Yada, yada", "UTF-8", "xml");
        msg.saveChanges();
        type = new ContentType(msg.getContentType());
        assertTrue(type.match("text/xml"));
        assertEquals(type.getParameter("charset"), "UTF-8");
    }



    @Override
    protected void setUp() throws Exception {
        defaultMap = CommandMap.getDefaultCommandMap();
        final MailcapCommandMap myMap = new MailcapCommandMap();
        myMap.addMailcap("text/plain;;    x-java-content-handler=" + MimeMultipartTest.DummyTextHandler.class.getName());
        myMap.addMailcap("multipart/*;;    x-java-content-handler=" + MimeMultipartTest.DummyMultipartHandler.class.getName());
        CommandMap.setDefaultCommandMap(myMap);
        final Properties props = new Properties();
        props.put("mail.user", "tester");
        props.put("mail.host", "apache.org");

        session = Session.getInstance(props);
    }

    @Override
    protected void tearDown() throws Exception {
        CommandMap.setDefaultCommandMap(defaultMap);
    }
}
