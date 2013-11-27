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

import java.io.Serializable;

public class FactoryConfigurationError extends Error implements Serializable {
	Exception nested;

	public FactoryConfigurationError() {
	}

	public FactoryConfigurationError(Exception e) {
		super(e);
		nested = e;
	}

	public FactoryConfigurationError(Exception e, String msg) {
		super(msg, e);
		nested = e;
	}

	public FactoryConfigurationError(java.lang.String msg) {
		super(msg);
	}

	public FactoryConfigurationError(String msg, Exception e) {
		super(msg, e);
		nested = e;
	}

	public Exception getException() {
		return nested;
	}

	public String getMessage() {
		String msg = super.getMessage();
		if (msg != null)
			return msg;

		if (nested != null) {
			msg = nested.getMessage();
			if (msg == null)
				msg = nested.getClass().toString();
		}
		return msg;
	}
}