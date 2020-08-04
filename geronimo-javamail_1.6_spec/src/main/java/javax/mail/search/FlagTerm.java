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

import javax.mail.Flags;
import javax.mail.Message;
import javax.mail.MessagingException;

/**
 * Term for matching message {@link Flags}.
 *
 * @version $Rev$ $Date$
 */
public final class FlagTerm extends SearchTerm {
	
	private static final long serialVersionUID = -142991500302030647L; 
	
    /**
     * If true, test that all flags are set; if false, test that all flags are clear.
     */
    private final boolean set;
    /**
     * The flags to test.
     */
   private final Flags flags;

    /**
     * @param flags the flags to test
     * @param set test for set or clear; {@link #set}
     */
    public FlagTerm(final Flags flags, final boolean set) {
        this.set = set;
        this.flags = flags;
    }

    public Flags getFlags() {
        return flags;
    }

    public boolean getTestSet() {
        return set;
    }

    @Override
    public boolean match(final Message message) {
        try {
            final Flags msgFlags = message.getFlags();
            if (set) {
                return msgFlags.contains(flags);
            } else {
                // yuk - I wish we could get at the internal state of the Flags
                final Flags.Flag[] system = flags.getSystemFlags();
                for (int i = 0; i < system.length; i++) {
                    final Flags.Flag flag = system[i];
                    if (msgFlags.contains(flag)) {
                        return false;
                    }
                }
                final String[] user = flags.getUserFlags();
                for (int i = 0; i < user.length; i++) {
                    final String flag = user[i];
                    if (msgFlags.contains(flag)) {
                        return false;
                    }
                }
                return true;
            }
        } catch (final MessagingException e) {
            return false;
        }
    }

    @Override
    public boolean equals(final Object other) {
        if (other == this) {
			return true;
		}
        if (other instanceof FlagTerm == false) {
			return false;
		}
        final FlagTerm otherFlags = (FlagTerm) other;
        return otherFlags.set == this.set && otherFlags.flags.equals(flags);
    }

    @Override
    public int hashCode() {
        return set ? flags.hashCode() : ~flags.hashCode();
    }
}
