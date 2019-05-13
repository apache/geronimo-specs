/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with this
 * work for additional information regarding copyright ownership. The ASF
 * licenses this file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package jakarta.validation;

import java.util.List;

/**
 * @version $Rev$ $Date$
 */
public interface Path extends Iterable<Path.Node> {

    /** @since 2.0 */
    @Override
    String toString();

    interface Node {
        String getName();

        boolean isInIterable();

        Integer getIndex();

        Object getKey();

        /** @since 1.1 */
        ElementKind getKind();

        /** @since 1.1 */
        <T extends Node> T as(Class<T> nodeType);

        /** @since 2.0 */
        @Override
        String toString();
    }

    /** @since 1.1 */
    interface MethodNode extends Node {
        List<Class<?>> getParameterTypes();
    }

    /** @since 1.1 */
    interface ConstructorNode extends Node {
        List<Class<?>> getParameterTypes();
    }

    /** @since 1.1 */
    interface ReturnValueNode extends Node {
    }

    /** @since 1.1 */
    interface ParameterNode extends Node {
        int getParameterIndex();
    }

    /** @since 1.1 */
    interface CrossParameterNode extends Node {
    }

    /** @since 1.1 */
    interface BeanNode extends Node {

        /** @since 2.0 */
        Class<?> getContainerClass();

        /** @since 2.0 */
        Integer getTypeArgumentIndex();
    }

    /** @since 1.1 */
    interface PropertyNode extends Node {

        /** @since 2.0 */
        Class<?> getContainerClass();

        /** @since 2.0 */
        Integer getTypeArgumentIndex();
    }
    
    /** @since 2.0 */
    interface ContainerElementNode extends Node {

        Class<?> getContainerClass();

        Integer getTypeArgumentIndex();
    }
}
