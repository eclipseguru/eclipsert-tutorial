package hello.persistence.cloud.osgi;

import hello.persistence.cloud.PreferenceBasedGreetingServiceImpl;
import hello.service.GreetingService;

import java.util.Collection;

import org.eclipse.gyrex.cloud.environment.INodeEnvironment;

import org.osgi.service.component.ComponentContext;

/**
 * OSGi service component for providing a {@link GreetingService} OSGi service.
 * <p>
 * This class is specified in the {@code OSGI-INF/prefs-greeting-service.xml}
 * OSGi declarative service component definition. It will be lazily instantiated
 * by the OSGi runtime environment when all required dependencies are available.
 * </p>
 */
public class PreferenceBasedGreetingServiceComponent implements GreetingService {

	private PreferenceBasedGreetingServiceImpl service;

	public void activate(final ComponentContext context) {
		final INodeEnvironment environment = getService(context, INodeEnvironment.class);
		service = new PreferenceBasedGreetingServiceImpl(environment.getNodeId());
	}

	public void deactivate(final ComponentContext context) {
		service = null;
	}

	@Override
	public Collection<String> getGreetings() throws Exception {
		// delegate to the real service
		return getService().getGreetings();
	}

	public GreetingService getService() {
		return service;
	}

	/**
	 * Utility method which retrieves a service from the component context using
	 * its simple name.
	 * 
	 * @param context
	 * @param serviceInterface
	 * @return the service instance
	 * @throws IllegalStateException
	 *             if the service could not be found
	 */
	protected <T> T getService(final ComponentContext context, final Class<T> serviceInterface) throws IllegalStateException {
		@SuppressWarnings("unchecked")
		final T service = (T) context.locateService(serviceInterface.getSimpleName());
		if (null == service) {
			throw new IllegalStateException(String.format("Please check the component definition. Service '%s' (type %s) could not be located!", serviceInterface.getSimpleName(), serviceInterface.getName()));
		}
		return service;
	}

	@Override
	public void processGreetings() throws Exception {
		// delegate to the real service
		getService().processGreetings();
	}

	@Override
	public void sayHello(final String greeting) throws Exception {
		// delegate to the real service
		getService().sayHello(greeting);
	}
}
