package org.jbpm.enterprise.platform.impl;

import org.jbpm.enterprise.platform.ExecutionEngineConfiguration;

public class DefaultExecutionEngineConfiguration implements ExecutionEngineConfiguration {
	
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
