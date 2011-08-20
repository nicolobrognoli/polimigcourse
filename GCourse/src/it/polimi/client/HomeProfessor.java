package it.polimi.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.DecoratedStackPanel;
import com.google.gwt.user.client.ui.ValuePicker;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.text.client.IntegerRenderer;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import it.polimi.server.data.UserPO;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Grid;

public class HomeProfessor implements EntryPoint {
	private FlowPanel autorizzazioni;

	@SuppressWarnings("deprecation")
	@Override
	public void onModuleLoad() {
		RootLayoutPanel rootLayoutPanel = RootLayoutPanel.get();
		rootLayoutPanel.setWidth("100%");
		rootLayoutPanel.setHeight("100%");
		FlowPanel corsi;
						
						TabPanel panel = new TabPanel();
						rootLayoutPanel.add(panel);
						panel.setAnimationEnabled(true);
						
						corsi = new FlowPanel();
						panel.add(corsi, "Corsi");
						corsi.setWidth("100%");
						corsi.setHeight("100%");
						
						DecoratedStackPanel azionePanel = new DecoratedStackPanel();
						corsi.add(azionePanel);
						
						FlowPanel aggiungiCorso = new FlowPanel();
						azionePanel.add(aggiungiCorso, "Aggiungi un corso", false);
						aggiungiCorso.setSize("100%", "100%");
						
						InlineLabel nlnlblNomeDelCorso = new InlineLabel("Nome del corso: ");
						aggiungiCorso.add(nlnlblNomeDelCorso);
						
						final TextBox tbNomeCorso = new TextBox();
						aggiungiCorso.add(tbNomeCorso);
						
						final Button btnAggiungi = new Button("Aggiungi");
						aggiungiCorso.add(btnAggiungi);
						
						FlowPanel gestisciCorsi = new FlowPanel();
						azionePanel.add(gestisciCorsi, "Gestisci i corsi esistenti", false);
						gestisciCorsi.setSize("100%", "100%");
						
						FlowPanel listaCorsi = new FlowPanel();
						gestisciCorsi.add(listaCorsi);
						
						RadioButton rdbtnCorsoEsistente = new RadioButton("new name", "corso esistente 1");
						listaCorsi.add(rdbtnCorsoEsistente);
						rdbtnCorsoEsistente.setChecked(true);
						
						
						
						final Button btnVai = new Button("Vai");
						gestisciCorsi.add(btnVai);
								
								panel.selectTab(0);
								panel.addStyleName("table-center");
								panel.setSize("100%", "100%");
								
								autorizzazioni = new FlowPanel();
								panel.add(autorizzazioni, "Autorizzazioni");
								autorizzazioni.setWidth("100%");
								autorizzazioni.setHeight("100%");
								
								FlowPanel googlePanel = new FlowPanel();
								autorizzazioni.add(googlePanel);
								
								InlineLabel nlnlblConcediAGcourse = new InlineLabel("Concedi a GCourse l'accesso al tuo account Google (richiesto)");
								googlePanel.add(nlnlblConcediAGcourse);
								
								Button btnGoogle = new Button("Vai");
								googlePanel.add(btnGoogle);
								
								FlowPanel TwitterPanel = new FlowPanel();
								autorizzazioni.add(TwitterPanel);
								
								InlineLabel nlnlblConcediAGcourse_1 = new InlineLabel("Concedi a GCourse l'accesso al tuo account Twitter (opzionale)");
								TwitterPanel.add(nlnlblConcediAGcourse_1);
								
								Button btnTwitter = new Button("Vai");
								TwitterPanel.add(btnTwitter);
	}
}
