package it.polimi.server.utils;

import it.polimi.server.data.PMF;
import it.polimi.server.data.UserPO;

import java.util.Iterator;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

public class LoadStore {

	public static String storeAccessToken(String email, String kind, String accessToken, String refreshToken){

		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			// get POs from DataStore
			Query query = pm.newQuery(UserPO.class);
			@SuppressWarnings("unchecked")
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
						if(kind.equals("google"))
						{
							userTemp.setGoogleAccessToken(accessToken);
							userTemp.setGoogleRefreshToken(refreshToken);
						}
						else if(kind.equals("twitter"))
						{
							userTemp.setTwitterAccessToken(accessToken);
							userTemp.setTwitterSecretToken(refreshToken);
						}
						else
							return "Parameter kind error";
						
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
