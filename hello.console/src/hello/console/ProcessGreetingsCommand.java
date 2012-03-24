package hello.console;

import hello.job.HelloJobProviderComponent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.eclipse.gyrex.common.console.Command;
import org.eclipse.gyrex.context.IRuntimeContext;
import org.eclipse.gyrex.context.registry.IRuntimeContextRegistry;
import org.eclipse.gyrex.jobs.IJob;
import org.eclipse.gyrex.jobs.manager.IJobManager;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

import org.kohsuke.args4j.Argument;

/**
 * A command for scheduling the processing job
 */
public class ProcessGreetingsCommand extends Command {

	/** TEST_LOCK */
	private static final String GLOBAL_LOCK_NAME = "testLock";

	@Argument(index = 0, usage = "The path to the context in which the job should be executed. This is used for multi-tenency purposes. The default is '/' which is suitable for the demo.", required = false, metaVar = "CONTEXT-PATH")
	protected String contextPath = "/";

	public ProcessGreetingsCommand() {
		super("schedules a greetings processing job for execution");
	}

	@Override
	protected void doExecute() throws Exception {
		// get the context
		final IRuntimeContext context = getRuntimeContext(new Path(contextPath));

		// get the job manager
		final IJobManager jobManager = context.get(IJobManager.class);
		if (null == jobManager) {
			throw new IllegalStateException("no context registry is available; please check installation or context configuration");
		}

		// create job
		final Map<String, String> parameter = new HashMap<String, String>(2);
		// (using a shared context specific lock to
		// prevent parallel execution within the cluster)
		parameter.put(IJobManager.LOCK_ID, getLockId(context));
		final IJob job = jobManager.createJob(HelloJobProviderComponent.ID, HelloJobProviderComponent.ID.concat("_run_").concat(String.valueOf(UUID.randomUUID())), parameter);

		// queue job (using default queue)
		jobManager.queueJob(job.getId(), null, "OSGi Console");

		// done
		printf("Processing of greetings has been queue as job '%s'.", job.getId());
	}

	private BundleContext getBundleContext() {
		/*
		 * W are lazy here and use te FrameworkUtil method.
		 * This returns the OSGi bundle for a class loaded by
		 * an OSGi bundle class loader.
		 *
		 * Another possible approach is to implement an Activator
		 * that will keep the BundleContext in a static instance
		 * variable while the bundle is active.
		 */
		final Bundle bundle = FrameworkUtil.getBundle(getClass());
		if (bundle == null) {
			throw new IllegalStateException("not loaded by OSGi class loader (!?)");
		}
		return bundle.getBundleContext();
	}

	private String getLockId(final IRuntimeContext context) {
		// this hashing is not very smart but it should work for the demo
		return String.format("job-lock-%d", context.getContextPath().toString().hashCode());
	}

	/**
	 * Returns the runtime context to use.
	 * <p>
	 * In Gyrex pretty much everything is associated to a runtime context. All
	 * the way across the sample code we just use the ROOT context. Therefor,
	 * it's easy to retrieve it. In a real-word multi-tenancy application each
	 * of your tenants might have its own context or it might be even more
	 * granular. The {@link IRuntimeContextRegistry} comes to the rescue and
	 * allows to lookup contexts.
	 * </p>
	 * 
	 * @return the context
	 */
	private IRuntimeContext getRuntimeContext(final IPath contextPath) {
		/*
		 * Injecting OSGi services into Gyrex commands is not supported
		 * right now. It should be possible, though. Hey that's an area
		 * for contributing to Gyrex. :)
		 *
		 * For demonstration purposes we'll use the brutal manual way of
		 * working with OSGi services. Look below what code has to be
		 * written with DS is not an option.
		 */

		// 1: get bundle context
		final BundleContext context = getBundleContext();

		// 2: find service reference
		final ServiceReference<IRuntimeContextRegistry> reference = context.getServiceReference(IRuntimeContextRegistry.class);
		if (null == reference) {
			throw new IllegalStateException("no context registry is available; please check installation");
		}
		try {
			// 3: acquire service
			final IRuntimeContextRegistry contextRegistry = context.getService(reference);
			if (null == contextRegistry) {
				throw new IllegalStateException("context registry is not available anymore; please re-try");
			}

			// 4: do something with the service
			final IRuntimeContext runtimeContext = contextRegistry.get(contextPath);
			if (null == runtimeContext) {
				throw new IllegalStateException(String.format("context '%s' is not defined; please check system configuration", contextPath.toString()));
			}
			return runtimeContext;
		} finally {
			// 5: unget service (never forget this)
			context.ungetService(reference);
		}
	}
}
