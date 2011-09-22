package it.polimi.client;





import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.TextBox;


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
		rootLayoutPanel.setWidgetLeftWidth(layoutPanel, 0.0, Unit.PX, 620.0, Unit.PX);
		rootLayoutPanel.setWidgetTopHeight(layoutPanel, 0.0, Unit.PX, 465.0, Unit.PX);
		
		Label lblNome = new Label("Nickname:");
		layoutPanel.add(lblNome);
		layoutPanel.setWidgetLeftWidth(lblNome, 197.0, Unit.PX, 111.0, Unit.PX);
		layoutPanel.setWidgetTopHeight(lblNome, 92.0, Unit.PX, 18.0, Unit.PX);
		
		final RadioButton rdbtnStudente = new RadioButton("radio", "Studente");
		rdbtnStudente.setValue(true);
		layoutPanel.add(rdbtnStudente);
		layoutPanel.setWidgetLeftWidth(rdbtnStudente, 271.0, Unit.PX, 129.0, Unit.PX);
		layoutPanel.setWidgetTopHeight(rdbtnStudente, 232.0, Unit.PX, 19.0, Unit.PX);
		
		final RadioButton rdbtnDocente = new RadioButton("radio", "Docente");
		rdbtnDocente.setValue(false);
		layoutPanel.add(rdbtnDocente);
		layoutPanel.setWidgetLeftWidth(rdbtnDocente, 271.0, Unit.PX, 129.0, Unit.PX);
		layoutPanel.setWidgetTopHeight(rdbtnDocente, 257.0, Unit.PX, 19.0, Unit.PX);
		
		final TextBox name = new TextBox();
		layoutPanel.add(name);
		layoutPanel.setWidgetLeftWidth(name, 318.0, Unit.PX, 129.0, Unit.PX);
		layoutPanel.setWidgetTopHeight(name, 92.0, Unit.PX, 23.0, Unit.PX);
		
		Button btnProsegui = new Button("Prosegui");
		layoutPanel.add(btnProsegui);
		layoutPanel.setWidgetLeftWidth(btnProsegui, 318.0, Unit.PX, 82.0, Unit.PX);
		layoutPanel.setWidgetTopHeight(btnProsegui, 292.0, Unit.PX, 22.0, Unit.PX);
		
		final PasswordTextBox passwordTextBox = new PasswordTextBox();
		layoutPanel.add(passwordTextBox);
		layoutPanel.setWidgetLeftWidth(passwordTextBox, 318.0, Unit.PX, 129.0, Unit.PX);
		layoutPanel.setWidgetTopHeight(passwordTextBox, 121.0, Unit.PX, 23.0, Unit.PX);
		
		Label lblPassword = new Label("Password:");
		layoutPanel.add(lblPassword);
		layoutPanel.setWidgetLeftWidth(lblPassword, 197.0, Unit.PX, 111.0, Unit.PX);
		layoutPanel.setWidgetTopHeight(lblPassword, 121.0, Unit.PX, 18.0, Unit.PX);
		
		final PasswordTextBox passwordCheckTextBox = new PasswordTextBox();
		layoutPanel.add(passwordCheckTextBox);
		layoutPanel.setWidgetLeftWidth(passwordCheckTextBox, 318.0, Unit.PX, 129.0, Unit.PX);
		layoutPanel.setWidgetTopHeight(passwordCheckTextBox, 150.0, Unit.PX, 23.0, Unit.PX);
		
		Label lblConfermaPassword = new Label("Conferma password:");
		layoutPanel.add(lblConfermaPassword);
		layoutPanel.setWidgetTopHeight(lblConfermaPassword, 145.0, Unit.PX, 23.0, Unit.PX);
		layoutPanel.setWidgetLeftWidth(lblConfermaPassword, 131.0, Unit.PX, 177.0, Unit.PX);
		
		final Label lblSessione = new Label("Account email: ");
		layoutPanel.add(lblSessione);
		layoutPanel.setWidgetLeftWidth(lblSessione, 166.0, Unit.PX, 391.0, Unit.PX);
		layoutPanel.setWidgetTopHeight(lblSessione, 27.0, Unit.PX, 23.0, Unit.PX);
		
		final TextBox textSites = new TextBox();
		layoutPanel.add(textSites);
		layoutPanel.setWidgetLeftWidth(textSites, 318.0, Unit.PX, 129.0, Unit.PX);
		layoutPanel.setWidgetTopHeight(textSites, 179.0, Unit.PX, 23.0, Unit.PX);
		
		Label lblGoogleSitesName = new Label("Google sites name:");
		layoutPanel.add(lblGoogleSitesName);
		layoutPanel.setWidgetLeftWidth(lblGoogleSitesName, 141.0, Unit.PX, 167.0, Unit.PX);
		layoutPanel.setWidgetTopHeight(lblGoogleSitesName, 174.0, Unit.PX, 18.0, Unit.PX);
	
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
					loadStoreService.updateUser(email, nickname, pwd , textSites.getText(),rdbtnDocente.getValue(), new AsyncCallback<String>() {

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