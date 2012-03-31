package org.jbpm.enterprise.platform;

import java.util.UUID;

/**
 * Resolver is responsible for looking up for proper <code>ExecutionEngine</code> instance in repository
 * based on various filter criteria given on runtime.
 *
 */
public interface ExecutionEngineResolver {
	
	/**
	 * Returns unique identifier of the resolver. It is up to the implementation to build UUID based on specific criteria to ensure uniqueness.
	 * @return UUID for this resolver
	 */
	public UUID getUUID();

	/**
	 * Inspects given <code>requestContext</code> and its properties if they satisfy requirements of this resolver.
	 * If so, it keeps the requestContext object for further processing.
	 * @param requestContext request specific information
	 * @return true if resolver's requirements can be meet be given requestContext, otherwise false.
	 */
	public boolean accepts(RequestContext requestContext);
	
	/**
	 * Performs look up in the underlying repository for <code>ExecutionEngine</code> based on given requestContext.
	 * 
	 * This method can be executed only if accepts(requestContext) returned true. Otherwise it will result in exception.
	 * @return returns instance of the ExecutionEngine if found, otherwise throws a runtime exception
	 */
	public ExecutionEngine lookUpExecutionEngine(RequestContext requestContext);
}
