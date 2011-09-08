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
	
	private void postOnTwitter(UserPO tempUser,String returned){
		  //send new tweet
	    String accessToken = tempUser.getTwitterAccessToken();
	    if(accessToken!=null){
		    String secretToken = tempUser.getTwitterSecretToken();
			TwitterManager t = new TwitterManager(new AccessToken(accessToken, secretToken), "GDwPipm8wdr40M6RHVcPA", "pduDWo2CbhpqJlRIcNX9PEG7F1AOqR8uo5A7yNt5Lo");
			t.sendTweet("Nuovo materiale disponibile al link: " + returned);
	    }
	}
	
	
	private String uploadRequest(String content,UserPO tempUser, ArrayList<String> stringList) throws MalformedURLException, IOException{
		String msgBody="",returned;
		
		returned=parseBody(content);
		if(!returned.contains("Errore")&&!returned.contains("Corso non presente")){
		msgBody+=this.pageContent+this.pageName;
		SiteModifier siteModifier=new SiteModifier(tempUser.getGoogleAccessToken(),tempUser.getSiteName());
	    returned=siteModifier.createPage(this.pageName,this.pageContent,this.course,stringList);
	    if(returned.contains("expired")){
			GoogleAccessProtectedResource access=new GoogleAccessProtectedResource(tempUser.getGoogleAccessToken(),TRANSPORT,JSON_FACTORY,CLIENT_ID, CLIENT_SECRET,tempUser.getGoogleRefreshToken());
			access.refreshToken();
			String newAccessToken=access.getAccessToken();
			LoadStore.updateAccessToken(tempUser.getUser().getEmail(), newAccessToken);
			//tempUser.setGoogleAccessToken(newAccessToken);
			siteModifier=new SiteModifier(newAccessToken,tempUser.getSiteName());
		    returned=siteModifier.createPage(this.pageName,this.pageContent,this.course,stringList);
		    msgBody+="Ritornato: "+returned;
	    }
	    postOnTwitter(tempUser,returned);
		return msgBody+="Ritornato: "+returned;
		}else{
			if(returned.contains("Corso non presente")){
				return "Errore: "+returned;
			}
			return returned;
		}
	}
	
	
	private String postRequest(String content,UserPO tempUser) throws MalformedURLException, IOException{
		String msgBody="",returned; 
		msgBody+=this.pageContent+this.pageName;
		returned=parseBody(content);
		if(!returned.contains("Errore")&&!returned.contains("Corso non presente")){
		SiteModifier siteModifier=new SiteModifier(tempUser.getGoogleAccessToken(),tempUser.getSiteName());
	    returned=siteModifier.createPage(this.pageName,this.pageContent,this.course,null);
	    if(returned.contains("expired")){
			GoogleAccessProtectedResource access=new GoogleAccessProtectedResource(tempUser.getGoogleAccessToken(),TRANSPORT,JSON_FACTORY,CLIENT_ID, CLIENT_SECRET,tempUser.getGoogleRefreshToken());
			access.refreshToken();
			String newAccessToken=access.getAccessToken();
			LoadStore.updateAccessToken(tempUser.getUser().getEmail(), newAccessToken);
			siteModifier=new SiteModifier(newAccessToken,tempUser.getSiteName());
		    returned=siteModifier.createPage(this.pageName,this.pageContent,this.course,null);
	    }

	    postOnTwitter(tempUser,returned);
		return "ok";
		}else{
			return returned;
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
        	SiteModifier siteModifier;
        	ArrayList<String> stringList=new ArrayList<String>();
        	ArrayList<Part> partList=new ArrayList<Part>();
			MimeMessage message = new MimeMessage(session, req.getInputStream());
			String content="",subject,sender=message.getFrom()[0].toString(),msgBody = "",realSender;
        	Object o=message.getContent();
        	subject=message.getSubject();
            Message msg = new MimeMessage(session);
            InputStream input=null;
            int size=0,count;
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
	        	if(subject.contains("Upload")){
	        		msgBody=msgBody+"Richiesta di Upload. \n";
		        		if(tempUser==null){
		        			msgBody+="Errore: User non registrato.\n";
		        		}
		        		else{
		        			if(partList.size()!=0){
		        				  returned=uploadRequest(content,tempUser,stringList);
		        				  if(!returned.contains("Errore")){
		        					  for(count=0;count<partList.size();count++){
			        					  msgBody+=returned+"\n";
			        					  returned=uploadFile(partList.get(count),tempUser);
		        					  }
		        				  }

	        					  msgBody+=returned;
		        			  }
		        			  else{
		        				  msgBody+="Errore: richiesta di \"Upload\" senza file.\n";
		        			  }
		        		}
	        			/*Azioni per il caricamento su Google Site di un post di comunicazione*/
		        		
	        	}
	        	else{
	        		if(subject.contains("Post")){
	        			msgBody=msgBody+"Richiesta di Post.\n";
		        		if(tempUser==null){
		        			msgBody+="Errore: User non registrato.\n";
		        		}
		        		else{
		        			returned=postRequest(content,tempUser);
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
	        			siteModifier=new SiteModifier(tempUser.getGoogleAccessToken(),tempUser.getSiteName());
	        		    returned=siteModifier.createPage(this.pageName,this.pageContent,null,null);
	        		    if(returned.contains("expired")){
	        				GoogleAccessProtectedResource access=new GoogleAccessProtectedResource(tempUser.getGoogleAccessToken(),TRANSPORT,JSON_FACTORY,CLIENT_ID, CLIENT_SECRET,tempUser.getGoogleRefreshToken());
	        				access.refreshToken();
	        				String newAccessToken=access.getAccessToken();
	        				LoadStore.updateAccessToken(tempUser.getUser().getEmail(), newAccessToken);
		        			siteModifier=new SiteModifier(newAccessToken,tempUser.getSiteName());
		        		    returned=siteModifier.createPage(this.pageName,this.pageContent,null,null);
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