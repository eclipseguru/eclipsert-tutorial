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