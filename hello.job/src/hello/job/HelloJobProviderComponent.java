/**
 * Copyright (C) 2012 AGETO Service GmbH and others.
 *
 * This program and the accompanying materials are made available
 * under the terms of the Eclipse Distribution License v1.0 which
 * accompanies this distribution, is reproduced below, and is
 * available at http://www.eclipse.org/org/documents/edl-v10.php
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are permitted provided that the following
 * conditions are met:
 *
 * - Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * - Redistributions in binary form must reproduce the above
 *   copyright notice, this list of conditions and the following
 *   disclaimer in the documentation and/or other materials provided
 *   with the distribution.
 *
 * - Neither the name of the Eclipse Foundation, Inc. nor the
 *   names of its contributors may be used to endorse or promote
 *   products derived from this software without specific prior
 *   written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND
 * CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package hello.job;

import hello.service.GreetingService;
import hello.service.GreetingServiceImpl;
import hello.service.GreetingServiceProvider;

import java.util.Collections;

import org.eclipse.gyrex.cloud.environment.INodeEnvironment;
import org.eclipse.gyrex.jobs.IJobContext;
import org.eclipse.gyrex.jobs.provider.JobProvider;

import org.eclipse.core.runtime.jobs.Job;

/**
 * OSGi service component for providing {@link HelloCloudJob} instances.
 */
public class HelloJobProviderComponent extends JobProvider implements GreetingServiceProvider {

	public static final String ID = "hello.job.provider";
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
	 * Sets the node environment.
	 * 
	 * @param environment
	 *            the environment to set
	 */
	public void setEnvironment(final INodeEnvironment environment) {
		service = new GreetingServiceImpl(environment.getNodeId());
	}
}
