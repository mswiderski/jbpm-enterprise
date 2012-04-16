package org.jbpm.enterprise.platform;

import java.util.UUID;

/**
 * SessionMappingStrategy is responsible for holding information about business key and internal identifiers
 * to allow applications deal only with business data and not to be aware of any internal ids.
 * 
 */
public interface SessionMappingStrategy {

	/**
	 * Returns internal id for given business key
	 * @param businessKey user defined business key
	 * @return internal id if one exists under given business key or -1 if id was not found
	 */
	public int resolveIdByBusinessKey(String businessKey);
	
	/**
	 * 
	 * Returns business key for given id
	 * @param id internal id of a resource
	 * @return business key if one exists under given id or null if business key was not found
	 */
	public String resolveBusinessKeyById(int id);
	
	/**
	 * Stores business key and id mapping in underlying data store
	 * @param businessKey user defined business key
	 * @param id internal identifier
	 * @return returns true if store operation was successful otherwise false
	 */
	public boolean storeMapping(String businessKey, int id);
	
	/**
	 * Returns UUID of the strategy instance
	 * @return UUID
	 */
	public UUID getUUID();
}
