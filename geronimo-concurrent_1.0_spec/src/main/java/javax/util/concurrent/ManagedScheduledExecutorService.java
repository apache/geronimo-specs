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

import java.util.concurrent.Callable;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;



/**
 * A manageable version of a {@link java.util.concurrent.ScheduledExecutorService}.<p>
 *
 * A ManagedScheduledExecutorService provides methods for submitting delayed or
 * periodic tasks for execution in a managed environment.
 * Implementations of the ManagedScheduledExecutorService are
 * provided by a Java&trade; EE Product Provider.  Application Component Providers
 * use the Java Naming and Directory Interface&trade; (JNDI) to look-up instances of one
 * or more ManagedExecutorService objects using resource environment references.<p>
 *
 * The Concurrency Utilities for Java&trade; EE specification describes several
 * behaviors that a ManagedScheduledExecutorService can implement.  The Application
 * Component Provider and Deployer identify these requirements and map the
 * resource environment reference appropriately.<p>
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
 * Asynchronous tasks are typically submitted to the ManagedScheduledExecutorService using one
 * of the <code>submit</code> or <code>schedule</code>methods, each of which return a {@link java.util.concurrent.Future}
 * instance.  The Future represents the result of the task and can also be used to
 * check if the task is complete or wait for its completion.<p>
 *
 * If the task is cancelled, the result fo the task is a
 * {@link java.util.concurrent.CancellationException} exception.  If the task is unable
 * to run due to start due to a reason other than cancellation, the result is a
 * {@link javax.util.concurrent.AbortedException} exception.  If the task is scheduled
 * with a {@link javax.util.concurrent.Trigger} and the Trigger forces the task to be skipped,
 * the result will be a {@link javax.util.concurrent.SkippedException} exception.<p>
 *
 * Tasks can be scheduled to run periodically using the <code>schedule</code> methods that
 * take a <code>Trigger</code> as an argument and the <code>scheduleAtFixedRate</code> and
 * <code>scheduleWithFixedDelay</code> methods.  The result of the <code>Future</code> will
 * be represented by the currently scheduled or running instance of the task.  Future and past executions
 * of the task are not represented by the Future.  The state of the <code>Future</code> will therefore change
 * and multiple results are expected.<p>
 *
 * For example, if a task is repeat, the lifecycle of the task would be:<br>
 * (Note:  See {@link javax.util.concurrent.ManagedTaskListener} for task lifecycle management details.)<p>
 *
 * <table>
 * <tr><td valign="top"><strong>Sequence</strong></td><td valign="top"><strong>State</strong></td><td valign="top"><strong>Action</strong></td><td valign="top"><strong>Listener</strong></td><td valign="top"><strong>Next state</strong></td></tr>
 * <tr><td valign="top">1A.</td><td valign="top">None</td><td valign="top">submit()</td><td valign="top">taskSubmitted</td><td valign="top">Submitted</td></tr>
 * <tr><td valign="top">2A.</td><td valign="top">Submitted</td><td valign="top">About to call run()</td><td valign="top">taskStarting</td><td valign="top">Started</td></tr>
 * <tr><td valign="top">3A.</td><td valign="top">Started</td><td valign="top">Exit run()</td><td valign="top">taskDone</td><td valign="top">Reschedule</td></tr>
 * <tr><td valign="top">1B.</td><td valign="top">Reschedule</td><td valign="top"></td><td valign="top">taskSubmitted</td><td valign="top">Submitted</td></tr>
 * <tr><td valign="top">2B.</td><td valign="top">Submitted</td><td valign="top">About to call run()</td><td valign="top">taskStarting</td><td valign="top">Started</td></tr>
 * <tr><td valign="top">3B.</td><td valign="top">Started</td><td valign="top">Exit run()</td><td valign="top">taskDone</td><td valign="top">Reschedule</td></tr>
 * </table>
 *
 *
 *
 */
public interface ManagedScheduledExecutorService extends ScheduledExecutorService, ManagedExecutorService
{
    /**
     * Creates and executes a task based on a Trigger.  The Trigger determines
     * when the task should run and how often.
     *
     * @param command the task to execute.
     * @param trigger the trigger that determines when the task should fire.
     * @param taskListener the ManagedTaskListener instance to receive a task's lifecycle events.
     * @return a Future representing pending completion of the task,
     * and whose <tt>get()</tt> method will return <tt>null</tt>
     * upon completion.
     * @throws RejectedExecutionException if task cannot be scheduled
     * for execution.
     * @throws NullPointerException if command is null
     */
    public ScheduledFuture<?> schedule(Runnable command, Trigger trigger, ManagedTaskListener taskListener);

    /**
     * Creates and executes a task based on a Trigger.  The Trigger determines
     * when the task should run and how often.
     *
     * @param callable the function to execute.
     * @param trigger the trigger that determines when the task should fire.
     * @param taskListener the ManagedTaskListener instance to receive a task's lifecycle events.
     * @return a ScheduledFuture that can be used to extract result or cancel.
     * @throws RejectedExecutionException if task cannot be scheduled
     * for execution.
     * @throws NullPointerException if callable is null
     */
    public <V> ScheduledFuture<V> schedule(Callable<V> callable, Trigger trigger, ManagedTaskListener taskListener);

    /**
     * Creates and executes a one-shot action that becomes enabled
     * after the given delay.
     * @param command the task to execute.
     * @param delay the time from now to delay execution.
     * @param unit the time unit of the delay parameter.
     * @param taskListener the ManagedTaskListener instance to receive a task's lifecycle events.
     * @return a Future representing pending completion of the task,
     * and whose <tt>get()</tt> method will return <tt>null</tt>
     * upon completion.
     * @throws RejectedExecutionException if task cannot be scheduled
     * for execution.
     * @throws NullPointerException if command is null
     */
    public ScheduledFuture<?> schedule(Runnable command, long delay,  TimeUnit unit, ManagedTaskListener taskListener);

    /**
     * Creates and executes a ScheduledFuture that becomes enabled after the
     * given delay.
     * @param callable the function to execute.
     * @param delay the time from now to delay execution.
     * @param unit the time unit of the delay parameter.
     * @param taskListener the ManagedTaskListener instance to receive a task's lifecycle events.
     * @return a ScheduledFuture that can be used to extract result or cancel.
     * @throws RejectedExecutionException if task cannot be scheduled
     * for execution.
     * @throws NullPointerException if callable is null
     */
    public <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit, ManagedTaskListener taskListener);

    /**
     * Creates and executes a periodic action that becomes enabled first
     * after the given initial delay, and subsequently with the given
     * period; that is executions will commence after
     * <tt>initialDelay</tt> then <tt>initialDelay+period</tt>, then
     * <tt>initialDelay + 2 * period</tt>, and so on.
     * If any execution of the task
     * encounters an exception, subsequent executions are suppressed.
     * Otherwise, the task will only terminate via cancellation or
     * termination of the executor.
     * @param command the task to execute.
     * @param initialDelay the time to delay first execution.
     * @param period the period between successive executions.
     * @param unit the time unit of the initialDelay and period parameters
     * @param taskListener the ManagedTaskListener instance to receive a task's lifecycle events.
     * @return a Future representing pending completion of the task,
     * and whose <tt>get()</tt> method will throw an exception upon
     * cancellation.
     * @throws RejectedExecutionException if task cannot be scheduled
     * for execution.
     * @throws NullPointerException if command is null
     * @throws IllegalArgumentException if period less than or equal to zero.
     */
    public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay,  long period, TimeUnit unit, ManagedTaskListener taskListener);

    /**
     * Creates and executes a periodic action that becomes enabled first
     * after the given initial delay, and subsequently with the
     * given delay between the termination of one execution and the
     * commencement of the next. If any execution of the task
     * encounters an exception, subsequent executions are suppressed.
     * Otherwise, the task will only terminate via cancellation or
     * termination of the executor.
     * @param command the task to execute.
     * @param initialDelay the time to delay first execution.
     * @param delay the delay between the termination of one
     * execution and the commencement of the next.
     * @param unit the time unit of the initialDelay and delay parameters
     * @param taskListener the ManagedTaskListener instance to receive a task's lifecycle events.
     * @return a Future representing pending completion of the task,
     * and whose <tt>get()</tt> method will throw an exception upon
     * cancellation.
     * @throws RejectedExecutionException if task cannot be scheduled
     * for execution.
     * @throws NullPointerException if command is null
     * @throws IllegalArgumentException if delay less than or equal to zero.
     */
    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay,  long delay, TimeUnit unit, ManagedTaskListener taskListener);
}
