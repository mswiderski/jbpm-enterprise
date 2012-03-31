package org.jbpm.enterprise.platform;

import org.drools.runtime.StatefulKnowledgeSession;

public interface SessionDelegate extends StatefulKnowledgeSession {

	public StatefulKnowledgeSession getDelegate();
}
