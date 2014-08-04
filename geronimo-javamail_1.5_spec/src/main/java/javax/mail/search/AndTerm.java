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

import java.util.Arrays;

import javax.mail.Message;

/**
 * Term that implements a logical AND across terms.
 *
 * @version $Rev$ $Date$
 */
public final class AndTerm extends SearchTerm {
	
	private static final long serialVersionUID = -3583274505380989582L;
	
    /**
     * Terms to which the AND operator should be applied.
     */
    private final SearchTerm[] terms;

    /**
     * Constructor for performing a binary AND.
     *
     * @param a the first term
     * @param b the second ter,
     */
    public AndTerm(final SearchTerm a, final SearchTerm b) {
        terms = new SearchTerm[]{a, b};
    }

    /**
     * Constructor for performing and AND across an arbitraty number of terms.
     * @param terms the terms to AND together
     */
    public AndTerm(final SearchTerm[] terms) {
        this.terms = terms;
    }

    /**
     * Return the terms.
     * @return the terms
     */
    public SearchTerm[] getTerms() {
        return terms;
    }

    /**
     * Match by applying the terms, in order, to the Message and performing an AND operation
     * to the result. Comparision will stop immediately if one of the terms returns false.
     *
     * @param message the Message to apply the terms to
     * @return true if all terms match
     */
    @Override
    public boolean match(final Message message) {
        for (int i = 0; i < terms.length; i++) {
            final SearchTerm term = terms[i];
            if (!term.match(message)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean equals(final Object other) {
        if (other == this) {
			return true;
		}
        if (other instanceof AndTerm == false) {
			return false;
		}
        return Arrays.equals(terms, ((AndTerm) other).terms);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        for (int i = 0; i < terms.length; i++) {
            hash = hash * 37 + terms[i].hashCode();
        }
        return hash;
    }
}
