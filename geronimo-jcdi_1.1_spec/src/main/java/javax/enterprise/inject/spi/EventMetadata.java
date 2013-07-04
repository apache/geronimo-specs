/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package javax.enterprise.inject.spi;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Set;

/**
 * An Observer Method can inject an {@link javax.enterprise.context.Dependent}
 * EventMetadata object containing information about the
 * sender of the event.
 */
public interface EventMetadata {

    /**
     * @return the qualifiers which were used to fire the event
     */
    public Set<Annotation> getQualifiers();

    /**
     * @return the {@link javax.enterprise.event.Event} InjectionPoitn or <code>null</code>
     *          if this event got fired via {@link BeanManager#fireEvent(Object, java.lang.annotation.Annotation...)}
     */
    public InjectionPoint getInjectionPoint();

    /**
     * @return the effective type of the fired event
     */
    public Type getType();
}
