package it.polimi.server;

import java.util.Iterator;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import it.polimi.client.LoginService;
import it.polimi.server.data.PMF;
import it.polimi.server.data.UserPO;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class LoginServiceImpl extends RemoteServiceServlet implements LoginService{

	@SuppressWarnings("unchecked")
	@Override
	public boolean checkUser(String nickname, String pwd) {
		boolean check;
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
				return false;
			else 
			{
				do{
					userTemp = (UserPO) iter.next();
					if(userTemp.getNickname().equals(nickname) && userTemp.getPassword().equals(pwd))				
						return true;
					else
						check = false;
				}while(iter.hasNext());									
			}
		} finally {
			
			// close persistence manager
			pm.close();
		}
		return check;
	}
}
