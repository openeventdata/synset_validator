package edu.utd.cs.bdma.synset.validator.client;

import java.util.ArrayList;
import java.util.HashMap;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.Heading;
import com.github.gwtbootstrap.client.ui.TextArea;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.github.gwtbootstrap.client.ui.constants.ButtonType;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.utd.cs.bdma.synset.validator.shared.entity.CameoRule;
import edu.utd.cs.bdma.synset.validator.shared.entity.CameoTranslatedRule;
import edu.utd.cs.bdma.synset.validator.shared.entity.UpdatedInfo;
import edu.utd.cs.bdma.synset.validator.shared.entity.VerdictOnRule;

public class CameoRulesPanel extends Composite{

	private static CameoRulesPanelUiBinder uiBinder = GWT.create(CameoRulesPanelUiBinder.class);

	private static final CameoServiceAsync cameoService = GWT.create(CameoService.class);
	
	private static final SubmissionServiceAsync submitService = GWT.create(SubmissionService.class);
	
	private UpdatedInfo savedInfo;
	
	
	
	private String cameoCode;
	private String word;
	private ArrayList<CameoRule> rules;
	PopupPanel editPopup = new PopupPanel();
	//HashMap<String, VerdictOnRule> verdicts = new HashMap<>();
	ArrayList<VerdictOnRule> verdicts = new ArrayList<>();
	HashMap<Long, VerdictOnRule> verdictMap = new HashMap<Long, VerdictOnRule>();
	
	@UiField
	FlexTable rulesTable;
	
	@UiField
	Heading sectionHeader;
	
	@UiField
	HorizontalPanel addRulePanel;
	
	@UiField
	TextBox ruleTextBox;
	
	@UiField
	TextBox codeTextBox;
	
	interface CameoRulesPanelUiBinder extends UiBinder<Widget, CameoRulesPanel> {
	}

	public CameoRulesPanel() {
		initWidget(uiBinder.createAndBindUi(this));
		this.setVisible(false);
	}


	public CameoRulesPanel(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));
		this.setVisible(false);
	}
	
	public void setInfo(String cCode, String w){
		this.cameoCode = cCode;
		this.word = w;
		
		cameoService.getCameoRules(this.cameoCode, this.word, new AsyncCallback<ArrayList<CameoRule>>() {
			
			@Override
			public void onSuccess(ArrayList<CameoRule> result) {
				// TODO Auto-generated method stub
				rules = result;
				submitService.retrieve(cameoCode, word, new AsyncCallback<UpdatedInfo>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onSuccess(UpdatedInfo result) {
						// TODO Auto-generated method stub
						ArrayList<VerdictOnRule> vs = result.getVerdictsOnRules();
						GWT.log("Saved data is "+ vs.size());
						for (VerdictOnRule v: vs){
							verdictMap.put(v.getIdTranslatedRule(), v);
						}
					}
				});
				designTable();
				codeTextBox.setPlaceholder("default code: "+cameoCode);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	public void setSavedInfo(UpdatedInfo savedInfo) {
		this.savedInfo = savedInfo;
	}
	
	public void designTable(){
		this.setVisible(false);
		sectionHeader.setVisible(false);
		verdictMap.clear();
		rulesTable.clear();
		rulesTable.setStyleName("infoTable");
		for (int i = 0; i < rules.size(); i++){
			rulesTable.setWidget(i, 0, new Label(rules.get(i).getRuleText()));
			final Button button = new Button("Edit");
			button.setType(ButtonType.LINK);
			final CameoRule tempRule = rules.get(i);
			button.addClickHandler(new ClickHandler() {
				CameoRule rule = tempRule;
				@Override
				public void onClick(ClickEvent event) {
					// TODO Auto-generated method stub
					GWT.log(rule.getRuleText());
					cameoService.getCameoTranslation(rule, "es", new AsyncCallback<ArrayList<CameoTranslatedRule>>() {

						@Override
						public void onFailure(Throwable caught) {
							// TODO Auto-generated method stub
							
						}

						@Override
						public void onSuccess(ArrayList<CameoTranslatedRule> result) {
							// TODO Auto-generated method stub
							showPopup(button, tempRule, result);
						}


					});
				}
			});
			rulesTable.setWidget(i, 1, button);
		}
		sectionHeader.setVisible(true);
		this.setVisible(true);
	}
	
	
	
	private void showPopup(Button button, final CameoRule rule, final ArrayList<CameoTranslatedRule> tRules) {
		// TODO Auto-generated method stub
		editPopup.clear();
		editPopup.setWidth("400px");
		
		VerticalPanel vPanel = new VerticalPanel();
		vPanel.add(new HTML("<h5>English Rule :</h5>   " + rule.getRuleText()));

		final TextArea commentTA = new TextArea();
		vPanel.add(new HTML("<h5>Translated Rules</h5>"));
		
		FlexTable rulesTable = new FlexTable();
		
		if (tRules.size() > 0){
			int i = 0; 
			for (CameoTranslatedRule tRule: tRules){
				rulesTable.setWidget(i, 0, new HTML(tRule.getText()));
				rulesTable.setWidget(i++, 1, new HTML(tRule.getComment()));
			}		
		} else {
			rulesTable.setWidget(0, 0, new HTML("<b>No Translation Found </b>"));
		}
				
//	    if (tRules.size() > 0)
//	    {
//	    	vPanel.add(new HTML(tRules.get(0).getText()));
//	    	VerdictOnRule vr = verdictMap.get(tRules.get(0).getId());
//		    String comment = (vr == null)? "": vr.getComment();
//		    GWT.log("Saved data is "+ verdictMap.size());
//		    commentTA.setText(comment);
//		    commentTA.setVisibleLines(7);
//		    commentTA.setCharacterWidth(70);
//	    }
	    final TextBox ruleTB = new TextBox();
		vPanel.add(rulesTable);
		vPanel.add(new HTML("<h5>Add a new Rule</h5>"));
		
		vPanel.add(ruleTB);
	    vPanel.add(new HTML("<h5>Comment</h5>"));
	    
	    vPanel.add(commentTA);
		
        Button saveButton = new Button("Save");
        saveButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				editPopup.hide();
				//Do Work Here
				CameoTranslatedRule tRule = new CameoTranslatedRule();
				tRule.setRuleID(rule.getId());
				tRule.setText(ruleTB.getText());
				tRule.setComment(commentTA.getText());
				
				cameoService.addTranslatedRule(tRule, new AsyncCallback<CameoTranslatedRule>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onSuccess(CameoTranslatedRule result) {
						// TODO Auto-generated method stub
						GWT.log("Success");
					}
				});
//				String text = commentTA.getText();
//				VerdictOnRule verdict = new VerdictOnRule(VerdictOnRule.CORRECT, text);
//				verdict.setIdTranslatedRule(tRules.get(0).getId());
//				verdictMap.put(verdict.getIdTranslatedRule(), verdict);
				
			}
		});
        
        
        
        Button cancelButton = new Button("Cancel");
		cancelButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				editPopup.hide();
			}
		});
        //vPanel.add(table);
        HorizontalPanel buttonPanel = new HorizontalPanel();
        buttonPanel.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);
        vPanel.add(buttonPanel);
        
		editPopup.add(vPanel);
	  
		editPopup.showRelativeTo(button);
		
		
		
		
	}
	
	@UiHandler("addRuleButton")
	void addCodeButtonClicked(ClickEvent e){
		String ruleText = ruleTextBox.getText();
		if (ruleText.length()==0 || !ruleText.contains("*")){
			return;
		} else {
			String code = codeTextBox.getText();
			if (code.length()==0) code = this.cameoCode;
			CameoRule rule = new CameoRule();
			
			for (CameoRule r: rules){
				if (r.equals(rule)){
					return;
				}
			}
			rule.setCameoCode(code);
			rule.setRuleText(ruleText);
			rule.setWord(word);
			
			cameoService.addCameoRule(rule, new AsyncCallback<CameoRule>() {

				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onSuccess(CameoRule result) {
					// TODO Auto-generated method stub
					if (result.getCameoCode().equals(cameoCode)){
						rules.add(result);
						ruleTextBox.setText("");
						codeTextBox.setText("");
						designTable();
					}
				}
			});
		}
	}
	
	void onAddRequest(){
		this.setVisible(false);
//		if (verdicts.size() > 0){
//			cameoService.addVerdictsOnRule(verdicts, new AsyncCallback<Integer>() {
//
//				@Override
//				public void onFailure(Throwable caught) {
//					// TODO Auto-generated method stub
//					
//				}
//
//				@Override
//				public void onSuccess(Integer result) {
//					// TODO Auto-generated method stub
//					GWT.log("Successs adding verdicts");
//				}
//			});
//		}
	}
	
	public ArrayList<VerdictOnRule> getVerdicts() {
		GWT.log("Number of comment : "+verdictMap.size());
		return new ArrayList<>(verdictMap.values());
	}


}
