package org.jbpm.enterprise.platform.impl;

import java.util.Collection;
import java.util.Map;

import org.drools.KnowledgeBase;
import org.drools.command.Command;
import org.drools.event.process.ProcessEventListener;
import org.drools.event.rule.AgendaEventListener;
import org.drools.event.rule.WorkingMemoryEventListener;
import org.drools.runtime.Calendars;
import org.drools.runtime.Channel;
import org.drools.runtime.Environment;
import org.drools.runtime.ExitPoint;
import org.drools.runtime.Globals;
import org.drools.runtime.KnowledgeSessionConfiguration;
import org.drools.runtime.ObjectFilter;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.process.ProcessInstance;
import org.drools.runtime.process.WorkItemManager;
import org.drools.runtime.rule.Agenda;
import org.drools.runtime.rule.AgendaFilter;
import org.drools.runtime.rule.FactHandle;
import org.drools.runtime.rule.LiveQuery;
import org.drools.runtime.rule.QueryResults;
import org.drools.runtime.rule.ViewChangedEventListener;
import org.drools.runtime.rule.WorkingMemoryEntryPoint;
import org.drools.time.SessionClock;
import org.jbpm.enterprise.platform.ExecutionEngine;
import org.jbpm.enterprise.platform.SessionDelegate;

public class SessionDelegateImpl implements SessionDelegate {
	
	private StatefulKnowledgeSession delegate;
	private ExecutionEngine onwingEngine;
	
	private boolean disposed = false;
	
	public SessionDelegateImpl(StatefulKnowledgeSession origin, ExecutionEngine execEngine) {
		this.delegate = origin;
		this.onwingEngine = execEngine;
	}

	public int fireAllRules() {
		return this.delegate.fireAllRules();
	}

	public int fireAllRules(int max) {
		
		return this.delegate.fireAllRules(max);
	}

	public int fireAllRules(AgendaFilter agendaFilter) {
		
		return this.delegate.fireAllRules(agendaFilter);
	}

	public int fireAllRules(AgendaFilter agendaFilter, int max) {
		
		return this.delegate.fireAllRules(agendaFilter, max);
	}

	public void fireUntilHalt() {
		this.delegate.fireUntilHalt();

	}

	public void fireUntilHalt(AgendaFilter agendaFilter) {
		this.delegate.fireUntilHalt(agendaFilter);

	}

	public <T> T execute(Command<T> command) {
		
		return this.delegate.execute(command);
	}

	public <T extends SessionClock> T getSessionClock() {
		
		return this.delegate.getSessionClock();
	}

	public void setGlobal(String identifier, Object value) {
		this.delegate.setGlobal(identifier, value);

	}

	public Object getGlobal(String identifier) {
		
		return this.delegate.getGlobal(identifier);
	}

	public Globals getGlobals() {
		
		return this.delegate.getGlobals();
	}

	public Calendars getCalendars() {
		
		return this.delegate.getCalendars();
	}

	public Environment getEnvironment() {
		
		return this.delegate.getEnvironment();
	}

	public KnowledgeBase getKnowledgeBase() {
		
		return this.delegate.getKnowledgeBase();
	}

	@Deprecated
	public void registerExitPoint(String name, ExitPoint exitPoint) {
		this.delegate.registerExitPoint(name, exitPoint);

	}

	@Deprecated
	public void unregisterExitPoint(String name) {
		this.delegate.unregisterExitPoint(name);

	}

	public void registerChannel(String name, Channel channel) {
		this.delegate.registerChannel(name, channel);

	}

	public void unregisterChannel(String name) {
		this.delegate.unregisterChannel(name);

	}

	public Map<String, Channel> getChannels() {
		
		return this.delegate.getChannels();
	}

	public KnowledgeSessionConfiguration getSessionConfiguration() {
		
		return this.delegate.getSessionConfiguration();
	}

	public void halt() {
		this.delegate.halt();

	}

	public Agenda getAgenda() {
		
		return this.delegate.getAgenda();
	}

	public WorkingMemoryEntryPoint getWorkingMemoryEntryPoint(String name) {
		
		return this.delegate.getWorkingMemoryEntryPoint(name);
	}

	public Collection<? extends WorkingMemoryEntryPoint> getWorkingMemoryEntryPoints() {
		
		return this.delegate.getWorkingMemoryEntryPoints();
	}

	public QueryResults getQueryResults(String query, Object... arguments) {
		
		return this.delegate.getQueryResults(query, arguments);
	}

	public LiveQuery openLiveQuery(String query, Object[] arguments, ViewChangedEventListener listener) {
		
		return this.delegate.openLiveQuery(query, arguments, listener);
	}

	public String getEntryPointId() {
		
		return this.delegate.getEntryPointId();
	}

	public FactHandle insert(Object object) {
		
		return this.delegate.insert(object);
	}

	public void retract(FactHandle handle) {
		this.delegate.retract(handle);

	}

	public void update(FactHandle handle, Object object) {
		this.delegate.update(handle, object);

	}

	public FactHandle getFactHandle(Object object) {
		
		return this.delegate.getFactHandle(object);
	}

	public Object getObject(FactHandle factHandle) {
		
		return this.delegate.getObject(factHandle);
	}

	public Collection<Object> getObjects() {
		
		return this.delegate.getObjects();
	}

	public Collection<Object> getObjects(ObjectFilter filter) {
		
		return this.delegate.getObjects(filter);
	}

	public <T extends FactHandle> Collection<T> getFactHandles() {
		
		return this.delegate.getFactHandles();
	}

	public <T extends FactHandle> Collection<T> getFactHandles(ObjectFilter filter) {
		
		return this.delegate.getFactHandles(filter);
	}

	public long getFactCount() {
		
		return this.delegate.getFactCount();
	}

	public ProcessInstance startProcess(String processId) {
		
		return this.delegate.startProcess(processId);
	}

	public ProcessInstance startProcess(String processId, Map<String, Object> parameters) {
		
		return this.delegate.startProcess(processId, parameters);
	}

	public ProcessInstance createProcessInstance(String processId, Map<String, Object> parameters) {
		
		return this.delegate.createProcessInstance(processId, parameters);
	}

	public ProcessInstance startProcessInstance(long processInstanceId) {
		
		return this.delegate.startProcessInstance(processInstanceId);
	}

	public void signalEvent(String type, Object event) {
		this.delegate.signalEvent(type, event);

	}

	public void signalEvent(String type, Object event, long processInstanceId) {
		this.delegate.signalEvent(type, event, processInstanceId);

	}

	public Collection<ProcessInstance> getProcessInstances() {
		
		return this.delegate.getProcessInstances();
	}

	public ProcessInstance getProcessInstance(long processInstanceId) {
		
		return this.delegate.getProcessInstance(processInstanceId);
	}

	public void abortProcessInstance(long processInstanceId) {
		this.delegate.abortProcessInstance(processInstanceId);

	}

	public WorkItemManager getWorkItemManager() {
		
		return this.delegate.getWorkItemManager();
	}

	public void addEventListener(WorkingMemoryEventListener listener) {
		this.delegate.addEventListener(listener);

	}

	public void removeEventListener(WorkingMemoryEventListener listener) {
		this.delegate.removeEventListener(listener);

	}

	public Collection<WorkingMemoryEventListener> getWorkingMemoryEventListeners() {
		
		return this.delegate.getWorkingMemoryEventListeners();
	}

	public void addEventListener(AgendaEventListener listener) {
		this.delegate.addEventListener(listener);

	}

	public void removeEventListener(AgendaEventListener listener) {
		this.delegate.removeEventListener(listener);

	}

	public Collection<AgendaEventListener> getAgendaEventListeners() {
		
		return this.delegate.getAgendaEventListeners();
	}

	public void addEventListener(ProcessEventListener listener) {
		this.delegate.addEventListener(listener);

	}

	public void removeEventListener(ProcessEventListener listener) {
		this.delegate.removeEventListener(listener);

	}

	public Collection<ProcessEventListener> getProcessEventListeners() {
		
		return this.delegate.getProcessEventListeners();
	}

	public int getId() {
		
		return this.delegate.getId();
	}

	public void dispose() {
		if (!disposed) {
			this.onwingEngine.disposeSession(this.delegate);
			this.disposed = true;
		}
	}
	
	public StatefulKnowledgeSession getDelegate() {
		return this.delegate;
	}

}
