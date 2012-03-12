package org.jbpm.enterprise.platform.impl;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.drools.KnowledgeBase;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.StatelessKnowledgeSession;
import org.drools.runtime.process.ProcessInstance;
import org.jbpm.enterprise.platform.ExecutionEngine;
import org.jbpm.enterprise.platform.ExecutionEngineCallback;
import org.jbpm.enterprise.platform.ExecutionEngineConfiguration;
import org.jbpm.enterprise.platform.ExecutionEngineMapperStrategy;

public class ExecutionEngineImpl implements ExecutionEngine {

	protected KnowledgeBase knowledgeBase;
	protected ExecutionEngineMapperStrategy strategy;
	protected ExecutionEngineCallback callback;
	protected ExecutionEngineConfiguration config;
	
	protected ExecutionEngineBuilder builder;
	
	protected Map<Integer, StatefulKnowledgeSession> localCache = new ConcurrentHashMap<Integer, StatefulKnowledgeSession>();
	
	public ExecutionEngineCallback getCallback() {
		return callback;
	}

	public void setCallback(ExecutionEngineCallback callback) {
		this.callback = callback;
	}

	public ExecutionEngineMapperStrategy getStrategy() {
		return strategy;
	}

	public void setStrategy(ExecutionEngineMapperStrategy strategy) {
		this.strategy = strategy;
	}

	public void setKnowledgeBase(KnowledgeBase kBase) {
		
		this.knowledgeBase = kBase;
	}
	
	public KnowledgeBase getKnowledgeBase() {
	
		return this.knowledgeBase;
	}

	public StatelessKnowledgeSession getStatelessSession() {
		
		return this.knowledgeBase.newStatelessKnowledgeSession();
	}

	public StatefulKnowledgeSession getSession(String businessKey) {
		int internalId = strategy.resolveIdByBusinessKey(businessKey);
		if (internalId == -1) {
			StatefulKnowledgeSession session = builder.retrieveSession(this.config, callback, strategy, businessKey, this.knowledgeBase);
			localCache.put(session.getId(), session);
			
			return session;
		} else {
			return localCache.get(internalId);
		}
	}

	public StatefulKnowledgeSession getSessionById(int id) {
		if (localCache.containsKey(id)) {
			return localCache.get(id);
		}
		
		return null;
	}

	public Object getHumanTaskConnector() {
		// TODO Auto-generated method stub
		return null;
	}

	public UUID getUUID() {
		// TODO Auto-generated method stub
		return null;
	}

	public String buildCompositeId(ProcessInstance instance) {
		
		return instance.getId() + "@" + getUUID();
	}

	public String buildCompositeId(String id) {
		
		return id  + "@" + getUUID();
	}

	public ExecutionEngineBuilder getBuilder() {
		return builder;
	}

	public void setBuilder(ExecutionEngineBuilder builder) {
		this.builder = builder;
	}

}
