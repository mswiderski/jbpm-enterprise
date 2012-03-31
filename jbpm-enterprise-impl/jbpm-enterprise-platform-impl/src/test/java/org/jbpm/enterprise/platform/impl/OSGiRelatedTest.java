package org.jbpm.enterprise.platform.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.osgi.framework.Filter;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.InvalidSyntaxException;

public class OSGiRelatedTest {

	@Test
	public void testFilterCreation() {
		long currentTime = System.currentTimeMillis();
		
		String filter =  "(&(validFrom>=" + currentTime + ")(validTo<=" + currentTime + "))";
		
		try {
			Filter osgiFilter = FrameworkUtil.createFilter(filter);
			assertNotNull(osgiFilter);
		} catch (InvalidSyntaxException e) {
			
			e.printStackTrace();
			fail("Filter creation failed: " + e.getMessage());
		}
	}
}
