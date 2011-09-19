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
import it.polimi.server.utils.CalendarHelper;
import it.polimi.server.utils.CourseManager;
import it.polimi.server.utils.LoadStore;
import it.polimi.server.utils.SiteModifier;
import it.polimi.server.utils.TwitterManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import twitter4j.auth.AccessToken;

import com.allen_sauer.gwt.log.client.Log;
import com.google.api.client.googleapis.auth.oauth2.draft10.GoogleAccessProtectedResource;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;




public class MailHandlerServlet extends HttpServlet { 
    
	private final String LECTURE = "lecture";
	private final String EXERCISE = "exercise";
	private static final String CLIENT_ID = "267706380696.apps.googleusercontent.com";
	private static final String CLIENT_SECRET = "zBWLvQsYnEF4-AAg1PZYu7eA";
	private static final HttpTransport TRANSPORT = new NetHttpTransport();
	private static final JsonFactory JSON_FACTORY = new JacksonFactory();
	private String pageName,pageContent,course=null, event, startTime, endTime, date;
	private SiteModifier siteModifier;
	private CourseManager courseManager;
	private CalendarHelper calendarHelper;
	
	private String parseBody(String body, String subject){
		int titleStart,titleEnd,contentStart,contentEnd,courseStart,courseEnd;
		if(!subject.contains("evento") && !subject.contains("Evento"))
		{		
			if(subject.contains("delete calendar") || subject.contains("Delete calendar") || subject.contains("Delete Calendar"))
			{
				//eliminazione calendario
				courseStart=body.indexOf("Corso");
				courseEnd=body.indexOf("/Corso");
				if((courseStart>-1)&&(courseEnd>-1))
				{
					this.course=body.substring(courseStart+5, courseEnd);
					this.course = this.course.trim();
				}
				else
				{
					return "Corso non presente";
				}
				return "ok";
			}
			else
			{ 
				//creazione pagine + creazione calendario
				titleStart=body.indexOf("Titolo");
				titleEnd=body.indexOf("/Titolo");
				contentStart=body.indexOf("Contenuto");
				contentEnd=body.indexOf("/Contenuto");
				courseStart=body.indexOf("Corso");
				courseEnd=body.indexOf("/Corso");
				if((titleStart>-1)&&(titleEnd>-1))
				{
					this.pageName=body.substring(titleStart+6, titleEnd);
					this.pageName = this.pageName.trim();
					if(subject.contains(this.EXERCISE) || subject.contains("Exercise"))
						this.pageName += "[Exercise]";
					if((contentStart>-1)&&(contentEnd>-1))
					{
						this.pageContent=body.substring(contentStart+9, contentEnd);
						this.pageContent = this.pageContent.trim();
						if((courseStart>-1)&&(courseEnd>-1))
						{
							this.course=body.substring(courseStart+5, courseEnd);
							this.course = this.course.trim();
						}
						else
						{
							return "Corso non presente";
						}
					}
					else{
						return "Errore: elemento \"Contenuto\" non presente";
					}
				}else{
					return "Errore: elemento \"Titolo\" non presente";
				}
				return "ok";
			}
			
		}
		else 
		{
			//creazione evento calendario
			int beginStart, beginEnd, finishStart, finishEnd, dateStart, dateEnd;

			titleStart=body.indexOf("Titolo");
			titleEnd=body.indexOf("/Titolo");
			contentStart=body.indexOf("Contenuto");
			contentEnd=body.indexOf("/Contenuto");
			courseStart = body.indexOf("Corso");
			courseEnd = body.indexOf("/Corso");
			dateStart = body.indexOf("Data");
			dateEnd = body.indexOf("/Data");
			beginStart = body.indexOf("Inizio");
			beginEnd = body.indexOf("/Inizio");
			finishStart = body.indexOf("Fine");
			finishEnd = body.indexOf("/Fine");
		
			//inserimento standard
			if((titleStart>-1)&&(titleEnd>-1))
			{
				this.pageName=body.substring(titleStart+6, titleEnd);
				this.pageName = this.pageName.trim();
			
				if((contentStart>-1)&&(contentEnd>-1))
				{
					this.pageContent=body.substring(contentStart+9, contentEnd);
					this.pageContent = this.pageContent.trim();
					if((courseStart>-1)&&(courseEnd>-1))
					{
						this.course=body.substring(courseStart+5, courseEnd);
						this.course = this.course.trim();
						if((beginStart>-1)&&(beginEnd>-1))
						{
							this.startTime = body.substring(beginStart + 6, beginEnd);
							this.startTime = this.startTime.trim();
							if((finishStart>-1)&&(finishEnd>-1))
							{
								this.endTime = body.substring(finishStart + 4, finishEnd);
								this.endTime = this.endTime.trim();
								if((dateStart>-1)&&(dateEnd>-1))
								{
									this.date = body.substring(dateStart + 4, dateEnd);
									this.date = this.date.trim();
								}
								else
									return "Errore: data non presente";
							}
							else 
								return "Errore: orario fine non presente";
						}
						else
							return "Errore: orario inizio non presente";
						
					}
					else
					{
						return "Corso non presente";
					}
				}
				else{
					return "Errore: elemento \"Contenuto\" non presente";
				}
			}else
			{
				return "Errore: elemento \"Titolo\" non presente";
			}
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
			if(returned.contains("Nuovo evento creato nel calendario"))
				t.sendTweet(returned);
			else
				t.sendTweet("Nuovo materiale disponibile al link: " + returned);
	    }
	}
	
	private static final long serialVersionUID = 1L;

	@Override
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
            msg.addRecipient(Message.RecipientType.TO,message.getFrom()[0]);
            msg.setSubject("Aggiornamento modifiche.");
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
		            msg.setText(msgBody);
		            Transport.send(msg);
	        		throw new MessagingException();
        		}else if(!tempUser.isProfessor()){
        			msgBody+="Errore: Sei uno studente.\n";
		            msg.setText(msgBody);
		            Transport.send(msg);
	        		throw new MessagingException();
        		}
	        	returned=parseBody(content, subject);
	        	if(returned.contains("Errore")){
	        		msgBody+=returned;
		            msg.setText(msgBody);
		            Transport.send(msg);
	        		throw new MessagingException();
	        	}
 
	        	this.courseManager = new CourseManager(this.course, tempUser);
    			this.siteModifier=new SiteModifier(tempUser.getGoogleAccessToken(),tempUser.getSiteName());
	        	//manage an upload request
    			if(subject.contains("Upload") || subject.contains("upload")){
	        		msgBody=msgBody+"Richiesta di Upload. \n";
	        		
        			if(partList.size()!=0){
        				  returned=this.siteModifier.uploadRequest(this.course,this.pageContent,this.pageName,tempUser,stringList);
        				  postOnTwitter(tempUser,returned);
        				  if(!returned.contains("Errore")){
        					  for(count=0;count<partList.size();count++){
	        					  returned=this.siteModifier.uploadFile(partList.get(count),tempUser);
        					  }
        				  }

        			  }
        			  else{
        				  msgBody+="Errore: richiesta di \"Upload\" senza file.\n";
        			  }
        			returned += "\nUtenti iscritti:\n";
        			List<UserPO> listStudents = LoadStore.getStudentsEnrolled(LoadStore.getCourseKey(this.course, tempUser.getUser().getEmail()));
        			Iterator<UserPO> iter = listStudents.iterator();
        			UserPO student;
        			do{
        				student = iter.next();
        				if(subject.contains(this.EXERCISE) || subject.contains("Exercise"))
        				{
        					if(this.courseManager.checkStudentsSettings(student, this.EXERCISE))
        					{
        						returned += student.getUser().getEmail() + " \n";
            					this.siteModifier = new SiteModifier(student.getGoogleAccessToken(),student.getSiteName());
            					if(partList.size()!=0)
            					{
                  				  returned=this.siteModifier.uploadRequest(this.course,this.pageContent,this.pageName,student,stringList);
                  				  if(!returned.contains("Errore"))
                  				  {
                  					  for(count=0;count<partList.size();count++){
          	        					  msgBody+=returned+"\n";
          	        					  returned=this.siteModifier.uploadFile(partList.get(count),student);
                  					  }
                  				  }
                  			  }    
        					}
        				}
        				else
        				{
        					if(this.courseManager.checkStudentsSettings(student, this.LECTURE))
        					{
        						returned += student.getUser().getEmail() + " \n";
            					this.siteModifier = new SiteModifier(student.getGoogleAccessToken(),student.getSiteName());
            					if(partList.size()!=0)
            					{
                  				  returned=this.siteModifier.uploadRequest(this.course,this.pageContent,this.pageName,student,stringList);
                  				  if(!returned.contains("Errore"))
                  				  {
                  					  for(count=0;count<partList.size();count++){
          	        					  msgBody+=returned+"\n";
          	        					  returned=this.siteModifier.uploadFile(partList.get(count),student);
                  					  }
                  				  }
                  			  }   
        					}
        				}
    				}while(iter.hasNext());	
		        		
	        	}
	        	else
	        	{
	        		if(subject.contains("Post") || subject.contains("post")){
	        			msgBody=msgBody+"Richiesta di Post.\n";
	        			returned=this.siteModifier.postRequest(this.course,this.pageContent,this.pageName,tempUser);
	        			if(!returned.contains("Errore")){
	        			//inviare aggiornamenti agli studenti
	        			List<UserPO> listStudents = LoadStore.getStudentsEnrolled(LoadStore.getCourseKey(this.course, tempUser.getUser().getEmail()));
	        			Iterator<UserPO> iter = listStudents.iterator();
	        			if(listStudents != null && !listStudents.isEmpty())
	        			{
	        				returned += "\nUtenti iscritti:\n";
	        				UserPO student;
		        			do{
		        				student = iter.next();
		        				if(subject.contains(this.EXERCISE) || subject.contains("Exercise"))
		        				{
		        					if(this.courseManager.checkStudentsSettings(student, this.EXERCISE))
		        					{
		        						returned += student.getUser().getEmail() + " \n";
		    	    					this.siteModifier = new SiteModifier(student.getGoogleAccessToken(),student.getSiteName());
		    	    					this.siteModifier.postRequest(this.course,this.pageContent,this.pageName,student);
		        					}
		        				}
		        				else
		        				{
		        					if(this.courseManager.checkStudentsSettings(student, this.LECTURE))
		        					{
		        						returned += student.getUser().getEmail() + " \n";
		    	    					this.siteModifier = new SiteModifier(student.getGoogleAccessToken(),student.getSiteName());
		    	    					this.siteModifier.postRequest(this.course,this.pageContent,this.pageName,student);
		        					}
		        				}
		    				}while(iter.hasNext());	        			
		        			
		        			
		        			if(!returned.contains("Errore")){
		        				msgBody+=returned+"\n";
		        			}else{
		        				postOnTwitter(tempUser,returned);
		        				msgBody+=returned+"\n";
		        			}
	        			}
	        			
	        		}else{
	        			msgBody="Errore richiesta post.";
	        		}
	        		}
	        		else 
	        		{
		        			if(subject.contains("Course") || subject.contains("course"))
		        			{
			        		    returned=this.siteModifier.createPage(this.pageName,this.pageContent,null,null);
			        		    if(returned.contains("expired")){
			        				GoogleAccessProtectedResource access=new GoogleAccessProtectedResource(tempUser.getGoogleAccessToken(),TRANSPORT,JSON_FACTORY,CLIENT_ID, CLIENT_SECRET,tempUser.getGoogleRefreshToken());
			        				access.refreshToken();
			        				String newAccessToken=access.getAccessToken();
			        				LoadStore.updateAccessToken(tempUser.getUser().getEmail(), newAccessToken);
				        			this.siteModifier=new SiteModifier(newAccessToken,tempUser.getSiteName());
				        		    returned=this.siteModifier.createPage(this.pageName,this.pageContent,null,null);
			        		    }
			        		    if(!returned.contains("Errore")){
				        		    postOnTwitter(tempUser,returned);
				        		    msgBody += "Corso creato alla pagina: " + returned;
				        			LoadStore.storeNewCourse(tempUser,this.pageName, this.pageContent);
				        			//creazione calendario associato al corso			        			
	        						calendarHelper = new CalendarHelper(tempUser.getUser().getEmail());
		        					String id = calendarHelper.createCalendar(this.pageName, this.pageContent);
	        						LoadStore.storeCalendarId(id, this.pageName, tempUser.getUser().getEmail());
	        						msgBody += "\nE' stato creato un calendario associato al corso.";	 
			        		    }else{
			        		    	msgBody="Errore creazione corso.";
			        		    }
			        		    
		        			}
		        			else
		        			{
		        				//TODO: calendar.
		        				if(subject.contains("New Calendar") || subject.contains("new calendar") || subject.contains("New calendar"))
		        				{
		        					if(this.course != null)
		        					{
		        						calendarHelper = new CalendarHelper(tempUser.getUser().getEmail());
			        					String id = calendarHelper.createCalendar(this.pageName, this.pageContent);
		        						LoadStore.storeCalendarId(id, this.course, tempUser.getUser().getEmail());
		        						msgBody += "Creazione calendario riuscita.";
		        					}		        						
		        					else
		        						msgBody += "Corso non specificato, nessuna modifica apportata";
		        				}
		        				else if(subject.contains("Evento") || subject.contains("evento"))
		        				{
		        					if(this.course != null)
		        					{
		        						calendarHelper = new CalendarHelper(tempUser.getUser().getEmail());
		        						String id = LoadStore.loadCalendarId(this.course, tempUser.getUser().getEmail());
		        						if(id == null)
		        							msgBody = "Creare prima il calendario per il corso: " + this.course;
		        						else
		        						{
		        							calendarHelper.createEvent(id, this.pageName, this.pageContent, this.date, this.startTime, this.endTime);
		        							msgBody += "Nuovo evento creato nel calendario del corso " + this.course;
		        							this.postOnTwitter(tempUser, "Nuovo evento creato sul calendario del corso: " +  this.course);
		        						}
		        					}
		        					else
		        						msgBody += "Corso non specificato, nessuna modifica apportata.";
		        					
		        				}
		        				else if(subject.contains("delete calendar") || subject.contains("Delete calendar") || subject.contains("Delete Calendar"))
		        				{
		        					if(this.course != null)
		        					{
		        						calendarHelper = new CalendarHelper(tempUser.getUser().getEmail());
		        						String id = LoadStore.loadCalendarId(this.course, tempUser.getUser().getEmail());
		        						if(id != null)
		        						{
		        							calendarHelper.deleteCalendar(id);
			        						LoadStore.storeCalendarId(null, this.course, tempUser.getUser().getEmail());
			        						msgBody += "Cancellazione calendario del corso " + this.course + " completata con successo.";
		        						}
		        						else
		        							msgBody += "Nessun calendario e' associato al corso: " + this.course;
		        							
		        					}
		        					else
		        						msgBody += "Corso non specificato, nessuna modifica apportata";
		        				}
		        					
		        			}	        			
	        		}
	        		
	        	}
	            msg.setText(msgBody);

    	        } catch (AddressException e) {
    	            e.printStackTrace();
    	        } catch (MessagingException e) {
    	            e.printStackTrace();
    	        }
            Transport.send(msg);

		} catch (MessagingException e) {
			e.printStackTrace();
		}
    }
}