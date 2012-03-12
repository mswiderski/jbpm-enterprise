package org.jbpm.enterprise.platform.impl;

import java.util.Iterator;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.Resource;
import org.drools.persistence.jpa.JPAKnowledgeService;
import org.drools.runtime.Environment;
import org.drools.runtime.EnvironmentName;
import org.drools.runtime.KnowledgeSessionConfiguration;
import org.drools.runtime.StatefulKnowledgeSession;
import org.jbpm.enterprise.platform.ExecutionEngineCallback;
import org.jbpm.enterprise.platform.ExecutionEngineConfiguration;
import org.jbpm.enterprise.platform.ExecutionEngineMapperStrategy;

public class ExecutionEngineBuilder {
	
	protected EntityManagerFactory emf; 

	public KnowledgeBase buildKnowledgeBase(ExecutionEngineConfiguration config, ExecutionEngineCallback callback) {
		KnowledgeBuilder builder = KnowledgeBuilderFactory.newKnowledgeBuilder();
		
		if (callback != null) {
			callback.preKnowledgeBaseCreate(builder);
		}
		Map<Resource, ResourceType> resources = config.getResources();
		Iterator<Resource> it = resources.keySet().iterator();
		while (it.hasNext()) {
			Resource resource = (Resource) it.next();
			builder.add(resource, resources.get(resource));
		}
		KnowledgeBase kBase = builder.newKnowledgeBase();
		
		if (builder.hasErrors()) {
			throw new RuntimeException("KnowledgeBase build has errors");
		}
		if (callback != null) {
			callback.postKnowledgeBaseCreate(kBase);
		}
		return kBase;
	}
	
	public StatefulKnowledgeSession retrieveSession(ExecutionEngineConfiguration config, ExecutionEngineCallback callback, 
			ExecutionEngineMapperStrategy strategy, String businessKey, KnowledgeBase kbase) {
		Environment environment = KnowledgeBaseFactory.newEnvironment();
		// TODO add required information to the environment
		KnowledgeSessionConfiguration configuration = KnowledgeBaseFactory.newKnowledgeSessionConfiguration();
		// TODO add required information to the configuration
		
		if (callback != null) {
			callback.preKnowledgeSessionCreate(environment, configuration, kbase);
		}
		StatefulKnowledgeSession session = null;
		
		if (config.isPersistenceEnabled()) {
			int internalId = strategy.resolveIdByBusinessKey(businessKey);
			if (emf == null) {
				emf = Persistence.createEntityManagerFactory(config.getPersistenceUnit());
			}
			environment.set(EnvironmentName.ENTITY_MANAGER_FACTORY, emf);

			if (internalId != -1) {
				session = JPAKnowledgeService.loadStatefulKnowledgeSession(internalId, kbase, configuration, environment);
			} else {
				session = JPAKnowledgeService.newStatefulKnowledgeSession(kbase, configuration, environment);
			}
		} else {
			// if there is no persistence involved build new session out of knowledge base
			session = kbase.newStatefulKnowledgeSession(configuration, environment);
		}
//		session.
		
		if (callback != null) {
			callback.postKnowledgeSessionCreate(session, businessKey);
		}
		
		return session;
	}
}
