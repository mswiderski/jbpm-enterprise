
package org.jbpm.enterprise.platform.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.jbpm.enterprise.platform.ExecutionEngineMapperStrategy;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SerializedMapExecutionEngineMapperStrategyTest {
	
	private ExecutionEngineMapperStrategy strategy = null;
	
	@Before
	public void setup() {
		strategy = new SerializedMapExecutionEngineMapperStrategy("test-case", System.getProperty("java.io.tmpdir"));
	}
	
	@After
	public void teardown() {
		File serialized = new File(System.getProperty("java.io.tmpdir") + File.separator + strategy.getUUID() + ".ser");
		// clean up
		serialized.delete();
	}

	@Test
	public void testStoreMapping() {
		
		boolean result = strategy.storeMapping("unit-test-key", 100);

		assertTrue(result);
		File serialized = new File(System.getProperty("java.io.tmpdir") + File.separator + strategy.getUUID() + ".ser");
		assertTrue(serialized.exists());

	}
	
	@Test
	public void testStoreMappingOverride() {
		
		boolean result = strategy.storeMapping("unit-test-key", 100);
		assertTrue(result);
		
		result = strategy.storeMapping("unit-test-key", 200);
		assertTrue(!result);

	}
	
	@Test
	public void testGetIdByBusinessKey() {
		
		boolean result = strategy.storeMapping("unit-test-key", 100);
		assertTrue(result);
		
		int id = strategy.resolveIdByBusinessKey("unit-test-key");
		assertEquals(100, id);
	}
	
	@Test
	public void testGetNonExistingIdByBusinessKey() {
		
		boolean result = strategy.storeMapping("unit-test-key", 100);
		assertTrue(result);
		
		int id = strategy.resolveIdByBusinessKey("non-existing-key");
		assertEquals(-1, id);
	}
	
	@Test
	public void testGetBusinessKeyById() {
		boolean result = strategy.storeMapping("unit-test-key", 100);
		assertTrue(result);
		
		String businessKey = strategy.resolveBusinessKeyById(100);
		assertNotNull(businessKey);
		
	}
	
	@Test
	public void testGetNonExistingBusinessKeyById() {
		boolean result = strategy.storeMapping("unit-test-key", 100);
		assertTrue(result);
		
		String businessKey = strategy.resolveBusinessKeyById(200);
		assertNull(businessKey);
		
	}
}
