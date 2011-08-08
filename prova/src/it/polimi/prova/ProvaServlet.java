package it.polimi.prova;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import javax.servlet.http.*;

@SuppressWarnings("serial")
public class ProvaServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/plain");
		resp.getWriter().println("Hello, world");
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        String msgBody = "ciao,come va?";

        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress("simone.benefico@gmail.com", "Example.com Admin"));
            msg.addRecipient(Message.RecipientType.TO,
                             new InternetAddress("andrea.colombo88@gmail.com", "Mr. User"));
            msg.setSubject("Your Example.com account has been activated");
            msg.setText(msgBody);
            Transport.send(msg);

        } catch (AddressException e) {
            // ...
        } catch (MessagingException e) {
            // ...
        }
	}
}
