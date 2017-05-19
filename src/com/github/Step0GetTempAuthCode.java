package com.github;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Step0GetTempAuthCode
 */
public class Step0GetTempAuthCode extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Step0GetTempAuthCode() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    
	    /*
	    	Step 1.	Pass the client ID and get user code (access code). User need to approve the app to access requested data (one time) before you get access code
		
		  	Step 2. Use the code (access code from previous step) and exchange access_token
	
	  		Step 3. Use the access token to access the API
	  */

	protected void doGet(HttpServletRequest request, HttpServletResponse res) throws ServletException, IOException {
		//Pass the client ID and get user code (access code). 
		//User need to approve the app to access requested data before you get access code
		String urlRedirect = "https://github.com/login/oauth/authorize?client_id=142773aa0436a5d877df&scope=user&redirect_uri=https://sandeephoodaiot.appspot.com/GitHub/Step1GetAccessToken&state=1" ;
		res.sendRedirect(urlRedirect);
		
		
		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
