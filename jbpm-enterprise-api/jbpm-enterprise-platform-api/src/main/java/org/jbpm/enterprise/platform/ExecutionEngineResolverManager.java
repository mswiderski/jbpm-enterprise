package org.jbpm.enterprise.platform;

import java.util.Collection;
import java.util.UUID;

/**
 * Manages all available <code>ExecutionEngineResolver</code> instances on the platform in singleton fashion.
 *
 */
public interface ExecutionEngineResolverManager {

	/**
	 * Registers given <code>ExecutionEngineResolver</code> for a given owner
	 * @param owner a component that owns given resolver for reference
	 * @param resolver instance of <code>ExecutionEngineResolver</code> to be registered
	 */
	public void register(String owner, ExecutionEngineResolver resolver);
	
	/**
	 * Unregisters previously registered resolver based on owner and unique identifier of the resolver.
	 * @param owner component that owns the resolver
	 * @param resolverUniqueId unique identifier of resolver
	 */
	public void unregister(String owner, UUID resolverUniqueId);
	
	/**
	 * Performs search on all registered resolvers to find suitable resolver for given request. Each resolver
	 * is responsible to accept or reject request context. First resolver that accepts the request will be returned.
	 * By default resolvers are kept in order they were registered, this policy can be altered by concrete implementation.
	 * @param context request context
	 * @return returns first resolver that accepted the request context
	 */
	public ExecutionEngineResolver find(RequestContext context);
	
	/**
	 * Perform same routine as find method and in addition to that (if resolver was found) directly look up for 
	 * <code>ExecutionEngine</code> using found resolver.
	 * @param context request context
	 * @return returns ExecutionEngine instance looked up be the found resolver
	 */
	public ExecutionEngine findAndLookUp(RequestContext context);
	
	/**
	 * Returns collection of all registered resolvers. The list should be read only.
	 * @return collection of registered resolvers.
	 */
	public Collection<ExecutionEngineResolver> getResolvers();
}
