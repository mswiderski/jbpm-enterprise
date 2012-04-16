package org.jbpm.enterprise.platform;


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
	
	public void setOwner(String owner);
	
	public String getPersistenceUnit();
	
	public boolean isPersistenceEnabled();
	
	
	public void setChangeSet(String changesetLocation);
	
	public String getChangeSet();
}
