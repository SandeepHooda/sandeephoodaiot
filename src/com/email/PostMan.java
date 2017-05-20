package com.email;


import java.util.Properties;
import java.util.logging.Logger;

import javax.mail.*;  
import javax.mail.internet.*;

  

public class PostMan {
	private static final Logger log = Logger.getLogger(PostMan.class.getName());

	public static void sendEmail(String text){
		
	    Properties props = new Properties();
	    Session session = Session.getDefaultInstance(props, null);

	    try {
	      Message msg = new MimeMessage(session);
	      msg.setFrom(new InternetAddress("sonu.hooda@gmail.com"));
	      msg.addRecipient(Message.RecipientType.TO,
	                       new InternetAddress("sonu.hooda@gmail.com"));
	      msg.setSubject("Sandeep Hooda IOT project");
	      msg.setText(text);
	      Transport.send(msg);
	    } catch (AddressException e) {
	    	log.info("email failure "+e.getMessage());
	      } catch (MessagingException e) {
	    	  log.info("email failure "+e.getMessage());
	      } 
	}
	
}
