package it.polimi.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.DecoratedStackPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.ValueBoxBase.TextAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.HorizontalPanel;

public class Home implements EntryPoint {

	boolean isProfessor = false;
	@SuppressWarnings("deprecation")
	@Override
	public void onModuleLoad() {
		RootLayoutPanel rootLayoutPanel = RootLayoutPanel.get();
		rootLayoutPanel.setWidth("100%");
		rootLayoutPanel.setHeight("100%");
		TabPanel panel = new TabPanel();
		panel.setWidth("100%");
		rootLayoutPanel.add(panel);
		
		SimplePanel corsi = new SimplePanel();
		panel.add(corsi, "Corsi", false);
		corsi.setSize("100%", "100%");
		
		DecoratedStackPanel corsiPanel = new DecoratedStackPanel();
		corsi.setWidget(corsiPanel);
		corsiPanel.setStyleName("gwt-StackPanel");
		corsiPanel.setSize("100%", "100%");
		
		VerticalPanel gestisciCorsi = new VerticalPanel();
		corsiPanel.add(gestisciCorsi, "Gestisci i corsi a cui sei iscritto", false);
		gestisciCorsi.setSize("100%", "100%");
		
		//TODO : da modificare con for che estrae dal datastore i corsi "giusti"
		RadioButton rdbtnCorsoEsistente = new RadioButton("listaCorsi", "corso esistente 1");
		gestisciCorsi.add(rdbtnCorsoEsistente);
		rdbtnCorsoEsistente.setChecked(true);		
		
		final Button btnVai = new Button("Vai");
		gestisciCorsi.add(btnVai);
		
		VerticalPanel aggiungiCorso = new VerticalPanel();
		corsiPanel.add(aggiungiCorso, "Iscriviti ad un corso", false);
		aggiungiCorso.setSize("100%", "100%");
		
		//TODO : da modificare con for che estrae dal datastore i corsi "giusti"
		ListBox listCorsi = new ListBox(true);
		aggiungiCorso.add(listCorsi);
		listCorsi.addItem("Corso1");
		listCorsi.addItem("Corso2");
		listCorsi.addItem("Corso3");
		listCorsi.addItem("Corso1");
		listCorsi.addItem("Corso2");
		listCorsi.addItem("Corso3");
		listCorsi.addItem("Corso1");
		listCorsi.addItem("Corso2");
		listCorsi.addItem("Corso3");
		listCorsi.setSelectedIndex(0);
		listCorsi.setVisibleItemCount(6);			
		
		Button btnVai_3 = new Button("Vai");
		aggiungiCorso.add(btnVai_3);
		
		VerticalPanel twitter = new VerticalPanel();
		panel.add(twitter, "Twitter", false);
		twitter.setSize("100%", "100%");
		
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		twitter.add(horizontalPanel);
		
		InlineLabel nlnlblConsentiAGcourse = new InlineLabel("Consenti a GCourse l'accesso al tuo account Twitter");
		horizontalPanel.add(nlnlblConsentiAGcourse);
		
		Button btnVai_1 = new Button("Vai");
		horizontalPanel.add(btnVai_1);
		
		HorizontalPanel horizontalPanel_1 = new HorizontalPanel();
		twitter.add(horizontalPanel_1);
		
		InlineLabel nlnlblScollegaIlTuo = new InlineLabel("Scollega il tuo account Twitter da GCourse");
		horizontalPanel_1.add(nlnlblScollegaIlTuo);
		
		Button btnVai_2 = new Button("Vai");
		horizontalPanel_1.add(btnVai_2);
	}
}
