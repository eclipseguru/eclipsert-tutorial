/*******************************************************************************
 * Copyright (c) 2012 AGETO Service GmbH and others.
 * All rights reserved.
 *  
 * This program and the accompanying materials are made available under the 
 * terms of the Eclipse Distribution License v1.0 which accompanies this distribution,
 * and is available at https://www.eclipse.org/org/documents/edl-v10.html.
 *
 * Contributors:
 *     Gunnar Wagenknecht - initial API and implementation
 *******************************************************************************/
/*******************************************************************************
 * This is sample code. Use at your own risk. It comes WITHOUT any warranty.
 * Released to public domain. Please copy & paste as you wish.
 *
 * Initial Contribution: Gunnar Wagenknecht
 *******************************************************************************/
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