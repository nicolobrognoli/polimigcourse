package it.polimi.server;

import java.io.IOException;
import java.util.Properties;


import javax.mail.Session;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class ConfirmRegistrationServlet extends HttpServlet {
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("text/plain");
		resp.getWriter().println("Hello, world");
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);
		resp.getWriter().println("Mail From: " + req.getParameter("email"));
	}
}
