package com.gateKeeper;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mangodb.MangoDB;

/**
 * Servlet implementation class MotionDetected
 */
public class MotionDetected extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MotionDetected() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		MangoDB.createNewDocument("sandeepdb", "gate-keeper", "{   \"_id\": \"1\",   \"motionDetected\": \"true\" , \"time\" : \""+(new Date())+"\"}", MangoDB.mlabKeySonu);
		
		response.getWriter().print(MangoDB.getDocumentWithQuery("sandeepdb", "gate-keeper", "1",true, MangoDB.mlabKeySonu,null));
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
