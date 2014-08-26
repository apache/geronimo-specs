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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

import javax.mail.event.TransportEvent;
import javax.mail.event.TransportListener;

/**
 * Abstract class modeling a message transport.
 *
 * @version $Rev$ $Date$
 */
public abstract class Transport extends Service {
    /**
     * Send a message to all recipient addresses the message contains (as returned by {@link Message#getAllRecipients()})
     * using message transports appropriate for each address. Message addresses are checked during submission,
     * but there is no guarantee that the ultimate address is valid or that the message will ever be delivered.
     * <p/>
     * {@link Message#saveChanges()} will be called before the message is actually sent.
     *
     * @param message the message to send
     * @throws MessagingException if there was a problem sending the message
     */
    public static void send(final Message message) throws MessagingException {
        send(message, message.getAllRecipients());
    }

    /**
     * Send a message to all addresses provided irrespective of any recipients contained in the message, 
     * using message transports appropriate for each address. Message addresses are checked during submission,
     * but there is no guarantee that the ultimate address is valid or that the message will ever be delivered.
     * <p/>
     * {@link Message#saveChanges()} will be called before the message is actually sent.
     *
     * @param message   the message to send
     * @param addresses the addesses to send to
     * @throws MessagingException if there was a problem sending the message
     */
    public static void send(final Message message, final Address[] addresses) throws MessagingException {
        sendInternal(message, addresses, null, null);
    }
    
    
    private static void sendInternal(final Message message, final Address[] addresses, final String user, final String password) throws MessagingException {
        
        if (addresses == null || addresses.length == 0) {
            throw new SendFailedException("No recipient addresses");
        }
        
        final Session session = message.session;
        final Map<Transport, List<Address>> msgsByTransport = new HashMap<Transport, List<Address>>();
        for (int i = 0; i < addresses.length; i++) {
            final Address address = addresses[i];
            final Transport transport = session.getTransport(address);
            List<Address> addrs = msgsByTransport.get(transport);
            if (addrs == null) {
                addrs = new ArrayList<Address>();
                msgsByTransport.put(transport, addrs);
            }
            addrs.add(address);
        }

        message.saveChanges();

        // Since we might be sending to multiple protocols, we need to catch and process each exception
        // when we send and then throw a new SendFailedException when everything is done.  Unfortunately, this
        // also means unwrapping the information in any SendFailedExceptions we receive and building
        // composite failed list.
        MessagingException chainedException = null;
        final List<Address> sentAddresses = new ArrayList<Address>();
        final List<Address> unsentAddresses = new ArrayList<Address>();
        final List<Address> invalidAddresses = new ArrayList<Address>();


        for (final Iterator<Entry<Transport, List<Address>>> i = msgsByTransport.entrySet().iterator(); i.hasNext();) {
            final Entry<Transport, List<Address>> entry = i.next();
            final Transport transport = entry.getKey();
            final List<Address> addrs = entry.getValue();
            try {
                // we MUST connect to the transport before attempting to send.
                if(user != null) {
                    transport.connect(user, password);
                } else {
                    transport.connect();
                }
                transport.sendMessage(message, addrs.toArray(new Address[addrs.size()]));
                // if we have to throw an exception because of another failure, these addresses need to
                // be in the valid list.  Since we succeeded here, we can add these now.
                sentAddresses.addAll(addrs);
            } catch (final SendFailedException e) {
                // a true send failure.  The exception contains a wealth of information about
                // the failures, including a potential chain of exceptions explaining what went wrong.  We're
                // going to send a new one of these, so we need to merge the information.

                // add this to our exception chain
                if (chainedException == null) {
                    chainedException = e;
                }
                else {
                    chainedException.setNextException(e);
                }

                // now extract each of the address categories from
                Address[] exAddrs = e.getValidSentAddresses();
                if (exAddrs != null) {
                    for (int j = 0; j < exAddrs.length; j++) {
                        sentAddresses.add(exAddrs[j]);
                    }
                }

                exAddrs = e.getValidUnsentAddresses();
                if (exAddrs != null) {
                    for (int j = 0; j < exAddrs.length; j++) {
                        unsentAddresses.add(exAddrs[j]);
                    }
                }

                exAddrs = e.getInvalidAddresses();
                if (exAddrs != null) {
                    for (int j = 0; j < exAddrs.length; j++) {
                        invalidAddresses.add(exAddrs[j]);
                    }
                }

            } catch (final MessagingException e) {
                // add this to our exception chain
                if (chainedException == null) {
                    chainedException = e;
                }
                else {
                    chainedException.setNextException(e);
                }
            }
            finally {
                transport.close();
            }
        }

        // if we have an exception chain then we need to throw a new exception giving the failure
        // information.
        if (chainedException != null) {
            // if we're only sending to a single transport (common), and we received a SendFailedException
            // as a result, then we have a fully formed exception already.  Rather than wrap this in another
            // exception, we can just rethrow the one we have.
            if (msgsByTransport.size() == 1 && chainedException instanceof SendFailedException) {
                throw chainedException;
            }

            // create our lists for notification and exception reporting from this point on.
            final Address[] sent = sentAddresses.toArray(new Address[0]);
            final Address[] unsent = unsentAddresses.toArray(new Address[0]);
            final Address[] invalid = invalidAddresses.toArray(new Address[0]);

            throw new SendFailedException("Send failure", chainedException, sent, unsent, invalid);
        }
    }

    
    /**
     * Send a message.  The message will be sent to all recipient
     * addresses specified in the message (as returned from the
     * Message method getAllRecipients).
     * The send method calls the saveChanges
     * method on the message before sending it. 
     *
     * Use the specified user name and password to authenticate to
     * the mail server.
     *
     * @param   msg the message to send
     * @param   user    the user name
     * @param   password this user's password
     * @exception   SendFailedException if the message could not
     *          be sent to some or any of the recipients.
     * @exception   MessagingException
     * @see     Message#saveChanges
     * @see             #send(Message)
     * @see     javax.mail.SendFailedException
     * @since       JavaMail 1.5
     */
    public static void send(final Message msg,
        final String user, final String password) throws MessagingException {
        
        send(msg, msg.getAllRecipients(), user, password);
    }

    /**
     * Send the message to the specified addresses, ignoring any
     * recipients specified in the message itself. The
     * send method calls the saveChanges
     * method on the message before sending it. 
     *
     * Use the specified user name and password to authenticate to
     * the mail server.
     *
     * @param   msg the message to send
     * @param   addresses the addresses to which to send the message
     * @param   user    the user name
     * @param   password this user's password
     * @exception   SendFailedException if the message could not
     *          be sent to some or any of the recipients.
     * @exception   MessagingException
     * @see     Message#saveChanges
     * @see             #send(Message)
     * @see     javax.mail.SendFailedException
     * @since       JavaMail 1.5
     */
    public static void send(final Message msg, final Address[] addresses,
        final String user, final String password) throws MessagingException {
        
        sendInternal(msg, addresses, user, password);
    
    }
    
    
    /**
     * Constructor taking Session and URLName parameters required for {@link Service#Service(Session, URLName)}.
     *
     * @param session the Session this transport is for
     * @param name    the location this transport is for
     */
    public Transport(final Session session, final URLName name) {
        super(session, name);
    }

    /**
     * Send a message to the supplied addresses using this transport; if any of the addresses are
     * invalid then a {@link SendFailedException} is thrown. Whether the message is actually sent
     * to any of the addresses is undefined.
     * <p/>
     * Unlike the static {@link #send(Message, Address[])} method, {@link Message#saveChanges()} is
     * not called. A {@link TransportEvent} will be sent to registered listeners once the delivery
     * attempt has been made.
     *
     * @param message   the message to send
     * @param addresses list of addresses to send it to
     * @throws SendFailedException if the send failed
     * @throws MessagingException  if there was a problem sending the message
     */
    public abstract void sendMessage(Message message, Address[] addresses) throws MessagingException;

    private final Vector<TransportListener> transportListeners = new Vector<TransportListener>();

    public void addTransportListener(final TransportListener listener) {
        transportListeners.add(listener);
    }

    public void removeTransportListener(final TransportListener listener) {
        transportListeners.remove(listener);
    }

    protected void notifyTransportListeners(final int type, final Address[] validSent, final Address[] validUnsent, final Address[] invalid, final Message message) {
        queueEvent(new TransportEvent(this, type, validSent, validUnsent, invalid, message), transportListeners);
    }
}                                                                                            
