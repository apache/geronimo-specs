/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.    
 */

package javax.ws.rs.core;

import javax.ws.rs.ext.RuntimeDelegate;
import javax.ws.rs.ext.RuntimeDelegate.HeaderDelegate;

public class EntityTag {
    private final boolean isWeak;
    private final String  value;

    public EntityTag(String value) {
        this(value, false);
    }

    public EntityTag(String value, boolean weak) {
        if (value == null) {
            throw new IllegalArgumentException();
        }
        this.value = value;
        this.isWeak = weak;
    }

    @Override
    public boolean equals(java.lang.Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof EntityTag)) {
            return false;
        }

        EntityTag other = (EntityTag)obj;
        if (isWeak != other.isWeak()) {
            return false;
        }

        if (!value.equals(other.getValue())) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + ((isWeak) ? 1 : 0);
        result = 31 * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }

    public String getValue() {
        return value;
    }

    public boolean isWeak() {
        return isWeak;
    }

    private static final HeaderDelegate<EntityTag> headerDelegate =
                                                                      RuntimeDelegate
                                                                          .getInstance()
                                                                          .createHeaderDelegate(EntityTag.class);

    @Override
    public String toString() {
        return headerDelegate.toString(this);
    }

    public static EntityTag valueOf(String value) {
        return headerDelegate.fromString(value);
    }
}
