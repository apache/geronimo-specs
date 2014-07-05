package javax.enterprise.concurrent;

import java.util.Date;

public interface Trigger {
    Date getNextRunTime(LastExecution lastExecutionInfo, Date taskScheduledTime);

    boolean skipRun(LastExecution lastExecutionInfo, Date scheduledRunTime);
}
