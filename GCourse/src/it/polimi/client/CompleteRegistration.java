package it.polimi.client;





import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.PasswordTextBox;


public class CompleteRegistration implements EntryPoint {

	private final SessionHandlerAsync sessionHandlerService = GWT.create(SessionHandler.class);
	private final LoadStoreServiceAsync loadStoreService = GWT.create(LoadStoreService.class);
	String email;
	
	@Override
	public void onModuleLoad() {
		
		
		// add widgets to layout panel
		RootLayoutPanel rootLayoutPanel = RootLayoutPanel.get();
		
		LayoutPanel layoutPanel = new LayoutPanel();
		rootLayoutPanel.add(layoutPanel);
		rootLayoutPanel.setWidgetLeftWidth(layoutPanel, 0.0, Unit.PX, 469.0, Unit.PX);
		rootLayoutPanel.setWidgetTopHeight(layoutPanel, 90.0, Unit.PX, 233.0, Unit.PX);
		
		Label lblNome = new Label("Nickname:");
		layoutPanel.add(lblNome);
		layoutPanel.setWidgetLeftWidth(lblNome, 142.0, Unit.PX, 68.0, Unit.PX);
		layoutPanel.setWidgetTopHeight(lblNome, 5.0, Unit.PX, 18.0, Unit.PX);
		
		final RadioButton rdbtnStudente = new RadioButton("radio", "Studente");
		rdbtnStudente.setValue(true);
		layoutPanel.add(rdbtnStudente);
		layoutPanel.setWidgetLeftWidth(rdbtnStudente, 216.0, Unit.PX, 129.0, Unit.PX);
		layoutPanel.setWidgetTopHeight(rdbtnStudente, 97.0, Unit.PX, 19.0, Unit.PX);
		
		final RadioButton rdbtnDocente = new RadioButton("radio", "Docente");
		rdbtnDocente.setValue(false);
		layoutPanel.add(rdbtnDocente);
		layoutPanel.setWidgetLeftWidth(rdbtnDocente, 216.0, Unit.PX, 129.0, Unit.PX);
		layoutPanel.setWidgetTopHeight(rdbtnDocente, 122.0, Unit.PX, 19.0, Unit.PX);
		
		final TextBox name = new TextBox();
		layoutPanel.add(name);
		layoutPanel.setWidgetLeftWidth(name, 216.0, Unit.PX, 129.0, Unit.PX);
		layoutPanel.setWidgetTopHeight(name, 0.0, Unit.PX, 23.0, Unit.PX);
		
		Button btnProsegui = new Button("Prosegui");
		layoutPanel.add(btnProsegui);
		layoutPanel.setWidgetLeftWidth(btnProsegui, 263.0, Unit.PX, 82.0, Unit.PX);
		layoutPanel.setWidgetTopHeight(btnProsegui, 157.0, Unit.PX, 22.0, Unit.PX);
		
		final PasswordTextBox passwordTextBox = new PasswordTextBox();
		layoutPanel.add(passwordTextBox);
		layoutPanel.setWidgetLeftWidth(passwordTextBox, 216.0, Unit.PX, 129.0, Unit.PX);
		layoutPanel.setWidgetTopHeight(passwordTextBox, 29.0, Unit.PX, 23.0, Unit.PX);
		
		Label lblPassword = new Label("Password:");
		layoutPanel.add(lblPassword);
		layoutPanel.setWidgetLeftWidth(lblPassword, 142.0, Unit.PX, 65.0, Unit.PX);
		layoutPanel.setWidgetTopHeight(lblPassword, 34.0, Unit.PX, 18.0, Unit.PX);
		
		final PasswordTextBox passwordCheckTextBox = new PasswordTextBox();
		layoutPanel.add(passwordCheckTextBox);
		layoutPanel.setWidgetLeftWidth(passwordCheckTextBox, 216.0, Unit.PX, 129.0, Unit.PX);
		layoutPanel.setWidgetTopHeight(passwordCheckTextBox, 58.0, Unit.PX, 23.0, Unit.PX);
		
		Label lblConfermaPassword = new Label("Conferma password:");
		layoutPanel.add(lblConfermaPassword);
		layoutPanel.setWidgetTopHeight(lblConfermaPassword, 58.0, Unit.PX, 23.0, Unit.PX);
		layoutPanel.setWidgetLeftWidth(lblConfermaPassword, 76.0, Unit.PX, 134.0, Unit.PX);
		
		final Label lblSessione = new Label("Account email: ");
		rootLayoutPanel.add(lblSessione);
		rootLayoutPanel.setWidgetLeftWidth(lblSessione, 59.0, Unit.PX, 367.0, Unit.PX);
		rootLayoutPanel.setWidgetTopHeight(lblSessione, 62.0, Unit.PX, 18.0, Unit.PX);
	
		rdbtnDocente.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event){
			}
		});
		
		rdbtnStudente.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event){
			}
		});
		
		sessionHandlerService.getSessionEmail(new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				
				// display error
				Window.alert("Session error!");
			}

			@Override
			public void onSuccess(String e) {
				email = e;
				lblSessione.setText(lblSessione.getText() + email);	
			}
		});
		
		btnProsegui.addClickHandler(new ClickHandler() {
			String nickname;
			String pwd;
			
			@Override
			public void onClick(ClickEvent event){
				pwd = passwordTextBox.getText();
				String pwdCheck = passwordCheckTextBox.getText();
				nickname = name.getText();
				
				if(pwd.equals(pwdCheck))
				{
					loadStoreService.updateUser(email, nickname, pwd , rdbtnDocente.getValue(), new AsyncCallback<String>() {

						@Override
						public void onFailure(Throwable caught) {
							// display error
							Window.alert("Store error!");
						}

						@Override
						public void onSuccess(String result) {
							if(rdbtnDocente.getValue())	
								Window.open("twitter.html",  "_self", "");
							else
								Window.open("home.html",  "_self", "");
						}
					});
				}
				else
				{
					Window.alert("Password non corrispondenti.");
					passwordTextBox.setText("");
					passwordCheckTextBox.setText("");
				}			
			}
		});
	}
}