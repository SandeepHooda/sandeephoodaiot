package com.iot;

import java.io.IOException;
import java.net.URL;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.appengine.api.urlfetch.FetchOptions;
import com.google.appengine.api.urlfetch.HTTPHeader;
import com.google.appengine.api.urlfetch.HTTPMethod;
import com.google.appengine.api.urlfetch.HTTPRequest;
import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;

/**
 * Servlet implementation class SetAppliance
 */
public class SetAppliance extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SetAppliance() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		URLFetchService fetcher = URLFetchServiceFactory.getURLFetchService();
		FetchOptions lFetchOptions = FetchOptions.Builder.doNotValidateCertificate();
		String collection = request.getParameter("collection");
		String currentData = getCurrentStatus(collection);
		
		String light = request.getParameter("light");
		String fan = request.getParameter("fan");
		String respo = "";
		JSONObject applianceJsonStatus =null;
		try {
			applianceJsonStatus = new JSONObject(currentData);
			if (light != null){
				if("on".equalsIgnoreCase(light)){//Because wiring is done do
					light = "off";
				}else {
					light = "on";
				}
				applianceJsonStatus.put("light", light);
			}
			if (fan != null){
				/*if("on".equalsIgnoreCase(fan)){//Fan is not controlled yet
					fan = "off";
				}else {
					fan = "on";
				}*/
				applianceJsonStatus.put("fan", fan);
			}
			
			String httpsURL = "https://api.mlab.com/api/1/databases/sandeepdb/collections/"+collection+"?apiKey=soblgT7uxiAE6RsBOGwI9ZuLmcCgcvh_";
			
			 try {
				
			        URL url = new URL(httpsURL);
		            HTTPRequest req = new HTTPRequest(url, HTTPMethod.PUT, lFetchOptions);
		            HTTPHeader header = new HTTPHeader("Content-type", "application/json");
		            
		            req.setHeader(header);
		           
		            req.setPayload((applianceJsonStatus.toString()).getBytes());
		            HTTPResponse res = fetcher.fetch(req);
		            respo =(new String(res.getContent()));
		 
		        } catch (IOException e) {
		        	respo = e.getMessage();
		        }
		} catch (JSONException e1) {
			respo = e1.getMessage();
			e1.printStackTrace();
		}
		
		
		
		
		
	  
		 response.setContentType("text/plain");
		
		if (null != applianceJsonStatus){
			response.getWriter().println(applianceJsonStatus.toString());
		}else {
			response.getWriter().println("Could not find current status of appliance");
		}
		 
	}
	private String getCurrentStatus(String collection){
		URLFetchService fetcher = URLFetchServiceFactory.getURLFetchService();
		FetchOptions lFetchOptions = FetchOptions.Builder.doNotValidateCertificate();

		String httpsURL = "https://api.mlab.com/api/1/databases/sandeepdb/collections/"+collection+"?apiKey=soblgT7uxiAE6RsBOGwI9ZuLmcCgcvh_";
		
			httpsURL += "&f={\"_id\":0}";
		
		String respo = "";
		 try {
			
		        URL url = new URL(httpsURL);
	            HTTPRequest req = new HTTPRequest(url, HTTPMethod.GET, lFetchOptions);
	            HTTPResponse res = fetcher.fetch(req);
	            respo =(new String(res.getContent()));
	 
	        } catch (IOException e) {
	        	respo = e.getMessage();
	        }
		
	  
		 
		
		
		 return respo.replaceAll("\\[", "").replaceAll("]", "");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
