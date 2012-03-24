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
