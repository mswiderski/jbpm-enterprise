package org.jbpm.enterprise.platform.impl;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.drools.KnowledgeBase;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.process.ProcessInstance;
import org.jbpm.enterprise.platform.ExecutionEngine;
import org.jbpm.enterprise.platform.ExecutionEngineCallback;
import org.jbpm.enterprise.platform.ExecutionEngineConfiguration;
import org.jbpm.enterprise.platform.ExecutionEngineMapperStrategy;
import org.jbpm.enterprise.platform.SessionDelegate;

public class ExecutionEngineImpl implements ExecutionEngine {

	private UUID myUUID;
	
	protected KnowledgeBase knowledgeBase;
	protected ExecutionEngineMapperStrategy strategy;
	protected ExecutionEngineCallback callback;
	protected ExecutionEngineConfiguration config;
	protected ClassLoader bundleClassLoader;
	protected ExecutionEngineBuilder builder;
	
	protected Map<Integer, SessionDelegate> localCache = new ConcurrentHashMap<Integer, SessionDelegate>();
	
	public ExecutionEngineImpl(ExecutionEngineConfiguration config, ClassLoader bundleClassLoader) {
		this.config = config;
		this.bundleClassLoader = bundleClassLoader;
		
		if (this.config.getOwner() == null || this.config.getOwner().length() < 1) {
			throw new RuntimeException("Owner is not configured, execution engine cannot be created. Please set owner on configuration object");
		}
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
		if (this.myUUID == null) {
			this.myUUID = new UUID("ExecutionEngineImpl".hashCode(), config.getOwner().hashCode());;
		}
		return this.myUUID;
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

	public void disposeSession(SessionDelegate session) {
		disposeSession((StatefulKnowledgeSession)session.getDelegate());
		
	}
	
	public void disposeSession(StatefulKnowledgeSession session) {
		this.localCache.remove(session.getId());
		session.dispose();
		
	}

}
