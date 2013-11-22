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

    /** @since 1.1 */
    <T> T unwrap(Class<T> type);

    interface ConstraintViolationBuilder {
        @Deprecated
        NodeBuilderDefinedContext addNode(String name);

        ConstraintValidatorContext addConstraintViolation();

        // @since 1.1

        NodeBuilderCustomizableContext addPropertyNode(String name);
        LeafNodeBuilderCustomizableContext addBeanNode();
        NodeBuilderDefinedContext addParameterNode(int index);

        interface NodeBuilderDefinedContext {
            @Deprecated
            NodeBuilderCustomizableContext addNode(String name);

            ConstraintValidatorContext addConstraintViolation();

            // @since 1.1

            NodeBuilderCustomizableContext addPropertyNode(String name);
            LeafNodeBuilderCustomizableContext addBeanNode();
        }

        interface NodeBuilderCustomizableContext {

            NodeContextBuilder inIterable();

            @Deprecated
            NodeBuilderCustomizableContext addNode(String name);

            ConstraintValidatorContext addConstraintViolation();

            // @since 1.1

            NodeBuilderCustomizableContext addPropertyNode(String name);
            LeafNodeBuilderCustomizableContext addBeanNode();
        }

        interface NodeContextBuilder {

            NodeBuilderDefinedContext atKey(Object key);

            NodeBuilderDefinedContext atIndex(Integer index);

            @Deprecated
            NodeBuilderCustomizableContext addNode(String name);

            ConstraintValidatorContext addConstraintViolation();

            // @since 1.1

            NodeBuilderCustomizableContext addPropertyNode(String name);
            LeafNodeBuilderCustomizableContext addBeanNode();
        }

        /** @since 1.1 */
        interface LeafNodeBuilderDefinedContext {
            ConstraintValidatorContext addConstraintViolation();
        }

        /** @since 1.1 */
        interface LeafNodeBuilderCustomizableContext {
            LeafNodeContextBuilder inIterable();
            ConstraintValidatorContext addConstraintViolation();
        }

        /** @since 1.1 */
        interface LeafNodeContextBuilder {
            LeafNodeBuilderDefinedContext atKey(Object key);
            LeafNodeBuilderDefinedContext atIndex(Integer index);
            ConstraintValidatorContext addConstraintViolation();
        }
    }
}

