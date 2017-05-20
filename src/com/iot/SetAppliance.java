package com.iot;


import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.appengine.api.urlfetch.FetchOptions;

import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;

/**
 * Servlet implementation class SetAppliance
 */
public class SetAppliance extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(SetAppliance.class.getName());
       
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
		boolean aValidRequest = true;
		//1. Validate request
		String access_token = request.getParameter("access_token");
		if (null == access_token || access_token.trim().length() == 0){
			response.sendError(response.SC_BAD_REQUEST);
			log.info("Bad request missing access token");
			aValidRequest = false;
		
		}
		
		//2. Get User details
		String userDetails = Utils.getUserDetails(access_token);
		JSONObject userDetailsJson = null;
		String email = "";
		String userName = "";
		if (null != userDetails){
			try {
				userDetailsJson = new JSONObject(userDetails);
				if (null == userDetailsJson || userDetailsJson.getString("email") == null || "".equals(userDetailsJson.getString("email")) || userDetailsJson.getString("name") == null){
					response.sendError(response.SC_UNAUTHORIZED);
					log.info("Un Authorised request ");
					aValidRequest = false;
					
				}else {
					email = userDetailsJson.getString("email");
					if(!Utils.isUserEnrolled(email)){
						response.sendError(response.SC_FORBIDDEN);
						log.info("User is not enrolled  "+email);
						aValidRequest = false;
					}
					userName = userDetailsJson.getString("name");
					if (null != userName && !"".equals(userName)){
						if (userName.indexOf(" ") >0){
							userName = userName.substring(0, userName.indexOf(" "));
						}
					}else {
						userName = "";
					}
				}
				
			} catch (JSONException e) {
				e.printStackTrace();
				response.sendError(response.SC_UNAUTHORIZED);
				log.info("Un-Authorised request having exception Details "+e.getMessage()+" Access token "+access_token);
				aValidRequest = false;
				return;
			}
		}
		
		
		if (aValidRequest){
			//3. Now When we have a valid user details
			
			URLFetchService fetcher = URLFetchServiceFactory.getURLFetchService();
			FetchOptions lFetchOptions = FetchOptions.Builder.doNotValidateCertificate();
			String collectionNameOrignal = request.getParameter("collection");
			String collection = collectionNameOrignal;
			collection += "_"+email ;
			
			//4. Get appliance current status
			String currentData = Utils.getCurrentStatus(collection);
			if(null == currentData || "".equals(currentData.trim())){// There is no collection with that name so lets create that
				currentData = "{ \"fan\" : \"off\" , \"light\" : \"off\"}";
				Utils.createNewCollection(collectionNameOrignal, email);
				Utils.createNewCollection("microCtrl_"+collectionNameOrignal, email);
			}
			
			
			//5. Create json data from current data in db and applying status from request 
			JSONObject applianceJsonStatus =null;
			applianceJsonStatus = Utils.createApplianceStatusFromReq(request, applianceJsonStatus, currentData);
			
			//6. Update teh appliance status in DB
			Utils.updateData(collection, applianceJsonStatus);
			
		  
			 response.setContentType("text/plain");
			 try {
				applianceJsonStatus.put("userName", userName);
				applianceJsonStatus.put("email", email);
				log.info("Setting user details  name "+userName + " email ="+email);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 
			if (null != applianceJsonStatus){
				response.getWriter().println(applianceJsonStatus.toString());
			}else {
				response.getWriter().println("Could not find current status of appliance");
			}
		}
		
		 
	}
	
	
	

	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
