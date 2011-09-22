package it.polimi.client;


import java.util.List;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DecoratedStackPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase.TextAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Home implements EntryPoint {

	private final SessionHandlerAsync sessionHandlerService = GWT.create(SessionHandler.class);
	private final LoadStoreServiceAsync loadStoreService = GWT.create(LoadStoreService.class);
	private final TwitterServiceAsync twitterService = GWT.create(TwitterService.class);
	private final SitesServiceAsync sitesService = GWT.create(SitesService.class);
	private final CalendarServiceAsync calendarService = GWT.create(CalendarService.class);
	
	boolean isProfessor;

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
						String strGestisciCorsi = "Gestisci i corsi a cui sei iscritto";
						String strAggiungiCorso;
						if(isProfessor){
							strAggiungiCorso = "Crea un corso";
						}
						else{
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
						
						final DecoratedStackPanel corsiPanel = new DecoratedStackPanel();
						corsi.setWidget(corsiPanel);
						corsiPanel.setStyleName("gwt-StackPanel");
						corsiPanel.setSize("100%", "100%");
						
						
												
						if(!isProfessor)					
						{
							final VerticalPanel gestisciCorsi = new VerticalPanel();
							corsiPanel.add(gestisciCorsi, strGestisciCorsi, false);
							gestisciCorsi.setSize("100%", "100%");
							
							
							
							final ListBox listCorsi = new ListBox(true);
						
							loadStoreService.getAttendedCourses(email, new AsyncCallback<List<String>>(){
								@Override
								public void onFailure(Throwable caught) {
									Window.alert("Errore nel recupero della lista dei corsi");
								}

								@Override
								public void onSuccess(List<String> courses) {
									if(courses.size()>0){
										for(final String key : courses)
										{
											loadStoreService.getCourseNameProfessor(key, new AsyncCallback<String>(){
												@Override
												public void onFailure(Throwable caught) {
													Window.alert("Errore nel recupero della lista dei corsi");
												}
				
												@Override
												public void onSuccess(String name) {
													listCorsi.addItem(name, key);
													
												}
											});	
											
										}
											
									}
								}
							});	
							
							Button btnGestisci = new Button("Gestisci");
							btnGestisci.addClickHandler(new ClickHandler() {
								@Override
								public void onClick(ClickEvent event) {
									if(!isProfessor){
										gestisciCorsi.clear();
										int index = listCorsi.getSelectedIndex();
										final String course = listCorsi.getItemText(index);
										final String key = listCorsi.getValue(index);
										
										final Label corso = new Label("Corso: " +course);
										final Label nota = new Label("Decidere quali contenuti ricevere:");
										final CheckBox lecture = new CheckBox("Appunti");
										final CheckBox exercise = new CheckBox("Esercizi");
										final Button confirm = new Button("Conferma");
										final Button delete = new Button("Cancellati");
										final Button download = new Button("Scarica il materiale gia' presente");
										
										loadStoreService.getCourseSettings(key, email, "lecture", new AsyncCallback<Boolean>(){
												@Override
												public void onFailure(Throwable caught) {
													Window.alert("Errore nella lettura dei settings");
												}
				
												@Override
												public void onSuccess(Boolean value) {
													lecture.setValue(value);													
												}
											});
										loadStoreService.getCourseSettings(key, email, "exercise", new AsyncCallback<Boolean>(){
											@Override
											public void onFailure(Throwable caught) {
												Window.alert("Errore nella lettura dei settings");
											}
			
											@Override
											public void onSuccess(Boolean value) {
												exercise.setValue(value);												
											}
										});									
										
										gestisciCorsi.add(corso);
										gestisciCorsi.add(nota);
										gestisciCorsi.add(lecture);
										gestisciCorsi.add(exercise);
										gestisciCorsi.add(confirm);
										gestisciCorsi.add(download);
										gestisciCorsi.add(delete);
										
										
										delete.addClickHandler(new ClickHandler(){
											@Override
											public void onClick(ClickEvent event){
												loadStoreService.deleteFromCourse(key,email,new AsyncCallback<String>(){
													@Override
													public void onFailure(Throwable caught) {
														Window.alert("Errore nella lettura dei settings");
													}
					
													@Override
													public void onSuccess(String value) {
														Window.open("/home.html", "_self", "");											
													}
												});	
											}
										});
										confirm.addClickHandler(new ClickHandler() {
											@Override
											public void onClick(ClickEvent event) {
												loadStoreService.storeCourseSettings(key, email, lecture.getValue(), exercise.getValue(), new AsyncCallback<String>(){
													@Override
													public void onFailure(Throwable caught) {
														Window.alert("Errore nella lettura dei settings");
													}
					
													@Override
													public void onSuccess(String value) {
														Window.open("/home.html", "_self", "");											
													}
												});													
											}
										});
										final String name = course.substring(0,course.indexOf("-")).trim();
										//scaricare materiale gia' presente.
										loadStoreService.getCourseProfessor(key, new AsyncCallback<String>(){
													@Override
													public void onFailure(Throwable caught) {
														Window.alert("RPC error");
													}
					
													@Override
													public void onSuccess(final String profEmail) {
														download.addClickHandler(new ClickHandler() {
															@Override
															public void onClick(ClickEvent event) {
																loadStoreService.storeCourseSettings(key, email, lecture.getValue(), exercise.getValue(), new AsyncCallback<String>(){
																	@Override
																	public void onFailure(Throwable caught) {
																		Window.alert("Errore nella lettura dei settings");
																	}
									
																	@Override
																	public void onSuccess(String value) {			
																	}
																});	
																sitesService.pushContentToStudents(profEmail, name, new AsyncCallback<String>(){
																	@Override
																	public void onFailure(Throwable caught) {
																		Window.alert("RPC error");
																	}
									
																	@Override
																	public void onSuccess(String value) {
																		if(value.equals("ok"))
																		{
																			Window.alert("Aggiornamento eseguito correttamente.");
																			Window.open("/home.html", "_self", "");
																		}
																			
																	}
																});
																								
															}
														});								
													}
												});
										
										
									}
								}
							});
							gestisciCorsi.add(listCorsi);
							gestisciCorsi.add(btnGestisci);
						}
						
						
						
						final VerticalPanel aggiungiCorso = new VerticalPanel();
						corsiPanel.add(aggiungiCorso, strAggiungiCorso, false);
						aggiungiCorso.setSize("100%", "100%");		
						
						if(isProfessor){						
							loadStoreService.getTaughtCourses(email, new AsyncCallback<List<String>>(){
								@Override
								public void onFailure(Throwable caught) {
									Window.alert("Errore nel recupero della lista dei corsi");
								}

								@Override
								public void onSuccess(List<String> courses) {
									if(courses.size()>0){
										VerticalPanel listaCorsi = new VerticalPanel();
										corsiPanel.add(listaCorsi, "Lista Corsi", false);
										listaCorsi.setSize("100%", "100%");
										
										HorizontalPanel listaCorsiPanel = new HorizontalPanel();
										listaCorsi.add(listaCorsiPanel);
										final ListBox listCorsi = new ListBox(true);
										listaCorsi.add(listCorsi);
										
										for(final String name : courses)
										{
											listCorsi.addItem(name);
										}
										listCorsi.setSelectedIndex(0);
										listCorsi.setVisibleItemCount(6);
									}
								}
							});
							
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
								@Override
								public void onClick(ClickEvent event) {
									//create the course page
									sitesService.createNewPage(email, tbNome.getText(), taDescrizione.getText(), new AsyncCallback<String>(){
										@Override
										public void onFailure(Throwable caught) {
											Window.alert("Errore nella creazione della pagina");
										}

										@Override
										public void onSuccess(String result) {
											Window.alert("Pagina creata: " + result);											
										}										
									});		
									//TODO:create the calendar
									calendarService.createCalendar(email, tbNome.getText(), taDescrizione.getText(),new AsyncCallback<String>(){
										@Override
										public void onFailure(Throwable caught) {										
										}

										@Override
										public void onSuccess(String calendarId) {
											//store the new course
											loadStoreService.storeNewCourse(email, tbNome.getText(), taDescrizione.getText(), calendarId, new AsyncCallback<String>(){
												@Override
												public void onFailure(Throwable caught) {
													Window.alert("Errore nel salvataggio del corso");
												}

												@Override
												public void onSuccess(String result) {
													Window.open("/home.html", "_self", "");
												}
											});									
										}										
									});		
				        		    
								}
							});
							btnCorsoPanel.add(btnCreaCorso);
						}
						else{
							final ListBox listCorsi = new ListBox(true);
							aggiungiCorso.add(listCorsi);
							
							loadStoreService.getNotAttendedCourses(email, new AsyncCallback<List<String>>(){
								@Override
								public void onFailure(Throwable caught) {
									Window.alert("Errore nel recupero della lista dei corsi");
								}

								@Override
								public void onSuccess(List<String> courses) {
									if(courses.size()>0){
										for(final String key : courses)
										{
											loadStoreService.getCourseNameProfessor(key, new AsyncCallback<String>(){
												@Override
												public void onFailure(Throwable caught) {
													Window.alert("Errore nel recupero della lista dei corsi");
												}
				
												@Override
												public void onSuccess(String name) {
													listCorsi.addItem(name, key);
												}
											});	
											
										}
											
									}
								}
							});
							listCorsi.setSelectedIndex(0);
							listCorsi.setVisibleItemCount(6);			
						
							Button btnIscriviti = new Button("Iscriviti");
							btnIscriviti.addClickHandler(new ClickHandler() {
								@Override
								public void onClick(ClickEvent event) {
									int index = listCorsi.getSelectedIndex();
									final String key = listCorsi.getValue(index);
									final String course = listCorsi.getItemText(index);
									loadStoreService.addStudentToCourse(email, key, new AsyncCallback<String>(){
										@Override
										public void onFailure(Throwable caught) {
											Window.alert("Errore nell' iscrizione ai corsi");
										}
										@Override
										public void onSuccess(String result) {
											Window.alert("Iscrizione al corso: " + course + " avvenuta con successo.");											
										}
									});
									
									
									
									final String name = course.substring(0,course.indexOf("-")).trim();
									final String userEmail = course.substring(course.indexOf("-")+1).trim();
									//TODO: iscrizione al calendar del corso.
									calendarService.subscribeCalendar(email, userEmail, key, new AsyncCallback<String>(){
										@Override
										public void onFailure(Throwable caught) {
											Window.alert("Errore RPC");
										}
										@Override
										public void onSuccess(String result) {
											if(result.equals("ok"))
												Window.alert("Calendario creato.");											
										}
									});
									loadStoreService.getCourseDescription(name, userEmail, new AsyncCallback<String>(){
										@Override
										public void onFailure(Throwable caught) {
											Window.alert("RPC error.");
										}
										@Override
										public void onSuccess(String descr) {
											String corso = course.substring(0, course.indexOf("-"));
											corso = corso.trim();
											sitesService.createNewPage(email, corso, descr, new AsyncCallback<String>(){
												@Override
												public void onFailure(Throwable caught) {													
												}
				
												@Override
												public void onSuccess(String result) {	
													Window.open("/home.html", "_self", "");
												}
											});
										}
									});
										
									
								}
							});
							aggiungiCorso.add(btnIscriviti);
							
							/*Inizio parte COLO*/
							
							Button btnGetContent = new Button("Prendi Contenuto");
							btnGetContent.addClickHandler(new ClickHandler() {
								@Override
								public void onClick(ClickEvent event) {
									int index = listCorsi.getSelectedIndex();
									final String key = listCorsi.getValue(index);
									final String course = listCorsi.getItemText(index);
									final String nameCourse = course.substring(0,course.indexOf("-")).trim();
									final String teacherEmail = course.substring(course.indexOf("-")+1).trim();
									sitesService.listSiteContent(teacherEmail, nameCourse,  new AsyncCallback<List<String>>(){
										@Override
										public void onFailure(Throwable caught) {													
										}
		
										@Override
										public void onSuccess(List<String> result) {	
											aggiungiCorso.clear();
											aggiungiCorso.setSize("100%", "100%");	
											final ListBox listTitoli = new ListBox(false);
											String temp,title,content;
											
											for(int cont=0;cont<result.size();cont++){
												temp=result.get(cont);
												title=temp.substring(temp.indexOf("<t>")+3, temp.indexOf("</t>"));
												content=temp.substring(temp.indexOf("<c>")+3, temp.indexOf("</c>"));
												listTitoli.addItem(title,content);
											}
											listTitoli.setSelectedIndex(0);
											listTitoli.setVisibleItemCount(6);	
											aggiungiCorso.add(listTitoli);

											Button btnCopyContent = new Button("Copia Contenuto");
											btnCopyContent.addClickHandler(new ClickHandler() {
												@Override
												public void onClick(ClickEvent event) {
													int index = listTitoli.getSelectedIndex();
													final String content = listTitoli.getValue(index);
													final String title = listTitoli.getItemText(index);
														loadStoreService.getCourseDescription(nameCourse,teacherEmail,new AsyncCallback<String>(){
														@Override
														public void onFailure(Throwable caught) {
															Window.alert("RPC error.");
														}
														@Override
														public void onSuccess(String descr) {
															String corso = course.substring(0, course.indexOf("-"));
															corso = corso.trim();
															sitesService.createNewPage(email, nameCourse, descr, new AsyncCallback<String>(){
																@Override
																public void onFailure(Throwable caught) {													
																}
								
																@Override
																public void onSuccess(String result) {	
																	sitesService.createNewPage(email,title,content,nameCourse , new AsyncCallback<String>(){
																		@Override
																		public void onFailure(Throwable caught) {
																			Window.alert("Errore nell'elaborazione dell'URL");
																		}

																		@Override
																		public void onSuccess(String result) {
																			Window.alert("Creazione avvenuta con successo.");

																			Window.open("/home.html", "_self", "");
																		}
																	});
																}
															});
														}
													});


												}
											});
											aggiungiCorso.add(btnCopyContent);
											
										}
									});
								}
							});

							aggiungiCorso.add(btnGetContent);
							/*Fine parte COLO*/
							
							
						}	
						if(isProfessor)
						{
							VerticalPanel twitter = new VerticalPanel();
							panel.add(twitter, "Twitter", false);
							twitter.setSize("100%", "100%");
							
							HorizontalPanel horizontalPanel = new HorizontalPanel();
							twitter.add(horizontalPanel);
							
							InlineLabel nlnlblConsentiAGcourse = new InlineLabel("Consenti a GCourse l'accesso al tuo account Twitter");
							horizontalPanel.add(nlnlblConsentiAGcourse);
							
							Button btnTwitGrant = new Button("Vai");
							btnTwitGrant.addClickHandler(new ClickHandler() {
								@Override
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
								@Override
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
							
							
							//tab di informazioni per inviare le mail
							VerticalPanel help = new VerticalPanel();
							panel.add(help, "Help", false);
							help.setSize("100%", "100%");
							
							HorizontalPanel horPanel = new HorizontalPanel();
							help.add(horPanel);
							
							InlineLabel lblHelp = new InlineLabel("Istruzioni per creare contenuto: ");
							Button btnHelp = new Button("Visualizza");
							btnHelp.addClickHandler(new ClickHandler() {
								@Override
								public void onClick(ClickEvent event) {
									Window.open("/help.html","_blank", "");
								}
							});
							horPanel.add(lblHelp);
							horPanel.add(btnHelp);	
						}
						panel.selectTab(0);	
						
						//tab per il logout
						VerticalPanel logout = new VerticalPanel();
						panel.add(logout, "Logout", false);
						logout.setSize("100%", "100%");
						
						HorizontalPanel horPanel2 = new HorizontalPanel();
						logout.add(horPanel2);						
						
						Button btnOut = new Button("Effettua il logout");
						btnOut.addClickHandler(new ClickHandler() {
							@Override
							public void onClick(ClickEvent event) {
								Window.open("/register.html","_self", "");
							}
						});
						
						horPanel2.add(btnOut);	
					}
				});
			}
		});
	}
}
