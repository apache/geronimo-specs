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

import java.util.Map;

public interface ManagedTask {
    String LONGRUNNING_HINT = "javax.enterprise.concurrent.LONGRUNNING_HINT";
    String TRANSACTION = "javax.enterprise.concurrent.TRANSACTION";
    String SUSPEND = "SUSPEND";
    String USE_TRANSACTION_OF_EXECUTION_THREAD = "USE_TRANSACTION_OF_EXECUTION_THREAD";
    String IDENTITY_NAME = "javax.enterprise.concurrent.IDENTITY_NAME";

    ManagedTaskListener getManagedTaskListener();

    Map<String, String> getExecutionProperties();
}

