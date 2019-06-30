package com.voltage;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.voltage.vo.VoltageData;
import com.voltage.vo.VoltageVO;

import mangodb.MangoDB;

/**
 * Servlet implementation class VoltageNoW
 */
public class VoltageNoW extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Gson gson = new Gson();  
	
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public VoltageNoW() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		StringBuilder table = new StringBuilder("<table style=\"width:100%\" border=\"1\">");
		//Get current voltage 
		Calendar cal = new GregorianCalendar();
		SimpleDateFormat idDateSdf = new SimpleDateFormat(VoltageLogger.idDate);
		idDateSdf.setTimeZone(VoltageLogger.userTimeZone);
		SimpleDateFormat sdf = new SimpleDateFormat(VoltageLogger.timeFormat);
		sdf.setTimeZone(VoltageLogger.userTimeZone);
		 
		String id = idDateSdf.format(cal.getTime());
		String dataNow = MangoDB.getDocumentWithQuery("voltage-logger", "voltage-logger", id, true, MangoDB.mlabKeyReminder, null);
		VoltageVO voltageVO = null;
		if (null == dataNow || dataNow.trim().length() ==0) {
			voltageVO = new VoltageVO();
		}else {
			voltageVO = gson.fromJson(dataNow, new TypeToken<VoltageVO>() {}.getType());
			VoltageData vdataNow = voltageVO.getVoltageData().get(voltageVO.getVoltageData().size()-1);
			voltageVO.setVoltage( vdataNow.getVoltage());
			voltageVO.setVoltageDate(sdf.format(vdataNow.getTime()));
			voltageVO.setVoltageData(null);
			table.append("<tr><td>Current Voltage</td><td>"+voltageVO.getVoltage()+"</td></tr/>");
			table.append("<tr><td>Time</td><td>"+voltageVO.getVoltageDate()+"</td></tr/>");
		}
		
		String fromDateStr = request.getParameter("fromDate");
		//Get stats for a day from, PM to AM
		if (null != fromDateStr) {
			SimpleDateFormat idDateSdfUI = new SimpleDateFormat(VoltageLogger.idDateUI);
			try {
				Date fromDate = idDateSdfUI.parse(fromDateStr);
				cal.setTime(fromDate);
				cal.add(Calendar.DAY_OF_MONTH, 1);
				String toDateStr = idDateSdfUI.format(cal.getTime())+"_AM";
				fromDateStr += "_PM";
				
				VoltageVO voltageLogs = null;
				String dataFrom = MangoDB.getDocumentWithQuery("voltage-logger", "voltage-logger", fromDateStr, true, MangoDB.mlabKeyReminder, null);
				voltageLogs = gson.fromJson(dataFrom, new TypeToken<VoltageVO>() {}.getType());
				 if (null == voltageLogs) {
					 voltageLogs  = new VoltageVO();
				 }
				 
				 String dataTo = MangoDB.getDocumentWithQuery("voltage-logger", "voltage-logger", toDateStr, true, MangoDB.mlabKeyReminder, null);
				 VoltageVO voltageLogsMorning = gson.fromJson(dataTo, new TypeToken<VoltageVO>() {}.getType());
				 if (null != voltageLogsMorning) {
					 voltageLogs.getVoltageData().addAll(voltageLogsMorning.getVoltageData());
				 }
				 
				 //from the stats populate the count in voltageVO
				 for (VoltageData aData:voltageLogs.getVoltageData() ) {
						float voltage = aData.getVoltage();
						
						if (voltage<=150) {
							voltageVO.data150below++;
						}else if (voltage >150 && voltage <=160) {
							voltageVO.data150_160 ++;
						}else if (voltage >160 && voltage <=170) {
							voltageVO.data160_170++;
						}else if (voltage >170 && voltage <=180) {
							voltageVO.data170_180++;
						}else if (voltage >180 && voltage <=190) {
							voltageVO.data180_190++;
						}else if (voltage >190 && voltage <=200) {
							voltageVO.data190_200++;
						}else if (voltage >200) {
							voltageVO.data200Above++;
						}
					}
				 
				 //what is the stats recording time
				 if (voltageLogs.getVoltageData().size() >2) {
					 voltageVO.setRecordingStartTime(sdf.format(new Date(voltageLogs.getVoltageData().get(0).getTime())));
					 voltageVO.setRecordingEndTime(sdf.format(new Date(voltageLogs.getVoltageData().get(voltageLogs.getVoltageData().size()-1).getTime())));
					 
					 
					 table.append("<tr><td>Less that 150</td><td>"+voltageVO.data150below+"</td></tr/>");
					 table.append("<tr><td>150 - 160</td><td>"+voltageVO.data150_160+"</td></tr/>");
					 table.append("<tr><td>160-170</td><td>"+voltageVO.data160_170+"</td></tr/>");
					 table.append("<tr><td>170 - 180 </td><td>"+voltageVO.data170_180+"</td></tr/>");
					 table.append("<tr><td>180 - 190</td><td>"+voltageVO.data180_190+"</td></tr/>");
					 table.append("<tr><td>190 - 200</td><td>"+voltageVO.data190_200+"</td></tr/>");
					 table.append("<tr><td>200 above</td><td>"+voltageVO.data200Above+"</td></tr/>");
					 table.append("<tr><td>Start Time</td><td>"+voltageVO.getRecordingStartTime()+"</td></tr/>");
					 table.append("<tr><td>End Time</td><td>"+voltageVO.getRecordingEndTime()+"</td></tr/>");
				 }
				
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
		
		
		//Print output
		table.append("</table>");
		PrintWriter out = response.getWriter();
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		out.print(table.toString());
		out.flush();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
