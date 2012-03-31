package org.jbpm.enterprise.platform;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultExecutionEngineResolverManager implements ExecutionEngineResolverManager {

	private Map<UUID, ExecutionEngineResolver> resolvers = new ConcurrentHashMap<UUID, ExecutionEngineResolver>();
	
	public void register(String owner, ExecutionEngineResolver resolver) {
		 resolvers.put(resolver.getUUID(), resolver);

	}

	public void unregister(String owner, UUID resolverUniqueId) {
		resolvers.remove(resolverUniqueId);

	}

	public ExecutionEngineResolver find(RequestContext context) {
		Iterator<ExecutionEngineResolver> it = resolvers.values().iterator();
		while (it.hasNext()) {
			ExecutionEngineResolver executionEngineResolver = (ExecutionEngineResolver) it.next();
			
			boolean accepted = executionEngineResolver.accepts(context);
			
			if(accepted) {
				return executionEngineResolver;
			}
		}
		return null;
	}

	public ExecutionEngine findAndLookUp(RequestContext context) {
		ExecutionEngineResolver found = find(context);
		if (found != null) {
			
			return found.lookUpExecutionEngine(context);
		}
		return null;
	}

	public Collection<ExecutionEngineResolver> getResolvers() {
		
		return Collections.unmodifiableCollection(resolvers.values());
	}

}
