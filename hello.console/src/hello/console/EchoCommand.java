/**
 * Copyright (c) 2012 Gunnar Wagenknecht and others.
 * All rights reserved.
 *
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Gunnar Wagenknecht - initial API and implementation
 */
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
