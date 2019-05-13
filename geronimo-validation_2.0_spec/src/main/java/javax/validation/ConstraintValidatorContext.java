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
package javax.validation;

/**
 * @version $Rev$ $Date$
 */
public interface ConstraintValidatorContext {
    void disableDefaultConstraintViolation();

    String getDefaultConstraintMessageTemplate();

    ConstraintViolationBuilder buildConstraintViolationWithTemplate(String messageTemplate);

    /** @since 2.0 */
    ClockProvider getClockProvider();

    /** @since 1.1 */
    <T> T unwrap(Class<T> type);

    interface ConstraintViolationBuilder {
        /**
         * @deprecated since 1.1
         * @see #addBeanNode()
         * @see #addPropertyNode(String)
         * @see #addParameterNode(int)
         */
        @Deprecated
        NodeBuilderDefinedContext addNode(String name);

        ConstraintValidatorContext addConstraintViolation();

        /** @since 1.1 */
        NodeBuilderCustomizableContext addPropertyNode(String name);

        /** @since 1.1 */
        LeafNodeBuilderCustomizableContext addBeanNode();

        /** @since 1.1 */
        NodeBuilderDefinedContext addParameterNode(int index);

        /** @since 2.0 */
        ContainerElementNodeBuilderCustomizableContext addContainerElementNode(String name, Class<?> containerType,
            Integer typeArgumentIndex);

        interface NodeBuilderDefinedContext {
            /**
             * @deprecated since 1.1
             * @see #addPropertyNode(String)
             * @see #addBeanNode()
             */
            @Deprecated
            NodeBuilderCustomizableContext addNode(String name);

            ConstraintValidatorContext addConstraintViolation();

            /** @since 1.1 */
            NodeBuilderCustomizableContext addPropertyNode(String name);

            /** @since 1.1 */
            LeafNodeBuilderCustomizableContext addBeanNode();

            /** @since 2.0 */
            ContainerElementNodeBuilderCustomizableContext addContainerElementNode(String name, Class<?> containerType,
                Integer typeArgumentIndex);
        }

        interface NodeBuilderCustomizableContext {

            NodeContextBuilder inIterable();

            NodeBuilderCustomizableContext addNode(String name);

            ConstraintValidatorContext addConstraintViolation();

            /** @since 1.1 */
            NodeBuilderCustomizableContext addPropertyNode(String name);

            /** @since 1.1 */
            LeafNodeBuilderCustomizableContext addBeanNode();

            /** @since 2.0 */
            NodeBuilderCustomizableContext inContainer(Class<?> containerClass, Integer typeArgumentIndex);

            /** @since 2.0 */
            ContainerElementNodeBuilderCustomizableContext addContainerElementNode(String name, Class<?> containerType,
                Integer typeArgumentIndex);
        }

        interface NodeContextBuilder {

            NodeBuilderDefinedContext atKey(Object key);

            NodeBuilderDefinedContext atIndex(Integer index);

            NodeBuilderCustomizableContext addNode(String name);

            ConstraintValidatorContext addConstraintViolation();

            /** @since 1.1 */
            NodeBuilderCustomizableContext addPropertyNode(String name);

            /** @since 1.1 */
            LeafNodeBuilderCustomizableContext addBeanNode();

            /** @since 2.0 */
            ContainerElementNodeBuilderCustomizableContext addContainerElementNode(String name, Class<?> containerType,
                Integer typeArgumentIndex);
        }

        /** @since 1.1 */
        interface LeafNodeBuilderDefinedContext {
            ConstraintValidatorContext addConstraintViolation();
        }

        /** @since 1.1 */
        interface LeafNodeBuilderCustomizableContext {
            LeafNodeContextBuilder inIterable();
            ConstraintValidatorContext addConstraintViolation();

            /** @since 2.0 */
            LeafNodeBuilderCustomizableContext inContainer(Class<?> containerClass, Integer typeArgumentIndex);
        }

        /** @since 1.1 */
        interface LeafNodeContextBuilder {
            LeafNodeBuilderDefinedContext atKey(Object key);
            LeafNodeBuilderDefinedContext atIndex(Integer index);
            ConstraintValidatorContext addConstraintViolation();
        }

        /** @since 2.0 */
        interface ContainerElementNodeBuilderDefinedContext {
            NodeBuilderCustomizableContext addPropertyNode(String name);
            LeafNodeBuilderCustomizableContext addBeanNode();
            ContainerElementNodeBuilderCustomizableContext addContainerElementNode(String name, Class<?> containerType,
                Integer typeArgumentIndex);
            ConstraintValidatorContext addConstraintViolation();
        }

        /** @since 2.0 */
        interface ContainerElementNodeBuilderCustomizableContext {
            ContainerElementNodeContextBuilder inIterable();
            NodeBuilderCustomizableContext addPropertyNode(String name);
            LeafNodeBuilderCustomizableContext addBeanNode();
            ContainerElementNodeBuilderCustomizableContext addContainerElementNode(String name, Class<?> containerType,
                Integer typeArgumentIndex);
            ConstraintValidatorContext addConstraintViolation();
        }

        /** @since 2.0 */
        interface ContainerElementNodeContextBuilder {
            ContainerElementNodeBuilderDefinedContext atKey(Object key);
            ContainerElementNodeBuilderDefinedContext atIndex(Integer index);
            NodeBuilderCustomizableContext addPropertyNode(String name);
            LeafNodeBuilderCustomizableContext addBeanNode();
            ContainerElementNodeBuilderCustomizableContext addContainerElementNode(String name, Class<?> containerType,
                Integer typeArgumentIndex);
            ConstraintValidatorContext addConstraintViolation();
        }
    }
}
