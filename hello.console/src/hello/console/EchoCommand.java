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

import org.eclipse.gyrex.common.console.Command;

import org.apache.commons.lang.StringUtils;
import org.kohsuke.args4j.Argument;

/**
 * A command using ARGS4J based injection of arguments and options.
 */
public class EchoCommand extends Command {

	@Argument(index = 0, metaVar = "WHATEVER", usage = "a string that will be printed")
	private String text;

	public EchoCommand() {
		super("echos a given text");
	}

	@Override
	protected void doExecute() throws Exception {
		printf(StringUtils.trimToEmpty(text));
	}

}
