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
	protected ClassLoader bundleClassLoader;
	protected ExecutionEngineBuilder builder;
	
	protected Map<Integer, StatefulKnowledgeSession> localCache = new ConcurrentHashMap<Integer, StatefulKnowledgeSession>();
	
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
		configureClassLoader();
		return this.knowledgeBase.newStatelessKnowledgeSession();
	}

	public StatefulKnowledgeSession getSession(String businessKey) {
		configureClassLoader();
		int internalId = strategy.resolveIdByBusinessKey(businessKey);
		if (internalId == -1) {
			StatefulKnowledgeSession session = builder.retrieveSession(this.config, callback, strategy, businessKey, this.knowledgeBase, this.bundleClassLoader);
			SessionDelegateImpl delegate = new SessionDelegateImpl(session, this);
			localCache.put(session.getId(), delegate);
			
			return delegate;
		} else {
			return localCache.get(internalId);
		}
	}

	public StatefulKnowledgeSession getSessionById(int id) {
		configureClassLoader();
		if (localCache.containsKey(id)) {
			return localCache.get(id);
		}
		
		return null;
	}

	public Object getHumanTaskConnector() {
		configureClassLoader();
		// TODO Auto-generated method stub
		return null;
	}

	public UUID getUUID() {
		// TODO make it unique regardless of restarts
		return UUID.randomUUID();
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

	public void disposeSession(StatefulKnowledgeSession session) {
		configureClassLoader();
		this.localCache.remove(session.getId());
		session.dispose();
		
	}
	
	protected void configureClassLoader() {
		// set context class loader to allow CompositeClassLoader of Drools and jBPM to locate classes
		Thread.currentThread().setContextClassLoader(this.bundleClassLoader);
	}

}
