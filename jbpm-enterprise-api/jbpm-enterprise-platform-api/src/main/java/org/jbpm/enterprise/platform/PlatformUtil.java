package org.jbpm.enterprise.platform;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

public class PlatformUtil {

//	public static void configureCompositeClassloader(ExecutionEngineFactory eeFactory, ClassLoader bundleClassLoader) {
//		
//		CompositeClassLoader cl = new CompositeClassLoader();
//		cl.addClassLoader(eeFactory.getClass().getClassLoader());
//		cl.addClassLoader(bundleClassLoader);
//		Thread.currentThread().setContextClassLoader(cl);
//	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static ExecutionEngine getExecutionEngine(BundleContext bundleContext, RequestContext context) {
		ServiceReference srf = bundleContext.getServiceReference(ExecutionEngineResolverManager.class.getName());
		ExecutionEngineResolverManager resolverManager = (ExecutionEngineResolverManager) bundleContext.getService(srf);
		
		ExecutionEngine engine = resolverManager.findAndLookUp(context);
		
		return engine;
	}
}
