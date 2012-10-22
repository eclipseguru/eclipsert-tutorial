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
package hello.jaxrs.json;

import hello.jaxrs.json.model.Greeting;
import hello.service.GreetingService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

/**
 * Hello World JAX-RS Resource.
 */
@Path("/greetings")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class JsonGreetingResource {

	@Context
	private GreetingService greetingService;

	@GET
	public List<Greeting> getGreetings() {
		// read greetings
		Collection<String> messages;
		try {
			messages = getGreetingService().getGreetings();
		} catch (final IllegalStateException e) {
			// no service is available; lets report that properly
			throw new WebApplicationException(e, Status.SERVICE_UNAVAILABLE);
		} catch (final Exception e) {
			// this looks like an issue deeper in some underlying code; we should log this properly
			throw new WebApplicationException(e);
		}

		if (messages.isEmpty()) {
			return Collections.emptyList();
		}

		final List<Greeting> greetings = new ArrayList<Greeting>(messages.size());
		for (final String message : messages) {
			final Greeting greeting = new Greeting();
			greeting.setMessage(message);
			greetings.add(greeting);
		}
		return greetings;
	}

	public GreetingService getGreetingService() {
		return greetingService;
	}
}
