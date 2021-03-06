package com.iot;


import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.json.JSONObject;

import com.email.PostMan;


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
		boolean errorInRequest = false;
		//1. Validate request
		String access_token = request.getParameter("access_token");
		String source = request.getParameter("source");
		
		Map<String, String> userDetails =  new HashMap<String, String>(); 
		if ("iot_index_html".equals(source)) {//called from index .html
			
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
		}else {//Called from alexa
			if (null == source || null == access_token || access_token.trim().length() == 0){
				response.sendError(HttpServletResponse.SC_BAD_REQUEST);
				log.info("Bad request missing access token");
				aValidRequest = false;
			
			}else {
				//2. Get User details
				userDetails = Utils.getUserDetailsFromMangoDB(access_token);
			}
			
		}
		
		
		
		String unAuthEmailText = userDetails.get("name")+" "+userDetails.get("email") +" Tried to access IOT project but was marked as (1) SC_UNAUTHORIZED. User might not have published his email address or his token expired";
		String forbiddenEmailText = userDetails.get("name")+" "+userDetails.get("email") +" Tried to access IOT project but was marked as (2) SC_FORBIDDEN. User has done all the setting right but he need to pay money and get his email enrolled.";
		
		String email = "";
		String userName = "";
		if (null != userDetails){
			try {
				
				if (userDetails.get("email") == null || userDetails.get("name") == null){
					response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
					log.info("Un Authorised request ");
					aValidRequest = false;
					PostMan.sendEmail(unAuthEmailText);
					
				}else {
					email = userDetails.get("email");
					if(!Utils.isUserEnrolled(email)){
						response.sendError(HttpServletResponse.SC_FORBIDDEN);
						log.info("User is not enrolled  "+email);
						PostMan.sendEmail(forbiddenEmailText);
						
						aValidRequest = false;
					}
					userName =userDetails.get("name");
					if (null == userName) {
						userName = "Dear user";
					}
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
				log.info("Un-Authorised request having exception Details "+e.getMessage()+" Access token "+access_token);
				PostMan.sendEmail(unAuthEmailText);
				aValidRequest = false;
				return;
			}
		}
		
		
		if (aValidRequest){
			//3. Now When we have a valid user details
			
			
			String collectionNameOrignal = request.getParameter("collection");
			String collection = collectionNameOrignal;
			collection += "_"+email ;
			
			//4. Get appliance current status
			String currentData = Utils.getCurrentStatus(collection);
			
			if(null == currentData || "".equals(currentData.trim())){// There is no collection with that name so lets create that
				currentData = "{ \"fan\" : \"on\" , \"light\" : \"on\"}";
				Utils.createNewCollection(collectionNameOrignal, email);
				Utils.createNewCollection("microCtrl_"+collectionNameOrignal, email);
			}else if (currentData.indexOf("Error") >=0 ){
				errorInRequest = true;
				return;
			}
			
			if (!errorInRequest){
				
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
					
					e.printStackTrace();
				}
				 
				if (null != applianceJsonStatus){
					response.getWriter().println(applianceJsonStatus.toString());
				}else {
					response.getWriter().println("Could not find current status of appliance");
				}
				
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
