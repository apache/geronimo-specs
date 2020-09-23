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

package javax.mail.internet;

/**
 * @version $Rev$ $Date$
 */
public class AddressException extends ParseException {
	
	private static final long serialVersionUID = 9134583443539323120L;
	
    protected int pos;
    protected String ref;

    public AddressException() {
        this(null);
    }

    public AddressException(final String message) {
        this(message, null);
    }

    public AddressException(final String message, final String ref) {
        this(message, null, -1);
    }

    public AddressException(final String message, final String ref, final int pos) {
        super(message);
        this.ref = ref;
        this.pos = pos;
    }

    public String getRef() {
        return ref;
    }

    public int getPos() {
        return pos;
    }

    @Override
    public String toString() {
        return super.toString() + " (" + ref + "," + pos + ")";
    }
}
