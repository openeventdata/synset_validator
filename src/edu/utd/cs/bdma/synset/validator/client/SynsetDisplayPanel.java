package edu.utd.cs.bdma.synset.validator.client;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

import org.gwtbootstrap3.client.ui.CheckBox;
import org.gwtbootstrap3.client.ui.Radio;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.Heading;
import com.github.gwtbootstrap.client.ui.RadioButton;
import com.github.gwtbootstrap.client.ui.TextArea;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.github.gwtbootstrap.client.ui.constants.ButtonType;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.storage.client.Storage;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLTable.CellFormatter;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.utd.cs.bdma.synset.validator.shared.entity.CameoEntry;
import edu.utd.cs.bdma.synset.validator.shared.entity.CameoSelectedSynset;
import edu.utd.cs.bdma.synset.validator.shared.entity.FeedbackOnSynsetWord;
import edu.utd.cs.bdma.synset.validator.shared.entity.SynsetEntry;
import edu.utd.cs.bdma.synset.validator.shared.entity.SynsetEntryWithWords;
import edu.utd.cs.bdma.synset.validator.shared.entity.SynsetExample;
import edu.utd.cs.bdma.synset.validator.shared.entity.SynsetExampleVerdict;
import edu.utd.cs.bdma.synset.validator.shared.entity.SynsetVerdict;
import edu.utd.cs.bdma.synset.validator.shared.entity.SynsetWord;
import edu.utd.cs.bdma.synset.validator.shared.entity.SynsetWordWithFeedback;
import edu.utd.cs.bdma.synset.validator.shared.entity.UpdatedInfo;

public class SynsetDisplayPanel extends Composite {

	private final static UserInfoServiceAsync userService = GWT.create(UserInfoService.class);
	private static final WordServiceAsync wordService = GWT.create(WordService.class);
	private static SubmissionServiceAsync submitService = GWT.create(SubmissionService.class);
	private static SynsetDisplayPanelUiBinder uiBinder = GWT.create(SynsetDisplayPanelUiBinder.class);

	private ArrayList<CameoSelectedSynset> selections = new ArrayList<>();
	private ArrayList<FeedbackOnSynsetWord> selFeedbacks = new ArrayList<>();
	private ArrayList<SynsetWordWithFeedback> newWords = new ArrayList<>();

	private static ArrayList<String> countries = null;

	Storage localDB = null;

	private HashMap<Long, FeedbackOnSynsetWord> feedbacks = new HashMap<>();

	private HashMap<Long, String> newWordsToSynset = new HashMap<>();

	SynsetEntryWithWords dummyEntry;

	ArrayList<SynsetEntryWithWords> newSynsets = new ArrayList<>();

	ArrayList<SynsetWord> wordsInNewSynset = new ArrayList<>();
	
	HashMap<Long, SynsetVerdict> synsetVerdicts = new HashMap<>();
	
	ArrayList<SynsetVerdict> newVerdicts = new ArrayList<>();
	
	ArrayList<PopupLabel> examplesLabel = new ArrayList<>();
	
	HashMap<Long, SynsetExampleVerdict> exampleVerdicts = new HashMap<>();
	
	HashMap<String, ArrayList<SynsetWord>> wordsToSW = new HashMap<>();

	interface SynsetDisplayPanelUiBinder extends UiBinder<Widget, SynsetDisplayPanel> {
	}

	private String word;
	private CameoEntry cameoEntry;
	private ArrayList<SynsetEntryWithWords> entriesWithWords;

	public SynsetDisplayPanel() {
		initWidget(uiBinder.createAndBindUi(this));
		displayContents(false);
		mainPanel.add(lPanel);

	}

	public SynsetDisplayPanel(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));

	}

	private void init() {
		GWT.log("Inside init() method");
		newWordsToSynset.clear();
		newWords.clear();
		feedbacks.clear();
		SynsetEntry entry = new SynsetEntry("", "");
		dummyEntry = new SynsetEntryWithWords(entry);
		newSynsets.clear();
		synsetTable.clear();
		examplesLabel.clear();
		GWT.log("Initialization Complete");
		
		if (countries == null){
		wordService.getCountries("es", new AsyncCallback<ArrayList<String>>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onSuccess(ArrayList<String> result) {
				// TODO Auto-generated method stub
				countries = result;
				GWT.log("Initialization Complete");
			}
		});
		}
		// countries = localDB.getItem("country_list").split(",");
	}

	@UiField
	VerticalPanel mainPanel;

	@UiField
	FlexTable synsetTable;

	@UiField
	Heading synsetHeader;

	@UiField
	LoadingPanel lPanel;

	@UiField
	Button newEntryButton;
	
//	@UiField
//	Form signInForm;

	@UiField
	HTML infoMessage;
	
	@UiField
    TextArea overallComment;
	
	@UiField
	Heading commentHeader;

	TextBox newwordTextBox = new TextBox();

	PopupPanel wordEditPanel = new PopupPanel();
	private String cameoCode;

	private ArrayList<FlexTable> wordTables = new ArrayList<>();
	private ArrayList<TextBox> newWordTextBoxes = new ArrayList<>();

	public void setWord(String w) {
		if (this.word == null || !this.word.equals(w))
			this.word = w;
		lPanel.show();
		wordService.getAll(word, cameoCode, "es", new AsyncCallback<ArrayList<SynsetEntryWithWords>>() {

			@Override
			public void onSuccess(ArrayList<SynsetEntryWithWords> result) {
				// TODO Auto-generated method stub
				GWT.log("Result is " + (result == null));
				entriesWithWords = result;
				shuffle(entriesWithWords);
				for (SynsetEntryWithWords synent: entriesWithWords){
					shuffle(synent.getWords());
				}
				// GWT.log("Number of words: "+
				// result.get(0).getWords().size());
				GWT.log("Success fetching synsets");
				init();
				submitService.retrieve(cameoCode, word, new AsyncCallback<UpdatedInfo>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onSuccess(UpdatedInfo result) {
						// TODO Auto-generated method stub
						GWT.log("Result is " + (result == null));
						synsetVerdicts.clear();
						exampleVerdicts.clear();

						if (result != null) {
							ArrayList<FeedbackOnSynsetWord> fbs = result.getFeedbackOnSynsetWords();
							for (FeedbackOnSynsetWord fb : fbs) {
								feedbacks.put(fb.getIdWord(), fb);
							}
							GWT.log("" + result.getSynsetFeedbacks().size());
							
							

							for (SynsetVerdict verdict: result.getSynsetFeedbacks()){
								synsetVerdicts.put(verdict.getSynsetId(), verdict);
							}
							GWT.log("NUMBER OF VERDICTS FOR EXAMPLES: "+result.getExampleVerdicts().size());
							for (SynsetExampleVerdict v: result.getExampleVerdicts()){
								exampleVerdicts.put(v.getSynsetExampleId(), v);
							}
						}
						GWT.log("Success Fetching Submission");
						lPanel.hide();
						//Window.alert("Data Collection Complete.");
						designUIArabic();
					}
				});
				

			}

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub

			}
		});

	}
	
    public void setCameoEntry(CameoEntry cameoEntry) {
		this.cameoEntry = cameoEntry;
	} 

	private <Item> void shuffle(ArrayList<Item> list)
	{
	
		    // If running on Java 6 or older, use `new Random()` on RHS here
		    Random rnd = new Random();
		    for (int i = list.size() - 1; i > 0; i--)
		    {
		      int index = rnd.nextInt(i + 1);
		      // Simple swap
		      Item a = list.get(index);
		      list.set(index, list.get(i));
		      list.set(i, a);
		    }
	
	}
	public ArrayList<FeedbackOnSynsetWord> getSelFeedbacks() {
		return selFeedbacks;
	}

	

	public ArrayList<SynsetEntryWithWords> getNewSynsets() {
		return newSynsets;
	}

	public ArrayList<SynsetWordWithFeedback> getNewWords() {
		return newWords;
	}
	
	public String getComment(){
		return overallComment.getText();
	}

	public void setCameoCode(String cameoCode) {
		this.cameoCode = cameoCode;
	}

	public String getWord() {
		return word;
	}

	@UiHandler("newEntryButton")
	void requestAddForNewEntry(ClickEvent e) {
		showNewEntryPopUp((Widget) e.getSource(), dummyEntry);
//		SynsetEntryPanel panel = new SynsetEntryPanel();
//		panel.show((Widget) e.getSource());
	}

	private MultipleSelectListBox prepareCountryListBox(String defaultSelections) {
		MultipleSelectListBox lb = new MultipleSelectListBox("Select Countries", 20);

		lb.addOptions(countries);
		if (defaultSelections != null)
			lb.setDefaultSelections(defaultSelections, ",");
		return lb;
	}

	public void onSave() {
		
		/**
		 * For Arabic
		 */
		selFeedbacks.clear();
		newVerdicts.clear();
		//Window.alert("On save method starts");
		if (wordTables.size() > 0)
		{
			FlexTable table = wordTables.get(0);
			//Window.alert("On save method");
			//GWT.log("Iterating for insertion 2");
			
			for (int j = 1; j < table.getRowCount(); j++) {
				
				TextArea commentTA = (TextArea) table.getWidget(j, 4);
				
				GWT.log(commentTA.getText());
				FeedbackOnSynsetWord fb = new FeedbackOnSynsetWord(FeedbackOnSynsetWord.CORRECT,
						commentTA.getText());
				GWT.log("" + ((Radio) table.getWidget(j, 2)).getValue());
				if (((Radio) table.getWidget(j, 2)).getValue()) {
					GWT.log("Incorrect");
					fb.setVerdict(FeedbackOnSynsetWord.INCORRECT);
				} else if (((Radio) table.getWidget(j, 3)).getValue()) {
					GWT.log("Ambiguous");
					fb.setVerdict(FeedbackOnSynsetWord.AMBIGUOUS);
				}
				String name = ((Radio) table.getWidget(j, 2)).getName();
				
				ArrayList<SynsetWord> sws = wordsToSW.get(name.substring(5));
				ArrayList<FeedbackOnSynsetWord> tempList = new ArrayList<>();
				
				for (SynsetWord sw: sws){
					FeedbackOnSynsetWord tempFB = new FeedbackOnSynsetWord();
					tempFB.setComment(fb.getComment());
					tempFB.setCountry(fb.getCountry());
					tempFB.setIdWord(sw.getId());
					tempFB.setVerdict(fb.getVerdict());
					tempList.add(tempFB);
					GWT.log(tempFB.getVerdict());
				}
				
				selFeedbacks.addAll(tempList);
				GWT.log(fb.toString());
				
			}
		}
		//Window.alert("On save method ends");
		
		if (entriesWithWords.get(0) != null){
	       this.newWords.addAll(getAddedArabicWords(newWordTextBoxes.get(0), entriesWithWords.get(0)));
		} else {
			SynsetEntry entry = new SynsetEntry("", null);
			ArrayList<SynsetWord> words = new ArrayList<>();

			String[] newSynWords = newWordTextBoxes.get(0).getText().split(",");
			for (String w : newSynWords) {
				SynsetWord synWord = new SynsetWord(w.trim(), "ar");

				words.add(synWord);
			}
			ArrayList<SynsetExample> exms = new ArrayList<>();
						
			SynsetEntryWithWords newInfo = new SynsetEntryWithWords(entry);
			newInfo.addAllWords(words);
			newInfo.setExamples(exms);
			ArrayList<SynsetEntryWithWords> sew = new ArrayList<>();
			sew.add(newInfo);
			
			wordService.addSynset(sew, cameoCode, word, new AsyncCallback<ArrayList<SynsetEntryWithWords>>() {

				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onSuccess(ArrayList<SynsetEntryWithWords> result) {
					// TODO Auto-generated method stub
					GWT.log("New words added Sucessfully");
				}
			});
			
			
		}

		//Window.alert("On save method ends");
		
		/**
		 * for spanish
		 * 
		 */
//		/*
//		selFeedbacks.clear();
//		newVerdicts.clear();
//		ArrayList<FeedbackOnSynsetWord> feedbacks = new ArrayList<>();
//		
//		for (int i = 0; i < entriesWithWords.size(); i++) {
//			GWT.log("Iterating for insertion");
//			VerticalPanel synsetFeedbackPanel = (VerticalPanel) synsetTable.getWidget(i + newSynsets.size(), 3);
//			int result = feedbackFromSynsetPanel(synsetFeedbackPanel);
//			SynsetVerdict synsetVerdict = new SynsetVerdict();
//			synsetVerdict.setSynsetId(entriesWithWords.get(i).getEntry().getId());
//			
//			if (result == 0) {
//				GWT.log("Iterating for insertion 1");
//				synsetVerdict.setVerdict(SynsetVerdict.CORRECT);
//				
//				FlexTable table = wordTables.get(i);
//				GWT.log("Iterating for insertion 2");
//				for (int j = 1; j < table.getRowCount(); j++) {
//					TextArea commentTA = (TextArea) table.getWidget(j, 4);
//					GWT.log(commentTA.getText());
//					FeedbackOnSynsetWord fb = new FeedbackOnSynsetWord(FeedbackOnSynsetWord.CORRECT,
//							commentTA.getText());
//					GWT.log("" + ((Radio) table.getWidget(j, 2)).getValue());
//					if (((Radio) table.getWidget(j, 2)).getValue()) {
//						GWT.log("Incorrect");
//						fb.setVerdict(FeedbackOnSynsetWord.INCORRECT);
//					} else if (((Radio) table.getWidget(j, 3)).getValue()) {
//						GWT.log("Ambiguous");
//						fb.setVerdict(FeedbackOnSynsetWord.AMBIGUOUS);
//					}
//					fb.setIdWord(entriesWithWords.get(i).getWords().get(j - 1).getId());
//					// fb.setCountry(((ListBox)table.getWidget(j,
//					// 5)).getSelectedItemText());
//					feedbacks.add(fb);
//					fb.setCountry(((MultipleSelectListBox) table.getWidget(j, 5)).getSelectedItemsAsStr(","));
//					selFeedbacks.add(fb);
//					GWT.log(fb.toString());
//					
//				}
//
//				GWT.log("Iterating for insertion 3");
//				this.newWords.addAll(getAddedWords(i + newSynsets.size(), entriesWithWords.get(i)));
//				GWT.log("Iterating for insertion 4");
//			}
//
//			else if (result == 1){
//				synsetVerdict.setVerdict(SynsetVerdict.INCORRECT);
//			}
//			
//			else if (result == 2){
//				synsetVerdict.setVerdict(SynsetVerdict.AMBIGUOUS);
//
//			}
//			newVerdicts.add(synsetVerdict);
//			synsetVerdicts.put(synsetVerdict.getId(), synsetVerdict);
//		}
//		ArrayList<SynsetEntryWithWords> tempSynsets = new ArrayList<>();
//
//		
//		
//		for (PopupLabel pLabel: examplesLabel){
//			SynsetExampleVerdict sev = new SynsetExampleVerdict();
//			sev.setSynsetExampleId(pLabel.getId());
//			sev.setVerdict(pLabel.getVerdict());
//			exampleVerdicts.put(pLabel.getId(), sev);
//		}
//
//		newSynsets = tempSynsets;
//		*/
		displayContents(false);

	}
	
	private int feedbackFromSynsetPanel(VerticalPanel panel){
		for (int i = 0; i < 3; i ++){
			RadioButton button = (RadioButton) panel.getWidget(i);
			if (button.getValue()) return i;
		}
		return -1;
	}
	
	public ArrayList<SynsetVerdict> getSynsetVerdicts(){
		return newVerdicts;
	}
	
	public ArrayList<SynsetExampleVerdict> getExampleVerdicts() {
		return new ArrayList<>(exampleVerdicts.values());
	}
	
	

	private ArrayList<SynsetWordWithFeedback> getAddedWords(int rowId, int columnNo, SynsetEntryWithWords entry) {
		FlexTable ftable = (FlexTable) ((VerticalPanel) ((VerticalPanel) synsetTable.getWidget(rowId, columnNo)).getWidget(1))
				.getWidget(0);
		GWT.log("Iterating for insertion 4");
		ArrayList<SynsetWordWithFeedback> addedWords = new ArrayList<SynsetWordWithFeedback>();
		for (int j = 0; j < ftable.getRowCount(); j++) {
			String word = ((TextBox) ftable.getWidget(j, 0)).getText();
			if (word.length() > 0) {
				SynsetWord synsetWord = new SynsetWord();
				synsetWord.setWord(word);
				synsetWord.setLanguageCode("es");
				synsetWord.setIdSynsetEntry(entry.getEntry().getId());

				FeedbackOnSynsetWord feedback = new FeedbackOnSynsetWord();
				feedback.setVerdict(FeedbackOnSynsetWord.CORRECT);
				feedback.setComment(((TextArea) ftable.getWidget(j, 1)).getText());
				feedback.setCountry(((MultipleSelectListBox) ftable.getWidget(j, 2)).getSelectedItemsAsStr(","));
				SynsetWordWithFeedback sbf = new SynsetWordWithFeedback();
				sbf.setFeedback(feedback);
				sbf.setSynsetWord(synsetWord);

				addedWords.add(sbf);
			}
		}
        //Window.alert("New Words COllected");
		return addedWords;
	}
	
	private ArrayList<SynsetWordWithFeedback> getAddedArabicWords(TextBox wordBox, SynsetEntryWithWords entry) {
		
		GWT.log("Iterating for insertion 4");
		ArrayList<SynsetWordWithFeedback> addedWords = new ArrayList<SynsetWordWithFeedback>();
		String[] words = wordBox.getText().split(",");
		for (int j = 0; j < words.length; j++) {
			String word = words[j].trim();
			if (word.length() > 0) {
				SynsetWord synsetWord = new SynsetWord();
				synsetWord.setWord(word);
				synsetWord.setLanguageCode("ar");
				if (entry != null) synsetWord.setIdSynsetEntry(entry.getEntry().getId());

				FeedbackOnSynsetWord feedback = new FeedbackOnSynsetWord();
				feedback.setVerdict(FeedbackOnSynsetWord.CORRECT);
				
				SynsetWordWithFeedback sbf = new SynsetWordWithFeedback();
				sbf.setFeedback(feedback);
				sbf.setSynsetWord(synsetWord);

				addedWords.add(sbf);
			}
		}
        //Window.alert("New Words COllected");
		return addedWords;
	}

	private void showNewEntryPopUp(Widget source, final SynsetEntryWithWords entry) {
		// TODO Auto-generated method stub
		final PopupPanel popUpPanel = new PopupPanel();

		final VerticalPanel vPanel = new VerticalPanel();
		HTML glossLabel = new HTML("<h4>Synonym Set Definition:</h4>");
		final TextArea textArea = new TextArea();
		textArea.setText(entry.getEntry().getDescription());
		HTML examplesLabel = new HTML("<h4>Examples:</h4>");
		final TextArea examplesTA = new TextArea();
		
		
		StringBuilder sb = new StringBuilder();
		if (entry.getExamples() != null && entry.getExamples().size() > 0){
			for (SynsetExample ex: entry.getExamples()){
				sb.append(ex.getExample()+"\n");
			}
			examplesTA.setText(sb.toString());
		}
		
		HTML wordsLabel = new HTML("<h5>Synset Words:</h5>");
		final TextArea wordBox = new TextArea();
		if (entry.getWords().size() == 0) {
			wordBox.setPlaceholder("Use COMMA to seperate multiple words");
		} else {
			wordBox.setText(wordstoString(entry.getWords()));
		}

		Button saveButton = new Button("Save");
		Button cancelButton = new Button("Cancel");

		saveButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				String gloss = textArea.getText();
				if (gloss.trim().length() != 0) {
					SynsetEntry entry = new SynsetEntry(gloss, null);
					ArrayList<SynsetWord> words = new ArrayList<>();

					String[] newSynWords = wordBox.getText().split(",");
					for (String w : newSynWords) {
						SynsetWord synWord = new SynsetWord(w.trim(), "es");

						words.add(synWord);
					}
					
					String[] examples = examplesTA.getText().split("\n");

					ArrayList<SynsetExample> exms = new ArrayList<>();
					
					for (String s: examples){
						SynsetExample ex = new SynsetExample();
						ex.setExample(s);
						exms.add(ex);
					}
					
					
					
					SynsetEntryWithWords newInfo = new SynsetEntryWithWords(entry);
					newInfo.addAllWords(words);
					newInfo.setExamples(exms);
					//newSynsets.add(newInfo);
					
					ArrayList<SynsetEntryWithWords> sew = new ArrayList<>();
					sew.add(newInfo);
					wordService.addSynset(sew, cameoCode, word, new AsyncCallback<ArrayList<SynsetEntryWithWords>>() {

						@Override
						public void onFailure(Throwable caught) {
							// TODO Auto-generated method stub
							
						}

						@Override
						public void onSuccess(ArrayList<SynsetEntryWithWords> result) {
							// TODO Auto-generated method stub
							//newSynsets.addAll(result);
							GWT.log("Size of Synsets: "+result.size());
							newSynsets.addAll(0, result);
							designUIArabic();
							popUpPanel.hide();
						}
					});
					
				
					
				}
			}
		});

		cancelButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				popUpPanel.hide();
			}
		});
		vPanel.add(glossLabel);
		vPanel.add(textArea);
		vPanel.add(examplesLabel);
		vPanel.add(examplesTA);
		vPanel.add(wordsLabel);
		vPanel.add(wordBox);
		

		HorizontalPanel buttonsPanel = new HorizontalPanel();
		buttonsPanel.add(saveButton);
		buttonsPanel.add(cancelButton);
		vPanel.add(buttonsPanel);
		popUpPanel.add(vPanel);
		textArea.setWidth("inherit");
		vPanel.setWidth("300px");
		popUpPanel.showRelativeTo(source);

	}

	private SynsetEntryWithWords create(String gloss, String wordline) {
		SynsetEntry entry = new SynsetEntry(gloss, null);
		ArrayList<SynsetWord> words = new ArrayList<>();

		String[] newSynWords = wordline.split(",");
		for (String w : newSynWords) {
			SynsetWord synWord = new SynsetWord(w.trim(), "es");

			words.add(synWord);
		}
		SynsetEntryWithWords newInfo = new SynsetEntryWithWords(entry);
		newInfo.addAllWords(words);
		return newInfo;
	}

	void displayContents(boolean visible) {
		synsetTable.setVisible(visible);
		synsetHeader.setVisible(visible);
		//infoMessage.setVisible(visible);
		// lPanel.setVisible(!visible);
		//newEntryButton.setVisible(visible);
		//commentHeader.setVisible(visible);
		//overallComment.setVisible(visible);
		
	}

	protected void designUI() {
		// TODO Auto-generated method stub
		displayContents(true);
		infoMessage
				.setHTML("<span style=\"color: green;\"> " + entriesWithWords.size() + " synonym set(s) found.</span>");
		int i = 0;
		
		GWT.log("Synset Length" + entriesWithWords.size());
		synsetTable.setBorderWidth(1);
		synsetTable.clear();
		synsetTable.removeAllRows();
		synsetHeader.setText("Synset Entries for " + word);
		// synsetTable = new FlexTable();

		for (final SynsetEntryWithWords newEntry : newSynsets) {
			synsetTable.getRowFormatter().addStyleName(i,"NewEntry");
			VerticalPanel synsetFeedbackPanel = buildSynsetFeedbackForm(newEntry.getEntry());
            synsetTable.setWidget(i, 0, new HTML(cameoEntry.getCode()+" "+cameoEntry.getConcept()));
            synsetTable.setWidget(i, 1, new HTML(word));
			synsetTable.setWidget(i, 3, synsetFeedbackPanel);
			VerticalPanel glossPanel = new VerticalPanel();
			glossPanel.add(new HTML(contextInMultiline(newEntry.getEntry().getGloss())));
			glossPanel.add(new HTML("<span style=\"color: green;\"><u><b> Examples: </b></u></span>"));
			VerticalPanel vPanel = new VerticalPanel();
			addExamples(vPanel, newEntry.getExamples());
			
			glossPanel.add(vPanel);
			
			final Button addExampleButton = new Button("Add Example");
			
			addExampleButton.setType(ButtonType.LINK);
			
			addExampleButton.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					// TODO Auto-generated method stub
					showAddExamplePopUp(addExampleButton, newEntry.getEntry());
				}
			});
            final Button updateButton = new Button("Update this Synset");
			
			updateButton.setType(ButtonType.LINK);
			
			updateButton.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					// TODO Auto-generated method stub
					showNewEntryPopUp(updateButton, newEntry);
				}
			});
			glossPanel.add(addExampleButton);
			glossPanel.add(updateButton);
			synsetTable.setWidget(i, 2, glossPanel);
			String newAddedWords = newWordsToSynset.get(newEntry.getEntry().getId());
			VerticalPanel temp = buildWordsTable(newEntry);

			synsetTable.setWidget(i, 4, temp);

			i++;
		}
		wordTables.clear();
		newWordTextBoxes.clear();
        
		for (final SynsetEntryWithWords entry : entriesWithWords) {
			VerticalPanel synsetVerdictPanel = buildSynsetFeedbackForm(entry.getEntry());
			synsetTable.setWidget(i, 0, new HTML(cameoEntry.getCode()+" "+cameoEntry.getConcept()));
            synsetTable.setWidget(i, 1, new HTML(word));
//			final CheckBox checkBox = new CheckBox();
//			checkBox.setValue(isSelected(entry));
			synsetTable.setWidget(i, 3, synsetVerdictPanel);
			GWT.log(entry.getEntry().getGloss());
			VerticalPanel glossPanel = new VerticalPanel();
			glossPanel.add(new HTML(contextInMultiline(entry.getEntry().getGloss())));
			glossPanel.add(new HTML("<span style=\"color: green;\"><u><b> Examples: </b></u></span>"));
			VerticalPanel vPanel = new VerticalPanel();
			addExamples(vPanel, entry.getExamples());
			
			glossPanel.add(vPanel);
			
			final Button addExampleButton = new Button("Add Example");
			
			addExampleButton.setType(ButtonType.LINK);
			
			addExampleButton.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					// TODO Auto-generated method stub
					showAddExamplePopUp(addExampleButton, entry.getEntry());
				}
			});
			glossPanel.add(addExampleButton);
			
            final Button updateButton = new Button("Update this Synset");
			
			updateButton.setType(ButtonType.LINK);
			
			updateButton.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					// TODO Auto-generated method stub
					showNewEntryPopUp(updateButton, entry);
				}
			});
			
			glossPanel.add(updateButton);
			synsetTable.setWidget(i, 2, glossPanel);
			String newAddedWords = newWordsToSynset.get(entry.getEntry().getId());
			VerticalPanel temp = buildWordsTable(entry);

			synsetTable.setWidget(i, 4, temp);

			i++;
		}
	}
	
	protected void designUIArabic() {
		// TODO Auto-generated method stub
		displayContents(true);
//		
		
		GWT.log("Synset Length" + entriesWithWords.size());
		synsetTable.setBorderWidth(1);
		synsetTable.clear();
		synsetTable.removeAllRows();
		//Window.alert("Table Cleared");
		//synsetHeader.setText("Synset Entries for " + word);
		// synsetTable = new FlexTable();
        SynsetEntryWithWords tempSE = new SynsetEntryWithWords();
        //wordTables.clear();
		        
		for (final SynsetEntryWithWords entry : entriesWithWords) {
			tempSE.addAllWords(entry.getWords());
		}
		
		wordTables.clear();
		//Window.alert("Number of words: "+tempSE.getWords().size());
		VerticalPanel mainPanel = new VerticalPanel();
		final FlexTable table = new FlexTable();
		wordTables.add(table);
		TextBox newWordTextBox = new TextBox();
		if (tempSE.getWords().size() == 0) {
			infoMessage.setHTML("<span style=\"color: green;\">  0 arabic word(s) found.</span>");

			table.setWidget(0, 0, new HTML("<h5>No word in record</h5>"));
		}

		else {
			
			table.setWidget(0, 0, new HTML("<b>Translated Word</b>"));
			table.setWidget(0, 1, new HTML("<b>Correct</b>"));
			table.setWidget(0, 2, new HTML("<b>Incorrect</b>"));
			table.setWidget(0, 3, new HTML("<b>Ambiguous</b>"));
			table.setWidget(0, 4, new HTML("<b>Notes</b>"));

			// table.setWidget(0, 5, new HTML("<b>Country</b>"));
			table.setBorderWidth(1);
            ArrayList<SynsetWord> words = tempSE.getWords();
            wordsToSW.clear();
            for (SynsetWord sw: words){
            	if (!wordsToSW.containsKey(sw.getWord())){
            		ArrayList<SynsetWord> arr = new ArrayList<>();
            		arr.add(sw);
            		wordsToSW.put(sw.getWord(), arr);
            	} else {
            	   ArrayList<SynsetWord> arr = wordsToSW.get(sw.getWord());
            	   arr.add(sw);
            	   wordsToSW.put(sw.getWord(), arr);
            	}
            }
            
            ArrayList<String> wordlist = new ArrayList<>(wordsToSW.keySet());
            infoMessage.setHTML("<span style=\"color: green;\"> " + wordlist.size() + " arabic word(s) found.</span>");

            for (int i = 0; i < wordlist.size(); i++) {
				Radio radio = new Radio("Radio" + wordlist.get(i));
				radio.setValue(true);
				String textWord = wordlist.get(i);
				try {
					table.setWidget(i + 1, 0, new HTML(new String(textWord.getBytes(), "UTF-8")));
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					table.setWidget(i + 1, 0, new HTML(textWord));
				}
				SynsetWord sw = wordsToSW.get(textWord).get(0);
				FeedbackOnSynsetWord feedback = feedbacks.get(sw.getId());

				String comment = "";

				String country = "";

				if (feedback == null) {
					GWT.log("Feedback is NULL");
					table.setWidget(i + 1, 2, radio);
					table.setWidget(i + 1, 1, new Radio("Radio" + wordlist.get(i)));
					table.setWidget(i + 1, 3, new Radio("Radio" + wordlist.get(i)));
				} else {
					GWT.log("Feedback is: "+feedback.getVerdict());
					if (feedback.getVerdict().equals(FeedbackOnSynsetWord.CORRECT)) {
						table.setWidget(i + 1, 1, radio);
						table.setWidget(i + 1, 2, new Radio("Radio" + wordlist.get(i)));
						table.setWidget(i + 1, 3, new Radio("Radio" + wordlist.get(i)));
					} else if (feedback.getVerdict().equals(FeedbackOnSynsetWord.INCORRECT)) {
						table.setWidget(i + 1, 2, radio);
						table.setWidget(i + 1, 1, new Radio("Radio" + wordlist.get(i)));
						table.setWidget(i + 1, 3, new Radio("Radio" + wordlist.get(i)));
					} else {
						table.setWidget(i + 1, 3, radio);
						table.setWidget(i + 1, 1, new Radio("Radio" + wordlist.get(i)));
						table.setWidget(i + 1, 2, new Radio("Radio" + wordlist.get(i)));
					}
					if (feedback.getComment() != null)
						comment = feedback.getComment();

				}

				TextArea commentTA = new TextArea();
				commentTA.setText(comment);

				table.setWidget(i + 1, 4, commentTA);
				// SuggestBox sbox = new SuggestBox(countrySuggestions);

			}
			//Window.alert("Table Created");
//			String words = (newWordsToSynset.get(entry.getEntry().getId()) == null) ? ""
//					: newWordsToSynset.get(entry.getEntry().getId());
//			newWordTextBox.setText(words);
            
		}
		wordTables.add(table);
		HTML newWordHTML = new HTML("<b>Add new words(comma seperated)</b>");

		mainPanel.add(table);
		mainPanel.add(newWordHTML);
		
		mainPanel.add(newWordTextBox);
		synsetTable.setWidget(0,0,mainPanel);
		newWordTextBoxes.clear();
		newWordTextBoxes.add(newWordTextBox);

		

	}
	
	

	
	protected void showAddExamplePopUp(Button addExampleButton, final SynsetEntry synsetEntry) {
		// TODO Auto-generated method stub
		VerticalPanel vPanel = new VerticalPanel();
		final TextArea examplesTA = new TextArea();
		examplesTA.setPlaceholder("Enter examples, one per line");
		Button saveButton = new Button("Save");
		Button cancelButton  = new Button("Cancel");
		
		vPanel.add(examplesTA);
		vPanel.add(saveButton);
		vPanel.add(cancelButton);
		
		final PopupPanel pPanel = new PopupPanel();
		pPanel.add(vPanel);
		
		saveButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				String[] examples = examplesTA.getText().split("\n");
				addExamples(synsetEntry, examples);
				pPanel.hide();
			}
		});
		
		cancelButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				pPanel.hide();
			}
		});
		pPanel.showRelativeTo(addExampleButton);
				
	}
	
	

	protected void addExamples(final SynsetEntry synsetEntry, String[] examples) {
		// TODO Auto-generated method stub
		
		if (examples.length > 0){
			wordService.addExamples(synsetEntry, examples, new AsyncCallback<ArrayList<SynsetExample>>() {

				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onSuccess(ArrayList<SynsetExample> result) {
					// TODO Auto-generated method stub
					int i = 0;
					for (SynsetEntryWithWords sew: entriesWithWords){
						if (sew.getEntry().getId().equals(synsetEntry.getId())){
							sew.getExamples().addAll(result);
							break;
						}
						i++;
					}
					
					VerticalPanel vPanel = (VerticalPanel)(((VerticalPanel)synsetTable.getWidget(i, 1)).getWidget(2));
					addExamples(vPanel, result);
				}
			});
		}
		
	}

	private void addExamples(VerticalPanel glossPanel, ArrayList<SynsetExample> examples) {
		// TODO Auto-generated method stub
		
		
		if (examples.size() == 0){
			return;
		}
		
		
		GWT.log("Number of Examples: "+examples.size());
		for (int i = 0; i < examples.size(); i++){
			SynsetExampleVerdict v = exampleVerdicts.get(examples.get(i).getId());
			int verdict = (v == null)? 0: v.getVerdict();
			PopupLabel pLabel = new PopupLabel(examples.get(i).getExample(), verdict, examples.get(i).getId());
			examplesLabel.add(pLabel);
			glossPanel.add(pLabel);
		}
		
	}

	boolean isExamplesCorrect(SynsetEntryWithWords entry){
		for (CameoSelectedSynset e : selections) {
			//GWT.log("Testing " + e.getIdSynsetEntry() + " " + entry.getEntry().getId());
			if (e.getIdSynsetEntry().equals(entry.getEntry().getId())) {
				return e.isExamplesValid();
			}
		}

		return true;
	}


	
	private VerticalPanel buildSynsetFeedbackForm(SynsetEntry entry){
		VerticalPanel synsetVerdictPanel = new VerticalPanel();
		Long entryId = entry.getId();
		int temp = 0;
		if (!synsetVerdicts.containsKey(entryId)){
			temp = 0;
			GWT.log("CORRECT __");
		}
		else if (synsetVerdicts.get(entryId).getVerdict().equals(SynsetVerdict.CORRECT)){
		       temp = 0;
		       GWT.log("CORRECT");
		} else if (synsetVerdicts.get(entryId).getVerdict().equals(SynsetVerdict.INCORRECT)){
			temp = 1;
			GWT.log("INCORRECT");
		} else {
			temp = 2;
			GWT.log("AMBIGUOUS");
		}
		RadioButton correctButton = new RadioButton("SynsetVerdict"+entryId, "Correct");
		correctButton.setValue(temp==0);
		RadioButton incorrectButton = new RadioButton("SynsetVerdict"+entryId, "Incorrect");
		incorrectButton.setValue(temp==1);
		RadioButton ambiguousButton = new RadioButton("SynsetVerdict"+entryId, "Ambiguous");
		ambiguousButton.setValue(temp==2);
		synsetVerdictPanel.add(correctButton);
		synsetVerdictPanel.add(incorrectButton);
		synsetVerdictPanel.add(ambiguousButton);
		return synsetVerdictPanel;
		
	}

	private VerticalPanel buildWordsTable(final SynsetEntryWithWords entry) {

		VerticalPanel mainPanel = new VerticalPanel();
		final FlexTable table = new FlexTable();
		wordTables.add(table);
		TextBox newWordTextBox = new TextBox();
		if (entry.getWords().size() == 0) {
			table.setWidget(0, 0, new HTML("<h5>No word in record</h5>"));
		}

		else {
			table.setWidget(0, 0, new HTML("<b>Translated Word</b>"));
			table.setWidget(0, 1, new HTML("<b>Correct</b>"));
			table.setWidget(0, 2, new HTML("<b>Incorrect</b>"));
			table.setWidget(0, 3, new HTML("<b>Ambiguous</b>"));
			table.setWidget(0, 4, new HTML("<b>Notes</b>"));

			// table.setWidget(0, 5, new HTML("<b>Country</b>"));
			table.setBorderWidth(1);

			for (int i = 0; i < entry.getWords().size(); i++) {
				Radio radio = new Radio("Radio" + entry.getEntry().getId() + "_" + i);
				radio.setValue(true);
				String textWord = entry.getWords().get(i).getWord();
				try {
					table.setWidget(i + 1, 0, new HTML(new String(textWord.getBytes(), "UTF-8")));
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					table.setWidget(i + 1, 0, new HTML(textWord));
				}
				SynsetWord sw = entry.getWords().get(i);
				FeedbackOnSynsetWord feedback = feedbacks.get(sw.getId());

				String comment = "";

				String country = "";
				if (feedback == null) {
					table.setWidget(i + 1, 1, radio);
					table.setWidget(i + 1, 2, new Radio("Radio" + entry.getEntry().getId() + "_" + i));
					table.setWidget(i + 1, 3, new Radio("Radio" + entry.getEntry().getId() + "_" + i));
				} else {
					GWT.log(feedback.getVerdict());
					if (feedback.getVerdict().equals(FeedbackOnSynsetWord.CORRECT)) {
						table.setWidget(i + 1, 1, radio);
						table.setWidget(i + 1, 2, new Radio("Radio" + entry.getEntry().getId() + "_" + i));
						table.setWidget(i + 1, 3, new Radio("Radio" + entry.getEntry().getId() + "_" + i));
					} else if (feedback.getVerdict().equals(FeedbackOnSynsetWord.INCORRECT)) {
						table.setWidget(i + 1, 2, radio);
						table.setWidget(i + 1, 1, new Radio("Radio" + entry.getEntry().getId() + "_" + i));
						table.setWidget(i + 1, 3, new Radio("Radio" + entry.getEntry().getId() + "_" + i));
					} else {
						table.setWidget(i + 1, 3, radio);
						table.setWidget(i + 1, 1, new Radio("Radio" + entry.getEntry().getId() + "_" + i));
						table.setWidget(i + 1, 2, new Radio("Radio" + entry.getEntry().getId() + "_" + i));
					}
					if (feedback.getComment() != null)
						comment = feedback.getComment();

					if (feedback.getCountry() != null)
						country = feedback.getCountry();
				}

				TextArea commentTA = new TextArea();
				commentTA.setText(comment);

				table.setWidget(i + 1, 4, commentTA);
				// SuggestBox sbox = new SuggestBox(countrySuggestions);

			}
//			String words = (newWordsToSynset.get(entry.getEntry().getId()) == null) ? ""
//					: newWordsToSynset.get(entry.getEntry().getId());
//			newWordTextBox.setText(words);
		}

		HTML newWordHTML = new HTML("<b>Add new words(comma seperated)</b>");

		mainPanel.add(table);
		 mainPanel.add(newWordHTML);
		
		 mainPanel.add(newWordTextBox);
		 newWordTextBoxes.add(newWordTextBox);

		//mainPanel.add(makeNewWordTable());

		return mainPanel;

	}

	// public ArrayList<SynsetWord> newWords(){
	//
	// }

	private VerticalPanel makeNewWordTable() {
		VerticalPanel panel = new VerticalPanel();
		final FlexTable ftable = new FlexTable();
		Button addWordButton = new Button("Add New Word");
		addWordButton.setType(ButtonType.LINK);

		panel.add(ftable);
		panel.add(addWordButton);
		ftable.setBorderWidth(0);
		addWordButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				int row = ftable.getRowCount();
				TextBox tb = new TextBox();
				tb.setPlaceholder("Enter word...");
				CellFormatter formatter = ftable.getCellFormatter();
				formatter.setVerticalAlignment(row, 0, HasVerticalAlignment.ALIGN_TOP);
				ftable.setWidget(row, 0, tb);
				TextArea ta = new TextArea();
				ta.setPlaceholder("Enter comment...");
				ftable.setWidget(row, 1, ta);
				formatter.setVerticalAlignment(row, 1, HasVerticalAlignment.ALIGN_TOP);
				ftable.setWidget(row, 2, prepareCountryListBox(""));
				formatter.setVerticalAlignment(row, 2, HasVerticalAlignment.ALIGN_TOP);
				ftable.setWidget(row, 3, new Button("Remove", new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						// TODO Auto-generated method stub
						ftable.removeRow(ftable.getCellForEvent(event).getRowIndex());
					}
				}));
				formatter.setVerticalAlignment(row, 3, HasVerticalAlignment.ALIGN_TOP);
			}
		});
		return panel;

	}
	
	private String contextInMultiline(String data){
		String[] words = data.split("\"");
		
		StringBuilder sb = new StringBuilder(words[0]);
//		for (int i = 1; i < words.length; i+= 2){
//			sb.append("<br/>- \""+words[i].trim()+"\"");		       
//		}
		return sb.toString();
	}

	private String wordstoString(ArrayList<SynsetWord> words) {
		StringBuilder sb = new StringBuilder("  ");
		for (SynsetWord word : words) {
			sb.append(word.getWord() + ", ");
		}
		return sb.toString().substring(0, sb.length() - 2).trim();
	}

}
