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

import java.util.concurrent.Future;



/**
 * A ManagedTaskListener is used to monitor the state of a task's Future.
 * It can be registered with a {@link javax.util.concurrent.ManagedExecutorService} using the
 * <code>submit</code> methods and will be invoked when the state of the Future changes.
 * Each listener method will run with the same context in which the task runs.
 * The listener becomes a Contextual Task.  All listeners run without an explicit transaction (they do not enlist in the application
 * component's transaction).  If a transaction is required, use a
 * {@link javax.transaction.UserTransaction} instance.<p>
 *
 * Each listener instance will be invoked within the same process in which the listener was registered.
 * If a single listener is submitted to multiple ManagedExecutorService instances, the
 * listener object may be invoked concurrently by multiple threads.<p>
 *
 * Each listener method supports a minimum quality of service of at-most-once.  A listener is not
 * guaranteed to be invoked due to a process failure or termination.
 *
 * <b>State Transition Diagram</b>
 * The following state transition figure and tables describe
 * the possible task lifecycle events that can occur when a
 * ManagedTaskListener is associated with a task.  Each method is invoked
 * when the state of the future moves from one state to another.<p>
 * <img src="doc-files/TaskListener_StateDiagram.gif"><p>
 *
 * <b>A. The task runs normally:</b>
 * <table>
 * <tr><td valign="top"><strong><u>Sequence</u></strong></td><td valign="top"><strong><u>State</u></strong></td><td valign="top"><strong><u>Action</u></strong></td><td valign="top"><strong><u>Listener</u></strong></td><td valign="top"><strong><u>Next state</u></strong></td></tr>
 * <tr><td valign="top">1.</td><td valign="top">None</td><td valign="top">submit()</td><td valign="top">taskSubmitted</td><td valign="top">Submitted</td></tr>
 * <tr><td valign="top">2.</td><td valign="top">Submitted</td><td valign="top">About to call run()</td><td valign="top">taskStarting</td><td valign="top">Started</td></tr>
 * <tr><td valign="top">3.</td><td valign="top">Started</td><td valign="top">Exit run()</td><td valign="top">taskDone</td><td valign="top">Done</td></tr>
 * </table><p>
 *
 * <b>B. The task is cancelled during taskSubmitted():</b>
 * <table>
 * <tr><td valign="top"><strong><u>Sequence</u></strong></td><td valign="top"><strong><u>State</u></strong></td><td valign="top"><strong><u>Action</u></strong></td><td valign="top"><strong><u>Listener</u></strong></td><td valign="top"><strong><u>Next state</u></strong></td></tr>
 * <tr><td valign="top">1.</td><td valign="top">None</td><td valign="top">submit()</td><td valign="top">taskSubmitted<br>Future is cancelled.</td><td valign="top">Cancelling</td></tr>
 * <tr><td valign="top">2.</td><td valign="top">Cancelling</td><td valign="top">&nbsp;</td><td valign="top">taskAborted</td><td valign="top">Cancelled</td></tr>
 * <tr><td valign="top">3.</td><td valign="top">Cancelled</td><td valign="top">&nbsp;</td><td valign="top">taskDone</td><td valign="top">Done</td></tr>
 * </table><p>
 *
 * <b>C. The task is cancelled or aborted after submitted, but before started:</b>
 * <table>
 * <tr><td valign="top"><strong><u>Sequence</u></strong></td><td valign="top"><strong><u>State</u></strong></td><td valign="top"><strong><u>Action</u></strong></td><td valign="top"><strong><u>Listener</u></strong></td><td valign="top"><strong><u>Next state</u></strong></td></tr>
 * <tr><td valign="top">1.</td><td valign="top">None</td><td valign="top">submit()</td><td valign="top">taskSubmitted</td><td valign="top">Submitted</td></tr>
 * <tr><td valign="top">2.</td><td valign="top">Submitted</td><td valign="top">cancel() or abort</td><td valign="top">taskAborted</td><td valign="top">Cancelled</td></tr>
 * <tr><td valign="top">3.</td><td valign="top">Cancelled</td><td valign="top">&nbsp;</td><td valign="top">taskDone</td><td valign="top">Done</td></tr>
 * </table><p>
 *
 * <b>D. The task is cancelled when it is starting:</b>
 * <table>
 * <tr><td valign="top"><strong><u>Sequence</u></strong></td><td valign="top"><strong><u>State</u></strong></td><td valign="top"><strong><u>Action</u></strong></td><td valign="top"><strong><u>Listener</u></strong></td><td valign="top"><strong><u>Next state</u></strong></td></tr>
 * <tr><td valign="top">1.</td><td valign="top">None</td><td valign="top">submit()</td><td valign="top">taskSubmitted</td><td valign="top">Submitted</td></tr>
 * <tr><td valign="top">2.</td><td valign="top">Submitted</td><td valign="top">About to call run()</td><td valign="top">taskStarting<br>Future is cancelled.</td><td valign="top">Cancelling</td></tr>
 * <tr><td valign="top">3.</td><td valign="top">Cancelling</td><td valign="top">&nbsp;</td><td valign="top">taskAborted</td><td valign="top">Cancelled</td></tr>
 * <tr><td valign="top">4.</td><td valign="top">Cancelled</td><td valign="top">&nbsp;</td><td valign="top">taskDone</td><td valign="top">Done</td></tr>
 * </table>
 */
public interface ManagedTaskListener
{
    /**
     * Called after the task has been submitted to the Executor.
     * The task will not enter the starting state until the taskSubmitted
     * listener has completed.  This method may be called from
     * the same thread that the task was submitted with. <p>
     *
     * This event does not indicate that the task has been scheduled for execution.
     *
     * @param future the future instance that was created when the task was submitted.
     * The <code>Future.get()</code> methods should not be used within the scope
     * of this listener method, as they may cause deadlocks.
     * @param executor the executor used to run the associated Future.
     */
    void taskSubmitted(Future<?> future, ManagedExecutorService executor);

    /**
     * Called when a task's Future has been cancelled anytime
     * during the life of a task.  This method may be called after taskDone().
     *
     * The <code>Future.isCancelled()</code> method returns false if the task was aborted
     * through another means other than Future.cancel().  The Future.get() method will throw
     * an exception that will represent the cause of the cancellation:
     * <ul>
     * <li>{@link java.util.concurrent.CancellationException} if the task was cancelled,
     * <li>{@link javax.util.concurrent.SkippedException} if the task was skipped or
     * <li>{@link javax.util.concurrent.AbortedException} if the task failed to start for another reason.
     * </ul>
     * The <code>AbortedException#getCause()</code> method will return the exception
     * that caused the task to fail to start.
     *
     * @param future the future instance that was created when the task was submitted.
     * @param executor the executor used to run the associated Future.
     */
    void taskAborted(Future<?> future, ManagedExecutorService executor, Throwable exception);

    /**
     * Called when a submitted task has completed running, successful or otherwise after
     * submitted.
     * @param future the future instance that was created when the task was submitted.
     * @param executor the executor used to run the associated Future.
     */
    void taskDone(Future<?> future, ManagedExecutorService executor, Throwable exception);

    /**
     * Called when a task is about to start running.<p>
     *
     * This method may be called from the same thread that the task was submitted with.
     *
     * @param future the future instance that was created when the task was submitted.
     * The <code>Future.get()</code> methods should not be used within the scope
     * of this listener method, as they may cause deadlocks.
     * @param executor the executor used to run the associated Future.
     */
    void taskStarting(Future<?> future, ManagedExecutorService executor);

}
