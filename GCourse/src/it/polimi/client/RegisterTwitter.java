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
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.HTML;

public class RegisterTwitter implements EntryPoint {

	private final TwitterServiceAsync twitterService = GWT.create(TwitterService.class);
	private final SessionHandlerAsync sessionHandlerService = GWT.create(SessionHandler.class);

	private String email;
	private String twitterLink;
	
	@Override
	public void onModuleLoad() {
		RootLayoutPanel rootLayoutPanel = RootLayoutPanel.get();
		
		final Label lblSessione = new Label("Account email: ");
		rootLayoutPanel.add(lblSessione);
		rootLayoutPanel.setWidgetLeftWidth(lblSessione, 90.0, Unit.PX, 342.0, Unit.PX);
		rootLayoutPanel.setWidgetTopHeight(lblSessione, 66.0, Unit.PX, 18.0, Unit.PX);
		
		final CheckBox chckbxCollegareAccountTwitter = new CheckBox("Collegare account Twitter");
		rootLayoutPanel.add(chckbxCollegareAccountTwitter);
		rootLayoutPanel.setWidgetLeftWidth(chckbxCollegareAccountTwitter, 90.0, Unit.PX, 194.0, Unit.PX);
		rootLayoutPanel.setWidgetTopHeight(chckbxCollegareAccountTwitter, 95.0, Unit.PX, 23.0, Unit.PX);
		
		final InlineHTML nlnhtmlTwitterlink = new InlineHTML("");
		rootLayoutPanel.add(nlnhtmlTwitterlink);
		rootLayoutPanel.setWidgetLeftWidth(nlnhtmlTwitterlink, 120.0, Unit.PX, 194.0, Unit.PX);
		rootLayoutPanel.setWidgetTopHeight(nlnhtmlTwitterlink, 124.0, Unit.PX, 23.0, Unit.PX);
		
		final HTML htmlProseguiDirettamente = new HTML("<a href=\"home.html\">Prosegui direttamente alla home page</a>", true);
		rootLayoutPanel.add(htmlProseguiDirettamente);
		rootLayoutPanel.setWidgetLeftWidth(htmlProseguiDirettamente, 121.0, Unit.PX, 246.0, Unit.PX);
		rootLayoutPanel.setWidgetTopHeight(htmlProseguiDirettamente, 124.0, Unit.PX, 18.0, Unit.PX);
		chckbxCollegareAccountTwitter.setVisible(true);
		
		chckbxCollegareAccountTwitter.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event){
				if(chckbxCollegareAccountTwitter.getValue())
				{
					nlnhtmlTwitterlink.setHTML("<a href=\"" + twitterLink + "\">Collega</a>");
					nlnhtmlTwitterlink.setVisible(true);
					htmlProseguiDirettamente.setVisible(false);
				}
				else
				{
					nlnhtmlTwitterlink.setVisible(false);
					htmlProseguiDirettamente.setVisible(true);
				}
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

	}
}
