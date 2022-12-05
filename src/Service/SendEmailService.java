package Service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import Model.Response;

public class SendEmailService {
	private String from;
	private String to;
	private String password;
	private String host;
	
	private HashMap<String, String> dict;
	
	public SendEmailService(String to)
	{
		this.from = "nccudelicacy@gmail.com";
		this.password = "pppwnjjkenmrfldq"; //aplication password
		this.to = to;
		this.host = "smtp.gmail.com";
		
		this.dict = new HashMap<String, String>();
	}
	
	public Response sendVerificationEmail()
	{
		//verification code save in dict
		String code = this.generateRandomVerificationCode();
		this.dict.put(this.to, code);
		
		// Get system properties
	    Properties props = System.getProperties();

	    // Setup mail server properties
	    props.put("mail.smtp.host", host);
	    props.put("mail.smtp.port", "587");
	    props.put("mail.smtp.starttls.enable", "true");
	    props.put("mail.smtp.auth", "true");
	    props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
	    props.put("mail.smtp.socketFactory.port", "587"); 
	    props.put("mail.smtp.starttls.enable", "true");
	    props.setProperty("mail.smtp.ssl.protocols", "TLSv1.2");
	    //props.put("mail.debug", "true");
	    
	    // Get the default Session object.
	    Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
	        protected PasswordAuthentication getPasswordAuthentication() {  
	        	  return new PasswordAuthentication(from, password);  
	        }  
	    });
	      
	    //session.setDebug(true);

	    try{
	       // Create a default MimeMessage object.
	       MimeMessage message = new MimeMessage(session);

	       // Set From: header field of the header.
	       message.setFrom(new InternetAddress(from));

	       // Set To: header field of the header.
	       message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

	       // Set Subject: header field
	       message.setSubject("<<政大吃貨驗證信>>");

	       // Now set the actual message
	       message.setText("您的驗證碼： "+code);

	       // Send message
	       Transport.send(message);	         
	       System.out.println("Sent message successfully....");
	       return new Response(true, "Send Email Succesfully!");
	    }catch (MessagingException mex) {
	       mex.printStackTrace();
	       return new Response(false, "Email Error Occured!");
	    }
	}
	
	public Response verify(String email, String code)
	{
		if(!this.dict.containsKey(email)) {
			return new Response(false, "This email haven't register yet!");
		}else
		{
			if(!dict.get(email).equals(code)) {
				return new Response(false, "Verification code is not correct!");
			}
		}
		return new Response(true, "Verified successfully!");
	}
	
	private String generateRandomVerificationCode()
	{
		//0<= <1 -> 0<= <9000 -> 1000<= <100000 -> 1000~9999
		int code = (int)(Math.random()*9000+1000);
		return String.valueOf(code);
	}
}
