package edu.utd.cs.bdma.synset.validator.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import edu.utd.cs.bdma.synset.validator.shared.LanguageCode;
import edu.utd.cs.bdma.synset.validator.shared.SynsetEntry;
import edu.utd.cs.bdma.synset.validator.shared.Verdict;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class SynsetValidator implements EntryPoint {
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network " + "connection and try again.";

	/**
	 * Create a remote service proxy to talk to the server-side Greeting
	 * service.
	 */

	private final ValidatorServiceAsync validatorService = GWT.create(ValidatorService.class);
	SynsetEntry obj = null, cachedEntry = null;
	private String sendButtonText = "All Words are Correct";
	final PopupPanel myPopupPanel = new PopupPanel();
	Image waitingImage = new Image("http://dribbble.s3.amazonaws.com/users/4613/screenshots/911982/jar-loading.gif");

	@Override
	public void onModuleLoad() {
		// TODO Auto-generated method stub
		waitingImage.setWidth("200px");
        RootPanel.get("validatorDiv").add(waitingImage);
		validatorService.getNextForValidation(1, new MyCallBack());
	}
	
	private void reload(SynsetEntry[] result){
						// TODO Auto-generated method stub
				obj = result[0];
				RootPanel.get("validatorDiv").remove(waitingImage);
				GWT.log(""+(obj==null));
				GWT.log(""+(obj.getSets()==null));
				List<String> englishWords = obj.wordsInLanguage("en");
				VerticalPanel mainPanel = new VerticalPanel();

				Label label = new Label(obj.getConcept());

				label.setStyleName("mainWord");

				Label engLabel = new Label("Related English words:");
				engLabel.setStylePrimaryName("regulartext");
				engLabel.setStyleDependentName("bold", true);

				
				GWT.log(""+(englishWords == null));
				StringBuilder sb = new StringBuilder();
				if (englishWords != null) {
                    GWT.log("Inside");
					for (String word : englishWords) {
						sb.append(" " + word + ",");
					}
					if (sb.length() > 0)
						sb.deleteCharAt(sb.length() - 1);
					else
						sb.append("No words found in record");
				} else
					sb.append("Error null found");

				Label engWordListLabel = new Label(sb.toString());
				engWordListLabel.setStylePrimaryName("regulartext");

				Label context = new HTML("<br/>" + contextInMultiline(obj.getGloss()));
				context.setStyleName("regulartext");
				Label contextHeader = new Label("Context");
				contextHeader.setStylePrimaryName("regulartext");
				contextHeader.setStyleDependentName("bold", true);
				HorizontalPanel hPanel = new HorizontalPanel();
				hPanel.setSpacing(10);
				hPanel.add(contextHeader);
				hPanel.add(context);
				mainPanel.add(label);
				mainPanel.add(hPanel);
				mainPanel.add(engLabel);
				mainPanel.add(engWordListLabel);
				
				

				final FlexTable table = new FlexTable();
				
				VerticalPanel panel2  = new VerticalPanel();
				panel2.setSpacing(20);
				Label headLb = new Label("Related Spanish Words");
				headLb.setStyleName("mainWord");
				panel2.add(headLb);
				
                
                     
              
                
                
				
				
				
				
				
				int i = 0;
				
				if (obj.wordsInLanguage(LanguageCode.SPANISH).size() == 0){
					Label noWordLabel = new Label("No words found");
					label.setStyleName("regulartext");
					table.setWidget(1, 1, noWordLabel);
				} else {
					sendButtonText = "All Words are Correct";
					for (String s: obj.wordsInLanguage(LanguageCode.SPANISH)){
					    Label lb = new Label(s);
					    final TextBox commentTextBox = new TextBox();
					    commentTextBox.setEnabled(false);
					    final ListBox listBox = new ListBox();
		                listBox.addItem("Correct");
		                listBox.addItem("Incorrect");
		                listBox.addItem("Ambiguous");
					    lb.setStyleName("regulartext");
					    
					    listBox.addChangeHandler(new ChangeHandler() {
							
							@Override
							public void onChange(ChangeEvent event) {
								// TODO Auto-generated method stub
								commentTextBox.setEnabled(listBox.getSelectedIndex()!=0);
							}
						});
					    
					    table.setWidget(i, 1, listBox);
					    
					    table.setWidget(i, 2, commentTextBox);
					    table.setWidget(i++, 0, lb);
					}
				}

				Button sendButton = new Button("Submit");
				
	            sendButton.setStyleName("sendButton"); 
	            
	            Label newWordLabel = new Label("Add a new SPANISH word:");
	            newWordLabel.setStyleName("regulartext-bold");
	            final TextBox newWordTextBox = new TextBox();
	            
	            sendButton.addClickHandler(new ClickHandler() {
					
					@Override
					public void onClick(ClickEvent event) {
						// TODO Auto-generated method stub
						waitingImage.setVisible(true);
						validatorService.submit(getVerdicts(table, newWordTextBox, LanguageCode.SPANISH), new AsyncCallback<Void>() {

							@Override
							public void onFailure(Throwable caught) {
								// TODO Auto-generated method stub
								
							}

							@Override
							public void onSuccess(Void result) {
								// TODO Auto-generated method stub
								validatorService.getNextForValidation(1, new MyCallBack());
								waitingImage.setVisible(false);
								RootPanel.get("validatorDiv").remove(0);
								RootPanel.get("wordlistDiv").remove(0);
							}
						});
					}
				});
	            
	            
				
				
                panel2.add(table);
                panel2.add(newWordLabel);
                panel2.add(newWordTextBox);
                panel2.add(sendButton);
                
				// RootPanel.get("validatorDiv").remove(0);
               
				RootPanel.get("validatorDiv").add(new Test(obj));
				
				RootPanel.get("wordlistDiv").add(panel2);
				
				//RootPanel.get().add(new Test());
			}
		
	
	
	private class MyCallBack implements AsyncCallback<SynsetEntry[]>{

		@Override
		public void onFailure(Throwable caught) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onSuccess(SynsetEntry[] result) {
			// TODO Auto-generated method stub
			reload(result);
			
		}
		
	}

	private ArrayList<Verdict> getVerdicts(FlexTable table, TextBox newWordBox, LanguageCode langCode){
		int rows = table.getRowCount();
		ArrayList<Verdict> verdicts = new ArrayList<>();
		List<String> words = obj.wordsInLanguage(langCode);
		if (words.size()==0) 
			return verdicts;
		
		for (int i = 0; i < rows; i++){
			ListBox lb = (ListBox) table.getWidget(i, 1);
			GWT.log(obj.getConceptID());
			Verdict verdict = new Verdict();
			verdict.setComment(((TextBox)table.getWidget(i, 2)).getText());
			verdict.setConceptId(obj.getConceptID());
			verdict.setLang(langCode.getCode());
			verdict.setWordInLang(words.get(i));
			

			if (lb.getItemText(lb.getSelectedIndex()).equalsIgnoreCase("Incorrect")){
			       verdict.setVerdict(Verdict.INCORRECT);   	
			} else if (lb.getItemText(lb.getSelectedIndex()).equalsIgnoreCase("Ambiguous")){
				verdict.setVerdict(Verdict.AMBIGUOUS);
			}
			verdicts.add(verdict);
			
		}
		if (newWordBox.getText().length() != 0){ 
		String[] newWords = newWordBox.getText().split(",");
		for (String newWord: newWords){
			Verdict  verdict = new Verdict();
			verdict.setConceptId(obj.getConceptID());
			verdict.setLang(langCode.getCode());
			verdict.setVerdict(Verdict.ADDITION);
			verdict.setWordInLang(newWord);
			verdicts.add(verdict);
		}
		}
		return verdicts;

	}
		
	
	
	private String contextInMultiline(String data){
		String[] words = data.split("\"");
		
		StringBuilder sb = new StringBuilder(words[0]);
		for (int i = 1; i < words.length; i+= 2){
			sb.append("<br/>- \""+words[i].trim()+"\"");		       
		}
		return sb.toString();
	}
	



}
