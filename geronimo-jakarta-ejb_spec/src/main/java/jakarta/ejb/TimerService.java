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

//
// This source code implements specifications defined by the Java
// Community Process. In order to remain compliant with the specification
// DO NOT add / change / or delete method signatures!
//

package jakarta.ejb;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

/**
 * @version $Rev$ $Date$
 */
public interface TimerService {
    Timer createTimer(Date initialExpiration, long intervalDuration, Serializable info) throws IllegalArgumentException, IllegalStateException, EJBException;

    Timer createTimer(Date expiration, Serializable info) throws IllegalArgumentException, IllegalStateException, EJBException;

    Timer createTimer(long initialDuration, long intervalDuration, Serializable info) throws IllegalArgumentException, IllegalStateException, EJBException;

    Timer createTimer(long duration, Serializable info) throws IllegalArgumentException, IllegalStateException, EJBException;

    Collection<Timer> getTimers() throws IllegalStateException, EJBException;

    Collection<Timer> getAllTimers() throws IllegalStateException, EJBException;

    Timer createSingleActionTimer(long duration, TimerConfig timerConfig) throws IllegalArgumentException, IllegalStateException, EJBException;

    Timer createSingleActionTimer(java.util.Date expiration, TimerConfig timerConfig) throws IllegalArgumentException, IllegalStateException, EJBException;

    Timer createIntervalTimer(long initialDuration, long intervalDuration, TimerConfig timerConfig) throws IllegalArgumentException, IllegalStateException, EJBException;

    Timer createIntervalTimer(java.util.Date initialExpiration, long intervalDuration, TimerConfig timerConfig) throws IllegalArgumentException, IllegalStateException, EJBException;

    Timer createCalendarTimer(ScheduleExpression schedule) throws IllegalArgumentException, IllegalStateException, EJBException;

    Timer createCalendarTimer(ScheduleExpression schedule, TimerConfig timerConfig) throws IllegalArgumentException, IllegalStateException, EJBException;


}
