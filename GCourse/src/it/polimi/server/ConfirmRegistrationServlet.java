package it.polimi.server;

import it.polimi.server.data.PMF;
import it.polimi.server.data.UserPO;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;


import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.api.client.googleapis.auth.oauth2.draft10.GoogleAuthorizationRequestUrl;

@SuppressWarnings("serial")
public class ConfirmRegistrationServlet extends HttpServlet {
	
	private static final String SCOPE = "https://sites.google.com/feeds/";
	private static final String CALLBACK_URL = "http://polimigcourse.appspot.com/googlecallback";
	private static final String CLIENT_ID = "267706380696.apps.googleusercontent.com";
	
	@SuppressWarnings("unchecked")
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("text/html");
		PrintWriter out = resp.getWriter();
		HttpSession session = req.getSession();
	
		String email = req.getParameter("email");
		session.setAttribute("email", email);
		out.println("<html>");
		out.println("<head>");
		out.println("<title> Confirm registration </title>");
		
		
		// get persistence manager
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		try {
			
			// get POs from DataStore
			Query query = pm.newQuery(UserPO.class);
			List<UserPO> results = (List<UserPO>)query.execute();
			Iterator<UserPO> iter = results.iterator();
			UserPO userTemp;
			// check empty results
			if (results.isEmpty())
			{
				out.println("</head>");
				out.println("<body>");
				out.println("Confirm registration of: " + email);
				out.println("<br>ERROR: Nessuna mail corrispondente trovata");
			}
			else 
			{
				boolean ok = false;
				do{
					userTemp = (UserPO) iter.next();
					if(userTemp.getUser().getEmail().equals(email) && !userTemp.isConfirmed())
					{
						userTemp.setConfirmed(true);	
						ok = true;
					}
						
				}while(iter.hasNext());		
				//write the results in the web page
				if(ok)
				{
					String authorizeUrl = new GoogleAuthorizationRequestUrl(CLIENT_ID, CALLBACK_URL, SCOPE).build();
					out.println("<head>");
					out.println("<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\">");
					out.println("<title>Registrazione</title>");
					out.println("</head>");
					out.println("<body>");
					out.println("Conferma registrazione account: " + email);
					out.println("<br><br><a href=\"" + authorizeUrl + "\">Aggiungi permessi Google al nostro servizio</a>");
					out.println("<br>Verrai reinderizzato alla pagina di login di Google.");
				}
				else
				{
					out.println("</head>");
					out.println("<body>");
					out.println("Confirm registration of: " + email);
					out.println("<br>ERROR: Account gia confermato oppure nessuna corrispondenza trovata.");
				}		
					
			}
			
		} finally {
			
			// close persistence manager
			pm.close();
		}
		out.println("</body>");
		out.println("</html>");
	}
}
