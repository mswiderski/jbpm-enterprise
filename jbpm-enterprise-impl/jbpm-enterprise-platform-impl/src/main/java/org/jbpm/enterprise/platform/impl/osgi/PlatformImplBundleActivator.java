package org.jbpm.enterprise.platform.impl.osgi;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import org.jbpm.enterprise.platform.ExecutionEngineFactory;
import org.jbpm.enterprise.platform.ExecutionEngineResolver;
import org.jbpm.enterprise.platform.ExecutionEngineResolverManager;
import org.jbpm.enterprise.platform.impl.ExecutionEngineFactoryImpl;
import org.jbpm.enterprise.platform.impl.resolvers.UUIDBasedResolver;
import org.jbpm.enterprise.platform.impl.resolvers.ValidTimeBasedResolver;
import org.jbpm.enterprise.platform.impl.resolvers.VersionBasedResolver;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;

@SuppressWarnings({"rawtypes", "unchecked"})
public class PlatformImplBundleActivator implements BundleActivator {

	
	private ServiceRegistration eeFactoryServiceReg;
	
	private List<ExecutionEngineResolver> registeredResolvers = new ArrayList<ExecutionEngineResolver>();
	
	public void start(BundleContext context) throws Exception {
		System.out.println("jBPM enterprise :: Starting Platform IMPL Activator");
		Dictionary<String, String> properties = new Hashtable<String, String>();
		properties.put("version", context.getBundle().getVersion().toString());

		eeFactoryServiceReg = context.registerService(ExecutionEngineFactory.class.getName(),
				new ExecutionEngineFactoryImpl(), properties);
		
		// register any resolvers that this impl bundle introduces
		ServiceReference resolverManagerRef = context.getServiceReference(ExecutionEngineResolverManager.class.getName());
		
		ExecutionEngineResolverManager resolverManager = (ExecutionEngineResolverManager)context.getService(resolverManagerRef);
		String bundleId = Long.toString(context.getBundle().getBundleId());
		
		ExecutionEngineResolver versionBasedResolver = new VersionBasedResolver(context);
		resolverManager.register(bundleId, versionBasedResolver);
		registeredResolvers.add(versionBasedResolver);
		
		ExecutionEngineResolver validTimeBasedResolver = new ValidTimeBasedResolver(context);
		resolverManager.register(bundleId, validTimeBasedResolver);
		registeredResolvers.add(validTimeBasedResolver);
		
		ExecutionEngineResolver uuidBasedResolver = new UUIDBasedResolver(context);
		resolverManager.register(bundleId, uuidBasedResolver);
		registeredResolvers.add(uuidBasedResolver);
	}

	public void stop(BundleContext context) throws Exception {
		if (eeFactoryServiceReg != null) {
			eeFactoryServiceReg.unregister();
		}

		if (!registeredResolvers.isEmpty()) {
			ServiceReference resolverManagerRef = context.getServiceReference(ExecutionEngineResolverManager.class.getName());
			
			if (resolverManagerRef != null) {
				ExecutionEngineResolverManager resolverManager = (ExecutionEngineResolverManager)context.getService(resolverManagerRef);
				String bundleId = Long.toString(context.getBundle().getBundleId());
				
				for (ExecutionEngineResolver resolver : registeredResolvers) {
					resolverManager.unregister(bundleId, resolver.getUUID());
				}
			}
			registeredResolvers.clear();
		}
	}

}
