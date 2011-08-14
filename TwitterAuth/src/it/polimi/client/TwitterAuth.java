package it.polimi.client;


import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootLayoutPanel;



public class TwitterAuth implements EntryPoint {
	
	private final TwitterServiceAsync twitterService = GWT.create(TwitterService.class);
	
	public void onModuleLoad() {
		// add widgets to layout panel
		RootLayoutPanel rootLayoutPanel = RootLayoutPanel.get();
		
		final Label lblTokenTwitter = new Label("Token Twitter:");
		rootLayoutPanel.add(lblTokenTwitter);
		rootLayoutPanel.setWidgetLeftWidth(lblTokenTwitter, 36.0, Unit.PX, 652.0, Unit.PX);
		rootLayoutPanel.setWidgetTopHeight(lblTokenTwitter, 108.0, Unit.PX, 59.0, Unit.PX);
		
		twitterService.getRequestToken(new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				
				// display error
				Window.alert("Service error!");
			}

			@Override
			public void onSuccess(String result) {
				lblTokenTwitter.setText(lblTokenTwitter.getText()+result);
			}
		});
		
	}
}
