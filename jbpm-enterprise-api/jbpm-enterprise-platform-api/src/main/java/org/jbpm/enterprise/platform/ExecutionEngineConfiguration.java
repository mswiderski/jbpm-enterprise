package org.jbpm.enterprise.platform;

import java.util.Map;

import org.drools.builder.ResourceType;
import org.drools.io.Resource;

/**
 * Data holder for all information related to configuration of Execution Engine including Human Task components.
 * 
 * This is a holder that maps indirectly to following configuration files:
 * <ul>
 * 	<li>executionengine-spec.xml</li>
 * 	<li>humantask-spec.xml</li>
 * </ul>
 *
 */
public interface ExecutionEngineConfiguration {

	//TODO define all available configuration points that matches spec files
	
	public String getOwner();
	
	public String getPersistenceUnit();
	
	public boolean isPersistenceEnabled();
	
	public void addResource(Resource resource, ResourceType type);
	
	public void setResources(Map<Resource, ResourceType> resources);
	
	public Map<Resource, ResourceType> getResources(); 
}
