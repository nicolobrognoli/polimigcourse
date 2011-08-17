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
		rootLayoutPanel.setWidgetLeftWidth(layoutPanel, 90.0, Unit.PX, 379.0, Unit.PX);
		rootLayoutPanel.setWidgetTopHeight(layoutPanel, 90.0, Unit.PX, 233.0, Unit.PX);
		
		Label lblNome = new Label("Nickname:");
		layoutPanel.add(lblNome);
		layoutPanel.setWidgetLeftWidth(lblNome, 13.0, Unit.PX, 68.0, Unit.PX);
		layoutPanel.setWidgetTopHeight(lblNome, 12.0, Unit.PX, 18.0, Unit.PX);
		
		final RadioButton rdbtnStudente = new RadioButton("radio", "Studente");
		rdbtnStudente.setValue(true);
		layoutPanel.add(rdbtnStudente);
		layoutPanel.setWidgetLeftWidth(rdbtnStudente, 64.0, Unit.PX, 129.0, Unit.PX);
		layoutPanel.setWidgetTopHeight(rdbtnStudente, 57.0, Unit.PX, 19.0, Unit.PX);
		
		final RadioButton rdbtnDocente = new RadioButton("radio", "Docente");
		rdbtnDocente.setValue(false);
		layoutPanel.add(rdbtnDocente);
		layoutPanel.setWidgetLeftWidth(rdbtnDocente, 64.0, Unit.PX, 129.0, Unit.PX);
		layoutPanel.setWidgetTopHeight(rdbtnDocente, 82.0, Unit.PX, 19.0, Unit.PX);
		
		final TextBox name = new TextBox();
		layoutPanel.add(name);
		layoutPanel.setWidgetLeftWidth(name, 84.0, Unit.PX, 129.0, Unit.PX);
		layoutPanel.setWidgetTopHeight(name, 7.0, Unit.PX, 23.0, Unit.PX);
		
		Button btnProsegui = new Button("Prosegui");
		layoutPanel.add(btnProsegui);
		layoutPanel.setWidgetLeftWidth(btnProsegui, 111.0, Unit.PX, 82.0, Unit.PX);
		layoutPanel.setWidgetTopHeight(btnProsegui, 117.0, Unit.PX, 22.0, Unit.PX);
		
		final Label lblSessione = new Label("Account: ");
		rootLayoutPanel.add(lblSessione);
		rootLayoutPanel.setWidgetLeftWidth(lblSessione, 90.0, Unit.PX, 298.0, Unit.PX);
		rootLayoutPanel.setWidgetTopHeight(lblSessione, 66.0, Unit.PX, 18.0, Unit.PX);
	
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

			boolean professor;
			String nickname;
			@Override
			public void onClick(ClickEvent event){
				
				if(rdbtnDocente.getValue())
					professor = true;
				else
					professor = false;
				
				nickname = name.getText();
				loadStoreService.updateUser(email, nickname, professor, new AsyncCallback<String>() {

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
		});
	}
}