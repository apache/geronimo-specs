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

package javax.mail;

import javax.mail.internet.MimeMessage;

public class TestData {
    public static Store getTestStore() {
        return new Store(
            getTestSession(),
            new URLName("http://alex@test.com")) {
            @Override
            public Folder getDefaultFolder() throws MessagingException {
                return getTestFolder();
            }
            @Override
            public Folder getFolder(final String name) throws MessagingException {
                if (name.equals("test")) {
                    return getTestFolder();
                } else {
                    return null;
                }
            }
            @Override
            public Folder getFolder(final URLName name) throws MessagingException {
                return getTestFolder();
            }
        };
    }
    public static Session getTestSession() {
        return Session.getDefaultInstance(System.getProperties());
    }
    public static Folder getTestFolder() {
        return new Folder(getTestStore()) {
            @Override
            public void appendMessages(final Message[] messages)
                throws MessagingException {
            }
            @Override
            public void close(final boolean expunge) throws MessagingException {
            }
            @Override
            public boolean create(final int type) throws MessagingException {
                return false;
            }
            @Override
            public boolean delete(final boolean recurse) throws MessagingException {
                return false;
            }
            @Override
            public boolean exists() throws MessagingException {
                return false;
            }
            @Override
            public Message[] expunge() throws MessagingException {
                return null;
            }
            @Override
            public Folder getFolder(final String name) throws MessagingException {
                return null;
            }
            @Override
            public String getFullName() {
                return null;
            }
            @Override
            public Message getMessage(final int id) throws MessagingException {
                return null;
            }
            @Override
            public int getMessageCount() throws MessagingException {
                return 0;
            }
            @Override
            public String getName() {
                return null;
            }
            @Override
            public Folder getParent() throws MessagingException {
                return null;
            }
            @Override
            public Flags getPermanentFlags() {
                return null;
            }
            @Override
            public char getSeparator() throws MessagingException {
                return 0;
            }
            @Override
            public int getType() throws MessagingException {
                return 0;
            }
            @Override
            public boolean hasNewMessages() throws MessagingException {
                return false;
            }
            @Override
            public boolean isOpen() {
                return false;
            }
            @Override
            public Folder[] list(final String pattern) throws MessagingException {
                return null;
            }
            @Override
            public void open(final int mode) throws MessagingException {
            }
            @Override
            public boolean renameTo(final Folder newName) throws MessagingException {
                return false;
            }
        };
    }
    public static Transport getTestTransport() {
        return new Transport(
            getTestSession(),
            new URLName("http://host.name")) {
            @Override
            public void sendMessage(final Message message, final Address[] addresses)
                throws MessagingException {
            }
        };
    }
    public static Message getMessage() {
        return new MimeMessage(getTestFolder(), 1) {
        };
    }
}
