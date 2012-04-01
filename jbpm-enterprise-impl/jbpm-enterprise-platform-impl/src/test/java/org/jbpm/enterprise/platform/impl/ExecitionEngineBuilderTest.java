package org.jbpm.enterprise.platform.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.File;

import org.drools.KnowledgeBase;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatefulKnowledgeSession;
import org.jbpm.enterprise.platform.ExecutionEngineConfiguration;
import org.jbpm.enterprise.platform.ExecutionEngineMapperStrategy;
import org.junit.BeforeClass;
import org.junit.Test;

import bitronix.tm.resource.jdbc.PoolingDataSource;

public class ExecitionEngineBuilderTest {
	
	@BeforeClass
	public static void setupOnce() {
		PoolingDataSource pds = new PoolingDataSource();
        pds.setUniqueName("jdbc/ProcessEngineDS");
        pds.setClassName("bitronix.tm.resource.jdbc.lrc.LrcXADataSource");
        pds.setMaxPoolSize(5);
        pds.setAllowLocalTransactions(true);
        pds.getDriverProperties().put("user", "sa");
        pds.getDriverProperties().put("password", "");
        pds.getDriverProperties().put("url", "jdbc:h2:mem:mydb");
        pds.getDriverProperties().put("driverClassName", "org.h2.Driver");
        pds.init();
	}

	@Test
	public void testCreateKnowledgeBase() {
		ExecutionEngineBuilder builder = new ExecutionEngineBuilder();
		ExecutionEngineConfiguration config = new DefaultExecutionEngineConfiguration();
		config.addResource(ResourceFactory.newClassPathResource("BPMN2-ScriptTask.bpmn2"), ResourceType.BPMN2);
		
		KnowledgeBase kbase = builder.buildKnowledgeBase(config, null, this.getClass().getClassLoader());
		assertNotNull(kbase);
		assertEquals(1, kbase.getKnowledgePackages().size());
		assertEquals(1, kbase.getProcesses().size());
	}
	
	@Test(expected=RuntimeException.class)
	public void testCreateKnowledgeBaseCorrupted() {
		ExecutionEngineBuilder builder = new ExecutionEngineBuilder();
		ExecutionEngineConfiguration config = new DefaultExecutionEngineConfiguration();
		config.addResource(ResourceFactory.newClassPathResource("does-not-exist.bpmn2"), ResourceType.BPMN2);
		
		KnowledgeBase kbase = builder.buildKnowledgeBase(config, null, this.getClass().getClassLoader());
		fail("Build of knowledge base should fail as resource does not exist" + kbase);
	}
	
	@Test
	public void testRetrieveStatefulSessionNoPersistence() {
		
		ExecutionEngineBuilder builder = new ExecutionEngineBuilder();
		ExecutionEngineConfiguration config = new DefaultExecutionEngineConfiguration();
		config.addResource(ResourceFactory.newClassPathResource("BPMN2-ScriptTask.bpmn2"), ResourceType.BPMN2);
		
		KnowledgeBase kbase = builder.buildKnowledgeBase(config, null, this.getClass().getClassLoader());
		ExecutionEngineMapperStrategy strategy = new SerializedMapExecutionEngineMapperStrategy("test", System.getProperty("java.io.tmpdir"));
		StatefulKnowledgeSession session = builder.retrieveSession(config, null, strategy, "testBaseKey", kbase, this.getClass().getClassLoader());
		assertNotNull(session);
	}
	
	@Test
	public void testRetrieveStatefulSessionPersistence() {
		
		ExecutionEngineBuilder builder = new ExecutionEngineBuilder();
		DefaultExecutionEngineConfiguration config = new DefaultExecutionEngineConfiguration();
		config.addResource(ResourceFactory.newClassPathResource("BPMN2-ScriptTask.bpmn2"), ResourceType.BPMN2);
		config.setPersistenceUnit("org.jbpm.persistence.jpa");
		
		KnowledgeBase kbase = builder.buildKnowledgeBase(config, null, this.getClass().getClassLoader());
		ExecutionEngineMapperStrategy strategy = new SerializedMapExecutionEngineMapperStrategy("test", System.getProperty("java.io.tmpdir"));
		StatefulKnowledgeSession session = builder.retrieveSession(config, null, strategy, "testBaseKey", kbase, this.getClass().getClassLoader());
		assertNotNull(session);
	}
	
	@Test
	public void testCreateKnowledgeBaseFromClasspathChangeset() {
		ExecutionEngineBuilder builder = new ExecutionEngineBuilder();
		ExecutionEngineConfiguration config = new DefaultExecutionEngineConfiguration();
		config.setChangeSet("classpath:ChangeSet.xml");
		
		KnowledgeBase kbase = builder.buildKnowledgeBase(config, null, this.getClass().getClassLoader());
		assertNotNull(kbase);
		assertEquals(1, kbase.getKnowledgePackages().size());
		assertEquals(1, kbase.getProcesses().size());
	}
	
	@Test
	public void testCreateKnowledgeBaseFromFileChangeset() {
		ExecutionEngineBuilder builder = new ExecutionEngineBuilder();
		ExecutionEngineConfiguration config = new DefaultExecutionEngineConfiguration();
		String dir = System.getProperty( "user.dir") + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator;
		config.setChangeSet("file:" + dir + "ChangeSet.xml");
		
		KnowledgeBase kbase = builder.buildKnowledgeBase(config, null, this.getClass().getClassLoader());
		assertNotNull(kbase);
		assertEquals(1, kbase.getKnowledgePackages().size());
		assertEquals(1, kbase.getProcesses().size());
	}
	
	@Test
	public void testRetrieveStatefulSessionNoPersistenceFromChangeSet() {
		
		ExecutionEngineBuilder builder = new ExecutionEngineBuilder();
		ExecutionEngineConfiguration config = new DefaultExecutionEngineConfiguration();
		config.setChangeSet("classpath:ChangeSet.xml");
		
		KnowledgeBase kbase = builder.buildKnowledgeBase(config, null, this.getClass().getClassLoader());
		ExecutionEngineMapperStrategy strategy = new SerializedMapExecutionEngineMapperStrategy("test", System.getProperty("java.io.tmpdir"));
		StatefulKnowledgeSession session = builder.retrieveSession(config, null, strategy, "testBaseKey", kbase, this.getClass().getClassLoader());
		assertNotNull(session);
	}
	
	@Test
	public void testRetrieveStatefulSessionPersistenceFromChangeSet() {
		
		ExecutionEngineBuilder builder = new ExecutionEngineBuilder();
		DefaultExecutionEngineConfiguration config = new DefaultExecutionEngineConfiguration();
		config.setChangeSet("classpath:ChangeSet.xml");
		config.setPersistenceUnit("org.jbpm.persistence.jpa");
		
		KnowledgeBase kbase = builder.buildKnowledgeBase(config, null, this.getClass().getClassLoader());
		ExecutionEngineMapperStrategy strategy = new SerializedMapExecutionEngineMapperStrategy("test", System.getProperty("java.io.tmpdir"));
		StatefulKnowledgeSession session = builder.retrieveSession(config, null, strategy, "testBaseKey", kbase, this.getClass().getClassLoader());
		assertNotNull(session);
	}
}
