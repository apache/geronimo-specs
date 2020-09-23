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

package javax.mail.search;

import javax.mail.Address;

/**
 * A Term that compares two Addresses as Strings.
 *
 * @version $Rev$ $Date$
 */
public abstract class AddressStringTerm extends StringTerm {
	
	private static final long serialVersionUID = 3086821234204980368L;
	
    /**
     * Constructor.
     * @param pattern the pattern to be compared
     */
    protected AddressStringTerm(final String pattern) {
        super(pattern);
    }

    /**
     * Tests if the patterm associated with this Term is a substring of
     * the address in the supplied object.
     *
     * @param address
     * @return
     */
    protected boolean match(final Address address) {
        return match(address.toString());
    }
}
