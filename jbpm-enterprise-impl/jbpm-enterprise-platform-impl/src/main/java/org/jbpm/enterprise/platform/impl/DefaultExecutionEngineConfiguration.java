package org.jbpm.enterprise.platform.impl;

import java.util.HashMap;
import java.util.Map;

import org.drools.builder.ResourceType;
import org.drools.io.Resource;
import org.jbpm.enterprise.platform.ExecutionEngineConfiguration;

public class DefaultExecutionEngineConfiguration implements ExecutionEngineConfiguration {
	
	private Map<Resource, ResourceType> resources = new HashMap<Resource, ResourceType>();
	private String owner;
	private String persistenceUnit;
	private String changeSet;

	public String getOwner() {
		return owner;
	}

	public String getPersistenceUnit() {
		
		return persistenceUnit;
	}

	public boolean isPersistenceEnabled() {
		
		return persistenceUnit != null;
	}

	public void addResource(Resource resource, ResourceType type) {
		resources.put(resource, type);
	}

	public void setResources(Map<Resource, ResourceType> resources) {
		resources.putAll(resources);
	}

	public Map<Resource, ResourceType> getResources() {
		
		return resources;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public void setPersistenceUnit(String persistenceUnit) {
		this.persistenceUnit = persistenceUnit;
	}

	public void setChangeSet(String changesetLocation) {
		this.changeSet = changesetLocation;
		
	}

	public String getChangeSet() {		
		return this.changeSet;
	}

}
