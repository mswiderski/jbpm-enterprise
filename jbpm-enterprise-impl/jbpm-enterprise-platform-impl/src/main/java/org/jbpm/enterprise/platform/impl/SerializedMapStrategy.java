package org.jbpm.enterprise.platform.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.jbpm.enterprise.platform.SessionMappingStrategy;

public class SerializedMapStrategy implements SessionMappingStrategy {

	protected String owner;
	protected String storageLocation;
	protected Map<String, Integer> internalMap = new ConcurrentHashMap<String, Integer>();
	
	private Map<Integer, String> localCache = new ConcurrentHashMap<Integer, String>();
	
	public SerializedMapStrategy(String owner, String storageLocation) {
		this.owner = owner;
		this.storageLocation = storageLocation;
		
		// read serializable data for this UUID if exists
		readSerializedFile();
	}
	
	public int resolveIdByBusinessKey(String businessKey) {
		if (internalMap.containsKey(businessKey)) {
			return internalMap.get(businessKey);
		} else {
			return -1;
		}
	}

	public String resolveBusinessKeyById(int id) {
		if (internalMap.containsValue(id)) {
			if (localCache.containsKey(id)) {
				return localCache.get(id);
			} else {
				String businessKey = null;
				Iterator<Map.Entry<String, Integer>> iter = internalMap.entrySet().iterator();
				while (iter.hasNext()) {
					Map.Entry<String, Integer> entry = (Map.Entry<String, Integer>) iter.next();
					if (entry.getValue().intValue() == id) {
						businessKey = entry.getKey();
						break;
					}
				}
					
				localCache.put(id, businessKey);
				return businessKey;
			}
			
		} else {
			return null;
		}
	}

	public boolean storeMapping(String businessKey, int id) {
		if (internalMap.containsKey(businessKey)) {
			return false;
		} else {
			internalMap.put(businessKey, id);
			// serialize internal map
			writeSerializedFile();

            //do eager load, it's inexpensive here
            localCache.put(id, businessKey);
			return true;
		}
	}

	public UUID getUUID() {
		
		return new UUID("SerializableMapMapperStrategyFor".hashCode(), owner.hashCode());
	}
	
	@SuppressWarnings("unchecked")
	protected Map<String, Integer> readSerializedFile() {
		String serializable = getAbsoluteFileName();
		if (new File(serializable).exists()) {
            FileInputStream fis = null;
            ObjectInputStream in = null;
            try {
                fis = new FileInputStream(serializable);
                in = new ObjectInputStream(fis);
                this.internalMap = (Map<String, Integer>) in.readObject();
                in.close();
            } catch (Exception e) {
                throw new IllegalStateException("Cannot read serizlized file with mapping information (business key -> session id)");
            } finally {
                try {
                    fis.close();
                } catch (Exception e) {
                    
                }
            }
        }
		return internalMap;
	}
	
	protected String getAbsoluteFileName() {
		return storageLocation + File.separator + getUUID() + ".ser";
	}

	protected void writeSerializedFile() {
		String serializable = getAbsoluteFileName();
		FileOutputStream fos = null;
        ObjectOutputStream out = null;
        try {
            fos = new FileOutputStream(serializable);
            out = new ObjectOutputStream(fos);
            out.writeObject(internalMap);
            out.close();
        } catch (IOException ex) {
        	throw new IllegalStateException("Cannot write serizlized file with mapping information (business key -> session id)");
        }
	}

}
