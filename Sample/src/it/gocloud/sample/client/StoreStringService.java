package it.gocloud.sample.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("store")
public interface StoreStringService extends RemoteService {

	void storeString(String data) throws IllegalArgumentException;
}
