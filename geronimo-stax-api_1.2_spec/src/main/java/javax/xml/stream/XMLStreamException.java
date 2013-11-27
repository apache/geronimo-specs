/*
 **
 ** Licensed to the Apache Software Foundation (ASF) under one
 ** or more contributor license agreements.  See the NOTICE file
 ** distributed with this work for additional information
 ** regarding copyright ownership.  The ASF licenses this file
 ** to you under the Apache License, Version 2.0 (the
 ** "License"); you may not use this file except in compliance
 ** with the License.  You may obtain a copy of the License at
 **
 **  http://www.apache.org/licenses/LICENSE-2.0
 **
 ** Unless required by applicable law or agreed to in writing,
 ** software distributed under the License is distributed on an
 ** "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 ** KIND, either express or implied.  See the License for the
 ** specific language governing permissions and limitations
 ** under the License.
 */
package javax.xml.stream;

public class XMLStreamException extends Exception {
	protected Throwable nested;

	protected Location location;

	public XMLStreamException() {
	}

	public XMLStreamException(java.lang.String msg) {
		super(msg);
	}

	public XMLStreamException(java.lang.Throwable th) {
		super(th);
		this.nested = th;
	}

	public XMLStreamException(java.lang.String msg, java.lang.Throwable th) {
		super(msg, th);
		this.nested = th;
	}

	public XMLStreamException(java.lang.String msg, Location location,
			java.lang.Throwable th) {
		super("ParseError at [row,col]:[" + location.getLineNumber() + ","
				+ location.getColumnNumber() + "]\n" + "Message: " + msg, th);
		this.location = location;
		this.nested = th;
	}

	public XMLStreamException(java.lang.String msg, Location location) {
		super("ParseError at [row,col]:[" + location.getLineNumber() + ","
				+ location.getColumnNumber() + "]\n" + "Message: " + msg);
		this.location = location;
	}

	public java.lang.Throwable getNestedException() {
		return nested;
	}

	public Location getLocation() {
		return location;
	}
}
