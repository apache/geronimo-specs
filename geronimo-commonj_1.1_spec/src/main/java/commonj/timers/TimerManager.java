/**
 *
 * Copyright 2006 The Apache Software Foundation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

//
// This source code implements specifications defined by the BEA and IBM.
// For more information, see:
//    http://dev2dev.bea.com/technologies/commonj/index.jsp
// or
//    http://www.ibm.com/developerworks/library/j-commonj-sdowmt
//
// In order to remain compliant with the specification
// DO NOT add / change / or delete method signatures!
//

package commonj.timers;

import java.util.Date;

/**
 * @version $Rev$ $Date$
 */
public interface TimerManager {

    static final long IMMEDIATE = 0;
    static final long INDEFINITE = java.lang.Long.MAX_VALUE;

    boolean isStopped();

    boolean isStopping();

    boolean isSuspended() throws IllegalStateException;

    boolean isSuspending() throws IllegalStateException;

    void resume() throws IllegalStateException;

    Timer schedule(TimerListener listener, long delayInMillis)
        throws IllegalStateException, IllegalArgumentException;

    Timer schedule(TimerListener listener, long delayInMillis, long repeatIntervalInMillis)
        throws IllegalStateException, IllegalArgumentException;

    Timer schedule(TimerListener listener, Date scheduleDate)
        throws IllegalStateException, IllegalArgumentException;

    Timer schedule(TimerListener listener, Date scheduleDate, long repeatIntervalInMillis)
        throws IllegalStateException, IllegalArgumentException;

    Timer scheduleAtFixedRate(TimerListener listener, long delayInMillis, long repeatIntervalInMillis)
        throws IllegalStateException, IllegalArgumentException;

    Timer scheduleAtFixedRate(TimerListener listener, Date scheduleDate, long repeatIntervalInMillis)
        throws IllegalStateException, IllegalArgumentException;

    void stop() throws IllegalStateException;

    void suspend() throws IllegalStateException;

    boolean waitForStop(long timeOut)
        throws InterruptedException, IllegalArgumentException;

    boolean waitForSuspend(long timOut)
        throws InterruptedException, IllegalStateException, IllegalArgumentException;

}
