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
import javax.servlet.http.HttpSession;

@SuppressWarnings("serial")
public class ConfirmRegistrationServlet extends HttpServlet {
	
	@SuppressWarnings("unchecked")
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("text/html");
		PrintWriter out = resp.getWriter();
		Properties props = new Properties();
		HttpSession session = req.getSession();
	
		String email = req.getParameter("email");
		
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
						session.setAttribute("email", email);
					}
						
				}while(iter.hasNext());		
				//write the results in the web page
				if(ok)
				{
					out.println("<head>");
					out.println("<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\">");
					out.println("<title>Registrazione</title>");
					out.println("</head>");
					out.println("<body>");
					out.println("<a href=\"confirm.html\"> Continua...</a>");
				
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
