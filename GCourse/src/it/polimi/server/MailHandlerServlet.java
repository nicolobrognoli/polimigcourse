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

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException; 
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
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
	
	private String parseBody(String body){
		int title,content;
		title=body.indexOf("Titolo: ");
		content=body.indexOf("Contenuto: ");
		if(title<content){
			this.pageName=body.substring(title+8, content);
			this.pageContent=body.substring(content+11,body.length());
		}else{
			this.pageContent=body.substring(content+11, title);
			this.pageName=body.substring(title+8,body.length());
		}
		return "cis";
	}
	
	private String uploadFile(Part part,UserPO tempUser) throws IOException, MessagingException{
		  InputStream input=part.getInputStream();
		  int size=part.getSize();
		  String siteName,fileName,fileType;
		  URL endpoint=new URL("https://sites.google.com/feeds/content/site/"+tempUser.getSiteName());
		  HttpURLConnection  urlc =(HttpURLConnection) endpoint.openConnection();
		  urlc.setDoOutput(true);
		  urlc.setDoInput(true);
		  urlc.setRequestProperty("Host","sites.google.com");
		  urlc.setRequestProperty("GData-Version","1.4");
		  urlc.setRequestProperty("Authorization","AuthSub token=\""+tempUser.getGoogleAccessToken()+"\"");
		  urlc.setRequestProperty("Content-Type","multipart/related;boundary=END_OF_PART");
		  OutputStream out = urlc.getOutputStream();
		  DataOutputStream writer = new DataOutputStream(out);
		  siteName=tempUser.getSiteName();
		  fileName=part.getFileName();
		  fileType=part.getContentType();
			  writer.writeBytes("\r\n--END_OF_PART\r\nContent-Type: application/atom+xml\r\n\r\n");
			  writer.writeBytes("<entry xmlns=\"http://www.w3.org/2005/Atom\">"+
" <category scheme=\"http://schemas.google.com/g/2005#kind\" "+
"term=\"http://schemas.google.com/sites/2008#attachment\" label=\"attachment\" />"+
"<link rel=\"http://schemas.google.com/sites/2008#parent\" "+
"href=\"https://sites.google.com/feeds/content/site/"+siteName+"\" />"+
"<title>"+fileName+"</title>"+
"<summary>HR packet</summary>"+
"</entry>"+
"\r\n\r\n--END_OF_PART\r\n");
		  writer.writeBytes("Content-Type: "+fileType+"\r\n\r\n") ;int j;
		  for(j=0;j<size;j++){
			 writer.write(input.read());
		  }
		  writer.writeBytes("\r\n--END_OF_PART--\r\n");
		  input.close();
		  writer.flush();
		  writer.close();
          StringBuffer answer = new StringBuffer();
          BufferedReader reader = new BufferedReader(new InputStreamReader(urlc.getInputStream()));
          String line;
          while ((line = reader.readLine()) != null) {
              answer.append(line);
          }
          reader.close();
		  urlc.disconnect();
		return answer.toString();
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
        	String returned;
			MimeMessage message = new MimeMessage(session, req.getInputStream());
			String content="",subject,sender=message.getFrom()[0].toString(),msgBody = "Messaggio ricevuto.",realSender;
        	Object o=message.getContent();
        	subject=message.getSubject();
            Message msg = new MimeMessage(session);
            InputStream input=null;
            int size=0;
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
	            	  msgBody=msgBody+"Parte "+i+": "+part.getFileName()+"Tipo: "+part.getContentType();
	            	  if(part.getFileName()!=null){
	            		  returned=uploadFile(part,tempUser);
	            		  msgBody+=returned;
	            		  if(returned.contains("Token invalid")){
	            			  	msgBody+="Aggiornato token e fatto upload";
		        				GoogleAccessProtectedResource access=new GoogleAccessProtectedResource(tempUser.getGoogleAccessToken(),TRANSPORT,JSON_FACTORY,CLIENT_ID, CLIENT_SECRET,tempUser.getGoogleRefreshToken());
		        				access.refreshToken();
		        				String newAccessToken=access.getAccessToken();
		        				LoadStore.updateAccessToken(tempUser.getUser().getEmail(), newAccessToken);
		        				returned=uploadFile(part,tempUser);
		        				if(returned.contains("Token invalid")){
		            			  	msgBody+="Aggiornato token e fatto upload";
		        				}
	            		  }
	            		  msgBody+="Size: "+size;
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
		        		if(tempUser==null){
		        			msgBody+="errore";
		        		}
		        		else{
		        			parseBody(content);
		        			msgBody+=this.pageContent+this.pageName;
		        			SiteModifier siteModifier=new SiteModifier(tempUser.getGoogleAccessToken(),tempUser.getSiteName());
		        		    returned=siteModifier.createPage(this.pageName,this.pageContent,this.course);
		        		    if(returned.contains("expired")){
		        				GoogleAccessProtectedResource access=new GoogleAccessProtectedResource(tempUser.getGoogleAccessToken(),TRANSPORT,JSON_FACTORY,CLIENT_ID, CLIENT_SECRET,tempUser.getGoogleRefreshToken());
		        				access.refreshToken();
		        				String newAccessToken=access.getAccessToken();
		        				LoadStore.updateAccessToken(tempUser.getUser().getEmail(), newAccessToken);
			        			siteModifier=new SiteModifier(newAccessToken,tempUser.getSiteName());
			        		    returned=siteModifier.createPage(this.pageName,this.pageContent,this.course);
			        		    msgBody+="Ritornato: "+returned;
		        		    }
		        		  //send new tweet
		        		    String accessToken = tempUser.getTwitterAccessToken();
		        		    String secretToken = tempUser.getTwitterSecretToken();
		        			TwitterManager t = new TwitterManager(new AccessToken(accessToken, secretToken), "GDwPipm8wdr40M6RHVcPA", "pduDWo2CbhpqJlRIcNX9PEG7F1AOqR8uo5A7yNt5Lo");
		        			t.sendTweet("Nuovo materiale disponibile al link: " + returned);
		        			msgBody+="Ritornato: "+returned;
		        		}
	        			/*Azioni per il caricamento su Google Site di un post di comunicazione*/

	        		
	        		
	        		
	        		
	        		
	        		

	        	}
	        	else{
	        		msgBody=msgBody+"upload no";
	        		if(subject.contains("Post")){
	        			msgBody=msgBody+"Post yes";
		        		if(tempUser==null){
		        			msgBody+="errore";
		        		}
		        		else{
		        			parseBody(content);
		        			msgBody+=this.pageContent+this.pageName;
		        			SiteModifier siteModifier=new SiteModifier(tempUser.getGoogleAccessToken(),tempUser.getSiteName());
		        		    returned=siteModifier.createPage(this.pageName,this.pageContent,this.course);
		        		    if(returned.contains("expired")){
		        				GoogleAccessProtectedResource access=new GoogleAccessProtectedResource(tempUser.getGoogleAccessToken(),TRANSPORT,JSON_FACTORY,CLIENT_ID, CLIENT_SECRET,tempUser.getGoogleRefreshToken());
		        				access.refreshToken();
		        				String newAccessToken=access.getAccessToken();
		        				LoadStore.updateAccessToken(tempUser.getUser().getEmail(), newAccessToken);
			        			siteModifier=new SiteModifier(newAccessToken,tempUser.getSiteName());
			        		    returned=siteModifier.createPage(this.pageName,this.pageContent,this.course);
		        		    }
		        		    //send new tweet
		        		    String accessToken = tempUser.getTwitterAccessToken();
		        		    String secretToken = tempUser.getTwitterSecretToken();
		        			TwitterManager t = new TwitterManager(new AccessToken(accessToken, secretToken), "GDwPipm8wdr40M6RHVcPA", "pduDWo2CbhpqJlRIcNX9PEG7F1AOqR8uo5A7yNt5Lo");
		        			t.sendTweet("Nuovo post disponibile al link: " + returned);
		        		}
	        			/*Azioni per il caricamento su Google Site di un post di comunicazione*/
	        		}else if(subject.contains("Course")){
	        			parseBody(content);
	        			msgBody+=this.pageContent+this.pageName;
	        			SiteModifier siteModifier=new SiteModifier(tempUser.getGoogleAccessToken(),tempUser.getSiteName());
	        		    returned=siteModifier.createPage(this.pageName,this.pageContent,this.course);
	        		    if(returned.contains("expired")){
	        				GoogleAccessProtectedResource access=new GoogleAccessProtectedResource(tempUser.getGoogleAccessToken(),TRANSPORT,JSON_FACTORY,CLIENT_ID, CLIENT_SECRET,tempUser.getGoogleRefreshToken());
	        				access.refreshToken();
	        				String newAccessToken=access.getAccessToken();
	        				LoadStore.updateAccessToken(tempUser.getUser().getEmail(), newAccessToken);
		        			siteModifier=new SiteModifier(newAccessToken,tempUser.getSiteName());
		        		    returned=siteModifier.createPage(this.pageName,this.pageContent,null);
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