/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
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
// This source code implements specifications defined by the Java
// Community Process. In order to remain compliant with the specification
// DO NOT add / change / or delete method signatures!
//
package jakarta.annotation.sql;

import static java.lang.annotation.ElementType.TYPE;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;

/**
 * @version $Revision$ $Date$
 */
@Retention(RUNTIME)
@Target({TYPE})
public @interface DataSourceDefinition {

    boolean transactional() default true;

    int initialPoolSize() default -1;

    int isolationLevel() default -1;

    int loginTimeout() default 0;

    int maxIdleTime() default -1;

    int maxPoolSize() default -1;

    int maxStatements() default -1;

    int minPoolSize() default -1;

    int portNumber() default -1;

    String databaseName() default "";

    String description() default "";

    String password() default "";

    String serverName() default "localhost";

    String url() default "";

    String user() default "";

    String[] properties() default {};

    String className();

    String name();

}
