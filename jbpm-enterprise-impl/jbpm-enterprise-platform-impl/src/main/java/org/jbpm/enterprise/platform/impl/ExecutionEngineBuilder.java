package org.jbpm.enterprise.platform.impl;

import java.util.Properties;

import javax.naming.InitialContext;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.SessionConfiguration;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderConfiguration;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;
import org.drools.persistence.jpa.JPAKnowledgeService;
import org.drools.runtime.Environment;
import org.drools.runtime.EnvironmentName;
import org.drools.runtime.KnowledgeSessionConfiguration;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.util.CompositeClassLoader;
import org.jbpm.enterprise.platform.ExecutionEngineCallback;
import org.jbpm.enterprise.platform.ExecutionEngineConfiguration;
import org.jbpm.enterprise.platform.ExecutionEngineMapperStrategy;

public class ExecutionEngineBuilder {
	
	protected EntityManagerFactory emf; 

	public KnowledgeBase buildKnowledgeBase(ExecutionEngineConfiguration config, ExecutionEngineCallback callback, ClassLoader classLoader) {
		CompositeClassLoader compositeCL = new CompositeClassLoader();
		compositeCL.addClassLoader(classLoader);
		compositeCL.addClassLoader(getClass().getClassLoader());
		KnowledgeBuilderConfiguration kBaseConfig = KnowledgeBuilderFactory.newKnowledgeBuilderConfiguration(new Properties(), compositeCL);
        KnowledgeBuilder builder = KnowledgeBuilderFactory.newKnowledgeBuilder(kBaseConfig);
		
		
		if (callback != null) {
			callback.preKnowledgeBaseCreate(builder);
		}
		
		if (config.getChangeSet() != null) {
			if (config.getChangeSet().startsWith("classpath:")) {
				String classPathResource = config.getChangeSet().replaceFirst("classpath:", "");
				builder.add(ResourceFactory.newClassPathResource(classPathResource), ResourceType.CHANGE_SET);
			} else {
				builder.add(ResourceFactory.newUrlResource(config.getChangeSet()), ResourceType.CHANGE_SET);
			}			
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
			ExecutionEngineMapperStrategy strategy, String businessKey, KnowledgeBase kbase, ClassLoader classLoader) {
		Environment environment = KnowledgeBaseFactory.newEnvironment();
		// TODO add required information to the environment
		KnowledgeSessionConfiguration configuration = KnowledgeBaseFactory.newKnowledgeSessionConfiguration();
		// TODO add required information to the configuration
		CompositeClassLoader compositeCL = new CompositeClassLoader();
		compositeCL.addClassLoader(classLoader);
		compositeCL.addClassLoader(getClass().getClassLoader());
		((SessionConfiguration) configuration).setClassLoader(compositeCL);
		
		if (callback != null) {
			callback.preKnowledgeSessionCreate(environment, configuration, kbase);
		}
		StatefulKnowledgeSession session = null;
		
		if (config.isPersistenceEnabled()) {
			
			int internalId = strategy.resolveIdByBusinessKey(businessKey);
			if (emf == null || !emf.isOpen()) {
				try {
                    InitialContext ctx = new InitialContext();
                    
                    emf = (EntityManagerFactory) ctx.lookup(config.getPersistenceUnit());
                } catch (Exception e) {
                    emf = Persistence.createEntityManagerFactory(config.getPersistenceUnit());
                    System.out.println(
                            "Error while building environment, creating/looking up entity manager factory "+
                            e.getMessage());
                }
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

		
		if (callback != null) {
			callback.postKnowledgeSessionCreate(session, businessKey);
		}
		
		return session;
	}
}
