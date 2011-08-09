package it.gocloud.sample.server;

import it.gocloud.sample.client.LoadStringService;
import it.gocloud.sample.server.data.DataPO;
import it.gocloud.sample.server.data.PMF;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class LoadStringServiceImpl extends RemoteServiceServlet implements LoadStringService {

	@SuppressWarnings("unchecked")
	@Override
	public String loadString() throws IllegalArgumentException {
		
		// get persistence manager
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		try {
			
			// get POs from DataStore
			Query query = pm.newQuery(DataPO.class);
			List<DataPO> results = (List<DataPO>)query.execute();
			
			// check empty results
			if (results.isEmpty()) return "NO DATA FOUND";
			else {
				
				// return first element data
				return results.get(0).getData();
			}
			
		} finally {
			
			// close persistence manager
			pm.close();
		}
	}
}
