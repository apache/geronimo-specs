/**
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package javax.util.concurrent;

import java.util.Map;

/**
 * The ContextService provides methods for creating <i>contextual dynamic proxy</i>
 * objects.
 * <p>
 *
 * The proxy objects follow the same rules as defined for the
 * {@link java.lang.reflect.Proxy} class with the following additions:
 * <ul>
 * <li>The proxy instance will retain the context of the application
 * container's thread (creator's context).
 * <li>The proxy instance will implement all of the interfaces specified on the
 * <code>createContextObject</code> methods.
 * <li>All interface method invocations on a proxy instance run in the
 * creator's context.
 * <li>Invocation of the <code>hashCode</code>, <code>equals</code>, and
 * <code>toString</code> methods declared in <code>java.lang.Object</code>
 * on a sproxy instance will not run in the creator's context.
 * <li>The proxy instance must implement <code>java.io.Serializable</code>.
 * <li>The proxied object instance must implement
 * <code>java.io.Serializable</code> if the proxy instance is serialized.
 * <li>Context properties can be stored with the proxy instance. Custom
 * property keys must not begin with "ctxsvc.".
 * <li>Context properties are to be used for controlling how various contextual
 * information is retreived and applied to the thread. Although application
 * components can store arbitrary property keys and values, it is not
 * recommended. Java EE product providers may impose limits to the size of the
 * keys and values.
 * <li>Context property keys and values must all be of type
 * <code>java.lang.String</code>. Use of the <code>put</code> and
 * <code>putAll</code> methods on the <code>java.util.Hashtable</code>
 * superclass are discouraged.
 * </ul>
 *
 */
public interface ContextService {
    /**
     * A contextual object property that disables the normal transaction
     * suspension and UserTransaction access from the proxied methods.
     * <p>
     *
     * If "false" (the default if unspecified), any transaction that is
     * currently active on the thread will be suspended and a UserTransaction
     * (accessible in the local JNDI namespace as "java:comp/UserTransaction")
     * will be available. When the proxied method returns the original
     * transaction is restored.
     * <p>
     *
     * If "true", the proxied method will run within the transaction (if any) of
     * the current thread. A UserTransaction will only be available if the the
     * container thread (for example, a Servlet or Bean Managed Transaction
     * EJB).
     */
    public String USE_PARENT_TRANSACTION = "ctxsvc.useparenttran";

    /**
     * Creates a new contextual object proxy for the input object instance.
     * <p>
     *
     * Each method invocation will have the context of the application component
     * instance that created the context object.
     * <p>
     *
     * The contextual object is useful when developing or using Java SE
     * threading mechanisms spraying events to other component instances or
     * communicating with component instances on different Java processes.
     * <p>
     *
     * If the application component that created the proxy is started or
     * deployed, all methods on reflected interfaces will throw a
     * <code>java.lang.IllegalState</code> exception.
     * <p>
     *
     * For example, to call a normal Runnable with the correct context using a
     * Java&trade; ExecutorService:
     * <P>
     *
     * <pre>
     * public class MyRunnable implements Runnable {
     *     public void run() {
     *         System.out.println(&quot;MyRunnable.run with J2EE Context available.&quot;);
     *     }
     * }
     *
     * InitialContext ctx = new InitialContext();
     *
     * ThreadFactory threadFactory = (ThreadFactory) ctx
     *         .lookup(&quot;java:comp/env/concurrent/ThreadFactory&quot;);
     *
     * ContextService ctxService = (ContextService) ctx
     *         .lookup(&quot;java:comp/env/concurrent/ContextService&quot;);
     *
     * Object rProxy = ctxService.createContextObject(myRunnableInstance,
     *         new Class[] { Runnable.class });
     *
     * ExecutorService exSvc = Executors.newThreadPool(10, threadFactory);
     *
     * Future f = exSvc.submit((Runnable) rProxy);
     * </pre>
     *
     * @param instance
     *            the instance of the object to proxy.
     * @param interfaces
     *            the interfaces that the proxy should implement.
     * @return a proxy for the input object that implements all of the specified
     *         interfaces.
     * @throws IllegalArgumentException
     *             if the Class does not have an interface or there is not an
     *             accessible default constructor.
     */
    public Object createContextObject(Object instance, Class<?>[] interfaces);

    /**
     * Creates a new contextual object proxy for the input object instance.
     * <p>
     *
     * The contextual object is useful when developing or using Java SE
     * threading mechanisms spraying events to other component instances or
     * communicating with component instances on different Java processes.
     * <p>
     *
     * If the application component that created the proxy is started or
     * deployed, all methods on reflected interfaces will throw a
     * <code>java.lang.IllegalState</code> exception.
     * <p>
     *
     * This method accepts a {@link Properties} object which allows the
     * contextual object creator to define what contexts or behaviors to capture
     * when creating the contextual object. The specified properties will remain
     * with the contextual object until the properties are updated or removed
     * using the {@link #setProperties(Object, Properties)} method.
     * <p>
     *
     * For example, to call a Message Driven Bean (MDB) with the sender's
     * context, but within the MDB's transaction:
     * <P>
     *
     * <pre>
     *      public class MyServlet ... {
     *        public void doPost() throws NamingException, JMSException {
     *            InitialContext ctx = new InitialContext();
     *
     *            // Get the ContextService that only propagates
     *            // security context.
     *            ContextService ctxSvc = (ContextService)
     *                ctx.lookup(&quot;java:comp/env/SecurityContext&quot;);
     *
     *            // Set any custom context data.
     *            Properties ctxProps = new Properties();
     *            ctxProps.setProperty(&quot;vendor_a.security.tokenexpiration&quot;, &quot;15000&quot;);
     *
     *            ProcessMessage msgProcessor =
     *                (ProcessMessage) ctxSvc.createContextObject(new MessageProcessor(),
     *                new Class[]{ProcessMessage.class},
     *                ctxProps);
     *
     *            ConnectionFactory cf = (ConnectionFactory)
     *                 ctx.lookup(&quot;java:comp/env/MyTopicConnectionFactory&quot;);
     *            Destination dest = (Destination) ctx.lookup(&quot;java:comp/env/MyTopic&quot;);
     *            Connection con = cf.createConnection();
     *
     *            Session session = con.createSession(true, Session.AUTO_ACKNOWLEDGE);
     *            MessageProducer producer = session.createProducer(dest);
     *
     *            Message msg = session.createObjectMessage((Serializable)msgProcessor);
     *            producer.send(dest, msg);
     *            ...
     *
     *        }
     *
     *      public class MyMDB ... {
     *        public void onMessage(Message msg) {
     *            // Get the ProcessMessage context object from the message.
     *            ObjectMessage omsg = (ObjectMessage)msg;
     *            ProcessMessage msgProcessor = (ProcessMessage)omsg.getObject();
     *
     *            // Update the context object and verify that the processMessage()
     *            // method runs inside the current transaction.  If we have a failure,
     *            // we don't want to consume the message.
     *            InitialContext ctx = new InitialContext();
     *            ContextService ctxSvc = (ContextService)
     *                ctx.lookup(&quot;java:comp/env/SecurityContext&quot;);
     *            Properties ctxProps = ctxSvc.getProperties(msgProcessor);
     *            ctxProps.setProperty(ContextService.USE_PARENT_TRANSACTION, &quot;true&quot;);
     *            ctxSvc.setProperties(msgProcessor, ctxProps);
     *
     *            // Process the message in the specified context.
     *            msgProcessor.processMessage(msg);
     *        }
     *      }
     *
     *      public interface  ProcessMessage {
     *          public void processMessage(Message msg);
     *      }
     *
     *      public class MessageProcessor implements ProcessMessage, Serializable {
     *          public void processMessage(Message msg) {
     *              // Process the message with the application container
     *              // context that sent the message.
     *
     *          }
     *      }
     * </pre>
     *
     * @param instance
     *            the instance of the object to proxy.
     * @param interfaces
     *            the interfaces that the proxy should implement.
     * @param contextProperties
     *            the properties to use when creating and running the context
     *            object.
     * @return a proxy for the input object that implements all of the specified
     *         interfaces.
     * @throws IllegalArgumentException
     *             if the Class does not have an interface or there is not an
     *             accessible default constructor.
     * @throws ClassCastException
     *             thrown if one of the keys or values in the specified
     *             Properties object are not of type String.
     */
    public Object createContextObject(Object instance, Class<?>[] interfaces,
            Map<String, String> contextProperties);

    /**
     * Sets the properties on the context proxy instance.
     * <p>
     *
     * All property keys and values must be strings. Storing other class types
     * may result in a <code>java.lang.ClassCastException</code>.
     *
     * @param contextObject
     *            the contextual proxy instance to set the properties.
     * @param contextProperties
     *            the properties to use when running the context object. Specify
     *            an empty Properties object to erase all current properties.
     * @throws IllegalArgumentException
     *             thrown if the input contextObject is not a valid contextual
     *             object proxy created with the
     *             <code>createContextObject</code> method.
     * @throws ClassCastException
     *             thrown if one of the keys or values in the specified
     *             Properties object are not of type String.
     */
    public void setProperties(Object contextObject, Map<String, String> contextProperties);

    /**
     * Gets the current properties on the context proxy instance.
     *
     * @param contextObject
     *            the contextual proxy instance to set the properties.
     * @return the current context object properties
     * @throws IllegalArgumentException
     *             thrown if the input contextObject is not a valid contextual
     *             object proxy created with the
     *             <code>createContextObject</code> method.
     */
    public Map<String, String> getProperties(Object contextObject);
}
