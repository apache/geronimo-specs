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

import java.io.InputStream;
import java.io.File;
import java.io.FileInputStream;

import java.util.Properties;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/*
 * Alrighty...here is the beef on the newInstance methods:
 * 
 * 1. Use the javax.xml.stream.XMLInputFactory system property. 2. Use the
 * properties file "lib/stax.properties" in the JRE directory. This
 * configuration file is in standard java.util.Properties format and contains
 * the fully qualified name of the implementation class with the key being the
 * system property defined above. 3. Use the Services API (as detailed in the
 * JAR specification), if available, to determine the classname. The Services
 * API will look for a classname in the file
 * META-INF/services/javax.xml.stream.XMLInputFactory in jars available to the
 * runtime. Platform default XMLInputFactory instance.
 * 
 * Once an application has obtained a reference to a XMLInputFactory it can use
 * the factory to configure and obtain stream instances.
 */

class FactoryLocator {
	static Object locate(String factoryId) throws FactoryConfigurationError {
		return locate(factoryId, null);
	}

	static Object locate(String factoryId, String fallbackClassName)
			throws FactoryConfigurationError {
		return locate(factoryId, fallbackClassName, null);
	}

	static Object locate(String factoryId, 
			             String fallbackClassName,
			             ClassLoader classLoader) throws FactoryConfigurationError {
		try {
			String prop = System.getProperty(factoryId);
			if (prop != null) {
				return newInstance(prop, classLoader);
			}
		} catch (Exception e) {
		}

		try {
			String configFile = System.getProperty("java.home") +
					              File.separator + "lib" + File.separator +
					              "jaxp.properties";
			File f = new File(configFile);
			if (f.exists()) {
				Properties props = new Properties();
				props.load(new FileInputStream(f));
				String factoryClassName = props.getProperty(factoryId);
				return newInstance(factoryClassName, classLoader);
			}
		} catch (Exception e) {
		}

		String serviceId = "META-INF/services/" + factoryId;
		try {
			InputStream is = null;

			if (classLoader == null) {
				is = ClassLoader.getSystemResourceAsStream(serviceId);
			} else {
				is = classLoader.getResourceAsStream(serviceId);
			}

			if (is != null) {
				BufferedReader br = new BufferedReader(new InputStreamReader(
						is, "UTF-8"));
				String factoryClassName = br.readLine();
				br.close();

				if (factoryClassName != null && !"".equals(factoryClassName)) {
					return newInstance(factoryClassName, classLoader);
				}
			}
		} catch (Exception ex) {
		}

		if (fallbackClassName == null) {
			throw new FactoryConfigurationError("Unable to locate factory for "
					+ factoryId + ".", null);
		}
		return newInstance(fallbackClassName, classLoader);
	}

	private static Object newInstance(String className, ClassLoader classLoader)
			throws FactoryConfigurationError {
		try {
			Class spiClass;
			if (classLoader == null) {
				spiClass = Class.forName(className);
			} else {
				spiClass = classLoader.loadClass(className);
			}
			return spiClass.newInstance();
		} catch (ClassNotFoundException x) {
			throw new FactoryConfigurationError("Requested factory "
					+ className + " cannot be located.", x);
		} catch (Exception x) {
			throw new FactoryConfigurationError("Requested factory "
					+ className + " could not be instantiated: " + x, x);
		}
	}

}