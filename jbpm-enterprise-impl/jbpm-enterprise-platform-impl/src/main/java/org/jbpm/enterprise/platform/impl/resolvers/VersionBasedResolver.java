package org.jbpm.enterprise.platform.impl.resolvers;

import java.util.UUID;

import org.jbpm.enterprise.platform.ExecutionEngine;
import org.jbpm.enterprise.platform.ExecutionEngineResolver;
import org.jbpm.enterprise.platform.RequestContext;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;

public class VersionBasedResolver implements ExecutionEngineResolver {

	private UUID myUUID;
	private BundleContext bundleContext;
	
	public VersionBasedResolver(BundleContext bundleContext) {
		this.bundleContext = bundleContext;
	}
	
	public UUID getUUID() {
		if (this.myUUID == null) {
			this.myUUID = new UUID("VersionBasedResolver".hashCode(), "Version1".hashCode());;
		}
		return this.myUUID;
	}

	public boolean accepts(RequestContext requestContext) {
		Object versionProp = requestContext.getProperty("version");
		
		if (versionProp != null) {
			return true;
		}
		return false;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ExecutionEngine lookUpExecutionEngine(RequestContext requestContext) {
		
		String filter =  "(version=" + requestContext.getProperty("version") + ")";
		try {
			ServiceReference[] refs = bundleContext.getServiceReferences(ExecutionEngine.class.getName(), filter);
			if (refs.length >= 1) {
				return (ExecutionEngine)bundleContext.getService(refs[0]);
			}
		} catch (InvalidSyntaxException e) {
			
			e.printStackTrace();
		}
		return null;
	}

}
