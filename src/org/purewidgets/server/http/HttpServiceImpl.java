package org.purewidgets.server.http;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;

import org.purewidgets.client.http.HttpService;
import org.purewidgets.shared.Log;
import org.purewidgets.shared.exceptions.HttpServerException;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * Provides a service for HTTP method invocations.
 * 
 * 
 */
@SuppressWarnings("serial")
public class HttpServiceImpl extends RemoteServiceServlet implements
		HttpService {
		
	@Override
	public String post( String data, String url ) throws HttpServerException  {
		return doMethod("POST", data, url);		
	}
	
	
	@Override
	public String get( String url ) throws HttpServerException  {
		String result = doMethod("GET", null, url);
		return result;
	}
	
	
	@Override
	public String delete( String url ) throws HttpServerException  {
		return doMethod("DELETE", null, url);
	}
	
	
	/**
	 * Makes a connection to the specified URL and returns the associated 
	 * InputStream. This method makes sure that the connection does not use 
	 * cache.
	 * 
	 * @param method The HTTP method to use
	 * @param data The payload data
	 * @param urlString The URL to invoke.
	 * 
	 * @return The server's response
	 * @throws HttpServerException In case of a IOException or server response other than 200 Ok.
	 */
	private String doMethod(String method, String data, String urlString) throws HttpServerException {
		Log.debug(this, "Calling " + method + " " + urlString );
		Log.debugFinest(this, "Request Body: " + data);
		System.setProperty("sun.net.http.retryPos", "false");
		try {
			java.net.URL url = new java.net.URL(urlString);
			HttpURLConnection con;
		
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
	

			
			if (con.getResponseCode() != 200 ) {
				Log.warn(this, "Received response: " );
				Log.warn(this, "\t Code:" + con.getResponseCode() );
				Log.warn(this, "\t Message:" + con.getResponseMessage() );
				Log.warn(this, "\t Body:" + builder.toString() );
				throw new HttpServerException(con.getResponseCode() + " : " + con.getResponseMessage() + " " + builder.toString()) ;
			}

			Log.debugFinest(this, "Response Body:" + builder.toString() );
			return builder.toString();
			
		
		
		} catch (IOException e) {
			Log.error(this, "IO Error. ", e);
			throw new HttpServerException(e.getMessage());
		}

	}
	
		
}
