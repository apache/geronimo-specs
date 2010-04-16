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


package javax.resource.spi.work;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @version $Rev$ $Date$
 * @since 1.6
 */
public class HintsContext implements WorkContext {

    public static final String LONGRUNNING_HINT = "javax.resource.LongRunning";
    public static final String NAME_HINT = "javax.resource.Name";
    
    private static final long serialVersionUID=7956353628297167255L;

    protected String description;

    protected String name;

    private final Map<String, Serializable> hints = new HashMap<String, Serializable>();

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Serializable> getHints() {
        return hints;
    }

    public void setHint(String name, Serializable hint) {
        hints.put(name, hint);
    }
}
