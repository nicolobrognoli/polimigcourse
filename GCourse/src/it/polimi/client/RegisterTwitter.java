package it.polimi.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.InlineHTML;

public class RegisterTwitter implements EntryPoint {

	private final TwitterServiceAsync twitterService = GWT.create(TwitterService.class);
	private final SessionHandlerAsync sessionHandlerService = GWT.create(SessionHandler.class);

	private String email;
	private String twitterLink;
	
	@Override
	public void onModuleLoad() {
		RootLayoutPanel rootLayoutPanel = RootLayoutPanel.get();
		
		LayoutPanel layoutPanel = new LayoutPanel();
		rootLayoutPanel.add(layoutPanel);
		rootLayoutPanel.setWidgetLeftWidth(layoutPanel, 90.0, Unit.PX, 379.0, Unit.PX);
		rootLayoutPanel.setWidgetTopHeight(layoutPanel, 90.0, Unit.PX, 233.0, Unit.PX);
		
		final Label lblSessione = new Label("Account: ");
		rootLayoutPanel.add(lblSessione);
		rootLayoutPanel.setWidgetLeftWidth(lblSessione, 90.0, Unit.PX, 298.0, Unit.PX);
		rootLayoutPanel.setWidgetTopHeight(lblSessione, 66.0, Unit.PX, 18.0, Unit.PX);
		
		final CheckBox chckbxCollegareAccountTwitter = new CheckBox("Collegare account Twitter");
		chckbxCollegareAccountTwitter.setVisible(true);
		layoutPanel.add(chckbxCollegareAccountTwitter);
		layoutPanel.setWidgetLeftWidth(chckbxCollegareAccountTwitter, 20.0, Unit.PX, 256.0, Unit.PX);
		layoutPanel.setWidgetTopHeight(chckbxCollegareAccountTwitter, 10.0, Unit.PX, 19.0, Unit.PX);
		
		final InlineHTML nlnhtmlTwitterlink = new InlineHTML("");
		layoutPanel.add(nlnhtmlTwitterlink);
		layoutPanel.setWidgetLeftWidth(nlnhtmlTwitterlink, 45.0, Unit.PX, 117.0, Unit.PX);
		layoutPanel.setWidgetTopHeight(nlnhtmlTwitterlink, 35.0, Unit.PX, 18.0, Unit.PX);
		
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
		
		twitterService.getAuthUrl(new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				
				// display error
				Window.alert("Service error!");
			}

			@Override
			public void onSuccess(String result) {
				twitterLink = result;
			}
		});
		
		chckbxCollegareAccountTwitter.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event){
				if(chckbxCollegareAccountTwitter.getValue())
				{
					nlnhtmlTwitterlink.setHTML("<a href=\"" + twitterLink + "\">Collega</a>");
					nlnhtmlTwitterlink.setVisible(true);
				}
				else
				{
					nlnhtmlTwitterlink.setVisible(false);
				}
			}
		});

	}
}
