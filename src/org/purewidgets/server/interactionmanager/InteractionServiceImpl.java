package org.purewidgets.server.interactionmanager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map;

import org.purewidgets.client.widgetmanager.InteractionService;
import org.purewidgets.shared.Log;
import org.purewidgets.shared.exceptions.InteractionManagerException;

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
	public String post(String json, String url) throws InteractionManagerException  {
		return doMethod("POST", json, url);		
	}
	
	
	@Override
	public String get(String url) throws InteractionManagerException  {
		String result = doMethod("GET", null, url);
		return result;
	}
	
	
//	@Override
//	public String getWidgetInput(String url) throws InteractionManagerException  {
//		return doMethod("GET", null, url);
//	}
	
	
	@Override
	public String delete( String url) throws InteractionManagerException  {
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
		Log.debugFinest(this, "Request Body: " + data);
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
			con.setRequestProperty("Accept-Charset", "UTF-8");
			con.setUseCaches(false);
			con.setRequestProperty("Cache-control", "max-age=0");
		
			//Log.warn(this, "REQUEST HEADERS");
			//this.printHeaders(con.getRequestProperties());
			con.connect();
			
			/*
			 * Write the data out.
			 */
			if ( null != data ) {
				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(con.getOutputStream(), "UTF-8" ));
				bw.write(data);
				bw.flush();
			}
			
			/*
			 * Read the response.
			 */
			BufferedReader bf = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8" ));
		
			StringBuilder builder = new StringBuilder();
			String line;
			do {
				line = bf.readLine();
				if (null != line) {
					builder.append(line);
				}
			} while (null != line);
	
			//this.printHeaders(con.getR)
			//Log.warn(this, "RESPONSE HEADERS");
			//this.printHeaders(con.getHeaderFields());
			
			if (con.getResponseCode() != 200 ) {
				Log.warn(this, "Received response: " );
				Log.warn(this, "\t Code:" + con.getResponseCode() );
				Log.warn(this, "\t Message:" + con.getResponseMessage() );
				Log.warn(this, "\t Body:" + builder.toString() );
				
			//	this.getThreadLocalResponse().sendError(con.getResponseCode(), con.getResponseMessage());
				throw new InteractionManagerException(con.getResponseCode() + " : " + con.getResponseMessage() + " " + builder.toString()) ;
			}
			//this.getThreadLocalResponse().setS setStatus(con.getResponseCode(), con.getResponseMessage());
			
			Log.debug(this, "Response Body:" + builder.toString() );
			return builder.toString();
			
		
		
		} catch (IOException e) {
			
			Log.error(this, "IO Error: " + e.getMessage());
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
