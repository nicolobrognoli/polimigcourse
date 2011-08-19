package it.polimi.server;

import java.util.Iterator;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import it.polimi.client.LoadStoreService;
import it.polimi.server.data.PMF;
import it.polimi.server.data.UserPO;

@SuppressWarnings("serial")
public class LoadStoreServiceImpl extends RemoteServiceServlet implements LoadStoreService {

	@SuppressWarnings("unchecked")
	public String updateUser(String email, String name, String  pwd, boolean professor) throws IllegalArgumentException {
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
				return "email not found";
			else 
			{
				do{
					userTemp = (UserPO) iter.next();
					if(userTemp.getUser().getEmail().equals(email))					
					{
						userTemp.setNickname(name);
						userTemp.setPassword(pwd);
						userTemp.setProfessor(professor);
					}
				}while(iter.hasNext());									
			}
		} finally {
			
			// close persistence manager
			pm.close();
		}
		return "updated";
	}

	@SuppressWarnings("unchecked")
	@Override
	public String updateUser(String email, String name, String pwd, String site, boolean professor) {
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
				return "email not found";
			else 
			{
				do{
					userTemp = (UserPO) iter.next();
					if(userTemp.getUser().getEmail().equals(email))					
					{
						userTemp.setNickname(name);
						userTemp.setPassword(pwd);
						userTemp.setProfessor(professor);
						userTemp.setSiteName(site);
					}
				}while(iter.hasNext());									
			}
		} finally {
			// close persistence manager
			pm.close();
		}
		return "updated";
	}
}
