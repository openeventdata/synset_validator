package edu.utd.cs.bdma.synset.validator.client;

import java.util.ArrayList;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.Heading;
import com.github.gwtbootstrap.client.ui.TextArea;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.PopupPanel.AnimationType;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.VerticalPanel;

import edu.utd.cs.bdma.synset.validator.shared.customevents.CameoWordSelectedEvent;
import edu.utd.cs.bdma.synset.validator.shared.customevents.SaveChangesEvent;
import edu.utd.cs.bdma.synset.validator.shared.entity.CameoEntry;
import edu.utd.cs.bdma.synset.validator.shared.entity.ForumMessage;;


public class CameoHeaderPanel extends Composite  {

	private static CameoHeaderPanelUiBinder uiBinder = GWT.create(CameoHeaderPanelUiBinder.class);

	private static final CameoServiceAsync cameoService = GWT.create(CameoService.class); 
	private static final ForumServiceAsync forumService = GWT.create(ForumService.class);
	
	private String cameoCode;
	
	interface CameoHeaderPanelUiBinder extends UiBinder<Widget, CameoHeaderPanel> {
	}

	public String getCameoCode() {
		return cameoCode;
	}

	@UiField
	Heading heading;
	
	@UiField
	Label summeryLabel;
	
	@UiField
	Button detailsButton;
	
	@UiField
	ListBox wordsListBox;
	
	@UiField
	HorizontalPanel horizontalPanel;
	
	@UiField
	Button saveButton;
	
	@UiField
	Button loadButton;
	
	@UiField
	CheckBox autoAdvance;
	
	@UiField
	TextBox addWordText;
	
	@UiField
	TextArea addRulesTextArea;
	
	@UiField
	HTML issueLabel;
	
	CameoPopUpPanel cameoPopUpPanel = new CameoPopUpPanel();
	
	@UiField
	HorizontalPanel newWordPanel;
	
	@UiField
	Button showPanelButton;
	
	
	CameoEntry entry;

	public CameoHeaderPanel() {
		initWidget(uiBinder.createAndBindUi(this));
		

	}
	
	public void setVisibilityOfWordPanel(boolean visibility){
		newWordPanel.setVisible(visibility);
	}
	
	public void setCode(final String cameoCode, final String defaultSelected){
		GWT.log("Inside Set()");
		if (this.cameoCode == null || !this.cameoCode.equals(cameoCode)){
			GWT.log("Insode IF");
			this.cameoCode = cameoCode;
			cameoPopUpPanel.setVisible(false);
			wordsListBox.setVisible(false);
			cameoService.getCameoInfo(cameoCode, new AsyncCallback<CameoEntry>() {
				
				@Override
				public void onSuccess(CameoEntry result) {
					// TODO Auto-generated method stub
					entry = result;

					prepareGUI();
				}
				
				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
					GWT.log("Failure fetching cameo info");
				}
			});
			
			cameoService.getRelatedWords(cameoCode, new AsyncCallback<ArrayList<String>>() {

				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onSuccess(ArrayList<String> result) {
					// TODO Auto-generated method stub
					GWT.log("Sucesss");
					wordsListBox.clear();
					for (String s: result){
						wordsListBox.addItem(s);
					}
					wordsListBox.setVisible(true);
					if (defaultSelected != null) {
						fireEvent(new CameoWordSelectedEvent(cameoCode, defaultSelected));
						setSelection(defaultSelected);
					}

					
				}
			});
			
		}
		
		else if (defaultSelected != null) {
			fireEvent(new CameoWordSelectedEvent(cameoCode, defaultSelected));
			setSelection(defaultSelected);
		}


	}
	
	
	
	public boolean advanceToNextWord(){
		if (autoAdvance.getValue()){
			if (wordsListBox.getSelectedIndex() < wordsListBox.getItemCount()-1){
				wordsListBox.setSelectedIndex(wordsListBox.getSelectedIndex()+1);
				fireEvent(new CameoWordSelectedEvent(cameoCode, wordsListBox.getSelectedItemText()));
			}
		}
		
		return autoAdvance.getValue();
	}
	
	public void setCode(String cameoCode){
		setCode(cameoCode, null);
	}
	
	
	public void setSelection(String word){
		int selectedIndex = 0;
		for (int i = 0; i < wordsListBox.getItemCount(); i++){
			if (wordsListBox.getItemText(i).equals(word)){
				selectedIndex = i;
				saveButton.setActive(true);
				break;
				
			}
		}
		wordsListBox.setSelectedIndex(selectedIndex);
	}
	
	@UiHandler("loadButton")
	public void onLoadRequest(ClickEvent e)
	{
		if (wordsListBox.getItemCount() != 0){
		    fireEvent(new CameoWordSelectedEvent(cameoCode, wordsListBox.getSelectedItemText()));
		    saveButton.setEnabled(true);
		    issueLabel.setVisible(false);
		}
	}
	
	
	
	@UiHandler("saveButton")
	public void onSaveRequest(ClickEvent e){
		GWT.log("Save Requested");
		fireEvent(new SaveChangesEvent());
	}

	protected void prepareGUI() {
		// TODO Auto-generated method stub
        horizontalPanel.getElement().getStyle().setPadding(10, Unit.PX); 
		detailsButton.addStyleDependentName("paddedLeft");
		
		heading.setText(entry.getCode()+" "+entry.getConcept());
		summeryLabel.setText(entry.getDescription());
		this.setVisible(true);
	}

	@UiHandler("addWordButton")
    public void onAddWordRequest(ClickEvent e){
		String newWord = addWordText.getText().toLowerCase();
		issueLabel.setVisible(true);
		String[] cameoRules = addRulesTextArea.getText().split("\n");
		
		if (addRulesTextArea.getText().length()==0){
			issueLabel.setHTML("<span style=\"color: red;\">Please add at-least one cameo rule.</span>");
			return;
		}
		issueLabel.setHTML("<span style=\"color: blue;\">Processing....</span>");
		cameoService.addRelatedWord(newWord, cameoRules, cameoCode, new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				issueLabel.setHTML("<span style=\"color: red;\">Error while adding related word.</span>");
			}

			@Override
			public void onSuccess(String result) {
				// TODO Auto-generated method stub
				issueLabel.setHTML("<span style=\"color: green\">Update Successful.</span>");
				wordsListBox.addItem(result);
				wordsListBox.setSelectedIndex(wordsListBox.getItemCount()-1);
				fireEvent(new CameoWordSelectedEvent(cameoCode, result));
				addWordText.setText("");
				addRulesTextArea.setText("");
				
				
			}
		});
		
		
	}
	boolean forumVisible = false ;
	
	@UiField
	Button forumButton;
	PopupPanel forumPanel = new PopupPanel();
	
	@UiHandler("forumButton")
	void loadForumMessages(ClickEvent e){
		forumPanel.clear();
		forumVisible = !forumVisible;
		if (forumVisible){
			forumButton.setText("Hide Forum Messages");
		} else {
			forumButton.setText("Show Forum Messages");
			forumPanel.setVisible(false);
			return;
		}
		forumPanel.setWidth("500px");
		forumPanel.setHeight("300px");
		final LoadingPanel loadingPanel = new LoadingPanel("Loading forum messages");
		loadingPanel.show();
		forumPanel.setAnimationType(AnimationType.ROLL_DOWN);
		//forumPanel.setWidth("50%");
		forumPanel.add(loadingPanel);
		
		forumPanel.showRelativeTo(forumButton);
		forumService.getAll(cameoCode, wordsListBox.getSelectedItemText(), new AsyncCallback<ArrayList<ForumMessage>>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(ArrayList<ForumMessage> result) {
				// TODO Auto-generated method stub
				ScrollPanel scrollPanel = new ScrollPanel();
				scrollPanel.setHeight("300px");
				VerticalPanel vPanel = new VerticalPanel();
				vPanel.setWidth("100%");
				vPanel.add(new HTML("Number of Messages: "+result.size()));
				for (ForumMessage message: result){
					vPanel.add(new ForumMessageWidget(message));
					vPanel.add(new HTML("<div style=\"width: 100%; height: 2px; background: #63631c; overflow: hidden;\">"));
				}
				
				vPanel.add(new HTML("<h5>Enter your comment</h5>"));
				final TextArea commentArea = new TextArea();
				commentArea.setVisibleLines(5);
				commentArea.setWidth("100%");
				vPanel.add(commentArea);
				vPanel.add(new Button("Add Comment", new ClickHandler() {
					
					@Override
					public void onClick(ClickEvent event) {
						// TODO Auto-generated method stub
						GWT.log(wordsListBox.getSelectedItemText());
						forumService.add(commentArea.getText(),  
								cameoCode, wordsListBox.getSelectedItemText(), 
								new AsyncCallback<ForumMessage>() {

									@Override
									public void onFailure(Throwable caught) {
										// TODO Auto-generated method stub
										
									}

									@Override
									public void onSuccess(ForumMessage result) {
										// TODO Auto-generated method stub
										forumVisible = !forumVisible;
										
										loadForumMessages(null);
									}
						});
					}
				}));
				forumPanel.remove(loadingPanel);
				loadingPanel.hide();
				scrollPanel.add(vPanel);
				forumPanel.add(scrollPanel);
				
				
			}
		});		
	}
	

	@UiHandler("detailsButton")
	void onClick(ClickEvent e) {
		cameoPopUpPanel.setCemeoEntry(entry);
		cameoPopUpPanel.getAndShow(entry.getCode(), detailsButton);
	}

	public CameoEntry getEntry() {
		return entry;
	}
	

}
