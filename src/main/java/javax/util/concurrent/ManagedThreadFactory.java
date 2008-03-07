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

import java.util.concurrent.ThreadFactory;

/**
 * A manageable version of a {@link java.util.concurrent.ThreadFactory}.<p>
 *
 * A ManagedThreadFactory provides a method for creating threads for execution
 * in a managed environment.  Implementations of the ManagedThreadFactory are
 * provided by a Java&TRADE; EE Product Provider.  Application Component Providers
 * use the Java Naming and Directory Interface&trade; (JNDI) to look-up instances of one
 * or more ManagedThreadFactory objects using resource environment references.<p>
 *
 * The Concurrency Utilities for Java&trade; EE specification describes several
 * behaviors that a ManagedThreadFactory can implement.  The Application
 * Component Provider and Deployer identify these requirements and map the
 * resource environment reference appropriately.<p>
 *
 * The Runnable task that is allocated to the new thread using the
 * {@link java.util.concurrent.ThreadFactory#newThread(java.lang.Runnable)} method
 * will run with the application component context of the component instance
 * that created (looked-up) this ManagedThreadFactory instance.<p>
 *
 * The task runs without an explicit transaction (they do not enlist in the application
 * component's transaction).  If a transaction is required, use a
 * {@link javax.transaction.UserTransaction} instance.  A UserTransaction instance is
 * available in JNDI using the name: &QUOT;java:comp/UserTransaction&QUOT<p>
 *
 * Example:<pre>
 * public run() {
 *     // Begin of task
 *     InitialContext ctx = new InitialContext();
 *     UserTransaction ut = (UserTransaction) ctx.lookup("java:comp/UserTransaction");
 *     ut.begin();
 *
 *     // Perform transactional business logic
 *
 *     ut.commit();
 * }</PRE>
 *
 * A ManagedThreadFactory can be used with Java SE ExecutorService implementations directly.<p>
 *
 * Example:<pre>
 * &#47;**
 *  * Create a ThreadPoolExecutor using a ManagedThreadFactory.
 *  * Resource Mappings:
 *  *  type:      javax.util.concurrent.ManagedThreadFactory
 *  *  jndi-name: concurrent/tf/DefaultThreadFactory
 *  *&#47;
 * public ExecutorService getManagedThreadPool() {
 *     InitialContext ctx = new InitialContext();
 *     ManagedThreadFactory tf = (ManagedThreadFactory)
 *         ctx.lookup("java:comp/env/concurrent/tf/DefaultThreadFactory");
 *
 *     // All threads will run as part of this application component.
 *     return new ThreadPoolExecutor(5, 10, 5, TimeUnit.SECONDS,
 *         new ArrayBlockingQueue&LT;Runnable&GT;(10), tf);
 * }
 * </pre>
 */
public interface ManagedThreadFactory extends ThreadFactory {

}
