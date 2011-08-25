package it.polimi.server.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;

import javax.servlet.http.HttpServletResponse;

import it.polimi.server.SitesException;
import it.polimi.server.SitesHelper;

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
	
	public String createPage(String namePage,String content) throws IOException{
		BaseContentEntry<?> page;
	    SitesHelper sitesHelper;
	    sitesHelper = new SitesHelper("polimigcourse","site",this.siteName);
	    sitesHelper.login(this.accessToken);
	    try {
			page = sitesHelper.createPage("webpage",namePage);
			XmlBlob xml = new XmlBlob();
			xml.setBlob("<p>"+content+"</p>");
			page.setContent(new XhtmlTextConstruct(xml));
			page.update();
			//sitesHelper.service.update(new URL(sitesHelper.getContentFeedUrl()), page);
			return "https://sites.google.com/site/"+this.siteName + "/" + namePage;
			//return this.accessToken+" "+this.siteName;

		
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "errore: Exception ServiceException expired";
		} catch (SitesException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "errore: Exception SitesException";
		}
	}

	public String createPage(String namePage, String content, InputStream input,int size) throws MalformedURLException, IOException {
		BaseContentEntry<?> page;
	    SitesHelper sitesHelper;
	    sitesHelper = new SitesHelper("polimigcourse","site",this.siteName);
	    sitesHelper.login(this.accessToken);
	    String path="";
	    try {
			page = sitesHelper.createPage("webpage",namePage);
			//Inizio BLob
			/*
			
			// Get a file service
			  FileService fileService = FileServiceFactory.getFileService();
			  // Create a new Blob file with mime-type "text/plain"
			  AppEngineFile file = fileService.createNewBlobFile("image/jpeg");
			  // Open a channel to write to it
			  boolean lock = true;
			  FileWriteChannel writeChannel = fileService.openWriteChannel(file, lock);

			  // Close without finalizing and save the file path for writing later
			  path = file.getFullPath();
			  // Write more to the file in a separate request:
			  // This time we write to the channel using standard Java
			  ByteBuffer buffer;
			  byte[] b=new byte[1024];
			  //input.read(b);
			  //input.read(b,0, size);
			  int len;
			  while((len=input.read(b))>0){
				  writeChannel.write(ByteBuffer.wrap(b, 0, len));
			  }

			  // Now finalize
			  writeChannel.closeFinally();

			*/
			//Fine blob
			/*BlobKey key=fileService.getBlobKey(file);
			String urlStr="https://appengine.google.com/blobstore/download?app_id=s~polimigcourse&key="+key.getKeyString();
			URL url = new URL(urlStr);
			URLConnection conn = url.openConnection ();
			// Get the response
			BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			StringBuffer sb = new StringBuffer();
			String line;
			while ((line = rd.readLine()) != null)
			{
			sb.append(line);
			}
			rd.close();
			String result = sb.toString();
		    URLFetchService nome=new URLFetchServiceFactory().getURLFetchService();
			HTTPResponse resp=nome.fetch(url);
			result=resp.getFinalUrl().getPath();
			resp=nome.fetch(resp.getFinalUrl());
			result=resp.getFinalUrl().getPath();*/
			//sitesHelper.uploadAttachment(new File(path), (BasePageEntry<?>)page);
			XmlBlob xml = new XmlBlob();
			xml.setBlob("<p>"+content+"</p>"+"<link href=\"https://sites.google.com/site/provamiagcourse/PDFFile\"/>");
			page.setContent(new XhtmlTextConstruct(xml));
			page.update();
			return "https://sites.google.com/site/"+this.siteName+"/"+namePage;


		
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "errore: Exception ServiceException expired"+" path: "+path;
		} catch (SitesException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "errore: Exception SitesException";
		}
	}

}
