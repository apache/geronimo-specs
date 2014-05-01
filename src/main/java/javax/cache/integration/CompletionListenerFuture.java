/*
 *
 * Apache Geronimo JCache Spec 1.0
 *
 * Copyright (C) 2003 - 2014 The Apache Software Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */


package javax.cache.integration;


import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public class CompletionListenerFuture implements CompletionListener, Future<Void> {

    private boolean isCompleted;
    private Exception exception;


    public CompletionListenerFuture() {
        this.isCompleted = false;
        this.exception = null;
    }


    @Override
    public void onCompletion() throws IllegalStateException {
        synchronized (this) {
            if (isCompleted) {
                throw new IllegalStateException("Attempted to use a CompletionListenerFuture instance more than once");
            } else {
                isCompleted = true;
                notify();
            }
        }
    }


    @Override
    public void onException(Exception e) throws IllegalStateException {
        synchronized (this) {
            if (isCompleted) {
                throw new IllegalStateException("Attempted to use a CompletionListenerFuture instance more than once");
            } else {
                isCompleted = true;
                exception = e;
                notify();
            }
        }
    }

    @Override
    public boolean cancel(boolean b) {
        throw new UnsupportedOperationException("CompletionListenerFutures can't be cancelled");
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public boolean isDone() {
        synchronized (this) {
            return isCompleted;
        }
    }


    @Override
    public Void get() throws InterruptedException, ExecutionException {
        synchronized (this) {
            while (!isCompleted) {
                wait();
            }

            if (exception == null) {
                return null;
            } else {
                throw new ExecutionException(exception);
            }
        }
    }


    @Override
    public Void get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        synchronized (this) {
            if (!isCompleted) {
                unit.timedWait(this, timeout);
            }

            if (isCompleted) {
                if (exception == null) {
                    return null;
                } else {
                    throw new ExecutionException(exception);
                }
            } else {
                throw new TimeoutException();
            }
        }
    }
}
