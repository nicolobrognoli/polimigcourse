package it.polimi.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Properties;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@SuppressWarnings("serial")
public class Authentication extends HttpServlet {

	@SuppressWarnings("unchecked")
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("text/html");
		PrintWriter out = resp.getWriter();
		Properties props = new Properties();
		String response="";
		HttpSession session = req.getSession();
		Object o=req.getParameter("code");
		if(o!=null){
			response=response+o.toString();
		}
		else{
			o=req.getParameter("error");
			if(o!=null){
				response=response+o.toString();
			}
		}
		out.println("<html>");
		out.println("<head>");
		out.println("<title> Confirm registration </title>");
		out.println("</head>");
		out.println("<body>");
		out.println("Response: " + response);
		out.println("</body>");
		out.println("</html>");
		URL u = new URL("https://sites.google.com/feeds/content/site/provagcoursepolimi");
		//URL u = new URL("https://sites.google.com/feeds/site/site");//tentativo per creare sito
		
		URLConnection c = u.openConnection();

		c.setDoOutput(true);
		if (c instanceof HttpURLConnection) {
			((HttpURLConnection)c).setRequestMethod("POST");
		}

		OutputStreamWriter out2 = new OutputStreamWriter(
			c.getOutputStream());
		String post="POST /feeds/content/site/provagcoursepolimi HTTP/1.1 Host: sites.google.com GData-Version: 1.4"+
				"Authorization: AuthSub token=\"ya29.AHES6ZQi_lY-nPURRLG3LJNa_x3X92zUZhEZd23CmwDuty0\""+
				"Content-Length: 328"+
				"Content-Type: application/atom+xml"+
				"<entry xmlns=\"http://www.w3.org/2005/Atom\">"+
				  "<category scheme=\"http://schemas.google.com/g/2005#kind\""+
				      "term=\"http://schemas.google.com/sites/2008#webpage\" label=\"webpage\"/>"+
				 " <title>New Webpage Title</title>"+
				  "<content type=\"xhtml\">"+
				   " <div xmlns=\"http://www.w3.org/1999/xhtml\"><body>ciaoioasioaoiasfoia</body></div>"+
				  "</content>"+
				"</entry>";
		// output your data here
		out2.write("POST /feeds/content/site/provagcoursepolimi HTTP/1.1 Host: sites.google.com GData-Version: 1.4"+
"Authorization: AuthSub token=\"ya29.AHES6ZQi_lY-nPURRLG3LJNa_x3X92zUZhEZd23CmwDuty0\""+
"Content-Length: 328"+
"Content-Type: application/atom+xml"+
"<entry xmlns=\"http://www.w3.org/2005/Atom\">"+
  "<category scheme=\"http://schemas.google.com/g/2005#kind\""+
      "term=\"http://schemas.google.com/sites/2008#webpage\" label=\"webpage\"/>"+
 " <title>New Webpage Title</title>"+
  "<content type=\"xhtml\">"+
   " <div xmlns=\"http://www.w3.org/1999/xhtml\"><body>ciaoioasioaoiasfoia</body></div>"+
  "</content>"+
"</entry>");
	/*	out2.write("POST feeds/site/provagcoursepolimi.com HTTP/1.1"+
				"Host: sites.google.com"+
				"GData-Version: 1.4"+
				"Authorization:AuthSub token=\"ya29.AHES6ZQi_lY-nPURRLG3LJNa_x3X92zUZhEZd23CmwDuty0\""+
				"<entry xmlns=\"http://www.w3.org/2005/Atom\" xmlns:sites=\"http://schemas.google.com/sites/2008\">"+
				  "<title>Source Site</title>"+
				 " <summary>A new site to hold memories</summary>"+
				  "<sites:theme>slate</sites:theme>"+
				"</entry>");*/
		out2.close();
		 BufferedReader in = new BufferedReader(
                 new InputStreamReader(
                     c.getInputStream()));

String s = null;
while ((s = in.readLine()) != null) {
     out.println(s);
}
in.close();
		}

}
