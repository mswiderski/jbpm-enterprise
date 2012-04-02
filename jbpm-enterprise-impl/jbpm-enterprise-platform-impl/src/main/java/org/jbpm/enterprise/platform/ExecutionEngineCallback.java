package org.jbpm.enterprise.platform;

import org.drools.KnowledgeBase;
import org.drools.builder.KnowledgeBuilder;
import org.drools.runtime.Environment;
import org.drools.runtime.KnowledgeSessionConfiguration;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.StatelessKnowledgeSession;
import org.drools.runtime.process.WorkItemHandler;

/**
 * Callback interface that allows callers to receive notification about events from the engine.
 *
 */
public interface ExecutionEngineCallback {
	
	/**
	 * Notification emitted when engine is about to create <code>KnowledgeBade</code>
	 * @param builder <code>KnowledgeBaseBuilder</code> that will be used to build knowledge base
	 */
	public void preKnowledgeBaseCreate(KnowledgeBuilder builder);
	
	/**
	 * Notification emitted after knowledge base was build successfully.
	 * @param kBase instance of the <code>KnowledgeBase</code>
	 */
	public void postKnowledgeBaseCreate(KnowledgeBase kBase);
	
	/**
	 * Notification emitted before session is created regardless of session type.
	 * @param environment environment used to build the session
	 * @param config configuration used to build the session
	 * @param kBase knowledge base used to build the session
	 */
	public void preKnowledgeSessionCreate(Environment environment, KnowledgeSessionConfiguration config, KnowledgeBase kBase);
	
	/**
	 * Notification emitted after stateful session was successfully created
	 * @param session stateful session instance
	 * @param businessKey user defined business key which will be used to register session 
	 */
	public void postKnowledgeSessionCreate(StatefulKnowledgeSession session, String businessKey);
	
	/**
	 * Notification emitted after stateless session was successfully created
	 * @param session stateless session instance
	 * @param businessKey user defined business key which will be used to register session
	 */
	public void postKnowledgeSessionCreate(StatelessKnowledgeSession session, String businessKey);
	
	/**
	 * Notification emitted before stateful session is restored from persistent store
	 * @param environment environment used to restore the session
	 * @param config configuration used to restore the session
	 * @param kBase knowledge base used to restore the session
	 */
	public void preKnowledgeSessionRestore(Environment environment, KnowledgeSessionConfiguration config, KnowledgeBase kBase);
	
	/**
	 * Notification emitted after stateful session was successfully restored
	 * @param session stateful session instance
	 * @param businessKey user defined business key which is used to restore session
	 */
	public void postKnowledgeSessionRestore(StatefulKnowledgeSession session, String businessKey);

	/**
	 * Notification emitted before session is disposed.
	 * @param session session that is going to be disposed.
	 * @param businessKey user defined key under which session is registered
	 */
	public void preSessionDispose(StatefulKnowledgeSession session, String businessKey);
	
	/**
	 * Notification emitted after session is disposed.
	 * @param session session that was just disposed.
	 * @param businessKey user defined key under which session is registered
	 */
	public void postSessionDispose(StatefulKnowledgeSession session, String businessKey);
	
	/**
	 * Notification emitted before work item handler is registered on the session.
	 * @param session session where work item handler will be registered
	 * @param businessKey business key of the session
	 * @param kBase knowledge base for reference
	 * @param handler work item handler instance
	 */
	public void preWorkItemRegister(StatefulKnowledgeSession session, String businessKey, KnowledgeBase kBase, WorkItemHandler handler);
	
	/**
	 * Notification emitted after work item handler is registered on the session.
	 * @param session session where work item handler was just registered
	 * @param businessKey business key of the session
	 * @param kBase knowledge base for reference
	 * @param handler work item handler instance
	 */
	public void postWorkItemRegister(StatefulKnowledgeSession session, String businessKey, KnowledgeBase kBase, WorkItemHandler handler);
}
