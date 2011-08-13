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
	public String createPage() {
		String returned = null;
		WebPageEntry createdEntry;
		try {
			createdEntry = this.createWebPage("New Webpage Title", "<b>HTML content</b>");
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

	public WebPageEntry createWebPage(String title, String content)
	    throws MalformedURLException, IOException, ServiceException {
		WebPageEntry entry = new WebPageEntry();
		entry.setTitle(new PlainTextConstruct(title));

		setContentBlob(entry, content); // Entry's HTML content		
		return client.insert(new URL(buildContentFeedUrl()), entry);
	}
	
	public String buildContentFeedUrl() {
		  String domain = "site";  // OR if the Site is hosted on Google Apps, your domain (e.g. example.com)
		  String siteName = "modellopolimigcourse";
		  return "https://sites.google.com/feeds/content/" + domain + "/" + siteName + "?access_token=ya29.AHES6ZRByLwDkIhHXjHKsGkoN-YP1g4eXmXqNHMiyfTeMXI";
		}

}
