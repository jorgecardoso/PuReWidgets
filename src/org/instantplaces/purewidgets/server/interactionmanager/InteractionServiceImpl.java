package org.instantplaces.purewidgets.server.interactionmanager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import org.instantplaces.purewidgets.client.widgetmanager.InteractionService;
import org.instantplaces.purewidgets.shared.Log;
import org.instantplaces.purewidgets.shared.exceptions.InteractionManagerException;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class InteractionServiceImpl extends RemoteServiceServlet implements
		InteractionService {
	
//	@Override
//	public String getApplicationsFromPlace(String url)  throws InteractionManagerException {
//		return doMethod("GET", null, url);
//	}
	
	@Override
	public String postWidget(String json, String url) throws InteractionManagerException  {
		return doMethod("POST", json, url);		
	}
	
	
	@Override
	public String get(String url) throws InteractionManagerException  {
		return doMethod("GET", null, url);
	}
	
	
//	@Override
//	public String getWidgetInput(String url) throws InteractionManagerException  {
//		return doMethod("GET", null, url);
//	}
	
	
	@Override
	public String deleteWidget( String url) throws InteractionManagerException  {
		return doMethod("DELETE", null, url);
	}
	
	
	/**
	 * Makes a connection to the specified URL and returns the associated 
	 * InputStream. This method makes sure that the connection does not use 
	 * the web cache.
	 * 
	 * @param urlString
	 * @return
	 */
	private String doMethod(String method, String data, String urlString) throws InteractionManagerException {
		Log.debug(this, method + " " + urlString );
		Log.debug(this, "  BODY: " + data);
		System.setProperty("sun.net.http.retryPos", "false");
		try {
			java.net.URL url = new java.net.URL(urlString);
			HttpURLConnection con;
		
			// 	con = (HttpURLConnection)u.openConnection(proxy);
			con = (HttpURLConnection) url.openConnection();
			con.setReadTimeout(1000*30);
			if ( null != method ) {
				con.setRequestMethod(method);
			}
			if ( null != data ) {
				con.setDoOutput(true);
			}
			con.addRequestProperty("Content-type", "application/json");
			
			con.setUseCaches(false);
			con.setRequestProperty("Cache-control", "max-age=0");
		
			con.connect();
			
			/*
			 * Write the data out.
			 */
			if ( null != data ) {
				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(con.getOutputStream()));
				bw.write(data);
				bw.flush();
			}
			
			/*
			 * Read the response.
			 */
			BufferedReader bf = new BufferedReader(new InputStreamReader(con.getInputStream()));
		
			StringBuilder builder = new StringBuilder();
			String line;
			do {
				line = bf.readLine();
				if (null != line) {
					builder.append(line);
				}
			} while (null != line);
		
			if (con.getResponseCode() != 200 ) {
			//	this.getThreadLocalResponse().sendError(con.getResponseCode(), con.getResponseMessage());
				throw new InteractionManagerException(con.getResponseCode() + " : " + con.getResponseMessage() + " " +builder.toString()) ;
			}
			//this.getThreadLocalResponse().setS setStatus(con.getResponseCode(), con.getResponseMessage());
			
			return builder.toString();
			
		
		
		} catch (IOException e) {
			
			Log.error(e.getMessage());
			throw new InteractionManagerException(e.getMessage());
		}
		//return null;
	}
	
	
	
	/**
	 * Escape an html string. Escaping data received from the client helps to
	 * prevent cross-site script vulnerabilities.
	 * 
	 * @param html the html string to escape
	 * @return the escaped string
	 */
	/*private String escapeHtml(String html) {
		if (html == null) {
			return null;
		}
		return html.replaceAll("&", "&amp;").replaceAll("<", "&lt;")
				.replaceAll(">", "&gt;");
	}*/

	
}
