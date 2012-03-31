package org.jbpm.enterprise.platform.osgi;

import java.util.Dictionary;
import java.util.Hashtable;

import org.jbpm.enterprise.platform.DefaultExecutionEngineResolverManager;
import org.jbpm.enterprise.platform.ExecutionEngineResolverManager;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class PlatformBundleActivator implements BundleActivator {
	
	@SuppressWarnings("rawtypes")
	private ServiceRegistration eeResolverManagerServiceReg;

	public void start(BundleContext context) throws Exception {
		System.out.println("jBPM enterprise :: Starting Platform Activator");
		Dictionary<String, String> properties = new Hashtable<String, String>();
		properties.put("version", context.getBundle().getVersion().toString());
		eeResolverManagerServiceReg = context.registerService(ExecutionEngineResolverManager.class.getName(),
				new DefaultExecutionEngineResolverManager(), properties);
		
	}

	public void stop(BundleContext context) throws Exception {
		
		if (eeResolverManagerServiceReg != null) {
			eeResolverManagerServiceReg.unregister();
		}
		
	}

}
