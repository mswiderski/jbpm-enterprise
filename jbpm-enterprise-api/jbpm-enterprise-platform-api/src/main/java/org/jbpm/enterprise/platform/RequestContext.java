package org.jbpm.enterprise.platform;

import java.util.Map;
import java.util.Set;

/**
 * RequestContext brings unified way of accessing request specific information regardless of its origin protocol.
 * 
 * It acts as simple facade for specific implementation, i.e. Http, JMS, etc.
 *
 */
public interface RequestContext {

	/**
	 * Returns object that is stored in the request under given <code>name</code> key if exists otherwise null.
	 * @param name name of the property
	 * @return object if exists otherwise null
	 */
	public Object getProperty(String name);
	
	/**
	 * Returns all properties for given request.
	 * @return map of all properties that given request contains
	 */
	public Map<String, Object> getProperties();
	
	/**
	 * Returns all names of properties given request contains.
	 * @return set of names of all available properties.
	 */
	public Set<String> getPropertyNames();
	
	/**
	 * Allows to set new properties for the request but they will not be persisted on underlying request object that
	 * is protocol specific. It is considered as place holder if any data should be propagated to downstream components
	 * @param name name of the property
	 * @param value value of the property
	 * @throws IllegalArgumentException in case property under given name already exists
	 */
	public void setProperty(String name, Object value) throws IllegalArgumentException;
}
