package org.purewidgets.server.cron;

import static com.google.appengine.api.taskqueue.TaskOptions.Builder.withUrl;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.purewidgets.server.PMF;
import org.purewidgets.server.application.PublicDisplayApplication;
import org.purewidgets.shared.logging.Log;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskAlreadyExistsException;
import com.google.appengine.api.taskqueue.TaskOptions.Method;

public class UpdateApplication  extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) {
		PersistenceManager pm = PMF.get().getPersistenceManager();

		// Load applications
		Query query = pm.newQuery(PublicDisplayApplication.class);
		
		List<PublicDisplayApplication> results = (List<PublicDisplayApplication>) query.execute();
		
		ArrayList<String> urlParameters = new ArrayList<String>();
		for ( PublicDisplayApplication application : results ) {
			String p ="?" + PublicDisplayApplication.PLACE_NAME_PARAMETER + "=" + application.getPlaceId() +
							  "&" +	PublicDisplayApplication.APP_NAME_PARAMETER + "=" + application.getAppId();
			Log.debug(this, p);
			urlParameters.add(p);
		}
		pm.close();
		
		
		String servlet = req.getParameter("servlet");
		
		try {
			Queue queue = QueueFactory.getDefaultQueue();
			int time = 1000*60;
			for ( String urlParam : urlParameters ) {
				Log.info(this, "Adding task: " + servlet+urlParam);
				queue.add(withUrl(servlet+urlParam).countdownMillis(time+=1000*60).method(Method.GET));
			}
		} catch (TaskAlreadyExistsException taee) {
			
		} catch (Exception e) {
			Log.error(this, "Could not submit task: " + e.getMessage());
		}
	}
}
