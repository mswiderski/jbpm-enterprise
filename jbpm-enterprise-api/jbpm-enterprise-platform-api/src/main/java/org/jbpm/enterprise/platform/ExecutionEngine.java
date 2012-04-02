package org.jbpm.enterprise.platform;

import java.util.UUID;

/**
 * Entry point for all clients that rely on processes and rules.
 * Encapsulates <code>KnowledgeBase</code> and its session management delivering simplified but not limited usage of jBPM and drools.
 * 
 * In addition to default usage of sessions in jBPM and drools projects it allows usage of business keys to interact with the session
 * rather than internal identifiers of the session when sessions are persisted in some sort of data store. One example of a business 
 * key can be user id so whenever user interacts with the engine (s)he does that over the same session.
 *
 */
public interface ExecutionEngine {

	/**
	 * Returns <code>KnowledgeBase</code> dedicated to this engine
	 * @return underlying <code>KnowledgeBase</code>
	 */
	public Object getKnowledgeBase();
	
	/**
	 * Creates and returns stateless sessions for "single shot" usage.
	 * @return <code>StatelessKnowledgeSession</code>
	 */
	public SessionDelegate getStatelessSession();
	
	/**
	 * Returns stateful session for given business key. If one does not exist new will be created
	 * and registered under given business key.
	 * @param businessKey user defined business key
	 * @return session found under given business key if such exist of new one
	 */
	public SessionDelegate getSession(String businessKey);
	
	/**
	 * Returns stateful session with given identifier if one exist or null
	 * @param id internal identifier of the session
	 * @return session if exists under given id or null
	 */
	public SessionDelegate getSessionById(int id);
	
	/**
	 * Returns instance of HumanTask connector that was created for this ExecutionEngine.
	 * @return instance of ExecutionEngine if one was configured or null
	 */
	public Object getHumanTaskConnector();
	
	/**
	 * Returns unique identifier for this ExecutionEngine
	 * @return UUID that identifies this instance of ExecutionEngine
	 */
	public UUID getUUID();
	
	
	/**
	 * Build composite identifier that includes UUID of the ExecutionEngine for given id
	 * @param id generic id that should be enhanced
	 * @return returns enhanced id with UUID of the ExecutionEngine
	 */
	public String buildCompositeId(String id);
	
	/**
	 * Dispose given session and informs the engine about is disposal.
	 * @param session
	 */
	public void disposeSession(SessionDelegate session);
}
