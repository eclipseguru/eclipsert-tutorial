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

/**
 * A provider that provides access to the {@link GreetingService} instance.
 * <p>
 * This concept might be used where it's inappropriate to couple domain code
 * with OSGi concepts.
 * </p>
 */
public interface GreetingServiceProvider {
	/**
	 * Provides access to the greeting service instance.
	 * <p>
	 * Note, the returned instance should not be held onto for a longer period
	 * of time. It's lifecycle may be different from the caller's lifecycle.
	 * </p>
	 * 
	 * @return the greeting service instance (maybe <code>null</code> if the
	 *         service is not available)
	 */
	GreetingService getService();
}