/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package javax.batch.operations;

import javax.batch.runtime.JobExecution;
import javax.batch.runtime.JobInstance;
import javax.batch.runtime.StepExecution;
import java.util.List;
import java.util.Properties;
import java.util.Set;

public interface JobOperator {
    Set<String> getJobNames() throws JobSecurityException;

    int getJobInstanceCount(String jobName) throws NoSuchJobException, JobSecurityException;

    List<JobInstance> getJobInstances(String jobName, int start, int count) throws NoSuchJobException, JobSecurityException;

    List<Long> getRunningExecutions(String jobName) throws NoSuchJobException, JobSecurityException;

    Properties getParameters(long executionId) throws NoSuchJobExecutionException, JobSecurityException;

    long start(String jobXMLName, Properties jobParameters) throws JobStartException, JobSecurityException;

    long restart(long executionId, Properties restartParameters) throws JobExecutionAlreadyCompleteException, NoSuchJobExecutionException, JobExecutionNotMostRecentException, JobRestartException, JobSecurityException;

    void stop(long executionId) throws NoSuchJobExecutionException, JobExecutionNotRunningException, JobSecurityException;

    void abandon(long executionId) throws NoSuchJobExecutionException, JobExecutionIsRunningException, JobSecurityException;

    JobInstance getJobInstance(long executionId) throws NoSuchJobExecutionException, JobSecurityException;

    List<JobExecution> getJobExecutions(JobInstance instance) throws NoSuchJobInstanceException, JobSecurityException;

    JobExecution getJobExecution(long executionId) throws NoSuchJobExecutionException, JobSecurityException;

    List<StepExecution> getStepExecutions(long jobExecutionId) throws NoSuchJobExecutionException, JobSecurityException;
}
