package org.jbpm.enterprise.platform.impl.resolvers;

import java.util.UUID;

import org.jbpm.enterprise.platform.ExecutionEngine;
import org.jbpm.enterprise.platform.ExecutionEngineResolver;
import org.jbpm.enterprise.platform.RequestContext;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;

public class UUIDBasedResolver implements ExecutionEngineResolver {

	private UUID myUUID;
	private BundleContext bundleContext;
	
	public UUIDBasedResolver(BundleContext bundleContext) {
		this.bundleContext = bundleContext;
	}
	
	public UUID getUUID() {
		if (this.myUUID == null) {
			this.myUUID = new UUID("UUIDBasedResolver".hashCode(), "Version1".hashCode());
		}
		return this.myUUID;
	}

	public boolean accepts(RequestContext requestContext) {
		Object uuidProp = requestContext.getProperty("uuid");
		Object processInstanceProp = requestContext.getProperty("processInstanceId");
		
		if (uuidProp != null || processInstanceProp != null) {
			return true;
		}
		return false;
	}

	public ExecutionEngine lookUpExecutionEngine(RequestContext requestContext) {
		Object uuidProp = requestContext.getProperty("uuid");
		Object processInstanceProp = requestContext.getProperty("processInstanceId");
		
		if (uuidProp == null) {
			uuidProp = processInstanceProp.toString().split("@")[1];
		}
		
		String filter = "(uuid=" + uuidProp + ")";
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
