/**
 * Copyright (c) 2012 AGETO Service GmbH and others.
 * All rights reserved.
 *
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Gunnar Wagenknecht - initial API and implementation
 */
package hello.service;

import java.util.Collection;

/**
 * A simple service for demoing some Gyrex features.
 */
public interface GreetingService {

	/**
	 * Returns a list of all available greetings.
	 * 
	 * @return a collection of all greetings
	 * @throws Exception
	 *             in case of errors
	 */
	Collection<String> getGreetings() throws Exception;

	/**
	 * Processes all greeting.
	 * 
	 * @throws Exception
	 *             in case of errors
	 */
	void processGreetings() throws Exception;

	/**
	 * Adds a new greeting.
	 * 
	 * @param greeting
	 *            the greeting to add (may not be <code>null</code>)
	 * @throws Exception
	 *             in case of errors
	 */
	void sayHello(final String greeting) throws Exception;

}