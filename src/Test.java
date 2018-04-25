

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.geronimo.mail.util.Base64Encoder;

import com.communication.email.EmailAddess;
import com.communication.email.EmailVO;
import com.google.appengine.api.urlfetch.FetchOptions;
import com.google.appengine.api.urlfetch.HTTPHeader;
import com.google.appengine.api.urlfetch.HTTPMethod;
import com.google.appengine.api.urlfetch.HTTPRequest;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


/**
 * Servlet implementation class Test
 */
public class Test extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private static FetchOptions lFetchOptions = FetchOptions.Builder.doNotValidateCertificate().setDeadline(300d);
	private static URLFetchService fetcher = URLFetchServiceFactory.getURLFetchService();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Test() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		InputStream is = request.getInputStream(); 
		ByteArrayOutputStream baos = new ByteArrayOutputStream(); 

		byte buf[] = new byte[1024]; 
		int letti; 

		while ((letti = is.read(buf)) > 0) 
		baos.write(buf, 0, letti); 
		baos.flush();
        System.out.println(" bytes read ="+baos.toByteArray().length);
        
        byte[] image = baos.toByteArray();
        String data = new String(image);
        System.out.println(data.indexOf("Connection: close"));
        
        FileOutputStream fos = new FileOutputStream("C:\\fas.jpg");
        fos.write(image);
        fos.flush();
        fos.close();
		
		/*String httpsURL  = "https://post-master.herokuapp.com/SendEmail";
		
		 try {
			 System.setProperty("http.proxyHost", "127.0.0.1");
				System.setProperty("https.proxyHost", "127.0.0.1");
				System.setProperty("http.proxyPort", "8888");
				System.setProperty("https.proxyPort", "8888");
			
		        URL url = new URL(httpsURL);
		        
		        HTTPRequest req = new HTTPRequest(url, HTTPMethod.POST, lFetchOptions);
	            HTTPHeader header = new HTTPHeader("Content-type", "application/json");
	            req.setHeader(header);
	           
	            header = new HTTPHeader("Accept", "application/json");
	            req.setHeader(header);
	           
	         
	            Gson  json = new Gson();
	            EmailAddess toAddress = new EmailAddess();
	            toAddress.setAddress("foscamnotificationsandeep@gmail.com");
	            EmailVO emailVO = prepareEmailVO(toAddress, "Customer order", "Please work on this order <a href=\"\">Order details</a>", "c2FuZGVlcCBob29kYSBhdHRhY2htZW50", "Order.html");
	            String data = json.toJson(emailVO, new TypeToken<EmailVO>() {}.getType());
	            req.setPayload(data.getBytes());
	            com.google.appengine.api.urlfetch.HTTPResponse res = fetcher.fetch(req);
	            if (res.getResponseCode() == 200 && "Done".equals(new String(res.getContent()))){
	            	System.out.println(" Done");
	            }else {
	            	System.out.println(" no Email");
	            }
	            
	            
	        } catch (Exception e) {
	        	e.printStackTrace();
	        	
	        }*/
		
		/*String httpsURL  = "http://192.168.1.6:8090/snapshot.cgi";
		
		 try {
			 System.setProperty("http.proxyHost", "127.0.0.1");
				System.setProperty("https.proxyHost", "127.0.0.1");
				System.setProperty("http.proxyPort", "8888");
				System.setProperty("https.proxyPort", "8888");
			
		        URL url = new URL(httpsURL);
		        
		        HTTPRequest req = new HTTPRequest(url, HTTPMethod.GET, lFetchOptions);
	            HTTPHeader header = new HTTPHeader("Authorization", "Digest username=\"sandeephooda\", realm=\"ipcamera_00626E473AAE\", nonce=\"79f17c7a2caabb6a7ca1a6910225c791\", uri=\"/snapshot.cgi\", algorithm=MD5, response=\"0a442fe97d3bbe01fff12bb2518a113b\", qop=auth, nc=00000003, cnonce=\"3d18f59d8b24e0b8\"");
	            req.setHeader(header);
	           
	        
	           
	         
	           
	            com.google.appengine.api.urlfetch.HTTPResponse res = fetcher.fetch(req);
	            if (res.getResponseCode() == 200 ){
	            	ByteArrayOutputStream bos = new ByteArrayOutputStream();
	            	byte[] image = res.getContent();
	            	bos.write(image, 0, image.length);
	            	System.out.println(image.length);
	            	ByteArrayOutputStream out = new ByteArrayOutputStream();
	             new Base64Encoder().encode(image,0,image.length,out);
	             System.out.println(new String(out.toByteArray()));
	            	response.setContentType("image/jpeg");
	            	response.getOutputStream().write(image);
	            	
	            }else {
	            	System.out.println(" no Email");
	            }
	            
	            
	        } catch (Exception e) {
	        	e.printStackTrace();
	        	
	        }*/
		
		
	}
	
	private static EmailVO prepareEmailVO(   EmailAddess toAddress, String subject , String htmlBody, String base64attachment, String attachmentName ) {
		EmailVO emailVO = new EmailVO();
		
		emailVO.setUserName( "foscamnotificationsandeep@gmail.com");
		emailVO.setPassword( "ebgrtafzzzoaweft");
		EmailAddess fromAddress = new EmailAddess();
		fromAddress.setAddress(emailVO.getUserName());
		fromAddress.setLabel("Gatekeeper");
		emailVO.setFromAddress( fromAddress);
		
		
		List<EmailAddess> toAddressList = new ArrayList<EmailAddess>();
		
		toAddressList.add(toAddress);
		emailVO.setToAddress(toAddressList);
		emailVO.setSubject(subject);
		emailVO.setHtmlContent(htmlBody);
		emailVO.setBase64Attachment(base64attachment);
		emailVO.setAttachmentName(attachmentName);
		return emailVO;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doGet(request, response);
	}

}
