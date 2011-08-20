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
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RadioButton;

public class Register implements EntryPoint {

	private final SendRegistrationMailAsync sendRegistrationService = GWT.create(SendRegistrationMail .class);
	private final LoginServiceAsync loginService = GWT.create(LoginService.class);
	
	@SuppressWarnings("deprecation")
	@Override
	public void onModuleLoad() {
		
		// add widgets to layout panel
		RootLayoutPanel rootLayoutPanel = RootLayoutPanel.get();
		
		LayoutPanel layoutPanel_1 = new LayoutPanel();
		rootLayoutPanel.add(layoutPanel_1);
		
		final Button btnLogin = new Button("Login");
		layoutPanel_1.add(btnLogin);
		layoutPanel_1.setWidgetLeftWidth(btnLogin, 358.0, Unit.PX, 82.0, Unit.PX);
		layoutPanel_1.setWidgetTopHeight(btnLogin, 74.0, Unit.PX, 22.0, Unit.PX);
		
		final TextBox textNickname = new TextBox();
		layoutPanel_1.add(textNickname);
		layoutPanel_1.setWidgetLeftWidth(textNickname, 188.0, Unit.PX, 153.0, Unit.PX);
		layoutPanel_1.setWidgetTopHeight(textNickname, 43.0, Unit.PX, 23.0, Unit.PX);
		
		final PasswordTextBox passwordTextBox = new PasswordTextBox();
		layoutPanel_1.add(passwordTextBox);
		layoutPanel_1.setWidgetLeftWidth(passwordTextBox, 188.0, Unit.PX, 153.0, Unit.PX);
		layoutPanel_1.setWidgetTopHeight(passwordTextBox, 73.0, Unit.PX, 23.0, Unit.PX);
		
		final Label lblNickname = new Label("Nickname:");
		layoutPanel_1.add(lblNickname);
		layoutPanel_1.setWidgetLeftWidth(lblNickname, 103.0, Unit.PX, 76.0, Unit.PX);
		layoutPanel_1.setWidgetTopHeight(lblNickname, 48.0, Unit.PX, 18.0, Unit.PX);
		
		final Label lblPassword = new Label("Password:");
		layoutPanel_1.add(lblPassword);
		layoutPanel_1.setWidgetLeftWidth(lblPassword, 103.0, Unit.PX, 79.0, Unit.PX);
		layoutPanel_1.setWidgetTopHeight(lblPassword, 74.0, Unit.PX, 18.0, Unit.PX);
		
		final Label lblIndirizzoMail = new Label("Indirizzo mail:");
		layoutPanel_1.add(lblIndirizzoMail);
		layoutPanel_1.setWidgetLeftWidth(lblIndirizzoMail, 121.0, Unit.PX, 106.0, Unit.PX);
		layoutPanel_1.setWidgetTopHeight(lblIndirizzoMail, 151.0, Unit.PX, 19.0, Unit.PX);
		lblIndirizzoMail.addStyleName("style");
		
		final TextBox textBox = new TextBox();
		layoutPanel_1.add(textBox);
		textBox.setHeight("23");
		layoutPanel_1.setWidgetLeftWidth(textBox, 233.0, Unit.PX, 163.0, Unit.PX);
		layoutPanel_1.setWidgetTopHeight(textBox, 151.0, Unit.PX, 19.0, Unit.PX);
		
		final Label lblVerrInviataUnemail = new Label("Verrà inviata un'email per la conferma all'indirizzo indicato");
		layoutPanel_1.add(lblVerrInviataUnemail);
		layoutPanel_1.setWidgetLeftWidth(lblVerrInviataUnemail, 64.0, Unit.PX, 386.0, Unit.PX);
		layoutPanel_1.setWidgetTopHeight(lblVerrInviataUnemail, 176.0, Unit.PX, 19.0, Unit.PX);
		
		final Button btnRegistra = new Button("Registra");
		layoutPanel_1.add(btnRegistra);
		layoutPanel_1.setWidgetLeftWidth(btnRegistra, 233.0, Unit.PX, 76.0, Unit.PX);
		layoutPanel_1.setWidgetTopHeight(btnRegistra, 201.0, Unit.PX, 23.0, Unit.PX);
		btnRegistra.setText("Registra");
		
		final RadioButton rdbtnLogin = new RadioButton("group", "Login");
		rdbtnLogin.setChecked(true);
		layoutPanel_1.add(rdbtnLogin);
		layoutPanel_1.setWidgetLeftWidth(rdbtnLogin, 52.0, Unit.PX, 127.0, Unit.PX);
		layoutPanel_1.setWidgetTopHeight(rdbtnLogin, 16.0, Unit.PX, 19.0, Unit.PX);
		
		final RadioButton rdbtnRegistrazione = new RadioButton("group", "Registrazione");
		layoutPanel_1.add(rdbtnRegistrazione);
		layoutPanel_1.setWidgetLeftWidth(rdbtnRegistrazione, 52.0, Unit.PX, 127.0, Unit.PX);
		layoutPanel_1.setWidgetTopHeight(rdbtnRegistrazione, 108.0, Unit.PX, 19.0, Unit.PX);
		
		rdbtnLogin.setValue(true);
		btnRegistra.setEnabled(!rdbtnLogin.getValue());
		textBox.setEnabled(!rdbtnLogin.getValue());
		
		// button event
		btnRegistra.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				String mitt = textBox.getText();
				sendRegistrationService.sendMail(mitt, new AsyncCallback<String>() {

					@Override
					public void onFailure(Throwable caught) {
						
						// display error
						Window.alert("Unable to send data to server!");
					}

					@Override
					public void onSuccess(String result) {
						
						// display success
						Window.confirm(result);
					}
				});
			}
		});
		
		btnLogin.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				loginService.checkUser(textNickname.getText(), passwordTextBox.getText(), new AsyncCallback<Boolean>() {

					@Override
					public void onFailure(Throwable caught) {
						// display error
						Window.alert("Invio dati fallito.");
					}

					@Override
					public void onSuccess(Boolean result) {
						if(result)
						{
							// display success
							//Window.confirm("Login avvenuto con successo.");
							
							Window.open("/home.html",  "_self", "");
						}
						else
						{
							// display error
							Window.alert("Nickname o password non corrette.");
							textNickname.setText("");
							passwordTextBox.setText("");
						}						
					}
				});
			}
			
		});
		
		rdbtnRegistrazione.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				btnRegistra.setEnabled(rdbtnRegistrazione.getValue());
				textBox.setEnabled(rdbtnRegistrazione.getValue());	
				
				textNickname.setEnabled(!rdbtnRegistrazione.getValue());
				btnLogin.setEnabled(!rdbtnRegistrazione.getValue());
				passwordTextBox.setEnabled(!rdbtnRegistrazione.getValue());
			}
			
		});
		
		rdbtnLogin.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				btnRegistra.setEnabled(!rdbtnLogin.getValue());
				textBox.setEnabled(!rdbtnLogin.getValue());	
				
				textNickname.setEnabled(rdbtnLogin.getValue());
				btnLogin.setEnabled(rdbtnLogin.getValue());
				passwordTextBox.setEnabled(rdbtnLogin.getValue());
			}
			
		});

	}
}
