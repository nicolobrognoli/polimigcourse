package it.polimi.client;


import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ProvaPosta implements EntryPoint {
	// Store String service
	private final GetParameterServiceAsync getParameterService = GWT.create(GetParameterService .class);
	
	@Override
	public void onModuleLoad() {
		// widgets
		final Label label = new Label("Mittente");
		final Label label2 = new Label();
		final Button okButton = new Button("Send");
		final TextBox mittente = new TextBox();
		VerticalPanel panel = new VerticalPanel();
		
		panel.add(label);
		panel.add(mittente);
		panel.add(okButton);
		panel.add(label2);
		
		// add widgets to layout panel
		RootLayoutPanel.get().add(panel);
		// button event
		okButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				String mitt = mittente.getText();
				getParameterService.getParameter(mitt, new AsyncCallback<Void>() {

					@Override
					public void onFailure(Throwable caught) {
						
						// display error
						Window.alert("Unable to send data to server!");
					}

					@Override
					public void onSuccess(Void result) {
						
						// display success
						Window.alert("Message sent...");
					}
				});
			}
		});

	}

}
