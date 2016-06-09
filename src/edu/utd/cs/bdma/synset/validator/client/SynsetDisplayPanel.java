package edu.utd.cs.bdma.synset.validator.client;

import java.util.ArrayList;
import java.util.HashMap;

import org.gwtbootstrap3.client.ui.CheckBox;
import org.gwtbootstrap3.client.ui.Radio;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.Heading;
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
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLTable.CellFormatter;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.utd.cs.bdma.synset.validator.shared.entity.CameoSelectedSynset;
import edu.utd.cs.bdma.synset.validator.shared.entity.FeedbackOnSynsetWord;
import edu.utd.cs.bdma.synset.validator.shared.entity.SynsetEntry;
import edu.utd.cs.bdma.synset.validator.shared.entity.SynsetEntryWithWords;
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
	
	private static ArrayList<String> countries = new ArrayList<>();
	
	Storage localDB = null;

	private HashMap<Long, FeedbackOnSynsetWord> feedbacks = new HashMap<>();

	private HashMap<Long, String> newWordsToSynset = new HashMap<>();

	SynsetEntryWithWords dummyEntry;

	ArrayList<SynsetEntryWithWords> newSynsets = new ArrayList<>();
	
	ArrayList<SynsetWord> wordsInNewSynset = new ArrayList<>();
	
	

	interface SynsetDisplayPanelUiBinder extends UiBinder<Widget, SynsetDisplayPanel> {
	}

	private String word;
	private ArrayList<SynsetEntryWithWords> entriesWithWords;
	private UpdatedInfo info;
	

	public SynsetDisplayPanel() {
		initWidget(uiBinder.createAndBindUi(this));
		displayContents(false);
		mainPanel.add(lPanel);
		wordService.getCountries("es", new AsyncCallback<ArrayList<String>>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(ArrayList<String> result) {
				// TODO Auto-generated method stub
				countries = result;
			}
		});
	}

	public SynsetDisplayPanel(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));
        
	}
	

	private void init() {
		if (localDB == null)
			localDB = Storage.getLocalStorageIfSupported();
		newWordsToSynset.clear();
		feedbacks.clear();
		SynsetEntry entry = new SynsetEntry("", "");
		dummyEntry = new SynsetEntryWithWords(entry);
		newSynsets.clear();
		synsetTable.clear();
		GWT.log(localDB.getItem("country"));

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

	TextBox newwordTextBox = new TextBox();

	PopupPanel wordEditPanel = new PopupPanel();
	private String cameoCode;

	private ArrayList<FlexTable> wordTables = new ArrayList<>();
	private ArrayList<TextBox> newWordTextBoxes = new ArrayList<>();

	public void setWord(String w) {
		if (this.word == null || !this.word.equals(w))
			this.word = w;
		lPanel.show();
		wordService.getAll(word, "es", new AsyncCallback<ArrayList<SynsetEntryWithWords>>() {

			@Override
			public void onSuccess(ArrayList<SynsetEntryWithWords> result) {
				// TODO Auto-generated method stub
				entriesWithWords = result;
				//GWT.log("Number of words: "+ result.get(0).getWords().size());
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
						 GWT.log("Result is "+(result==null)); 
						 if (result != null){
							 ArrayList<FeedbackOnSynsetWord> fbs = result.getFeedbackOnSynsetWords();
							 for (FeedbackOnSynsetWord fb: fbs){
								feedbacks.put(fb.getIdWord(), fb);
							 }
							 GWT.log(""+feedbacks.size());
							 //newSynsets = result.getSynsetEntryWithWords();
							 selections = result.getCameoSelectedSynsets();
						 }
						 GWT.log("Success Fetching Submission");
						 lPanel.hide();
						 designUI();
					}
				});
				//designUI();

			}

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub

			}
		});

	}
	
	public ArrayList<FeedbackOnSynsetWord> getSelFeedbacks() {
		return selFeedbacks;
	}
	
	
	public ArrayList<CameoSelectedSynset> getSelections() {
		return selections;
	}
	
	public ArrayList<SynsetEntryWithWords> getNewSynsets() {
		return newSynsets;
	}
	
	public ArrayList<SynsetWordWithFeedback> getNewWords() {
		return newWords;
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
	}
	
	private MultipleSelectListBox prepareCountryListBox(String defaultSelections){
		MultipleSelectListBox lb = new MultipleSelectListBox("Select Countries", 20);
		
		lb.addOptions(countries);
		if (defaultSelections != null) lb.setDefaultSelections(defaultSelections, ",");
		return lb;
	}

	public void onSave() {
		final ArrayList<CameoSelectedSynset> selections = new ArrayList<>();

		final ArrayList<SynsetWord> newWords = new ArrayList<>();

		ArrayList<FeedbackOnSynsetWord> feedbacks = new ArrayList<>();
		for (int i = 0; i < entriesWithWords.size(); i++) {
			GWT.log("Iterating for insertion");
			if (((CheckBox) synsetTable.getWidget(i + newSynsets.size(), 0)).getValue()) {
				GWT.log("Iterating for insertion 1");
				CameoSelectedSynset selection = new CameoSelectedSynset();
				selection.setIdSynsetEntry(entriesWithWords.get(i).getEntry().getId());
				selections.add(selection);
				FlexTable table = wordTables.get(i);
				GWT.log("Iterating for insertion 2");
				for (int j = 1; j < table.getRowCount(); j++) {
					TextArea commentTA = (TextArea) table.getWidget(j, 4);
					FeedbackOnSynsetWord fb = new FeedbackOnSynsetWord(FeedbackOnSynsetWord.CORRECT,
							commentTA.getText());

					if (((Radio) table.getWidget(j, 2)).getValue()) {
						fb.setVerdict(FeedbackOnSynsetWord.INCORRECT);
					} else if (((Radio) table.getWidget(j, 3)).getValue()) {
						fb.setVerdict(FeedbackOnSynsetWord.AMBIGUOUS);
					}
					fb.setIdWord(entriesWithWords.get(i).getWords().get(j - 1).getId());
					// fb.setCountry(((ListBox)table.getWidget(j,
					// 5)).getSelectedItemText());
					feedbacks.add(fb);
					fb.setCountry(((MultipleSelectListBox)table.getWidget(j,5)).getSelectedItemsAsStr(","));
                    selFeedbacks.add(fb); 
				}

//				if (newWordTextBoxes.get(i).getText().length() > 0) {
//					String[] words = newWordTextBoxes.get(i).getText().split(",");
//					for (String w : words) {
//						SynsetWord sw = new SynsetWord(w, "es");
//						sw.setIdSynsetEntry(entriesWithWords.get(i).getEntry().getId());
//						newWords.add(sw);
//					}
//				}
				GWT.log("Iterating for insertion 3");
				this.newWords.addAll(getAddedWords(i+newSynsets.size(), entriesWithWords.get(i)));

			}


			this.selections = selections;
		}
		ArrayList<SynsetEntryWithWords> tempSynsets = new ArrayList<>();

		for (int i = 0; i < newSynsets.size(); i++) {
			if (((CheckBox) synsetTable.getWidget(i, 0)).getValue()) {
				tempSynsets.add(newSynsets.get(i));
			}
		}
		
		newSynsets = tempSynsets;
		displayContents(false);

	}
	
	private ArrayList<SynsetWordWithFeedback> getAddedWords(int rowId, SynsetEntryWithWords entry){
		FlexTable ftable = (FlexTable) ((VerticalPanel)((VerticalPanel)synsetTable.getWidget(rowId, 2)).getWidget(1)).getWidget(0);
		GWT.log("Iterating for insertion 4");
		ArrayList<SynsetWordWithFeedback> addedWords = new ArrayList<SynsetWordWithFeedback>();
		for (int j = 0; j < ftable.getRowCount(); j++){
			String word = ((TextBox)ftable.getWidget(j, 0)).getText();
			if (word.length() > 0){
				SynsetWord synsetWord = new SynsetWord();
				synsetWord.setWord(word);
				synsetWord.setLanguageCode("es");
				synsetWord.setIdSynsetEntry(entry.getEntry().getId());
			
			
			FeedbackOnSynsetWord feedback = new FeedbackOnSynsetWord();
			feedback.setVerdict(FeedbackOnSynsetWord.CORRECT);
			feedback.setComment(((TextArea)ftable.getWidget(j, 1)).getText());
			feedback.setCountry(((MultipleSelectListBox)ftable.getWidget(j, 2)).getSelectedItemsAsStr(","));
			SynsetWordWithFeedback sbf = new SynsetWordWithFeedback();
			sbf.setFeedback(feedback);
			sbf.setSynsetWord(synsetWord);
			
			addedWords.add(sbf);
			}
		}
		
		return addedWords;
	}

	private void showNewEntryPopUp(Widget source, final SynsetEntryWithWords entry) {
		// TODO Auto-generated method stub
		final PopupPanel popUpPanel = new PopupPanel();

		final VerticalPanel vPanel = new VerticalPanel();
		HTML glossLabel = new HTML("<h5>Add gloss</h5>");
		final TextArea textArea = new TextArea();
		textArea.setText(entry.getEntry().getGloss());
		HTML wordsLabel = new HTML("<h5>Add words.</h5>");
		final TextBox wordBox = new TextBox();
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
					
//					FlexTable ftable = (FlexTable) ((VerticalPanel)vPanel.getWidget(3)).getWidget(0);
//					ArrayList<SynsetWordWithFeedback> addedWords = new ArrayList<SynsetWordWithFeedback>();
//					for (int j = 0; j < ftable.getRowCount(); j++){
//						String word = ((TextBox)ftable.getWidget(j, 0)).getText();
//						if (word.length() > 0){
//							SynsetWord synsetWord = new SynsetWord();
//							synsetWord.setWord(word);
//							synsetWord.setLanguageCode("es");
//							
//						
//						
//						FeedbackOnSynsetWord feedback = new FeedbackOnSynsetWord();
//						feedback.setVerdict(FeedbackOnSynsetWord.CORRECT);
//						feedback.setComment(((TextArea)ftable.getWidget(j, 1)).getText());
//						feedback.setCountry(((MultipleSelectListBox)ftable.getWidget(j, 2)).getSelectedItemsAsStr(","));
//						SynsetWordWithFeedback sbf = new SynsetWordWithFeedback();
//						sbf.setFeedback(feedback);
//						sbf.setSynsetWord(synsetWord);
//						
//						addedWords.add(sbf);
//						}
//					}
					SynsetEntryWithWords newInfo = new SynsetEntryWithWords(entry);
					newInfo.addAllWords(words);
					newSynsets.add(newInfo);
					designUI();
					popUpPanel.hide();
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
	
	private SynsetEntryWithWords create(String gloss, String wordline){
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
	
	void displayContents(boolean visible){
		synsetTable.setVisible(visible);
		synsetHeader.setVisible(visible);
		//lPanel.setVisible(!visible);
		newEntryButton.setVisible(visible);		
	}


	protected void designUI() {
		// TODO Auto-generated method stub
		displayContents(true);
		int i = 0;
		GWT.log("Synset Length" + entriesWithWords.size());
		synsetTable.setBorderWidth(1);
		synsetTable.clear();
		synsetTable.removeAllRows();
		synsetHeader.setText("Synset Entries for "+word);
		//synsetTable = new FlexTable();

		for (final SynsetEntryWithWords newEntry : newSynsets) {
			final CheckBox checkBox = new CheckBox();
			checkBox.setValue(true);
			

			synsetTable.setWidget(i, 0, checkBox);
			GWT.log(newEntry.getEntry().getGloss());
			synsetTable.setWidget(i, 1,
					new HTML("<span style=\"color: blue\">" + newEntry.getEntry().getGloss() + "</span>"));
			synsetTable.setWidget(i, 2,
					new HTML("<span style=\"color: blue\">" + wordstoString(newEntry.getWords()) + "</span>"));
			i++;
		}
		wordTables.clear();
		newWordTextBoxes.clear();
		
		for (final SynsetEntryWithWords entry : entriesWithWords) {
			final CheckBox checkBox = new CheckBox();
			checkBox.setValue(isSelected(entry));
			synsetTable.setWidget(i, 0, checkBox);
			GWT.log(entry.getEntry().getGloss());
			synsetTable.setWidget(i, 1, new HTML(entry.getEntry().getGloss()));
			String newAddedWords = newWordsToSynset.get(entry.getEntry().getId());
			
			synsetTable.setWidget(i, 2, buildWordsTable(entry));
			
			i++;
		}
	}
	
	private boolean isSelected(SynsetEntryWithWords entry){
		for (CameoSelectedSynset e: selections){
			GWT.log("Testing "+e.getIdSynsetEntry()+" "+ entry.getEntry().getId());
			if (e.getIdSynsetEntry().equals(entry.getEntry().getId())){
				return true;
			}
		}
		
		return false;
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
			table.setWidget(0, 5, new HTML("<b>Country</b>"));
			// table.setWidget(0, 5, new HTML("<b>Country</b>"));
			table.setBorderWidth(1);

			for (int i = 0; i < entry.getWords().size(); i++) {
				Radio radio = new Radio("Radio" + entry.getEntry().getId() + "_" + i);
				radio.setValue(true);
				table.setWidget(i + 1, 0, new HTML(entry.getWords().get(i).getWord()));
				SynsetWord sw = entry.getWords().get(i); 
				FeedbackOnSynsetWord feedback = feedbacks.get(sw.getId());
				
				String comment = "";
				
				String country= "";
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
                    if (feedback.getComment() != null) comment = feedback.getComment();
                    
					if (feedback.getCountry() != null) country = feedback.getCountry();
				}
				
				TextArea commentTA = new TextArea();
				commentTA.setText(comment);
				
				table.setWidget(i + 1, 4, commentTA);
				//SuggestBox sbox = new SuggestBox(countrySuggestions);
				table.setWidget(i+1, 5, this.prepareCountryListBox(country));
				
				
			}
			String words = (newWordsToSynset.get(entry.getEntry().getId()) == null) ? ""
					: newWordsToSynset.get(entry.getEntry().getId());
			newWordTextBox.setText(words);
		}

		HTML newWordHTML = new HTML("<b>Add new words(comma seperated)</b>");
 
		mainPanel.add(table);
//		mainPanel.add(newWordHTML);
//		
//		mainPanel.add(newWordTextBox);
//		newWordTextBoxes.add(newWordTextBox);
		
		mainPanel.add(makeNewWordTable());
		
		return mainPanel;

	}
	
//	public ArrayList<SynsetWord> newWords(){
//		
//	}
	
	private VerticalPanel makeNewWordTable(){
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

	private String wordstoString(ArrayList<SynsetWord> words) {
		StringBuilder sb = new StringBuilder("  ");
		for (SynsetWord word : words) {
			sb.append(word.getWord() + ", ");
		}
		return sb.toString().substring(0, sb.length() - 2).trim();
	}



}
