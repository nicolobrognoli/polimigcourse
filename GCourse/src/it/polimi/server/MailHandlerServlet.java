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

import java.io.IOException; 
import java.io.InputStream;
import java.util.ArrayList;
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




public class MailHandlerServlet extends HttpServlet { 
    /**
	 * 
	 */
	private static final String CLIENT_ID = "267706380696.apps.googleusercontent.com";
	private static final String CLIENT_SECRET = "zBWLvQsYnEF4-AAg1PZYu7eA";
	private static final HttpTransport TRANSPORT = new NetHttpTransport();
	private static final JsonFactory JSON_FACTORY = new JacksonFactory();
	private String pageName,pageContent,course=null;
	private SiteModifier siteModifier;
	
	private String parseBody(String body){
		int titleStart,titleEnd,contentStart,contentEnd,courseStart,courseEnd;
		titleStart=body.indexOf("Titolo");
		titleEnd=body.indexOf("/Titolo");
		contentStart=body.indexOf("Contenuto");
		contentEnd=body.indexOf("/Contenuto");
		courseStart=body.indexOf("Corso");
		courseEnd=body.indexOf("/Corso");
		if((titleStart>-1)&&(titleEnd>-1)){
			this.pageName=body.substring(titleStart+6, titleEnd);
			if((contentStart>-1)&&(contentEnd>-1)){
				this.pageContent=body.substring(contentStart+9, contentEnd);
				if((courseStart>-1)&&(courseEnd>-1)){
					this.course=body.substring(courseStart+5, courseEnd);
				}
				else{
					return "Corso non presente";
				}
			}
			else{
				return "Errore: elemnto \"Contenuto\" non presente";
			}
		}else{
			return "Errore: elemnto \"Titolo\" non presente";
		}
		return "ok";
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
	
	private void postOnTwitter(UserPO tempUser,String returned){
		  //send new tweet
	    String accessToken = tempUser.getTwitterAccessToken();
	    if(accessToken!=null){
		    String secretToken = tempUser.getTwitterSecretToken();
			TwitterManager t = new TwitterManager(new AccessToken(accessToken, secretToken), "GDwPipm8wdr40M6RHVcPA", "pduDWo2CbhpqJlRIcNX9PEG7F1AOqR8uo5A7yNt5Lo");
			t.sendTweet("Nuovo materiale disponibile al link: " + returned);
	    }
	}
	
	private static final long serialVersionUID = 1L;

	public void doPost(HttpServletRequest req, 
                       HttpServletResponse resp) 
            throws IOException { 
        Properties props = new Properties(); 
        Session session = Session.getDefaultInstance(props, null); 

        try {
        	String returned;
        	ArrayList<String> stringList=new ArrayList<String>();
        	ArrayList<Part> partList=new ArrayList<Part>();
			MimeMessage message = new MimeMessage(session, req.getInputStream());
			String content="",subject,sender=message.getFrom()[0].toString(),msgBody = "",realSender;
        	Object o=message.getContent();
        	subject=message.getSubject();
            Message msg = new MimeMessage(session);
            int count;
            realSender=this.getRealUser(sender);
            msg.setFrom(new InternetAddress("reply@polimigcourse.appspotmail.com", "PolimiGCourse"));
            System.out.println(realSender);
            msg.addRecipient(Message.RecipientType.TO,message.getFrom()[0]);
            msg.setSubject("Your Example.com account has been activated");
            MimeMultipart multipart=(MimeMultipart)o;
    		UserPO tempUser=LoadStore.loadUser(realSender);
	        try {
	        	for (int i = 0; i < multipart.getCount(); i++) {
	            	  Part part =  multipart.getBodyPart(i);
	            	  System.out.println(part.getFileName());
	            	  //msgBody=msgBody+"Parte "+i+": "+part.getFileName()+"Tipo: "+part.getContentType();
	            	  if(part.getFileName()!=null){
	            		  partList.add(part);
	            		  stringList.add(part.getFileName());
	            	  }else{
	            		  if(part.isMimeType("text/plain")||part.isMimeType("text/html")){
	            			  content=(String)part.getContent();
	            			  //msgBody=msgBody+"Body: "+content;
	            		 }
	            		  else{
	            			  Multipart multipart2=(Multipart)part.getContent();
	            			  for(int j=0;j<multipart2.getCount();j++){
	            				  Part part2=multipart2.getBodyPart(j);
	            				 // msgBody=msgBody+"Tipo: "+part2.getContentType()+"Contenuto: "+part2.getContent();
	    	            		  if(part2.isMimeType("text/plain")||part2.isMimeType("text/html")){
	    	            			  content=(String)part2.getContent();
	    	            			  //msgBody=msgBody+"Body: "+content;
	    	            		 }
	            			  }
	            		  }
	            	  }
	            }
        		if(tempUser==null){
        			msgBody+="Errore: User non registrato.\n";
	        		throw new MessagingException();
        		}
	        	returned=parseBody(content);
	        	if(returned.contains("Errore")||returned.contains("Corso non presente")){
	        		throw new MessagingException();
	        	}
    			this.siteModifier=new SiteModifier(tempUser.getGoogleAccessToken(),tempUser.getSiteName());
	        	if(subject.contains("Upload")){
	        		msgBody=msgBody+"Richiesta di Upload. \n";

		        			if(partList.size()!=0){
		        				  returned=this.siteModifier.uploadRequest(this.course,this.pageContent,this.pageName,tempUser,stringList);
		        				  if(!returned.contains("Errore")){
		        					  for(count=0;count<partList.size();count++){
			        					  msgBody+=returned+"\n";
			        					  returned=this.siteModifier.uploadFile(partList.get(count),tempUser);
		        					  }
		        				  }

	        					  msgBody+=returned;
		        			  }
		        			  else{
		        				  msgBody+="Errore: richiesta di \"Upload\" senza file.\n";
		        			  }
		        		
	        	}
	        	else{
	        		if(subject.contains("Post")){
	        			msgBody=msgBody+"Richiesta di Post.\n";
		        		if(tempUser==null){
		        			msgBody+="Errore: User non registrato.\n";
		        		}
		        		else{
		        			returned=this.siteModifier.postRequest(this.course,this.pageContent,this.pageName,tempUser);
		        			if(!returned.contains("Errore")){
		        				msgBody+=returned+"\n";
		        			}else{
		        				msgBody+=returned+"\n";
		        			}
		        		}
	        			/*Azioni per il caricamento su Google Site di un post di comunicazione*/
	        		}else if(subject.contains("Course")){
	        			parseBody(content);
	        			msgBody+=this.pageContent+this.pageName;
	        		    returned=this.siteModifier.createPage(this.pageName,this.pageContent,null,null);
	        		    if(returned.contains("expired")){
	        				GoogleAccessProtectedResource access=new GoogleAccessProtectedResource(tempUser.getGoogleAccessToken(),TRANSPORT,JSON_FACTORY,CLIENT_ID, CLIENT_SECRET,tempUser.getGoogleRefreshToken());
	        				access.refreshToken();
	        				String newAccessToken=access.getAccessToken();
	        				LoadStore.updateAccessToken(tempUser.getUser().getEmail(), newAccessToken);
		        			this.siteModifier=new SiteModifier(newAccessToken,tempUser.getSiteName());
		        		    returned=this.siteModifier.createPage(this.pageName,this.pageContent,null,null);
	        		    }
	        			LoadStore.storeNewCourse(tempUser,this.pageName, this.pageContent);
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