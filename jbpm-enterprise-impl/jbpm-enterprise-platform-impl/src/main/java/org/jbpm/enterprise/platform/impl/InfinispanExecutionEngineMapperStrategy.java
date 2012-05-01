package org.jbpm.enterprise.platform.impl;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.naming.InitialContext;

import org.infinispan.Cache;
import org.infinispan.manager.CacheContainer;
import org.infinispan.manager.DefaultCacheManager;
import org.jbpm.enterprise.platform.ExecutionEngineMapperStrategy;

public class InfinispanExecutionEngineMapperStrategy implements ExecutionEngineMapperStrategy {

	private UUID myUUID;
	private Cache<Object, Object> cache;
	
	private Map<Integer, String> localCache = new ConcurrentHashMap<Integer, String>();
	
	public InfinispanExecutionEngineMapperStrategy() {
		this("java:jboss/CacheManager/jbpm");
	}
	
	public InfinispanExecutionEngineMapperStrategy(String cacheManagerJndiName) {
		try {
			InitialContext ctx = new InitialContext();
			CacheContainer container = (CacheContainer) ctx.lookup(cacheManagerJndiName);
			cache = container.getCache();
		} catch (Exception e) {
			cache = new DefaultCacheManager().getCache();
		}
		
	}
	
	public int resolveIdByBusinessKey(String businessKey) {
		if (this.cache.containsKey(businessKey)) {
			return (Integer) this.cache.get(businessKey);
		} else {
			return -1;
		}
	}

	public String resolveBusinessKeyById(int id) {
		if (localCache.containsKey(id)) {
			return localCache.get(id);
		} else {
			String businessKey = null;
			Iterator<Map.Entry<Object, Object>> iter = this.cache.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry<Object, Object> entry = (Map.Entry<Object, Object>) iter.next();
				if (Integer.parseInt(entry.getValue().toString()) == id) {
					businessKey = (String) entry.getKey();
					break;
				}
			}
			if (businessKey != null) {	
				localCache.put(id, businessKey);
			}
			return businessKey;
		}
	}

	public boolean storeMapping(String businessKey, int id) {
		Object oldvalue = this.cache.putIfAbsent(businessKey, id);
		if (oldvalue == null) {
			return true;
		} else {
			return false;
		}
	}

	public UUID getUUID() {
		if (this.myUUID == null) {
			this.myUUID = new UUID("UUIDBasedResolver".hashCode(), "Version1".hashCode());
		}
		return this.myUUID;
	}

}
