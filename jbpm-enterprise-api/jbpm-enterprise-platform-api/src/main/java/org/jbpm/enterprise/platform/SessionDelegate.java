package org.jbpm.enterprise.platform;

import java.util.Map;


public interface SessionDelegate  {

	public Object getDelegate();
	
	public String startProcess(String id);
	
	public String startProcess(String id, Map<String, Object> properties);
	
	public int getId();
	
	public void dispose();
	
	public void signalEvent(String type, Object event);
	
	public void signalEvent(String type, Object event, long processInstanceId);
}
