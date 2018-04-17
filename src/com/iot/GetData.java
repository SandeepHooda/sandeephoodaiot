package com.iot;

import java.io.IOException;
import java.net.URL;

import javax.servlet.http.*;

import org.json.JSONException;
import org.json.JSONObject;

import com.Constants;
import com.google.appengine.api.urlfetch.FetchOptions;
import com.google.appengine.api.urlfetch.HTTPMethod;
import com.google.appengine.api.urlfetch.HTTPRequest;
import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;

/**
 * Servlet implementation class GetData
 */
public class GetData extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetData() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @throws IOException 
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException  {
		URLFetchService fetcher = URLFetchServiceFactory.getURLFetchService();
		FetchOptions lFetchOptions = FetchOptions.Builder.doNotValidateCertificate();
		String collection = request.getParameter("collection");
		String datakey = request.getParameter("datakey");
		String access_token = request.getParameter("access_token");
		if (access_token != null){
			String userDetails = Utils.getUserDetails(access_token);
			try {
				JSONObject userDetailsJson = new JSONObject(userDetails);
				collection += "_"+userDetailsJson.getString("email");
			} catch (JSONException e) {
				e.printStackTrace();
			};
		}
		
		
		
		String httpsURL = "https://api.mlab.com/api/1/databases/sandeepdb/collections/"+collection+"?apiKey="+Constants.mlabKey;
		if (datakey != null && datakey.trim().length() > 0){
			httpsURL += "&f={\""+datakey+"\":1,\"_id\":0}";
		}else{
			httpsURL += "&f={\"_id\":0}";
		}
		String respo = "";
		 try {
			
		        URL url = new URL(httpsURL);
	            HTTPRequest req = new HTTPRequest(url, HTTPMethod.GET, lFetchOptions);
	            HTTPResponse res = fetcher.fetch(req);
	            respo =(new String(res.getContent()));
	 
	        } catch (IOException e) {
	        	respo = e.getMessage();
	        }
		
	  
		 response.setContentType("text/plain");
		
		 respo = respo.replace("\\[", "").trim();
		 if (respo.indexOf("]") > 0){
			 respo.substring(0, respo.length()-1);
		 }
		 response.getWriter().println();
	}

	/**
	 * @throws IOException 
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException  {
		
		doGet(request, response);
	}

}
