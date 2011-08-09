package it.gocloud.sample.server;

import it.gocloud.sample.client.StoreStringService;
import it.gocloud.sample.server.data.DataPO;
import it.gocloud.sample.server.data.PMF;

import javax.jdo.PersistenceManager;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class StoreStringServiceImpl extends RemoteServiceServlet implements StoreStringService {

	@Override
	public void storeString(String data) throws IllegalArgumentException {
		
		// get persistence manager
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		try {
			
			// create a new Data object
			DataPO dataPO = new DataPO();
			dataPO.setData(data);
			
			// store object into DataStore
			pm.makePersistent(dataPO);
			
		} finally {
			
			// close persistence manager
			pm.close();
		}
	}
}
