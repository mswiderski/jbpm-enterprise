package org.jbpm.enterprise.web;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.jbpm.enterprise.platform.RequestContext;

public class HttpRequestContext implements RequestContext {

	private HttpServletRequest request;
	
	public HttpRequestContext(HttpServletRequest request) {
		this.request = request;
	}
	
	@Override
	public Object getProperty(String name) {
		
		return this.request.getParameter(name);
	}

	@Override
	public Map<String, Object> getProperties() {
		
		Map<String, Object> parameters = new HashMap<String, Object>();
		Enumeration<String> names = this.request.getParameterNames();
		while (names.hasMoreElements()) {
			String parameterName = (String) names.nextElement();
			parameters.put(parameterName, this.request.getParameterValues(parameterName));
		}
		return parameters;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Set<String> getPropertyNames() {
		Set<String> propertyNames = new HashSet<String>();
		Enumeration<String> names = this.request.getParameterNames();
		while (names.hasMoreElements()) {
			String parameterName = (String) names.nextElement();
			propertyNames.add(parameterName);
		}
		return propertyNames;
	}

	@Override
	public void setProperty(String name, Object value) throws IllegalArgumentException {
		// TODO Auto-generated method stub

	}

}
