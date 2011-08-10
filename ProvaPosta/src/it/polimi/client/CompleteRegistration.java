package it.polimi.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.SimpleRadioButton;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Hidden;

public class CompleteRegistration implements EntryPoint {

	@Override
	public void onModuleLoad() {
		
		// add widgets to layout panel
		RootLayoutPanel rootLayoutPanel = RootLayoutPanel.get();
		
		LayoutPanel layoutPanel = new LayoutPanel();
		rootLayoutPanel.add(layoutPanel);
		rootLayoutPanel.setWidgetLeftWidth(layoutPanel, 90.0, Unit.PX, 337.0, Unit.PX);
		rootLayoutPanel.setWidgetTopHeight(layoutPanel, 90.0, Unit.PX, 186.0, Unit.PX);
		
		Label lblNome = new Label("Nome:");
		layoutPanel.add(lblNome);
		layoutPanel.setWidgetLeftWidth(lblNome, 13.0, Unit.PX, 50.0, Unit.PX);
		layoutPanel.setWidgetTopHeight(lblNome, 12.0, Unit.PX, 18.0, Unit.PX);
		
		RadioButton rdbtnStudente = new RadioButton("radio", "Studente");
		layoutPanel.add(rdbtnStudente);
		layoutPanel.setWidgetLeftWidth(rdbtnStudente, 64.0, Unit.PX, 129.0, Unit.PX);
		layoutPanel.setWidgetTopHeight(rdbtnStudente, 57.0, Unit.PX, 19.0, Unit.PX);
		
		RadioButton rdbtnDocente = new RadioButton("radio", "Docente");
		layoutPanel.add(rdbtnDocente);
		layoutPanel.setWidgetLeftWidth(rdbtnDocente, 64.0, Unit.PX, 129.0, Unit.PX);
		layoutPanel.setWidgetTopHeight(rdbtnDocente, 82.0, Unit.PX, 19.0, Unit.PX);
		
		CheckBox chckbxCollegareAccountTwitter = new CheckBox("Collegare account Twitter");
		layoutPanel.add(chckbxCollegareAccountTwitter);
		layoutPanel.setWidgetLeftWidth(chckbxCollegareAccountTwitter, 14.0, Unit.PX, 256.0, Unit.PX);
		layoutPanel.setWidgetTopHeight(chckbxCollegareAccountTwitter, 107.0, Unit.PX, 19.0, Unit.PX);
		
		TextBox name = new TextBox();
		layoutPanel.add(name);
		layoutPanel.setWidgetLeftWidth(name, 64.0, Unit.PX, 129.0, Unit.PX);
		layoutPanel.setWidgetTopHeight(name, 12.0, Unit.PX, 23.0, Unit.PX);
		
		

	}
}