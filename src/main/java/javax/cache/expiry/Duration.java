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


package javax.cache.expiry;


import java.io.Serializable;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.DAYS;
import static java.util.concurrent.TimeUnit.HOURS;
import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.SECONDS;


public class Duration implements Serializable {
    public static final long serialVersionUID = 201305101442L;

    public static final Duration ETERNAL = new Duration();
    public static final Duration ONE_DAY = new Duration(DAYS, 1);
    public static final Duration ONE_HOUR = new Duration(HOURS, 1);
    public static final Duration THIRTY_MINUTES = new Duration(MINUTES, 30);
    public static final Duration TWENTY_MINUTES = new Duration(MINUTES, 20);
    public static final Duration TEN_MINUTES = new Duration(MINUTES, 10);
    public static final Duration FIVE_MINUTES = new Duration(MINUTES, 5);
    public static final Duration ONE_MINUTE = new Duration(MINUTES, 1);
    public static final Duration ZERO = new Duration(SECONDS, 0);

    private final TimeUnit timeUnit;
    private final long durationAmount;

    public Duration() {
        this.timeUnit = null;
        this.durationAmount = 0;
    }

    public Duration(final TimeUnit timeUnit, final long durationAmount) {
        if (timeUnit == null) {
            if (durationAmount == 0) {
                this.timeUnit = null;
                this.durationAmount = 0;
            } else {
                throw new NullPointerException();
            }
        } else {
            switch (timeUnit) {
                case NANOSECONDS:
                case MICROSECONDS:
                    throw new IllegalArgumentException("Must specify a TimeUnit of milliseconds or higher.");
                default:
                    this.timeUnit = timeUnit;
                    break;
            }
            if (durationAmount < 0) {
                throw new IllegalArgumentException("Cannot specify a negative durationAmount.");
            }
            this.durationAmount = durationAmount;
        }
    }

    public Duration(final long startTime, final long endTime) {
        if (startTime == Long.MAX_VALUE || endTime == Long.MAX_VALUE) {
            timeUnit = null;
            durationAmount = 0;
        } else if (startTime < 0) {
            throw new IllegalArgumentException("Cannot specify a negative startTime.");
        } else if (endTime < 0) {
            throw new IllegalArgumentException("Cannot specify a negative endTime.");
        } else {
            timeUnit = TimeUnit.MILLISECONDS;
            durationAmount = Math.max(startTime, endTime) - Math.min(startTime, endTime);
        }
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public long getDurationAmount() {
        return durationAmount;
    }

    public boolean isEternal() {
        return timeUnit == null && durationAmount == 0;
    }

    public boolean isZero() {
        return timeUnit != null && durationAmount == 0;
    }

    public long getAdjustedTime(final long time) {
        if (isEternal()) {
            return Long.MAX_VALUE;
        }
        return time + timeUnit.toMillis(durationAmount);
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        } else if (other == null || getClass() != other.getClass()) {
            return false;
        } else {
            final Duration duration = (Duration) other;
            if (this.timeUnit == null && duration.timeUnit == null &&
                    this.durationAmount == duration.durationAmount) {
                return true;
            } else if (this.timeUnit != null && duration.timeUnit != null) {
                long time1 = timeUnit.toMillis(durationAmount);
                long time2 = duration.timeUnit.toMillis(duration.durationAmount);
                return time1 == time2;
            }
            return false;
        }
    }

    @Override
    public int hashCode() {
        return timeUnit == null ? -1 : (int) timeUnit.toMillis(durationAmount);
    }
}
