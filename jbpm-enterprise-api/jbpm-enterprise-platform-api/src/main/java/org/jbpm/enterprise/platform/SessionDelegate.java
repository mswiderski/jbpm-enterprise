package org.jbpm.enterprise.platform;


public interface SessionDelegate  {

	public Object getDelegate();
	
	public long startProcess(String id);
	
	public int getId();
	
	public void dispose();
}
