package com.email;

import java.util.Properties;


import javax.mail.*;  
import javax.mail.internet.*;



public class PostManSMTPNotForAppSpot {
	

	public static void sendEmail(String text){
		
		  final String user="AlexaTestsanhoo2@gmail.com";
		  final String password="Sandeep@1234"; 
		    
		  String to="sonuhooda@gmail.com";
		  
		   //Get the session object  
		  
		 
		   Properties props = new Properties();
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.host", "smtp.gmail.com");
			props.put("mail.smtp.port", "587");
 
		     
			Session session = Session.getInstance(props,
					  new javax.mail.Authenticator() {
						protected PasswordAuthentication getPasswordAuthentication() {
							return new PasswordAuthentication(user, password);
						}
					  });
		    
		  
		   //Compose the message  
		    try {  
		     MimeMessage message = new MimeMessage(session);  
		     message.setFrom(new InternetAddress(user));  
		     message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));  
		     message.setSubject("Sandeep Hooda IOT project");  
		     message.setText(text);  
		       
		 	Transport.send(message);
		 	System.out.println(" email sent ");
		   
		     } catch (MessagingException e) {
		    	 e.printStackTrace();
		     }  
		 }  
}
