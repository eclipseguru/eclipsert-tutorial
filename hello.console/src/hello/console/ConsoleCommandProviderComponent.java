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
package hello.console;

import org.eclipse.gyrex.common.console.BaseCommandProvider;

import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.eclipse.osgi.framework.console.CommandProvider;

/**
 * Provides console commands to the Equinox OSGi console.
 */
public class ConsoleCommandProviderComponent extends BaseCommandProvider implements CommandProvider {

	public ConsoleCommandProviderComponent() {
		registerCommand("processGreetings", ProcessGreetingsCommand.class);
		registerCommand("echo", EchoCommand.class);
	}

	public void _hello(final CommandInterpreter ci) {
		execute(ci);
	}

	@Override
	protected String getCommandName() {
		return "hello";
	}
}
