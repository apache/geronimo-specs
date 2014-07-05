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
package javax.enterprise.concurrent;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

public class ManagedExecutors {
    public static final String NULL_TASK_ERROR_MSG = "Task cannot be null";

    public static boolean isCurrentThreadShutdown() {
        final Thread currThread = Thread.currentThread();
        return currThread instanceof ManageableThread && ManageableThread.class.cast(currThread).isShutdown();
    }

    public static Runnable managedTask(final Runnable task, final ManagedTaskListener taskListener)
            throws IllegalArgumentException {
        return managedTask(task, null, taskListener);
    }

    public static Runnable managedTask(final Runnable task, final Map<String, String> executionProperties,
                                       final ManagedTaskListener taskListener) throws IllegalArgumentException {
        if (task == null) {
            throw new IllegalArgumentException(NULL_TASK_ERROR_MSG);
        }
        return new RunnableAdapter(task, executionProperties, taskListener);
    }

    public static <V> Callable<V> managedTask(final Callable<V> task, final ManagedTaskListener taskListener)
            throws IllegalArgumentException {
        return managedTask(task, null, taskListener);
    }

    public static <V> Callable<V> managedTask(final Callable<V> task,
                                              final Map<String, String> executionProperties,
                                              final ManagedTaskListener taskListener) throws IllegalArgumentException {
        if (task == null) {
            throw new IllegalArgumentException(NULL_TASK_ERROR_MSG);
        }
        return new CallableAdapter<V>(task, executionProperties, taskListener);
    }

    private static final class RunnableAdapter extends Adapter implements Runnable {
        private final Runnable task;

        public RunnableAdapter(final Runnable task, final Map<String, String> executionProperties,
                               final ManagedTaskListener taskListener) {
            super(taskListener, executionProperties, ManagedTask.class.isInstance(task) ? ManagedTask.class.cast(task) : null);
            this.task = task;
        }

        @Override
        public void run() {
            task.run();
        }

    }

    /**
     * Adapter for Callable to include ManagedTask interface methods
     */
    private static final class CallableAdapter<V> extends Adapter implements Callable<V> {
        private final Callable<V> task;

        public CallableAdapter(final Callable<V> task, final Map<String, String> executionProperties,
                               final ManagedTaskListener taskListener) {
            super(taskListener, executionProperties, ManagedTask.class.isInstance(task) ? ManagedTask.class.cast(task) : null);
            this.task = task;
        }

        @Override
        public V call() throws Exception {
            return task.call();
        }

    }

    private static class Adapter implements ManagedTask {
        protected final ManagedTaskListener taskListener;
        protected final Map<String, String> executionProperties;
        protected final ManagedTask managedTask;

        public Adapter(ManagedTaskListener taskListener, Map<String, String> executionProperties, ManagedTask managedTask) {
            this.taskListener = taskListener;
            this.managedTask = managedTask;
            this.executionProperties =
                    initExecutionProperties(managedTask == null? null: managedTask.getExecutionProperties(),
                            executionProperties);
        }

        @Override
        public ManagedTaskListener getManagedTaskListener() {
            if (taskListener != null) {
                return taskListener;
            }
            if (managedTask != null) {
                return managedTask.getManagedTaskListener();
            }
            return null;
        }

        @Override
        public Map<String, String> getExecutionProperties() {
            if (executionProperties != null) {
                return executionProperties;
            }
            return null;
        }

        private static Map<String, String> initExecutionProperties(final Map<String, String> base,
                                                            final Map<String, String> override) {
            if (base == null && override == null) {
                return null;
            }

            final Map<String, String> props = new HashMap<String, String>();
            if (base != null) {
                props.putAll(base);
            }
            if (override != null) {
                props.putAll(override);
            }
            return props;
        }

    }

    private ManagedExecutors() {
        // no-op
    }
}

