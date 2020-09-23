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

/**
 * @version $Rev$ $Date$
 */
public interface UIDFolder {
    /**
     * A special value than can be passed as the <code>end</code> parameter to
     * {@link Folder#getMessages(int, int)} to indicate the last message in this folder.
     */
    public static final long LASTUID = -1;

    /**
     * The largest value possible for a UID, a 32-bit unsigned integer.
     * This can be used to fetch all new messages by keeping track of the
     * last UID that was seen and using:
     * <blockquote><pre>
     *
     * 	Folder f = store.getFolder("whatever");
     *	UIDFolder uf = (UIDFolder)f;
     *	Message[] newMsgs =
     *		uf.getMessagesByUID(lastSeenUID + 1, UIDFolder.MAXUID);
     *
     * </pre></blockquote><p>
     *
     * @since JavaMail 1.6
     */
    public static final long MAXUID = 0xffffffffL;

    /**
     * Get the UID validity value for this Folder.
     * 
     * @return The current UID validity value, as a long. 
     * @exception MessagingException
     */
    public abstract long getUIDValidity() throws MessagingException;

    /**
     * Retrieve a message using the UID rather than the 
     * message sequence number.  Returns null if the message
     * doesn't exist.
     * 
     * @param uid    The target UID.
     * 
     * @return the Message object.  Returns null if the message does
     *         not exist.
     * @exception MessagingException
     */
    public abstract Message getMessageByUID(long uid)
            throws MessagingException;

    /**
     * Get a series of messages using a UID range.  The 
     * special value LASTUID can be used to mark the 
     * last available message.
     * 
     * @param start  The start of the UID range.
     * @param end    The end of the UID range.  The special value
     *               LASTUID can be used to request all messages up
     *               to the last UID.
     * 
     * @return An array containing all of the messages in the 
     *         range.
     * @exception MessagingException
     */
    public abstract Message[] getMessagesByUID(long start, long end)
            throws MessagingException;

    /**
     * Retrieve a set of messages by explicit UIDs.  If 
     * any message in the list does not exist, null 
     * will be returned for the corresponding item.
     * 
     * @param ids    An array of UID values to be retrieved.
     * 
     * @return An array of Message items the same size as the ids
     *         argument array.  This array will contain null
     *         entries for any UIDs that do not exist.
     * @exception MessagingException
     */
    public abstract Message[] getMessagesByUID(long[] ids)
            throws MessagingException;

    /**
     * Retrieve the UID for a message from this Folder.
     * The argument Message MUST belong to this Folder
     * instance, otherwise a NoSuchElementException will 
     * be thrown.
     * 
     * @param message The target message.
     * 
     * @return The UID associated with this message.
     * @exception MessagingException
     */
    public abstract long getUID(Message message) throws MessagingException;

    /**
     * Returns the predicted UID that will be assigned to the
     * next message that is appended to this folder.
     * Messages might be appended to the folder after this value
     * is retrieved, causing this value to be out of date.
     * This value might only be updated when a folder is first opened.
     * Note that messages may have been appended to the folder
     * while it was open and thus this value may be out of
     * date. <p>
     *
     * If the value is unknown, -1 is returned.  <p>
     *
     * @return  the UIDNEXT value, or -1 if unknown
     * @exception       MessagingException for failures
     * @since   JavaMail 1.6
     */
    public long getUIDNext() throws MessagingException;

    /**
     * Special profile item used for fetching UID information.
     */
    public static class FetchProfileItem extends FetchProfile.Item {
        public static final FetchProfileItem UID = new FetchProfileItem("UID");

        protected FetchProfileItem(final String name) {
            super(name);
        }
    }
}
