package org.jorgecardoso.purewidgets.demo.placeinteraction.server;



import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.text.NumberFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jorgecardoso.purewidgets.demo.placeinteraction.client.SightingService;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class SightingServiceImpl extends RemoteServiceServlet implements
		SightingService {
	private static int deviceAddress;
	
	private static final Logger log; 
	static {
		log = Logger.getLogger("SigtingServiceImpl");
		log.setLevel(Level.ALL);
	}
	
	   /* return StringBuilder and/or make Writer param and write to stream directly*/
	   public static String htmlEntityEncode( String s )
	   {
	       StringBuilder buf = new StringBuilder(s.length());
	       for ( int i = 0; i < s.length(); i++ )
	       {
	           char c = s.charAt( i );
	           if ( c>='a' && c<='z' || c>='A' && c<='Z' || c>='0' && c<='9' )
	           {
	               buf.append( c );
	           }
	           else
	           {
	               buf.append("&#").append((int)c).append(";");
	           }
	       }
	       return buf.toString();
	   }
	   
	@Override
	public void sighting(String sighting, String date) {
		String encoded =  htmlEntityEncode(sighting);
		
		//deviceAddress++;
		// TODO: Check why we needed to generate random addresses
		deviceAddress = (int)(Math.random()*10000);
		
		NumberFormat nf=NumberFormat.getInstance(); // Get Instance of NumberFormat
		nf.setMinimumIntegerDigits(12);  // The minimum Digits required is 5
		nf.setMaximumIntegerDigits(12); // The maximum Digits required is 5
		nf.setGroupingUsed(false);
		String address=(nf.format(deviceAddress));
		log.info("Generated device address:" + address);
		
		
		String data = "<?xml version=\"1.0\"?>";
		data += "<sightings xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns=\"urn:Mynamespace\">";
		data += "<clientReportId>10</clientReportId>";
		data += "<scanningBeginTime>" + date + "</scanningBeginTime>";
		data += "<clientReportTime>" + date + "</clientReportTime>";
		data += "<sightingItemsList>";
		data += "<sightingItem>";
		data += "<deviceName>" + " " +  encoded + " " + "</deviceName>";
		data += "<deviceAddress>" + address + "</deviceAddress>";
		data += "<deviceClass>CellPhone</deviceClass>";
		data += "<isAuthenticated>false</isAuthenticated>";
		data += "<isConnected>false</isConnected>";
		data += "</sightingItem>";
		data += "</sightingItemsList>";
		data += "</sightings>";

		
		URL u = null;
		try {
			// TODO: Check URL of service
			u = new java.net.URL(
					"http://193.137.8.107/InstantPlacesService/instantPlacesService.svc/domain/dsi/place/jorge/scannerAddress/222222222222/reports");

			HttpURLConnection con;
			con = (HttpURLConnection) u.openConnection();
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setUseCaches(false);
			//con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/xml");
			DataOutputStream printout = new DataOutputStream(con
					.getOutputStream());
			printout.writeBytes(data);
			printout.flush();
			printout.close();

			InputStream is = con.getInputStream();

			InputStreamReader isr = new InputStreamReader(is, "utf-8");
			StringBuffer sb = new StringBuffer();

			char buffer[] = new char[1024];

			int c = 0;
			do {
				c = isr.read(buffer);
				if (c > 0)
					sb.append(buffer, 0, c);
			} while (c != -1);

			log.info("Server returned: " + sb.toString());
			//System.out.println("Server returned: " + sb.toString());
			isr.close();
			is.close();

		} catch (MalformedURLException murle) {
			log.severe(murle.getMessage());

		} catch (IOException e) {
			log.severe("Error: " + e.getMessage());

		} catch (Exception e) {
			log.severe(e.getMessage());
		}
	}

}
