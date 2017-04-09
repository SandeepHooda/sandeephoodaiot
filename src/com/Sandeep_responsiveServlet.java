package com;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.appengine.api.urlfetch.FetchOptions;

import java.io.BufferedReader;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class Sandeep_responsiveServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		FetchOptions.Builder.doNotValidateCertificate();
		String message = "";
		JSONObject jsonObj = new JSONObject();
		try {
			
			jsonObj.put("to", "fG03A_TSgOM:APA91bH7lDTX1yhDcWV_407jX1oRUnArE2SC-zu56hUJHrRCZ_bFndTFdvLejxom2ILCCjse-z0dUIfKb95TIWVAB_4PXXORTfXkS6JkzaVz1Il7PXk-K6Ej4buFLeE1nUci5PCW3csp");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// [START complex]
	    URL url = new URL("https://fcm.googleapis.com/fcm/send" );
	    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	    conn.setDoOutput(true);
	    conn.setRequestMethod("POST");
	    conn.setRequestProperty("Authorization", "key=AAAAIKVxuRg:APA91bFG3jtxpf7yVzFTXED07EQLjcL-hVerdRrm9Ap0s8JQA-YzUx9hXdFYSocrSz22JlaylcF94M3vhJ_d2AweEP4U8cuBG7FgljdHl4srh1CLabe-H59nPl_NM5fwWRhVwQx1k5JN");
	    conn.setRequestProperty("Content-Type","application/json");
	    
	    OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
	    writer.write(jsonObj.toString());
	    writer.close();

	    int respCode = conn.getResponseCode();  // New items get NOT_FOUND on PUT
	    if (respCode == HttpURLConnection.HTTP_OK || respCode == HttpURLConnection.HTTP_NOT_FOUND) {
	      req.setAttribute("error", "");
	      StringBuffer response = new StringBuffer();
	      String line;

	      BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	      while ((line = reader.readLine()) != null) {
	        response.append(line);
	      }
	      reader.close();
	      message = "All good "+response.toString();
	      req.setAttribute("response", response.toString());
	    } else {
	    	StringBuffer response = new StringBuffer();
		      String line;
	    	BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		      while ((line = reader.readLine()) != null) {
		        response.append(line);
		      }
		      reader.close();
	    	message += conn.getResponseCode() +" "+conn.getResponseMessage()+" "+response.toString();
	      
	    }
	    // [END complex]

		resp.setContentType("text/plain");
		resp.getWriter().println(message);
	  }

	 
}
