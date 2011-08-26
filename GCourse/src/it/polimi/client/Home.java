package it.polimi.client;

import java.util.List;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
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
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ClickEvent;

public class Home implements EntryPoint {

	private final SessionHandlerAsync sessionHandlerService = GWT.create(SessionHandler.class);
	private final LoadStoreServiceAsync loadStoreService = GWT.create(LoadStoreService.class);
	private final TwitterServiceAsync twitterService = GWT.create(TwitterService.class);
	
	boolean isProfessor;
	@SuppressWarnings("deprecation")
	@Override
	public void onModuleLoad() {
		sessionHandlerService.getSessionEmail(new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Per visualizzare questa pagina effettua il login");
				Window.open("/register.html", "_self", "");
			}

			@Override
			public void onSuccess(final String email) {
		
				loadStoreService.isProfessor(email, new AsyncCallback<Boolean>() {

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Errore: rieffettua il login");
						Window.open("/register.html", "_self", "");
					}

					@Override
					public void onSuccess(Boolean p) {
						isProfessor = p;
						String strGestisciCorsi;
						String strAggiungiCorso;
						if(isProfessor){
							strGestisciCorsi = "Gestisci i tuoi corsi";
							strAggiungiCorso = "Crea un corso";
						}
						else{
							strGestisciCorsi = "Gestisci i corsi a cui sei iscritto";
							strAggiungiCorso = "Iscriviti ad un corso";
						}
						RootLayoutPanel rootLayoutPanel = RootLayoutPanel.get();
						rootLayoutPanel.setSize("100%","100%");
						TabPanel panel = new TabPanel();
						panel.setSize("100%","100%");						
						rootLayoutPanel.add(panel);
						
						SimplePanel corsi = new SimplePanel();
						panel.add(corsi, "Corsi", false);
						corsi.setSize("100%", "100%");
						
						DecoratedStackPanel corsiPanel = new DecoratedStackPanel();
						corsi.setWidget(corsiPanel);
						corsiPanel.setStyleName("gwt-StackPanel");
						corsiPanel.setSize("100%", "100%");
						
						final VerticalPanel gestisciCorsi = new VerticalPanel();
						corsiPanel.add(gestisciCorsi, strGestisciCorsi, false);
						gestisciCorsi.setSize("100%", "100%");
												
						if(isProfessor)
							loadStoreService.getTaughtCourses(email, new AsyncCallback<List<String>>(){
								@Override
								public void onFailure(Throwable caught) {
									Window.alert("Errore nel recupero della lista dei corsi");
								}

								@Override
								public void onSuccess(List<String> courses) {
									if(courses.size()>0){
										RadioButton first = new RadioButton("listaCorsi", courses.get(0));
										gestisciCorsi.add(first);
										first.setValue(true);
										if(courses.size()>1){
											for(int i = 1; i<courses.size(); i++)
												gestisciCorsi.add(new RadioButton("listaCorsi", courses.get(i)));											
										}
									}
								}
							});
						else
							loadStoreService.getAttendedCourses(email, new AsyncCallback<List<String>>(){
								@Override
								public void onFailure(Throwable caught) {
									Window.alert("Errore nel recupero della lista dei corsi");
								}

								@Override
								public void onSuccess(List<String> courses) {
									if(courses.size()>0){
										RadioButton first = new RadioButton("listaCorsi", courses.get(0));
										gestisciCorsi.add(first);
										first.setValue(true);
										if(courses.size()>1){
											for(int i = 1; i<courses.size(); i++)
												gestisciCorsi.add(new RadioButton("listaCorsi", courses.get(i)));											
										}
									}
								}
							});								
						
						Button btnGestisci = new Button("Gestisci");
						btnGestisci.addClickHandler(new ClickHandler() {
							public void onClick(ClickEvent event) {
								if(isProfessor){
									//TODO: Gestisci il corso selezionato di cui sei il professore
								}
								else{
									//TODO: Gestisci i corsi selezionati di cui sei studente
								}
							}
						});
						gestisciCorsi.add(btnGestisci);
						
						VerticalPanel aggiungiCorso = new VerticalPanel();
						corsiPanel.add(aggiungiCorso, strAggiungiCorso, false);
						aggiungiCorso.setSize("100%", "100%");		
						
						if(isProfessor){
							HorizontalPanel nomeCorsoPanel = new HorizontalPanel();
							aggiungiCorso.add(nomeCorsoPanel);
							Label lblNome = new Label("Nome del corso: ");
							final TextBox tbNome = new TextBox();
							nomeCorsoPanel.add(lblNome);
							nomeCorsoPanel.add(tbNome);
							tbNome.setWidth("314px");
							
							HorizontalPanel descrizioneCorsoPanel = new HorizontalPanel();
							aggiungiCorso.add(descrizioneCorsoPanel);
							Label lblDescrizione = new Label("Descrizione: ");
							final TextArea taDescrizione = new TextArea();
							taDescrizione.setCharacterWidth(40);
							taDescrizione.setVisibleLines(3);
							taDescrizione.setAlignment(TextAlignment.JUSTIFY);
							descrizioneCorsoPanel.add(lblDescrizione);
							descrizioneCorsoPanel.add(taDescrizione);
							
							HorizontalPanel btnCorsoPanel = new HorizontalPanel();
							aggiungiCorso.add(btnCorsoPanel);
							Button btnCreaCorso = new Button("Crea");
							btnCreaCorso.addClickHandler(new ClickHandler() {
								public void onClick(ClickEvent event) {
									loadStoreService.storeNewCourse(email, tbNome.getText(), taDescrizione.getText(), new AsyncCallback<String>(){
										@Override
										public void onFailure(Throwable caught) {
											Window.alert("Errore nel salvataggio del corso");
										}

										@Override
										public void onSuccess(String result) {
											Window.alert(result);
										}
									});
								}
							});
							btnCorsoPanel.add(btnCreaCorso);
						}
						else{
							final ListBox listCorsi = new ListBox(true);
							aggiungiCorso.add(listCorsi);
							//TODO: sistemare...
							loadStoreService.getAllCourses(new AsyncCallback<List<String>>(){
								@Override
								public void onFailure(Throwable caught) {
									Window.alert("Errore nel recupero della lista dei corsi");
								}

								@Override
								public void onSuccess(List<String> courses) {
									if(courses.size()>0)
										for(String course : courses)
											listCorsi.addItem(course);
								}
							});
							listCorsi.setSelectedIndex(0);
							listCorsi.setVisibleItemCount(6);			
							
							Button btnIscriviti = new Button("Iscriviti");
							btnIscriviti.addClickHandler(new ClickHandler() {
								public void onClick(ClickEvent event) {
									int index = listCorsi.getSelectedIndex();
									final String course = listCorsi.getItemText(index);
									loadStoreService.addStudentToCourse(email, course, new AsyncCallback<String>(){
								@Override
								public void onFailure(Throwable caught) {
									Window.alert("Errore nell' iscrizione ai corsi");
								}

								@Override
								public void onSuccess(String result) {
									Window.alert("Iscrizione al corso: " + course + " avvenuta con successo.");
								}
							});
								}
							});
							aggiungiCorso.add(btnIscriviti);
						}	
						
						VerticalPanel twitter = new VerticalPanel();
						panel.add(twitter, "Twitter", false);
						twitter.setSize("100%", "100%");
						
						HorizontalPanel horizontalPanel = new HorizontalPanel();
						twitter.add(horizontalPanel);
						
						InlineLabel nlnlblConsentiAGcourse = new InlineLabel("Consenti a GCourse l'accesso al tuo account Twitter");
						horizontalPanel.add(nlnlblConsentiAGcourse);
						
						Button btnTwitGrant = new Button("Vai");
						btnTwitGrant.addClickHandler(new ClickHandler() {
							public void onClick(ClickEvent event) {
								twitterService.getAuthUrl(new AsyncCallback<String>(){
								@Override
								public void onFailure(Throwable caught) {
									Window.alert("Errore nell'elaborazione dell'URL");
								}

								@Override
								public void onSuccess(String result) {
									Window.open(result, "_self", "");
								}
							});
							}
						});
						horizontalPanel.add(btnTwitGrant);
						
						HorizontalPanel horizontalPanel_1 = new HorizontalPanel();
						twitter.add(horizontalPanel_1);
						
						InlineLabel nlnlblScollegaIlTuo = new InlineLabel("Scollega il tuo account Twitter da GCourse");
						horizontalPanel_1.add(nlnlblScollegaIlTuo);
						
						Button btnTwitDeny = new Button("Vai");
						btnTwitDeny.addClickHandler(new ClickHandler() {
							public void onClick(ClickEvent event) {
								loadStoreService.deleteTwitterTokens(email, new AsyncCallback<String>(){
								@Override
								public void onFailure(Throwable caught) {
									Window.alert("Errore nella cancellazione dei permessi");
								}

								@Override
								public void onSuccess(String result) {
									Window.alert(result);
								}
							});
							}
						});
						horizontalPanel_1.add(btnTwitDeny);
						panel.selectTab(0);
					}
				});
			}
		});
	}
}
