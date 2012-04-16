package org.jbpm.enterprise.platform.impl;

import java.util.Map;

import org.drools.runtime.StatelessKnowledgeSession;
import org.jbpm.enterprise.platform.SessionDelegate;

public class StatelessSessionDelegateImpl implements SessionDelegate {
	
	private StatelessKnowledgeSession delegate;
	
	public StatelessSessionDelegateImpl(StatelessKnowledgeSession session) {
		this.delegate = session;
	}

	public Object getDelegate() {
		return this.delegate;
	}

	public String startProcess(String id) {
		// stateless session cannot start processes
		return null;
	}

	public int getId() {
		// stateless session does not have id
		return -1;
	}

	public void dispose() {
		// do nothing

	}

	public String startProcess(String id, Map<String, Object> properties) {
		// stateless session cannot start processes
		return null;
	}

	public void signalEvent(String type, Object event) {
		// TODO Auto-generated method stub
		
	}

	public void signalEvent(String type, Object event, long processInstanceId) {
		// TODO Auto-generated method stub
		
	}

}
