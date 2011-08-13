package it.polimi.sites.client;

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
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;

public class GSitesEntryPoint implements EntryPoint {

	private final GSitesServiceAsync pageCreatorService = GWT.create(GSitesService.class);
	
	@Override
	public void onModuleLoad() {
		
		// add widgets to layout panel
		RootLayoutPanel rootLayoutPanel = RootLayoutPanel.get();
		
		LayoutPanel layoutPanel = new LayoutPanel();
		rootLayoutPanel.add(layoutPanel);
		rootLayoutPanel.setWidgetLeftRight(layoutPanel, 59.0, Unit.PX, 359.0, Unit.PX);
		rootLayoutPanel.setWidgetTopHeight(layoutPanel, 81.0, Unit.PX, 340.0, Unit.PX);
		
		Label lblSiteName = new Label("Site name:");
		lblSiteName.addStyleName("style");
		layoutPanel.add(lblSiteName);
		layoutPanel.setWidgetLeftWidth(lblSiteName, 28.0, Unit.PX, 98.0, Unit.PX);
		layoutPanel.setWidgetTopBottom(lblSiteName, 25.0, Unit.PX, 30.0, Unit.PX);
		
		final TextBox tbSiteName = new TextBox();
		layoutPanel.add(tbSiteName);
		layoutPanel.setWidgetLeftWidth(tbSiteName, 121.0, Unit.PX, 191.0, Unit.PX);
		layoutPanel.setWidgetTopHeight(tbSiteName, 25.0, Unit.PX, 23.0, Unit.PX);
		
		Label lblPageTitle = new Label("PageTitle:");
		lblPageTitle.addStyleName("style");
		layoutPanel.add(lblPageTitle);
		layoutPanel.setWidgetLeftWidth(lblPageTitle, 28.0, Unit.PX, 98.0, Unit.PX);
		layoutPanel.setWidgetTopBottom(lblPageTitle, 70.0, Unit.PX, 30.0, Unit.PX);
		
		final TextBox tbPageTitle = new TextBox();
		layoutPanel.add(tbPageTitle);
		layoutPanel.setWidgetLeftWidth(tbPageTitle, 121.0, Unit.PX, 191.0, Unit.PX);
		layoutPanel.setWidgetTopHeight(tbPageTitle, 70.0, Unit.PX, 23.0, Unit.PX);
		
		Label lblToken = new Label("Token:");
		lblToken.addStyleName("style");
		layoutPanel.add(lblToken);
		layoutPanel.setWidgetLeftWidth(lblToken, 28.0, Unit.PX, 98.0, Unit.PX);
		layoutPanel.setWidgetTopBottom(lblToken, 110.0, Unit.PX, 30.0, Unit.PX);
		
		final TextBox tbToken = new TextBox();
		layoutPanel.add(tbToken);
		layoutPanel.setWidgetLeftWidth(tbToken, 121.0, Unit.PX, 191.0, Unit.PX);
		layoutPanel.setWidgetTopHeight(tbToken, 110.0, Unit.PX, 23.0, Unit.PX);
		
		Label lblPageContent = new Label("PageContent:");
		lblPageContent.addStyleName("style");
		layoutPanel.add(lblPageContent);
		layoutPanel.setWidgetLeftWidth(lblPageContent, 28.0, Unit.PX, 98.0, Unit.PX);
		layoutPanel.setWidgetTopBottom(lblPageContent, 150.0, Unit.PX, 130.0, Unit.PX);
		
		final TextArea taPageContent = new TextArea();
		layoutPanel.add(taPageContent);
		layoutPanel.setWidgetLeftWidth(taPageContent, 121.0, Unit.PX, 191.0, Unit.PX);
		layoutPanel.setWidgetTopHeight(taPageContent, 150.0, Unit.PX, 123.0, Unit.PX);
		
		Button btnCrea = new Button("Crea");
		btnCrea.setText("Crea");
		layoutPanel.add(btnCrea);
		layoutPanel.setWidgetLeftWidth(btnCrea, 121.0, Unit.PX, 98.0, Unit.PX);
		layoutPanel.setWidgetTopHeight(btnCrea, 190.0, Unit.PX, 22.0, Unit.PX);
		
		btnCrea.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				String siteName = tbSiteName.getText();
				String pageTitle = tbPageTitle.getText();
				String token = tbToken.getText();
				String pageContent = taPageContent.getText();
				
				pageCreatorService.createPage(siteName, pageTitle, token, pageContent, new AsyncCallback<String>() {
			
					@Override
					public void onSuccess(String conferma) {
						Window.alert(conferma);
					}
					
					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Google Sites page creation failed");
					}
				});
			}
		});
	}

}
