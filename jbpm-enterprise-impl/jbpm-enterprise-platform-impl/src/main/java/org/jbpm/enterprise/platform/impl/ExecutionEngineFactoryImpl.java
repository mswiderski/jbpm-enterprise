package org.jbpm.enterprise.platform.impl;

import org.drools.KnowledgeBase;
import org.drools.util.CompositeClassLoader;
import org.jbpm.enterprise.platform.ExecutionEngine;
import org.jbpm.enterprise.platform.ExecutionEngineCallback;
import org.jbpm.enterprise.platform.ExecutionEngineConfiguration;
import org.jbpm.enterprise.platform.ExecutionEngineFactory;
import org.jbpm.enterprise.platform.ExecutionEngineMapperStrategy;

public class ExecutionEngineFactoryImpl implements ExecutionEngineFactory {

	public ExecutionEngine newExecutionEngine(ClassLoader bundleClassLoader) {
		
		DefaultExecutionEngineConfiguration config = new DefaultExecutionEngineConfiguration();
		// TODO assemble config object based on resources on class path accessible from bundleClassLoader
		return newExecutionEngine(bundleClassLoader, config, null, null);
	}

	public ExecutionEngine newExecutionEngine(ClassLoader bundleClassLoader, ExecutionEngineConfiguration config) {
		
		return newExecutionEngine(bundleClassLoader, config, null, null);
	}

	public ExecutionEngine newExecutionEngine(ClassLoader bundleClassLoader, ExecutionEngineConfiguration config, Object callback) {
		
		return newExecutionEngine(bundleClassLoader, config, null, callback);
	}

	public ExecutionEngine newExecutionEngine(ClassLoader bundleClassLoader, ExecutionEngineConfiguration config, ExecutionEngineMapperStrategy strategy,
			Object callback) {
		CompositeClassLoader compositeClassloader = buildCompositeClassloader(bundleClassLoader);
		ExecutionEngineImpl executionEngine = new ExecutionEngineImpl(config, compositeClassloader);
		ExecutionEngineBuilder eeBuilder = new ExecutionEngineBuilder();
		executionEngine.setBuilder(eeBuilder);
		
		// build and set knowledge base
		KnowledgeBase kBase = eeBuilder.buildKnowledgeBase(config, (ExecutionEngineCallback) callback, compositeClassloader);
		executionEngine.setKnowledgeBase(kBase);
		
		// configure strategy
		 if (strategy == null) {
			 strategy = new SerializedMapExecutionEngineMapperStrategy("engine", System.getProperty("java.io.tmpdir"));
		 }
		 executionEngine.setStrategy(strategy);
		 
		 // configure callback
		 executionEngine.setCallback((ExecutionEngineCallback) callback);
		
		return executionEngine;
	}
	
	protected CompositeClassLoader buildCompositeClassloader(ClassLoader bundleClassLoader) {
		
		if (bundleClassLoader instanceof CompositeClassLoader) {
			return (CompositeClassLoader) bundleClassLoader;
		}
		CompositeClassLoader cl = new CompositeClassLoader();
		cl.addClassLoader(this.getClass().getClassLoader());
		cl.addClassLoader(bundleClassLoader);
		Thread.currentThread().setContextClassLoader(cl);
		
		return cl;
	}

}
