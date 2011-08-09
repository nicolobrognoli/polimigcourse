package it.gocloud.sample.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.TextBox;

public class Page1 implements EntryPoint {

	// Store String service
	private final StoreStringServiceAsync storeStringService = GWT.create(StoreStringService.class);
	
	@Override
	public void onModuleLoad() {
		
		// widgets
		final Button okButton = new Button("Send");
		final TextBox text = new TextBox();
		FlowPanel panel = new FlowPanel();
		panel.add(text);
		panel.add(okButton);
		
		// add widgets to layout panel
		RootLayoutPanel.get().add(panel);
		
		// button event
		okButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				
				// send data to server
				storeStringService.storeString(text.getText(), new AsyncCallback<Void>() {

					@Override
					public void onFailure(Throwable caught) {
						
						// display error
						Window.alert("Unable to send data to server!");
					}

					@Override
					public void onSuccess(Void result) {
						
						// display success
						Window.alert("Data sent successfully!");
					}
				});
			}
		});
	}
}
