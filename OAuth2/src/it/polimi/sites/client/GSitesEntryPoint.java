package it.polimi.sites.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class GSitesEntryPoint implements EntryPoint {

private final GSitesServiceAsync pageCreatorService = GWT.create(GSitesService.class);
	
	@Override
	public void onModuleLoad() {
		pageCreatorService.createPage(new AsyncCallback<String>() {
			
			@Override
			public void onSuccess(String conferma) {
				Window.alert(conferma);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Google Sites page creation failed");
			}
		});
	}

}
