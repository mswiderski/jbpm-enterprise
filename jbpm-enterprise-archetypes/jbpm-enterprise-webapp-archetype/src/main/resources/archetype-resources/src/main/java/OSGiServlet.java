#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${groupId};

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jbpm.enterprise.platform.ExecutionEngine;
import org.jbpm.enterprise.platform.ExecutionEngineResolverManager;
import org.jbpm.enterprise.web.HttpRequestContext;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

/**
 * Servlet implementation class OSGiServlet
 */
public class OSGiServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Resource
	private BundleContext context;


	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
	    	ServiceReference srf = this.context.getServiceReference(ExecutionEngineResolverManager.class.getName());
    		ExecutionEngineResolverManager resolverManager = (ExecutionEngineResolverManager) this.context.getService(srf);
    		
    		ExecutionEngine engine = resolverManager.findAndLookUp(new HttpRequestContext(request));
    		
    		engine.getSession("default").startProcess(request.getParameter("id"));
	    } catch (Exception e) {
            e.printStackTrace();
        }
	}

}
