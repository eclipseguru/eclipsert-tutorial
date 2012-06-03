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
package hello.jaxrs;

import java.util.Date;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Hello World JAX-RS Resource
 */
@Path("/")
public class HelloResource {

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String sayHello() {
		return sayHello("there. It's now " + new Date());
	}

	@GET
	@Path("/{name}")
	@Produces(MediaType.TEXT_PLAIN)
	public String sayHello(@PathParam("name") final String name) {
		return String.format("Hello %s!", name);
	}
}
