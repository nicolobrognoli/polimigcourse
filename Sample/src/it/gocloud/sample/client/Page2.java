package it.gocloud.sample.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootLayoutPanel;

public class Page2 implements EntryPoint {

	// Load String service
	private final LoadStringServiceAsync loadStringService = GWT.create(LoadStringService.class);
	
	@Override
	public void onModuleLoad() {
		
		// widgets
		final Button okButton = new Button("Load");
		final Label label = new Label("press Load to retrieve data");
		FlowPanel panel = new FlowPanel();
		panel.add(label);
		panel.add(okButton);
		
		// add widgets to layout panel
		RootLayoutPanel.get().add(panel);
		
		// button event
		okButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				
				// load data from server
				loadStringService.loadString(new AsyncCallback<String>() {

					@Override
					public void onFailure(Throwable caught) {
						
						// display error
						Window.alert("Unable to load data from server!");
					}

					@Override
					public void onSuccess(String result) {
						
						// display result on label
						label.setText(result);
					}
				});
			}
		});
	}
}
