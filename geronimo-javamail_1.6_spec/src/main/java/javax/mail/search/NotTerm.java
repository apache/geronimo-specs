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

import javax.mail.Message;

/**
 * Term that implements a logical negation.
 *
 * @version $Rev$ $Date$
 */
public final class NotTerm extends SearchTerm {
	
	private static final long serialVersionUID = 7152293214217310216L;
	
    private final SearchTerm term;

    public NotTerm(final SearchTerm term) {
        this.term = term;
    }

    public SearchTerm getTerm() {
        return term;
    }

    @Override
    public boolean match(final Message message) {
        return !term.match(message);
    }

    @Override
    public boolean equals(final Object other) {
        if (other == this) {
			return true;
		}
        if (other instanceof NotTerm == false) {
			return false;
		}
        return term.equals(((NotTerm) other).term);
    }

    @Override
    public int hashCode() {
        return term.hashCode();
    }
}
