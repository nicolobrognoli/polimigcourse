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
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.TextArea;

public class HomeProfessor implements EntryPoint {

	@SuppressWarnings("deprecation")
	@Override
	public void onModuleLoad() {
		RootLayoutPanel rootLayoutPanel = RootLayoutPanel.get();
		rootLayoutPanel.setWidth("100%");
		rootLayoutPanel.setHeight("100%");
		FlowPanel corsi;
						
						TabPanel panel = new TabPanel();
						rootLayoutPanel.add(panel);
						
						corsi = new FlowPanel();
						corsi.setStyleName("gwt-TabBar .gwt-TabBarFirst");
						panel.add(corsi, "Corsi");
						corsi.setWidth("100%");
						corsi.setHeight("100%");
						
						DecoratedStackPanel corsiPanel = new DecoratedStackPanel();
						corsiPanel.setStyleName("gwt-StackPanel");
						corsi.add(corsiPanel);
						corsiPanel.setWidth("100%");
						
						FlowPanel gestisciCorsi = new FlowPanel();
						corsiPanel.add(gestisciCorsi, "Gestisci i corsi esistenti", false);
						gestisciCorsi.setSize("100%", "100%");
						
						FlowPanel listaCorsi = new FlowPanel();
						gestisciCorsi.add(listaCorsi);
						
						RadioButton rdbtnCorsoEsistente = new RadioButton("new name", "corso esistente 1");
						listaCorsi.add(rdbtnCorsoEsistente);
						rdbtnCorsoEsistente.setChecked(true);
						
						
						
						final Button btnVai = new Button("Vai");
						gestisciCorsi.add(btnVai);
						
						FlowPanel aggiungiCorso = new FlowPanel();
						corsiPanel.add(aggiungiCorso, "Aggiungi un corso", false);
						aggiungiCorso.setSize("100%", "100%");
						
						FlowPanel flowPanel_2 = new FlowPanel();
						aggiungiCorso.add(flowPanel_2);
						
						InlineLabel nlnlblNomeDelCorso = new InlineLabel("Nome del corso: ");
						flowPanel_2.add(nlnlblNomeDelCorso);
						nlnlblNomeDelCorso.setWidth("auto");
						
						final TextBox tbNomeCorso = new TextBox();
						flowPanel_2.add(tbNomeCorso);
						tbNomeCorso.setWidth("264px");
						
						FlowPanel flowPanel_3 = new FlowPanel();
						aggiungiCorso.add(flowPanel_3);
						
						InlineLabel nlnlblDescrizione = new InlineLabel("Descrizione: ");
						flowPanel_3.add(nlnlblDescrizione);
						
						TextArea textArea = new TextArea();
						flowPanel_3.add(textArea);
						textArea.setSize("295px", "130px");
						
						FlowPanel flowPanel_4 = new FlowPanel();
						aggiungiCorso.add(flowPanel_4);
						
						final Button btnAggiungi = new Button("Aggiungi");
						flowPanel_4.add(btnAggiungi);
								
								panel.selectTab(0);
								panel.addStyleName("table-center");
								panel.setSize("100%", "100%");
								
								FlowPanel twitter = new FlowPanel();
								twitter.setStyleName("gwt-TabBar .gwt-TabBarItem");
								panel.add(twitter, "Twitter");
								twitter.setWidth("100%");
								twitter.setHeight("100%");
								
								FlowPanel flowPanel = new FlowPanel();
								twitter.add(flowPanel);
								
								InlineLabel nlnlblConsentiAGcourse = new InlineLabel("Consenti a GCourse l'accesso al tuo account Twitter");
								flowPanel.add(nlnlblConsentiAGcourse);
								
								Button btnVai_1 = new Button("Vai");
								flowPanel.add(btnVai_1);
								
								FlowPanel flowPanel_1 = new FlowPanel();
								twitter.add(flowPanel_1);
								
								InlineLabel nlnlblScollegaIlTuo = new InlineLabel("Scollega il tuo account Twitter da GCourse");
								flowPanel_1.add(nlnlblScollegaIlTuo);
								
								Button btnVai_2 = new Button("Vai");
								flowPanel_1.add(btnVai_2);
	}
}
