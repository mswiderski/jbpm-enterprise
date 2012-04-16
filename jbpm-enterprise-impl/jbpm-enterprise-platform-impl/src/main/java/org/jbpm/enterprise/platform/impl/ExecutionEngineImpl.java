package org.jbpm.enterprise.platform.impl;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.drools.KnowledgeBase;
import org.drools.runtime.StatefulKnowledgeSession;
import org.jbpm.enterprise.platform.ExecutionEngine;
import org.jbpm.enterprise.platform.ExecutionEngineCallback;
import org.jbpm.enterprise.platform.ExecutionEngineConfiguration;
import org.jbpm.enterprise.platform.SessionMappingStrategy;
import org.jbpm.enterprise.platform.SessionDelegate;

public class ExecutionEngineImpl implements ExecutionEngine {

	protected KnowledgeBase knowledgeBase;
	protected SessionMappingStrategy strategy;
	protected ExecutionEngineCallback callback;
	protected ExecutionEngineConfiguration config;
	protected ClassLoader bundleClassLoader;
	protected ExecutionEngineBuilder builder;
	
	protected Map<Integer, SessionDelegate> localCache = new ConcurrentHashMap<Integer, SessionDelegate>();
	
	public ExecutionEngineImpl(ExecutionEngineConfiguration config, ClassLoader bundleClassLoader) {
		this.config = config;
		this.bundleClassLoader = bundleClassLoader;
	}
	
	public ExecutionEngineCallback getCallback() {
		return callback;
	}

	public void setCallback(ExecutionEngineCallback callback) {
		this.callback = callback;
	}

	public SessionMappingStrategy getStrategy() {
		return strategy;
	}

	public void setStrategy(SessionMappingStrategy strategy) {
		this.strategy = strategy;
	}

	public void setKnowledgeBase(KnowledgeBase kBase) {
		
		this.knowledgeBase = kBase;
	}
	
	public KnowledgeBase getKnowledgeBase() {
	
		return this.knowledgeBase;
	}

	public SessionDelegate getStatelessSession() {
		return new StatelessSessionDelegateImpl(this.knowledgeBase.newStatelessKnowledgeSession());
	}

	public SessionDelegate getSession(String businessKey) {
		int internalId = strategy.resolveIdByBusinessKey(businessKey);
		if (internalId == -1) {
			StatefulKnowledgeSession session = builder.retrieveSession(this.config, callback, strategy, businessKey, this.knowledgeBase, this.bundleClassLoader);
			StatefulSessionDelegateImpl delegate = new StatefulSessionDelegateImpl(session, this);
			localCache.put(session.getId(), delegate);
			
			return delegate;
		} else {
			return (SessionDelegate) localCache.get(internalId);
		}
	}

	public SessionDelegate getSessionById(int id) {
		if (localCache.containsKey(id)) {
			return (SessionDelegate) localCache.get(id);
		}
		
		return null;
	}

	public Object getHumanTaskConnector() {
		// TODO Auto-generated method stub
		return null;
	}

	public UUID getUUID() {
		// TODO make it unique regardless of restarts
		return UUID.randomUUID();
	}

	public ExecutionEngineBuilder getBuilder() {
		return builder;
	}

	public void setBuilder(ExecutionEngineBuilder builder) {
		this.builder = builder;
	}

	public void disposeSession(SessionDelegate session) {
		disposeSession((StatefulKnowledgeSession)session.getDelegate());
		
	}
	
	public void disposeSession(StatefulKnowledgeSession session) {
		this.localCache.remove(session.getId());
		session.dispose();
		
	}

}
