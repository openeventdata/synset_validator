package edu.utd.cs.bdma.synset.validator.client;

import com.github.gwtbootstrap.client.ui.Alert;
import com.github.gwtbootstrap.client.ui.AlertBlock;
import com.github.gwtbootstrap.client.ui.Brand;
import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.Label;
import com.github.gwtbootstrap.client.ui.ResponsiveNavbar;
import com.github.gwtbootstrap.client.ui.constants.AlertType;
import com.github.gwtbootstrap.client.ui.constants.ButtonType;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.ClosingEvent;
import com.google.gwt.user.client.Window.ClosingHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.PopupPanel.AnimationType;
import com.google.gwt.user.client.ui.RootPanel;

import edu.utd.cs.bdma.synset.validator.shared.customevents.CameoCodeSelectedEvent;
import edu.utd.cs.bdma.synset.validator.shared.customevents.CameoCodeSelectedEventHandler;
import edu.utd.cs.bdma.synset.validator.shared.customevents.CameoWordSelectedEvent;
import edu.utd.cs.bdma.synset.validator.shared.customevents.CameoWordSelectedEventHandler;
import edu.utd.cs.bdma.synset.validator.shared.customevents.LoginSuccessEvent;
import edu.utd.cs.bdma.synset.validator.shared.customevents.LoginSuceessEventHandler;
import edu.utd.cs.bdma.synset.validator.shared.customevents.SaveChangesEvent;
import edu.utd.cs.bdma.synset.validator.shared.customevents.SaveChangesEventHandler;
import edu.utd.cs.bdma.synset.validator.shared.entity.UpdatedInfo;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class CameoDevelopment implements EntryPoint, CameoCodeSelectedEventHandler {

	static SubmissionServiceAsync submitter = GWT.create(SubmissionService.class);
	private CameoHeaderPanel headerPanel = new CameoHeaderPanel();
	private LogInPanel logInPanel = new LogInPanel();
	private ResponsiveNavbar navBar = new ResponsiveNavbar();
	private SynsetDisplayPanel synsetPanel;
	private CameoRulesPanel rulesPanel;
	private Button saveButton ;
	@Override
	public void onModuleLoad() {
		
		final Button button = new Button("Cameo List");
		saveButton = new Button("Save Changes");
		saveButton.setType(ButtonType.PRIMARY);
		saveButton.setBlock(true);
		saveButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				saveChanges();
			}
		});
		saveButton.setVisible(false);
		final Button historyButton = new Button("History");
		historyButton.setType(ButtonType.LINK);
		
		rulesPanel = new CameoRulesPanel();
		button.setType(ButtonType.LINK);
		final Button logoutButton = new Button("Log out");
		logoutButton.setType(ButtonType.LINK);
		final HTML userLabel = new HTML();
		
		final HistoryPanel historyPanel = new HistoryPanel();
		
		final CameoSummeryPanel panel = new CameoSummeryPanel();
		//final LogInPanel logInPanel = new LogInPanel();
        logoutButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				logInPanel.logout();
			}
		});
        logInPanel.addHandler(new LoginSuceessEventHandler() {
			
			@Override
			public void onLoginSucceed(LoginSuccessEvent event) {
				// TODO Auto-generated method stub
				userLabel.setHTML("<b>"+logInPanel.getUserFullName()+"</b>");
				GWT.log("Found Username");
				
				
			}
		}, LoginSuccessEvent.TYPE);
        
        logInPanel.checkLogin();
        
		logInPanel.setGlassEnabled(true);
		logInPanel.setAnimationEnabled(true);
		logInPanel.setAnimationType(AnimationType.ROLL_DOWN);
		//logInPanel.center();
		
		//ResponsiveNavbar navBar = new ResponsiveNavbar();
		
		navBar.add(new Brand("Cameo Development"));
		navBar.add(button);
		navBar.add(historyButton);
		navBar.add(userLabel);
	    navBar.add(logoutButton);
	    userLabel.setStyleName("rightAlign");
	    logoutButton.setStyleName("rightAlign");
		//AlertBlock alertBlock = new AlertBlock("User login successful");
		synsetPanel = new SynsetDisplayPanel();
		
		panel.addHandler(new CameoCodeSelectedEventHandler() {
			
			@Override
			public void onCodeReceived(CameoCodeSelectedEvent e) {
				// TODO Auto-generated method stub
				GWT.log("DONE");
				headerPanel.setCode(e.getCode());
				saveButton.setVisible(false);
				rulesPanel.setVisible(false);
				synsetPanel.displayContents(false);
				History.newItem(e.getCode(), false);
			}
		}, CameoCodeSelectedEvent.TYPE);
		
		
		
		headerPanel.addHandler(new CameoWordSelectedEventHandler() {
			
			@Override
			public void onWordReceived(CameoWordSelectedEvent e) {
				// TODO Auto-generated method stub
				GWT.log(e.getCode()+" "+e.getWord());
				rulesPanel.setInfo(e.getCode(), e.getWord());
				saveButton.setVisible(true);
				History.newItem(e.getCode()+"#"+e.getWord(), false);
			}
			
		}, CameoWordSelectedEvent.TYPE);
		
       headerPanel.addHandler(new CameoWordSelectedEventHandler() {
			
			@Override
			public void onWordReceived(CameoWordSelectedEvent e) {
				// TODO Auto-generated method stub
				GWT.log(e.getCode()+" "+e.getWord());
				synsetPanel.setCameoCode(e.getCode());
				synsetPanel.setWord(e.getWord());
				saveButton.setVisible(true);
			}
		}, CameoWordSelectedEvent.TYPE);
	    button.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				GWT.log(""+panel.getOffsetWidth());
				panel.setAnimationType(AnimationType.ROLL_DOWN);
				panel.setAnimationEnabled(true);
				panel.setGlassEnabled(true);
				panel.getAndShow(button);
				//panel.setPopupPosition(button.getAbsoluteLeft()-panel.getOffsetWidth()+button.getOffsetWidth(), button.getAbsoluteTop()+button.getOffsetHeight());
			}
		});
	    
	    historyButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				GWT.log(""+panel.getOffsetWidth());
				historyPanel.setAnimationType(AnimationType.ROLL_DOWN);
				historyPanel.setAnimationEnabled(true);
				historyPanel.setGlassEnabled(true);
				historyPanel.getAndShow(button);
			}
		});
	    
	    headerPanel.addHandler(new SaveChangesEventHandler() {
			
			@Override
			public void onSaveChanges(SaveChangesEvent event) {
				// TODO Auto-generated method stub
				saveChanges();
			}
		}, SaveChangesEvent.TYPE);
	    
	    
	    
	    historyPanel.addHandler(new CameoWordSelectedEventHandler() {
			
			@Override
			public void onWordReceived(CameoWordSelectedEvent e) {
				// TODO Auto-generated method stub
				headerPanel.setCode(e.getCode(), e.getWord());
//				rulesPanel.setInfo(e.getCode(), e.getWord());
//				synsetPanel.setCameoCode(e.getCode());
//				synsetPanel.setWord(e.getWord());
//				saveButton.setVisible(true);
				//headerPanel.setSelection(e.getWord());
				History.newItem(e.getCode()+"#"+e.getWord(), false);
			}
		}, CameoWordSelectedEvent.TYPE);
	    
//	    Window.addWindowClosingHandler(new ClosingHandler() {
//			
//			@Override
//			public void onWindowClosing(ClosingEvent event) {
//				// TODO Auto-generated method stub
//				Window.confirm("Reloading the page will take you back to homepage. Ok to proceed?");
//			}
//		});
	    
	    
	    //RootPanel.get().add(alertBlock);
	    RootPanel.get("NavBar").add(navBar);
	    headerPanel.setVisible(false);
	    
	    RootPanel.get("Header").add(headerPanel);
	    RootPanel.get("CameoRules").add(rulesPanel);
	    RootPanel.get("Synset").add(synsetPanel);
	    RootPanel.get("Footer").add(saveButton);
	    
	    
	    History.addValueChangeHandler(new ValueChangeHandler<String>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				// TODO Auto-generated method stub
				String eventValue = event.getValue();
				
				if (eventValue.contains("#")){
					String[] parts = eventValue.split("#");
					GWT.log(parts[0]+" "+parts[1]);
					//panel.fireEvent(new CameoCodeSelectedEvent(parts[0]));
					headerPanel.setCode(parts[0], parts[1]);
					headerPanel.setSelection(parts[1]);
					
				} else {
					panel.fireEvent(new CameoCodeSelectedEvent(eventValue));
					
                  					
				}
				
			}
		});
	    
	    if ("".equals(History.getToken())) {


		} else {

			History.fireCurrentHistoryState();

		}
	    
	    
   }

	public void saveChanges(){
		UpdatedInfo info = new UpdatedInfo();
		saveButton.setEnabled(false);
		synsetPanel.onSave();
		//info.setFeedbackSWNewEntry(synsetPanel.getFeedbackForNESynWord());
		info.setCameoSelectedSynsets(synsetPanel.getSelections());
		GWT.log("Success retrieving data");
		info.setFeedbackOnSynsetWords(synsetPanel.getSelFeedbacks());
		info.setNewWords(synsetPanel.getNewWords());
		info.setSynsetEntryWithWords(synsetPanel.getNewSynsets());
		info.setComment(synsetPanel.getComment());
		
		GWT.log(synsetPanel.getSelFeedbacks().toString());
		info.setVerdictsOnRules(rulesPanel.getVerdicts());
		final String cameoCode = headerPanel.getCameoCode();
		final String word = synsetPanel.getWord();
		submitter.submit(info, headerPanel.getCameoCode(), synsetPanel.getWord(), new AsyncCallback<Void>() {
			
			@Override
			public void onSuccess(Void result) {
				// TODO Auto-generated method stub
				synsetPanel.displayContents(false);
				rulesPanel.setVisible(false);
				final PopupPanel notifyPanel = new PopupPanel();
				
				notifyPanel.setWidth(""+(int)Window.getClientWidth()*0.95+"px");
				notifyPanel.setAnimationEnabled(true);
				notifyPanel.setAnimationType(AnimationType.ROLL_DOWN);
				final AlertBlock success = new AlertBlock(AlertType.SUCCESS);
				
				success.setText("Submission Successful for Cameo Code "+cameoCode+" and word \""+word+"\".");
				notifyPanel.add(success);
				
				Timer timer = new Timer(){
					public void run() {
						notifyPanel.setVisible(false);
					}
				};
				notifyPanel.showRelativeTo(headerPanel);
				notifyPanel.addStyleDependentName("noborder");
				timer.schedule(2000);
				saveButton.setVisible(false);
				saveButton.setEnabled(true);
				headerPanel.advanceToNextWord();
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				saveButton.setEnabled(true);
			}
		});
	}
	
	

	@Override
	public void onCodeReceived(CameoCodeSelectedEvent e) {
		// TODO Auto-generated method stub
		GWT.log("Event found "+e.getCode());
	}
	
			
		
	
	
	
	



}
