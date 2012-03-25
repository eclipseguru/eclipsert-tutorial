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

import org.eclipse.gyrex.jobs.IJobContext;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A sample Eclipse {@link Job} which processes greetings.
 * <p>
 * Eclipse Jobs are executed in the background and allow to perform batch like
 * processing of data.
 * </p>
 */
public class ProcessGreetingsJob extends Job {

	private static final Logger LOG = LoggerFactory.getLogger(ProcessGreetingsJob.class);

	private final GreetingService greetingService;
	private final IJobContext jobContext;

	/**
	 * Creates a new instance.
	 */
	public ProcessGreetingsJob(final GreetingService greetingService, final IJobContext jobContext) {
		super("Process Greetings");
		this.greetingService = greetingService;
		this.jobContext = jobContext;
		setPriority(LONG);
		setSystem(false);
	}

	public GreetingService getGreetingService() {
		return greetingService;
	}

	public IJobContext getJobContext() {
		return jobContext;
	}

	@Override
	protected IStatus run(final IProgressMonitor monitor) {
		try {
			LOG.info("Job {} started.", getJobContext().getJobId());
			getGreetingService().processGreetings();
			LOG.info("Job {} finished.", getJobContext().getJobId());
			return Status.OK_STATUS;
		} catch (final IllegalStateException e) {
			LOG.error("Unable to process greetings. {}", e.getMessage(), e);
			return new Status(IStatus.CANCEL, "hello.cloud", "Unable to processing greetings. " + e.getMessage(), e);
		} catch (final Exception e) {
			LOG.error("Error processing greetings: {}", ExceptionUtils.getRootCauseMessage(e), e);
			return new Status(IStatus.ERROR, "hello.cloud", "Error processing greetings. " + e.getMessage(), e);
		}
	}

}
