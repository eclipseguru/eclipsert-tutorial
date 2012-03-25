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
package hello.job;

import hello.service.GreetingService;
import hello.service.GreetingServiceProvider;

import java.util.Collections;

import org.eclipse.gyrex.jobs.IJobContext;
import org.eclipse.gyrex.jobs.provider.JobProvider;

import org.eclipse.core.runtime.jobs.Job;

/**
 * OSGi service component for providing {@link HelloCloudJob} instances.
 */
public class HelloJobProviderComponent extends JobProvider implements GreetingServiceProvider {

	public static final String ID = "process.greetings.job";
	private GreetingService service;

	/**
	 * Creates a new instance.
	 */
	public HelloJobProviderComponent() {
		super(Collections.singleton(ID));
	}

	@Override
	public Job createJob(final String typeId, final IJobContext context) throws Exception {
		if (ID.equals(typeId)) {
			return new ProcessGreetingsJob(getService(), context);
		}
		return null;
	}

	@Override
	public GreetingService getService() {
		return service;
	}

	/**
	 * Sets the greeting service.
	 * <p>
	 * It is a <em>required</em> dependency as specified by <strong>
	 * <code>cardinality="1..1"</code> </strong> in the OSGi component
	 * definition file.
	 * </p>
	 * <p>
	 * This method will be called by the OSGi component runtime <em>when</em>
	 * the component is initialized as specified by <strong>
	 * <code>policy="static"</code> </strong> in the OSGi component definition
	 * file.
	 * </p>
	 * 
	 * <pre>
	 * &lt;reference bind="setGreetingService" cardinality="1..1" interface="hello.service.GreetingService" name="GreetingService" policy="static"/&gt;
	 * </pre>
	 * 
	 * @param service
	 *            the service to set
	 */
	public void setGreetingService(final GreetingService service) {
		this.service = service;
	}
}
