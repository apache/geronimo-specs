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

import java.util.Date;
import java.util.concurrent.Future;

/**
 * Triggers allow application developers to plug in rules
 * for when and how often a task should run.
 * The trigger can be as simple as a single, absolute date-time or can include
 * Java&trade; EE business calendar logic.
 *
 * A Trigger implementation is created by the application developer
 * (or may be supplied to the application externally) and is registered
 * with a task when it is submitted to a {@link javax.util.concurrent.ManagedScheduledExecutorService}
 * using any of the <code>schedule</code> methods.
 *
 * Each method will run with the same context in which the task runs.
 * The Trigger becomes a Contextual Task.<p>
 *
 * Each Trigger instance will be invoked within the same process in which it was registered.<p>
 *
 * Example:<pre>
 *   &#47;**
 *    * A trigger that only returns a single date.
 *    *&#47;
 *    public class SingleDateTrigger implements Trigger {
 *        private Date fireTime;
 *
 *        public TriggerSingleDate(Date newDate) {
 *            fireTime = newDate;
 *        }
 *
 *        public Date getNextRunTime(
 *           Future lastFuture, Date baseTime, Date lastActualRunTime,
 *           Date lastScheduledRunTime, Date lastCompleteTime) {
 *
 *           if(baseTime.after(fireTime)) {
 *               return null;
 *           }
 *           return fireTime;
 *        }
 *
 *        public boolean skipRun(Future lastFuture, Date scheduledRunTime) {
 *            return scheduledRunTime.after(fireTime);
 *        }
 *    }
 *
 *   &#47;**
 *    * A fixed-rate trigger that will skip any runs if
 *    * the latencyAllowance threshold is exceeded (the task
 *    * ran too late).
 *    *&#47;
 *    public class TriggerFixedRateLatencySensitive implements Trigger {
 *        private Date startTime;
 *        private long delta;
 *        private long latencyAllowance;
 *
 *        public TriggerFixedRateLatencySensitive(Date startTime, long delta, long latencyAllowance) {
 *            this.startTime = startTime;
 *            this.delta = delta;
 *            this.latencyAllowance = latencyAllowance;
 *        }
 *
 *        public Date getNextRunTime(Future lastFuture, Date baseTime, Date lastActualRunTime, Date lastScheduledRunTime, Date lastCompleteTime) {
 *            if(lastActualRunTime==null) {
 *                return startTime;
 *            }
 *            return new Date(lastScheduledRunTime.getTime() + delta);
 *        }
 *
 *        public boolean skipRun(Future lastFuture, Date scheduledRunTime) {
 *            return System.currentTimeMillis() - scheduledRunTime.getTime() > latencyAllowance;
 *        }
 *    }
 *
 * </pre>
 *
 */
public interface Trigger {

    /**
     * Retrieve the next time that the task should run after.
     *
     * @param lastFuture the state of the Future after the last run.
     *        This value will be null if the task has not yet run.
     * @param submitTime the time in which the task was originally submitted.
     * @param lastActualRunTime the time in which the last task actually ran.
     *        This value will be null if the task has not yet run.
     * @param lastScheduledRunTime the time in which the last task was scheduled to run.
     *        This value will be null if the task has not yet run.
     * @param lastCompleteTime the time in which the last task completed.
     *        This value will be null if the task has not yet run.
     * @return the date/time in which the next task iteration should execute on or after.
     */
    Date getNextRunTime(Future<?> lastFuture, Date submitTime, Date lastActualRunTime, Date lastScheduledRunTime, Date lastCompleteTime);

    /**
     * Return true if this run instance should be skipped.<p>
     *
     * This is useful if the task shouldn't run because it is late or if the task is
     * paused or suspended.<p>
     *
     * Once this task is skipped, the state of it's Future's result will throw a {@link SkippedException}.
     * Unchecked exceptions will be wrapped in a SkippedException.
     *
     * @param scheduledRunTime the date/time that the task was originally scheduled to run.
     *
     * @return true if the task should be skipped and rescheduled.
     */
    boolean skipRun(Future<?> lastFuture, Date scheduledRunTime);
}
