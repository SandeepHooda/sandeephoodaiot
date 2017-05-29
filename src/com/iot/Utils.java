package com.iot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONException;
import org.json.JSONObject;

import com.Constants;
import com.google.appengine.api.urlfetch.FetchOptions;
import com.google.appengine.api.urlfetch.HTTPHeader;
import com.google.appengine.api.urlfetch.HTTPMethod;
import com.google.appengine.api.urlfetch.HTTPRequest;
import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;

public class Utils {
	private static final Logger log = Logger.getLogger(Utils.class.getName());
	private static URLFetchService fetcher = URLFetchServiceFactory.getURLFetchService();
	private static FetchOptions lFetchOptions = FetchOptions.Builder.doNotValidateCertificate();
	private static List<String> enrolledEmails = new ArrayList<String>();
	static{
		enrolledEmails.add("sonu.hooda@gmail.com");
		enrolledEmails.add("alexatestsanhoo1@gmail.com");
		enrolledEmails.add("alexatestsanhoo2@gmail.com");
		
	}
	
	public static boolean isUserEnrolled(String email){
		if (null != email){
			email = email.toLowerCase();
		}
		return enrolledEmails.contains(email);
	}
	public static String getCurrentStatus(String collection){
		URLFetchService fetcher = URLFetchServiceFactory.getURLFetchService();
		FetchOptions lFetchOptions = FetchOptions.Builder.doNotValidateCertificate();

		String httpsURL = "https://api.mlab.com/api/1/databases/sandeepdb/collections/"+collection+"?apiKey="+Constants.mlabKey;
		
			httpsURL += "&f={\"_id\":0}";
		
		String respo = "";
		 try {
			
		        URL url = new URL(httpsURL);
	            HTTPRequest req = new HTTPRequest(url, HTTPMethod.GET, lFetchOptions);
	            HTTPResponse res = fetcher.fetch(req);
	            respo =(new String(res.getContent()));
	 
	        } catch (IOException e) {
	        	e.printStackTrace();
	        	respo ="Error : "+ e.getMessage();
	        }
		
	  
		 
		 
		
		 return respo.replaceAll("\\[", "").replaceAll("]", "") ;
	}
	
	public static String getUserDetails(String access_token){
		StringBuffer json = null;
		 
		 if (null != access_token){
			 json = new StringBuffer();
			 try {
					URL url = new URL("https://api.github.com/user?access_token="+access_token );
					 BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
					
					 String line;

					 while ((line = reader.readLine()) != null) {
					   json.append(line);
					 }
					 reader.close();
					
				} catch (Exception e) {
					
					e.printStackTrace();
					
				} 
		 }else {
			
		 }
		return json.toString();
	}
	public static void createNewCollection(String collection, String email){
		String collectionToCreate = collection +"_"+email;
		String httpsURL = "https://api.mlab.com/api/1/databases/sandeepdb/collections/"+collectionToCreate+"?apiKey="+Constants.mlabKey;
		
		 try {
			
		        URL url = new URL(httpsURL);
	            HTTPRequest req = new HTTPRequest(url, HTTPMethod.POST, lFetchOptions);
	            HTTPHeader header = new HTTPHeader("Content-type", "application/json");
	            
	            req.setHeader(header);
	           
	            req.setPayload("{ \"_id\" : 1 }".getBytes());
	            fetcher.fetch(req);
	            
	 
	        } catch (IOException e) {
	        	
	        }
	}
	
	public static void updateData(String collection, JSONObject applianceJsonStatus){
		String httpsURL = "https://api.mlab.com/api/1/databases/sandeepdb/collections/"+collection+"?apiKey="+Constants.mlabKey;
		
		 try {
			
		        URL url = new URL(httpsURL);
	            HTTPRequest req = new HTTPRequest(url, HTTPMethod.PUT, lFetchOptions);
	            HTTPHeader header = new HTTPHeader("Content-type", "application/json");
	            
	            req.setHeader(header);
	           
	            req.setPayload((applianceJsonStatus.toString()).getBytes());
	            fetcher.fetch(req);
	            
	            log.info("Updated the DB  collection "+collection+applianceJsonStatus.toString());
	 
	        } catch (IOException e) {
	        	 log.info("Error while  upfdating DB  collection "+collection+applianceJsonStatus.toString()+" Message "+e.getMessage());
	        	e.printStackTrace();
	        }
	}
	
	public static JSONObject createApplianceStatusFromReq(HttpServletRequest request, JSONObject applianceJsonStatus, String currentData){
		String light = request.getParameter("light");
		String fan = request.getParameter("fan");
		String everything = request.getParameter("everything");
		
		try {
			applianceJsonStatus = new JSONObject(currentData);
			log.info("New json object created from currentData "+currentData);
			if (light != null){
				applianceJsonStatus.put("light", light);
			}
			if (fan != null){
				
				applianceJsonStatus.put("fan", fan);
			}
			if (everything != null){
				applianceJsonStatus.put("light", everything);
				applianceJsonStatus.put("fan", everything);
			}
			applianceJsonStatus.put("requestTime", new Date().toString());
			
		} catch (JSONException e1) {
			
			e1.printStackTrace();
		}
		log.info("Final appliance status to be writed to DB "+applianceJsonStatus.toString());
		return applianceJsonStatus;
	}
	
	


}
