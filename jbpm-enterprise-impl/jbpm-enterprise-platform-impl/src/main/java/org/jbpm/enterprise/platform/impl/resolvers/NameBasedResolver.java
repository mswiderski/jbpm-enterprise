package org.jbpm.enterprise.platform.impl.resolvers;

import java.util.UUID;

import org.jbpm.enterprise.platform.ExecutionEngine;
import org.jbpm.enterprise.platform.ExecutionEngineResolver;
import org.jbpm.enterprise.platform.RequestContext;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;

public class NameBasedResolver implements ExecutionEngineResolver {

	private UUID myUUID;
	private BundleContext bundleContext;
	
	public NameBasedResolver(BundleContext bundleContext) {
		this.bundleContext = bundleContext;
	}
	
	public UUID getUUID() {
		if (this.myUUID == null) {
			this.myUUID = new UUID("NameBasedResolver".hashCode(), "Version1".hashCode());
		}
		return this.myUUID;
	}

	public boolean accepts(RequestContext requestContext) {
		String nameProp = (String)requestContext.getProperty("name");
		
		if (nameProp != null && nameProp.trim().length() > 0) {
			return true;
		}
		return false;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ExecutionEngine lookUpExecutionEngine(RequestContext requestContext) {

		String filter =  "(name=" + requestContext.getProperty("name") + ")";
		try {
			ServiceReference[] refs = bundleContext.getServiceReferences(ExecutionEngine.class.getName(), filter);
			if (refs != null && refs.length >= 1) {
				return (ExecutionEngine)bundleContext.getService(refs[0]);
			} else { 
				throw new IllegalArgumentException("Cannot find Execution engine with following criteria: " + filter);
			}
		} catch (InvalidSyntaxException e) {
			
			e.printStackTrace();
		}
		return null;
	}

}
