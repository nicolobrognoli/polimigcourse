package it.polimi.server.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.util.ArrayList;

import javax.mail.MessagingException;
import javax.mail.Part;
import javax.servlet.http.HttpServletResponse;

import it.polimi.server.SitesException;
import it.polimi.server.SitesHelper;
import it.polimi.server.data.UserPO;

import com.google.api.client.googleapis.auth.oauth2.draft10.GoogleAccessProtectedResource;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.appengine.api.blobstore.BlobInfo;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.files.AppEngineFile;
import com.google.appengine.api.files.FileReadChannel;
import com.google.appengine.api.files.FileService;
import com.google.appengine.api.files.FileServiceFactory;
import com.google.appengine.api.files.FileWriteChannel;
import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;
import com.google.gdata.client.sites.SitesService;
import com.google.gdata.data.Content;
import com.google.gdata.data.XhtmlTextConstruct;
import com.google.gdata.data.sites.BaseContentEntry;
import com.google.gdata.data.sites.BasePageEntry;
import com.google.gdata.data.sites.SiteEntry;
import com.google.gdata.util.ServiceException;
import com.google.gdata.util.XmlBlob;

public class SiteModifier {
	private static final String CLIENT_ID = "267706380696.apps.googleusercontent.com";
	private static final String CLIENT_SECRET = "zBWLvQsYnEF4-AAg1PZYu7eA";
	private static final HttpTransport TRANSPORT = new NetHttpTransport();
	private static final JsonFactory JSON_FACTORY = new JacksonFactory();
	
	private String accessToken, siteName;

	
	public SiteModifier(String accessToken,String siteName){
		this.siteName=siteName;
		this.accessToken=accessToken;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}
	public void login(){
	    SitesHelper sitesHelper;
	    sitesHelper = new SitesHelper("polimigcourse","site",this.siteName);
	    sitesHelper.login(this.accessToken);
	}
	public String createPage(String namePage, String content,String course, ArrayList<String> stringList) throws MalformedURLException, IOException {
		int count;
		BaseContentEntry<?> page;
	    SitesHelper sitesHelper;
	    sitesHelper = new SitesHelper("polimigcourse","site",this.siteName);
	    sitesHelper.login(this.accessToken);
	    String path="",file,body;
	    try {
	    	if(course==null){
	    		page = sitesHelper.createPage("webpage",namePage);
	    	}else{
	    		page = sitesHelper.createPage("webpage",namePage,course);
	    	}
	    	if(page!=null){
		    	if(stringList.size()!=0){
					XmlBlob xml = new XmlBlob();
					body="<p>"+content+"</p>";

		    		for(count=0;count<stringList.size();count++){
						file=stringList.get(count);
						body+="<p><a href=\"https://sites.google.com/site/"+this.siteName+"/"+file+"\">"+file+"</a></p>";
		    		}
					xml.setBlob(body);
					page.setContent(new XhtmlTextConstruct(xml));
					page.update();
		    	}else{
					XmlBlob xml = new XmlBlob();
					xml.setBlob("<p>"+content+"</p>");
					page.setContent(new XhtmlTextConstruct(xml));
					page.update();
		    	}
				return "https://sites.google.com/site/"+this.siteName+"/"+course+"/"+namePage;
	    	}else{
	    		return "Errore: creazione pagina fallita.\n";
	    	}
	
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "errore: Exception ServiceException expired"+e.toString();
		} catch (SitesException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "errore: Exception SitesException";
		}
	}
	
	public String uploadFile(Part part,UserPO tempUser) throws IOException, MessagingException{
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
	public String postRequest(String course,String pageContent,String pageName, UserPO tempUser) throws MalformedURLException, IOException{
		String returned; 
	    returned=createPage(pageName,pageContent,course,null);
	    if(returned.contains("expired")){
			GoogleAccessProtectedResource access=new GoogleAccessProtectedResource(tempUser.getGoogleAccessToken(),TRANSPORT,JSON_FACTORY,CLIENT_ID, CLIENT_SECRET,tempUser.getGoogleRefreshToken());
			access.refreshToken();
			String newAccessToken=access.getAccessToken();
			LoadStore.updateAccessToken(tempUser.getUser().getEmail(), newAccessToken);
		    returned=createPage(pageName,pageContent,course,null);
	    }
			return returned;
	}
	
	public String uploadRequest(String course,String pageContent,String pageName,UserPO tempUser, ArrayList<String> stringList) throws MalformedURLException, IOException{
		String returned;
	    returned=createPage(pageName,pageContent,course,stringList);
	    if(returned.contains("expired")){
			GoogleAccessProtectedResource access=new GoogleAccessProtectedResource(tempUser.getGoogleAccessToken(),TRANSPORT,JSON_FACTORY,CLIENT_ID, CLIENT_SECRET,tempUser.getGoogleRefreshToken());
			access.refreshToken();
			String newAccessToken=access.getAccessToken();
			LoadStore.updateAccessToken(tempUser.getUser().getEmail(), newAccessToken);
		    returned=createPage(pageName,pageContent,course,stringList);
		}
		return returned;
	}
	
}
