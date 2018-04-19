package com.iot;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.*;

import org.json.JSONException;
import org.json.JSONObject;

import com.Constants;
import com.google.Oauth;
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
	private static final Logger log = Logger.getLogger(GetData.class.getName());
       
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
    
    //Bt index.html to get DB status
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException  {
		URLFetchService fetcher = URLFetchServiceFactory.getURLFetchService();
		FetchOptions lFetchOptions = FetchOptions.Builder.doNotValidateCertificate();
		String collection = request.getParameter("collection");
		String datakey = request.getParameter("datakey");
		
		
		Map<String, String> userDetails =  new HashMap<String, String>(); 
		
			
			Cookie[] cookies = request.getCookies();
			 
			 if (null != cookies){
				 for (int i=0;i<cookies.length;i++){
					 Cookie aCookie = cookies[i];
					 if("email".equalsIgnoreCase(aCookie.getName())){
						 userDetails.put("email", aCookie.getValue())  ;
					 }else  if("name".equalsIgnoreCase(aCookie.getName())){
						 userDetails.put("name", aCookie.getValue())  ;
					 }
				 }
			 }
			 
			try {
				log.info("user email "+userDetails.get("email"));
				collection += "_"+userDetails.get("email");
			} catch (Exception e) {
				e.printStackTrace();
			};
		
		
		
		
		String httpsURL = "https://api.mlab.com/api/1/databases/sandeepdb/collections/"+collection+"?apiKey="+Constants.mlabKey;
		if (datakey != null && datakey.trim().length() > 0){
			httpsURL += "&f={\""+datakey+"\":1,\"_id\":0}";
		}else{
			httpsURL += "&f={\"_id\":0}";
		}
		String respo = "";
		 try {
			 log.info("connecting to mlab ");
		        URL url = new URL(httpsURL);
	            HTTPRequest req = new HTTPRequest(url, HTTPMethod.GET, lFetchOptions);
	            HTTPResponse res = fetcher.fetch(req);
	            respo =(new String(res.getContent()));
	            log.info(" response from mlab "+respo);
	        } catch (IOException e) {
	        	respo = e.getMessage();
	        }
		
	  
		 response.setContentType("text/plain");
		
		 respo = respo.replace("[", "").trim();
		 if (respo.indexOf("]") > 0){
			 respo = respo.trim();
			 respo =respo.substring(0, respo.length()-1);
		 }
		 response.getWriter().println(respo);
	}

	/**
	 * @throws IOException 
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException  {
		
		doGet(request, response);
	}

}
