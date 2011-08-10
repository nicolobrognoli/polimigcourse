package it.polimi.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.LayoutPanel;

public class Register implements EntryPoint {

	// send registration mail service
	private final SendRegistrationMailAsync sendRegistrationService = GWT.create(SendRegistrationMail .class);
	
	@Override
	public void onModuleLoad() {
		
		// add widgets to layout panel
		RootLayoutPanel rootLayoutPanel = RootLayoutPanel.get();
		
		LayoutPanel layoutPanel = new LayoutPanel();
		rootLayoutPanel.add(layoutPanel);
		rootLayoutPanel.setWidgetLeftRight(layoutPanel, 59.0, Unit.PX, 59.0, Unit.PX);
		rootLayoutPanel.setWidgetTopHeight(layoutPanel, 81.0, Unit.PX, 140.0, Unit.PX);
		
		Label lblIndirizzoMail = new Label("Indirizzo mail:");
		lblIndirizzoMail.addStyleName("style");
		layoutPanel.add(lblIndirizzoMail);
		layoutPanel.setWidgetLeftWidth(lblIndirizzoMail, 28.0, Unit.PX, 98.0, Unit.PX);
		layoutPanel.setWidgetTopBottom(lblIndirizzoMail, 30.0, Unit.PX, 92.0, Unit.PX);
		
		final TextBox textBox = new TextBox();
		layoutPanel.add(textBox);
		layoutPanel.setWidgetLeftWidth(textBox, 121.0, Unit.PX, 191.0, Unit.PX);
		layoutPanel.setWidgetTopHeight(textBox, 25.0, Unit.PX, 23.0, Unit.PX);
		
		Button btnRegistra = new Button("Registra");
		btnRegistra.setText("Registra");
		layoutPanel.add(btnRegistra);
		layoutPanel.setWidgetLeftWidth(btnRegistra, 121.0, Unit.PX, 98.0, Unit.PX);
		layoutPanel.setWidgetTopHeight(btnRegistra, 76.0, Unit.PX, 22.0, Unit.PX);
		
		Label lblVerrInviataUnemail = new Label("Verr\u00E0 inviata un'email per la conferma all'indirizzo indicato");
		layoutPanel.add(lblVerrInviataUnemail);
		layoutPanel.setWidgetLeftWidth(lblVerrInviataUnemail, 38.0, Unit.PX, 281.0, Unit.PX);
		layoutPanel.setWidgetTopHeight(lblVerrInviataUnemail, 54.0, Unit.PX, 18.0, Unit.PX);
		
		Label lblGcourse = new Label("GCourse: registrazione");
		rootLayoutPanel.add(lblGcourse);
		rootLayoutPanel.setWidgetLeftWidth(lblGcourse, 94.0, Unit.PX, 158.0, Unit.PX);
		rootLayoutPanel.setWidgetTopHeight(lblGcourse, 29.0, Unit.PX, 18.0, Unit.PX);
		
		// button event
		btnRegistra.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				String mitt = textBox.getText();
				sendRegistrationService.sendMail(mitt, new AsyncCallback<Void>() {

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
