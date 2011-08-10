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
import javax.mail.Session;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class ConfirmRegistrationServlet extends HttpServlet {
	
	@SuppressWarnings("unchecked")
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("text/html");
		PrintWriter out = resp.getWriter();
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);
		String email = req.getParameter("email");
		
		out.println("<html>");
		out.println("<head>");
		out.println("<title> Confirm registration </title>");
		out.println("</head>");
		out.println("<body>");
		out.println("Confirm registration of: " + email);
		
		// get persistence manager
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		try {
			
			// get POs from DataStore
			Query query = pm.newQuery(UserPO.class);
			List<UserPO> results = (List<UserPO>)query.execute();
			Iterator<UserPO> iter = results.iterator();
			UserPO user;
			// check empty results
			if (results.isEmpty())
			{
				out.println("ERROR: Nessuna mail corrispondente trovata");
			}
			else 
			{
				boolean ok = false;
				do{
					user = (UserPO) iter.next();
					if(user.getUser().getEmail().equals(email) && !user.isConfirmed())
					{
						user.setConfirmed(true);	
						ok = true;
					}
						
				}while(iter.hasNext());		
				//write the results in the web page
				if(ok)
				{
					
					
					out.println("<h1> Account confermato...</h1>");
					out.println("<a href=complete_registration.html>"+" Completa la registrazione "+"</a>");
					
					
				}
					
				else
					out.println("ERROR: Account gia confermato oppure nessuna corrispondenza trovata.");
					
			}
			
		} finally {
			
			// close persistence manager
			pm.close();
		}
		out.println("</body>");
		out.println("</html>");
	}
}
