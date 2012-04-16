package org.jbpm.enterprise.platform.impl;

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

	public long startProcess(String id) {
		// TODO Auto-generated method stub
		return -1;
	}

	public int getId() {
		// stateless session does not have id
		return -1;
	}

	public void dispose() {
		// do nothing

	}

}
