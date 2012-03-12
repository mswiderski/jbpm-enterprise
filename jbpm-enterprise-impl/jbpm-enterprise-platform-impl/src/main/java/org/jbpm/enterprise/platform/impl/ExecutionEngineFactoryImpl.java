package org.jbpm.enterprise.platform.impl;

import org.drools.KnowledgeBase;
import org.jbpm.enterprise.platform.ExecutionEngine;
import org.jbpm.enterprise.platform.ExecutionEngineCallback;
import org.jbpm.enterprise.platform.ExecutionEngineConfiguration;
import org.jbpm.enterprise.platform.ExecutionEngineFactory;
import org.jbpm.enterprise.platform.ExecutionEngineMapperStrategy;

public class ExecutionEngineFactoryImpl implements ExecutionEngineFactory {

	public ExecutionEngine newExecutionEngine(ClassLoader bundleClassLoader) {
		ExecutionEngineConfiguration config = null;
		// TODO assemble config object based on resources on class path accessible from bundleClassLoader
		return newExecutionEngine(bundleClassLoader, config, null, null);
	}

	public ExecutionEngine newExecutionEngine(ClassLoader bundleClassLoader, ExecutionEngineConfiguration config) {
		
		return newExecutionEngine(bundleClassLoader, config, null, null);
	}

	public ExecutionEngine newExecutionEngine(ClassLoader bundleClassLoader, ExecutionEngineConfiguration config, ExecutionEngineCallback callback) {
		
		return newExecutionEngine(bundleClassLoader, config, null, callback);
	}

	public ExecutionEngine newExecutionEngine(ClassLoader bundleClassLoader, ExecutionEngineConfiguration config, ExecutionEngineMapperStrategy strategy,
			ExecutionEngineCallback callback) {
		
		ExecutionEngineImpl executionEngine = new ExecutionEngineImpl();
		ExecutionEngineBuilder eeBuilder = new ExecutionEngineBuilder();
		
		// build and set knowledge base
		KnowledgeBase kBase = eeBuilder.buildKnowledgeBase(config, callback);
		executionEngine.setKnowledgeBase(kBase);
		
		// configure strategy
		 if (strategy == null) {
			 strategy = new SerializedMapExecutionEngineMapperStrategy(config.getOwner(), System.getProperty("java.io.tmpdir"));
		 }
		 executionEngine.setStrategy(strategy);
		 
		 // configure callback
		 executionEngine.setCallback(callback);
		
		return executionEngine;
	}

}
