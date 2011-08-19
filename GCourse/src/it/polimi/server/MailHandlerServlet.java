package it.polimi.server;
/*
import java.io.IOException;
import javax.servlet.http.*;

@SuppressWarnings("serial")
public class MailHandlerServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/plain");
		resp.getWriter().println("Hello, world");
	}
}
*/

import it.polimi.server.data.UserPO;
import it.polimi.server.utils.LoadStore;
import it.polimi.server.utils.SiteModifier;
import it.polimi.server.utils.TwitterManager;

import java.io.File;
import java.io.IOException; 
import java.io.InputStream;
import java.util.Properties; 

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session; 
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage; 
import javax.mail.internet.MimeMultipart;
import javax.servlet.http.*; 

import twitter4j.auth.AccessToken;

import com.google.api.client.googleapis.auth.oauth2.draft10.GoogleAccessProtectedResource;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.gdata.data.sites.BaseContentEntry;
import com.google.gdata.util.ServiceException;




public class MailHandlerServlet extends HttpServlet { 
    /**
	 * 
	 */
	private static final String CLIENT_ID = "267706380696.apps.googleusercontent.com";
	private static final String CLIENT_SECRET = "zBWLvQsYnEF4-AAg1PZYu7eA";
	private static final HttpTransport TRANSPORT = new NetHttpTransport();
	private static final JsonFactory JSON_FACTORY = new JacksonFactory();
	private String siteName,siteContent;
	
	private String parseBody(String body){
		int title,content;
		title=body.indexOf("Titolo: ");
		content=body.indexOf("Contenuto: ");
		if(title<content){
			this.siteName=body.substring(title+8, content);
			this.siteContent=body.substring(content+11,body.length());
		}else{
			this.siteContent=body.substring(content+11, title);
			this.siteName=body.substring(title+8,body.length());
		}
		return "cis";
	}
	private String getRealUser(String sender){
		String realUser;
		int i,start,end;
		for(end=start=i=0;i<sender.length();i++){
			if(sender.charAt(i)=='<'){
				start=i;
			}
			else if(sender.charAt(i)=='>'){
				end=i;
				i=sender.length();
			}
			
		}
		realUser=sender.substring(start+1,end);
		return realUser;
	}
	private static final long serialVersionUID = 1L;

	public void doPost(HttpServletRequest req, 
                       HttpServletResponse resp) 
            throws IOException { 
        Properties props = new Properties(); 
        Session session = Session.getDefaultInstance(props, null); 

        try {
			MimeMessage message = new MimeMessage(session, req.getInputStream());
			String content="",subject,sender=message.getFrom()[0].toString(),msgBody = "Messaggio ricevuto.",realSender;
        	Object o=message.getContent(),file=null;
        	subject=message.getSubject();
            Message msg = new MimeMessage(session);
            realSender=this.getRealUser(sender);
            msg.setFrom(new InternetAddress("reply@polimigcourse.appspotmail.com", "PolimiGCourse"));
            System.out.println(realSender);
            msg.addRecipient(Message.RecipientType.TO,message.getFrom()[0]);
            msg.setSubject("Your Example.com account has been activated");
            MimeMultipart multipart=(MimeMultipart)o;
	        try {
	        	for (int i = 0; i < multipart.getCount(); i++) {
	            	  Part part =  multipart.getBodyPart(i);
	            	  System.out.println(part.getFileName());
	            	  msgBody=msgBody+"Parte "+i+": "+part.getFileName()+"Tipo: "+part.getContentType();
	            	  if(part.getFileName()!=null){
            			  file=part.getContent();
            			  
            			  msgBody+="nome del file: "+part.getFileName()+" "+((File)file).getName();
	            		  InputStream input=part.getInputStream();
	            		  int j,size;
	            		  size=part.getSize();
	            		  for(j=0;j<size;j++){
	            			 input.read();
	            		  }
	            	  }else{
	            		  if(part.isMimeType("text/plain")||part.isMimeType("text/html")){
	            			  content=(String)part.getContent();
	            			  msgBody=msgBody+"Body: "+content;
	            		 }
	            		  else{
	            			  Multipart multipart2=(Multipart)part.getContent();
	            			  for(int j=0;j<multipart2.getCount();j++){
	            				  Part part2=multipart2.getBodyPart(j);
	            				  msgBody=msgBody+"Tipo: "+part2.getContentType()+"Contenuto: "+part2.getContent();
	    	            		  if(part2.isMimeType("text/plain")||part2.isMimeType("text/html")){
	    	            			  content=(String)part2.getContent();
	    	            			  msgBody=msgBody+"Body: "+content;
	    	            		 }
	            			  }
	            		  }
	            	  }
	            }
	        	if(subject.contains("Upload")){
	        		msgBody=msgBody+"upload yes";
		        		UserPO tempUser=LoadStore.verifyUser(realSender);
		        		if(tempUser==null){
		        			msgBody+="errore";
		        		}
		        		else{
		        			parseBody(content);
		        			msgBody+=this.siteContent+this.siteName;
		        			SiteModifier siteModifier=new SiteModifier(tempUser.getGoogleAccessToken(),/*tempUser.getSiteName()*/"provamiagcourse");
		        		    String returned=siteModifier.createPage(this.siteName,this.siteContent,file);
		        		    if(returned.contains("expired")){
		        				GoogleAccessProtectedResource access=new GoogleAccessProtectedResource(tempUser.getGoogleAccessToken(),TRANSPORT,JSON_FACTORY,CLIENT_ID, CLIENT_SECRET,tempUser.getGoogleRefreshToken());
		        				access.refreshToken();
		        				String newAccessToken=access.getAccessToken();
		        				LoadStore.updateAccessToken(tempUser.getUser().getEmail(), newAccessToken);
			        			siteModifier=new SiteModifier(newAccessToken,/*tempUser.getSiteName()*/"provamiagcourse");
			        		    returned=siteModifier.createPage(this.siteName,this.siteContent,file);
		        		    }
		        		  //send new tweet
		        		    String accessToken = tempUser.getTwitterAccessToken();
		        		    String secretToken = tempUser.getTwitterSecretToken();
		        			TwitterManager t = new TwitterManager(new AccessToken(accessToken, secretToken), "GDwPipm8wdr40M6RHVcPA", "pduDWo2CbhpqJlRIcNX9PEG7F1AOqR8uo5A7yNt5Lo");
		        			t.sendTweet("Nuovo materiale disponibile al link: " + returned);
		        		}
	        			/*Azioni per il caricamento su Google Site di un post di comunicazione*/

	        		
	        		
	        		
	        		
	        		
	        		

	        	}
	        	else{
	        		msgBody=msgBody+"upload no";
	        		if(subject.contains("Post")){
	        			msgBody=msgBody+"Post yes";
		        		UserPO tempUser=LoadStore.verifyUser(realSender);
		        		if(tempUser==null){
		        			msgBody+="errore";
		        		}
		        		else{
		        			parseBody(content);
		        			msgBody+=this.siteContent+this.siteName;
		        			SiteModifier siteModifier=new SiteModifier(tempUser.getGoogleAccessToken(),/*tempUser.getSiteName()*/"provamiagcourse");
		        		    String returned=siteModifier.createPage(this.siteName,this.siteContent);
		        		    if(returned.contains("expired")){
		        				GoogleAccessProtectedResource access=new GoogleAccessProtectedResource(tempUser.getGoogleAccessToken(),TRANSPORT,JSON_FACTORY,CLIENT_ID, CLIENT_SECRET,tempUser.getGoogleRefreshToken());
		        				access.refreshToken();
		        				String newAccessToken=access.getAccessToken();
		        				LoadStore.updateAccessToken(tempUser.getUser().getEmail(), newAccessToken);
			        			siteModifier=new SiteModifier(newAccessToken,/*tempUser.getSiteName()*/"provamiagcourse");
			        		    returned=siteModifier.createPage(this.siteName,this.siteContent);
		        		    }
		        		    //send new tweet
		        		    String accessToken = tempUser.getTwitterAccessToken();
		        		    String secretToken = tempUser.getTwitterSecretToken();
		        			TwitterManager t = new TwitterManager(new AccessToken(accessToken, secretToken), "GDwPipm8wdr40M6RHVcPA", "pduDWo2CbhpqJlRIcNX9PEG7F1AOqR8uo5A7yNt5Lo");
		        			t.sendTweet("Nuovo post disponibile al link: " + returned);
		        		}
	        			/*Azioni per il caricamento su Google Site di un post di comunicazione*/
	        		}else{
	        			msgBody=msgBody+"Errore";
	        			/*Errore oppure un'altra azione*/
	        		}
	        	}
	            msg.setText(msgBody);

    	        } catch (AddressException e) {
    	            // ...
    	        } catch (MessagingException e) {
    	            // ...
    	        }
            Transport.send(msg);

		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}