package it.gocloud.sample.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("load")
public interface LoadStringService extends RemoteService {

	String loadString() throws IllegalArgumentException;
}
