package com.AskAlexa;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Morgan
 */
public class Morgan extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Morgan() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String speechOutput = "Hello, You need to register with us first. We couldn't find your ssn and pin in Morganstanley DB";
		String ssn = request.getParameter("ssn");
		String pinVal = request.getParameter("pin");
		if (null != ssn && ssn.length() >=3){
			if (null != pinVal && pinVal.length() == 4){
				if ("123".equalsIgnoreCase(ssn) && "1234".equalsIgnoreCase(pinVal)) {
					speechOutput = " Welcome Sandeep, It was nice talking to you.";
				}else if ("456".equalsIgnoreCase(ssn) && "1234".equalsIgnoreCase(pinVal)) {
					speechOutput = " Welcome John, I wish to put alexa integration into production. ";
				}else if ("789".equalsIgnoreCase(ssn) && "1234".equalsIgnoreCase(pinVal)) {
					speechOutput = " Welcome Bhavin, I hope you enjoyed your recent chandigarh trip.";
				}
			}
		}
	
		response.getWriter().append(speechOutput);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
