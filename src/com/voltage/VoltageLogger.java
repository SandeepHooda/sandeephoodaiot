package com.voltage;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.voltage.vo.VoltageData;
import com.voltage.vo.VoltageVO;

import mangodb.MangoDB;

/**
 * Servlet implementation class VoltageLogger
 */
public class VoltageLogger extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static String timeFormat = "yyyy/M/d HH:mm:ss";
	public static String idDate = "yyyy_M_d_a";
	public static String idDateUI = "yyyy_M_d";
	public static TimeZone userTimeZone	=	TimeZone.getTimeZone("Asia/Kolkata");
    private Gson gson = new Gson();  
    /**
     * @see HttpServlet#HttpServlet()
     */
    public VoltageLogger() {
        super();
        // TODO Auto-generated constructor stub
    }
    private void saveVoltage(float voltage) {
    	Calendar cal = new GregorianCalendar();
		
		SimpleDateFormat sdf = new SimpleDateFormat(timeFormat);
		SimpleDateFormat idDateSdf = new SimpleDateFormat(idDate);
		
	   
		sdf.setTimeZone(userTimeZone);
		idDateSdf.setTimeZone(userTimeZone);
		
		
		
			
			String id = idDateSdf.format(new Date(cal.getTimeInMillis()));
			
			String data = MangoDB.getDocumentWithQuery("voltage-logger", "voltage-logger", id, true, MangoDB.mlabKeyReminder, null);
			
			VoltageVO voltageVO = null;
			if (null == data || data.trim().length() ==0) {
				voltageVO = new VoltageVO();
				voltageVO.set_id(id);
				
			}else {
				voltageVO = gson.fromJson(data, new TypeToken<VoltageVO>() {}.getType());
				
			}
				
			VoltageData vdata = new VoltageData();
			vdata.setTime(cal.getTimeInMillis());
			vdata.setVoltage(voltage);
			
			voltageVO.getVoltageData().add(vdata);
			
			 data =  gson.toJson(voltageVO,  new TypeToken<VoltageVO>() {}.getType());
			MangoDB.createNewDocumentInCollection("voltage-logger", "voltage-logger", data, null);
			
		
    }

    private void saveVoltage(String voltageStr) {
    	if (null != voltageStr) {
			float voltage = Float.parseFloat(voltageStr);
			saveVoltage(voltage);
    	}
    }
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String voltageStr = request.getParameter("voltage");
		saveVoltage(voltageStr);
		response.getWriter().append("voltage ").append(voltageStr );
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		byte[] serializedSpeechletRequest = IOUtils.toByteArray(request.getInputStream());
		String completeRequest = new String(serializedSpeechletRequest);
        
		Gson gson = new Gson();
        
        VoltageData voltageData = (VoltageData) gson.fromJson(completeRequest, VoltageData.class);
       
        saveVoltage(voltageData.getVoltage());
        response.getWriter().append("OK POST").append(""+voltageData.getVoltage());
	}

}
