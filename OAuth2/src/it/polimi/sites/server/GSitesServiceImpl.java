package it.polimi.sites.server;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import it.polimi.sites.client.GSitesService;

import com.google.gdata.client.sites.SitesService;
import com.google.gdata.data.PlainTextConstruct;
import com.google.gdata.data.XhtmlTextConstruct;
import com.google.gdata.data.sites.BaseContentEntry;
import com.google.gdata.data.sites.WebPageEntry;
import com.google.gdata.util.ServiceException;
import com.google.gdata.util.XmlBlob;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class GSitesServiceImpl extends RemoteServiceServlet implements
		GSitesService {

private static final long serialVersionUID = 589282201651899809L;
	
	SitesService client = new SitesService("GCourse-GCourse-v1");

	@Override
	public String createPage(String siteName, String pageName, String token, String pageContent) {
		String returned = null;
		WebPageEntry createdEntry;
		try {
			createdEntry = this.createWebPage(siteName, pageName, pageContent, token);
			returned = "Created! View at " + createdEntry.getHtmlLink().getHref();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return returned;
	}
	
	private void setContentBlob(BaseContentEntry<?> entry, String pageContent) {
		XmlBlob xml = new XmlBlob();
		xml.setBlob(pageContent);
		entry.setContent(new XhtmlTextConstruct(xml));
	}

	public WebPageEntry createWebPage(String siteName, String title, String content, String token)
	    throws MalformedURLException, IOException, ServiceException {
		WebPageEntry entry = new WebPageEntry();
		entry.setTitle(new PlainTextConstruct(title));

		setContentBlob(entry, content); // Entry's HTML content		
		return client.insert(new URL(buildContentFeedUrl(siteName, token)), entry);
	}
	
	public String buildContentFeedUrl(String siteName, String token) {
		  String domain = "site";  // OR if the Site is hosted on Google Apps, your domain (e.g. example.com)
		  return "https://sites.google.com/feeds/content/" + domain + "/" + siteName + "?access_token="+token;
		}

}
