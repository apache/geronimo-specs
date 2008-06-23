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

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;



/**
 * A manageable version of a {@link java.util.concurrent.ExecutorService}.<p>
 *
 * A ManagedExecutorService provides methods for submitting tasks for execution
 * in a managed environment.  Implementations of the ManagedExecutorService are
 * provided by a Java&trade; EE Product Provider.  Application Component Providers
 * use the Java Naming and Directory Interface&trade; (JNDI) to look-up instances of one
 * or more ManagedExecutorService objects using resource environment references.<p>
 *
 * The Concurrency Utilities for Java&trade; EE specification describes several
 * behaviors that a ManagedExecutorService can implement.  The Application
 * Component Provider and Deployer identify these requirements and map the
 * resource environment reference appropriately.<p>
 *
 * The most common uses for a ManagedExecutorService is to run short-duration asynchronous
 * tasks in the local JVM from a container such as an Enterprise
 * JavaBean&trade; (EJB&trade;) or servlet.  Use a managed ThreadFactory for long-running
 * daemon tasks.<p>
 *
 * Tasks run within the application component context that either
 * submitted the task or created the ManagedExecutorService instance (server-managed or component-managed).
 * All tasks run without an explicit transaction (they do not enlist in the application
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
 * Asynchronous tasks are typically submitted to the ManagedExecutorService using one
 * of the <code>submit</code> methods, each of which return a {@link java.util.concurrent.Future}
 * instance.  The <code>Future</code> represents the result of the task and can also be used to
 * check if the task is complete or wait for its completion.<p>
 *
 * If the task is cancelled, the result fo the task is a
 * {@link java.util.concurrent.CancellationException} exception.  If the task is unable
 * to run due to start due to a reason other than cancellation, the result is a
 * {@link javax.util.concurrent.AbortedException} exception.<p>
 *
 * Example:<pre>
 * &#47;**
 *  * Retrieve all accounts from several account databases in parallel.
 *  * Resource Mappings:
 *  *  type:      javax.util.concurrent.ManagedExecutorService
 *  *  jndi-name: mes/ThreadPool
 *  *  attributes:
 *  *    Run Location = Local
 *  *&#47;
 * public List<Account> getAccounts(long accountId) {
 *     try {
 *         javax.naming.InitialContext ctx = new InitialContext();
 *         <b>ManagedExecutorService mes = (ManagedExecutorService)
 *             ctx.lookup("java:comp/env/concurrent/ThreadPool");</b>
 *
 *         // Create a set of tasks to perform the account retrieval.
 *         ArrayList<Callable<Account>> retrieverTasks = new ArrayList<Callable<Account>>();
 *         retrieverTasks.add(new EISAccountRetriever());
 *         retrieverTasks.add(new RDBAccountRetriever());
 *
 *         // Submit the tasks to the thread pool and wait for them
 *         // to complete (successfully or otherwise).
 *         <b>List<Future<Account>> taskResults= mes.invokeAll(retrieverTasks);</b>
 *
 *         // Retrieve the results from the resulting Future list.
 *         ArrayList<Account> results = new ArrayList<Account>();
 *         for(Future<Account> taskResult : taskResults) {
 *             try {
 *                 <b>results.add(taskResult.get());</b>
 *             } catch (ExecutionException e) {
 *                 Throwable cause = e.getCause();
 *                 // Handle the AccountRetrieverError.
 *             }
 *         }
 *
 *         return results;
 *
 *     } catch (NamingException e) {
 *         // Throw exception for fatal error.
 *     } catch (InterruptedException e) {
 *         // Throw exception for shutdown or other interrupt condition.
 *     }
 *   }
 *
 * }
 *
 * public class EISAccountRetriever implements Callable<Account> {
 *     public Account call() {
 *         // Connect to our eis system and retrieve the info for the account.
 *         //...
 *         return null;
 *     }
 * }
 *
 * public class RDBAccountRetriever implements Callable<Account> {
 *     public Account call() {
 *         // Connect to our database and retrieve the info for the account.
 *         //...
 *     }
 * }
 *
 * public class Account {
 *     // Some account data...
 * }
 *
 *</pre>
 */
public interface ManagedExecutorService extends ExecutorService
{
    /**
     * This method has the same semantics as {@link ExecutorService#submit(java.lang.Runnable)}
     * but also includes the ability to be notified when the task's lifecycle changes.
     *
     * @param task the task to submit
     * @param taskListener the ManagedTaskListener instance to receive a task's lifecycle events.
     * @return a Future representing pending completion of the task,
     * and whose <tt>get()</tt> method will return <tt>null</tt>
     * upon completion.
     * @throws RejectedExecutionException if task cannot be scheduled
     * for execution
     * @throws NullPointerException if task null
     */
    Future<?> submit(Runnable task, ManagedTaskListener taskListener);

    /**
     * This method has the same semantics as {@link ExecutorService#submit(java.lang.Runnable, T)}
     * but also includes the ability to be notified when the task's lifecycle changes.
     *
     * @param task the task to submit
     * @param taskListener the ManagedTaskListener instance to receive a task's lifecycle events.
     * @param result the result to return
     * @return a Future representing pending completion of the task,
     * and whose <tt>get()</tt> method will return the given result
     * upon completion.
     * @throws RejectedExecutionException if task cannot be scheduled
     * for execution
     * @throws NullPointerException if task null
     */
    <T> Future<T> submit(Runnable task, T result, ManagedTaskListener taskListener);

    /**
     * This method has the same semantics as {@link ExecutorService#submit(java.util.concurrent.Callable)}
     * but also includes the ability to be notified when the task's lifecycle changes.
     *
     * @param task the task to submit
     * @param taskListener the ManagedTaskListener instance to receive a task's lifecycle events.
     * @return a Future representing pending completion of the task
     * @throws RejectedExecutionException if task cannot be scheduled
     * for execution
     * @throws NullPointerException if task null
     */
    <T> Future<T> submit(Callable<T> task, ManagedTaskListener taskListener);

    /**
     * This method has the same semantics as {@link ExecutorService#invokeAll(java.util.Collection)}
     * but also includes the ability to be notified when each task's lifecycle changes.
     *
     * @param tasks the collection of tasks
     * @param taskListener the ManagedTaskListener instance to receive a task's lifecycle events.
     * @return A list of Futures representing the tasks, in the same
     * sequential order as produced by the iterator for the given task
     * list, each of which has completed.
     * @throws InterruptedException if interrupted while waiting, in
     * which case unfinished tasks are cancelled.
     * @throws NullPointerException if tasks or any of its elements are <tt>null</tt>
     * @throws RejectedExecutionException if any task cannot be scheduled
     * for execution
     */
    <T> List<Future<T>> invokeAll(Collection<Callable<T>> tasks, ManagedTaskListener taskListener)
        throws InterruptedException;

    /**
     * This method has the same semantics as
     * {@link ExecutorService#invokeAll(java.util.Collection, long, java.util.concurrent.TimeUnit)}
     * but also includes the ability to be notified when each task's lifecycle changes.
     *
     * @param tasks the collection of tasks
     * @param timeout the maximum time to wait
     * @param unit the time unit of the timeout argument
     * @param taskListener the ManagedTaskListener instance to receive a task's lifecycle events.
     * @return A list of Futures representing the tasks, in the same
     * sequential order as produced by the iterator for the given
     * task list. If the operation did not time out, each task will
     * have completed. If it did time out, some of these tasks will
     * not have completed.
     * @throws InterruptedException if interrupted while waiting, in
     * which case unfinished tasks are cancelled.
     * @throws NullPointerException if tasks, any of its elements, or
     * unit are <tt>null</tt>
     * @throws RejectedExecutionException if any task cannot be scheduled
     * for execution
     */
    <T> List<Future<T>> invokeAll(Collection<Callable<T>> tasks,
                                    long timeout, TimeUnit unit, ManagedTaskListener taskListener)
        throws InterruptedException;

    /**
     * This method has the same semantics as
     * {@link ExecutorService#invokeAny(java.util.Collection)}
     * but also includes the ability to be notified when each task's lifecycle changes.
     *
     * Executes the given tasks, returning the result
     * of one that has completed successfully (i.e., without throwing
     * an exception), if any do. Upon normal or exceptional return,
     * tasks that have not completed are cancelled.
     * The results of this method are undefined if the given
     * collection is modified while this operation is in progress.
     * @param tasks the collection of tasks
     * @param taskListener the ManagedTaskListener instance to receive a task's lifecycle events.
     * @return The result returned by one of the tasks.
     * @throws InterruptedException if interrupted while waiting
     * @throws NullPointerException if tasks or any of its elements
     * are <tt>null</tt>
     * @throws IllegalArgumentException if tasks empty
     * @throws ExecutionException if no task successfully completes
     * @throws RejectedExecutionException if tasks cannot be scheduled
     * for execution
     */
    <T> T invokeAny(Collection<Callable<T>> tasks, ManagedTaskListener taskListener)
        throws InterruptedException, ExecutionException;

    /**
     * This method has the same semantics as
     * {@link ExecutorService#invokeAny(java.util.Collection, long, java.util.concurrent.TimeUnit)}
     * but also includes the ability to be notified when each task's lifecycle changes.
     *
     * @param tasks the collection of tasks
     * @param timeout the maximum time to wait
     * @param unit the time unit of the timeout argument
     * @return The result returned by one of the tasks.
     * @throws InterruptedException if interrupted while waiting
     * @throws NullPointerException if tasks, any of its elements, or
     * unit are <tt>null</tt>
     * @throws TimeoutException if the given timeout elapses before
     * any task successfully completes
     * @throws ExecutionException if no task successfully completes
     * @throws RejectedExecutionException if tasks cannot be scheduled
     * for execution
     */
    <T> T invokeAny(Collection<Callable<T>> tasks,
                    long timeout, TimeUnit unit, ManagedTaskListener taskListener)
        throws InterruptedException, ExecutionException, TimeoutException;


}
