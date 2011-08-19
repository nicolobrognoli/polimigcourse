package it.polimi.server.utils;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import it.polimi.server.SitesException;
import it.polimi.server.SitesHelper;

import com.google.api.client.googleapis.auth.oauth2.draft10.GoogleAccessProtectedResource;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.gdata.client.sites.SitesService;
import com.google.gdata.data.Content;
import com.google.gdata.data.XhtmlTextConstruct;
import com.google.gdata.data.sites.BaseContentEntry;
import com.google.gdata.data.sites.BasePageEntry;
import com.google.gdata.data.sites.SiteEntry;
import com.google.gdata.util.ServiceException;
import com.google.gdata.util.XmlBlob;

public class SiteModifier {
	
	private String accessToken,siteName;

	
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
			//return "https://sites.google.com/site/"+this.siteName+namePage;
			return this.accessToken+" "+this.siteName;

		
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

	public String createPage(String namePage, String content, Object file) throws MalformedURLException, IOException {
		BaseContentEntry<?> page;
	    SitesHelper sitesHelper;
	    sitesHelper = new SitesHelper("polimigcourse","site",this.siteName);
	    sitesHelper.login(this.accessToken);
	    try {
			page = sitesHelper.createPage("webpage",namePage);
			sitesHelper.uploadAttachment((File)file, (BasePageEntry<?>)page);
			XmlBlob xml = new XmlBlob();
			xml.setBlob("<p>"+content+"</p>");
			page.setContent(new XhtmlTextConstruct(xml));
			page.update();
			//sitesHelper.service.update(new URL(sitesHelper.getContentFeedUrl()), page);
			//return "https://sites.google.com/site/"+this.siteName+namePage;
			return this.accessToken+" "+this.siteName;

		
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

}
