package edu.utd.cs.bdma.synset.validator.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;

import edu.utd.cs.bdma.synset.validator.shared.LanguageCode;
import edu.utd.cs.bdma.synset.validator.shared.SynsetEntry;
import edu.utd.cs.bdma.synset.validator.shared.Verdict;

public class SubmitClickHandler implements ClickHandler{

	private final ValidatorServiceAsync validatorService = GWT.create(ValidatorService.class);
			
	private FlexTable table;
	private SynsetEntry synEntry;
	private LanguageCode langCode;
	private ArrayList<Verdict> verdicts;
	
	public SubmitClickHandler(FlexTable table, SynsetEntry entry, LanguageCode langCode) {
		// TODO Auto-generated constructor stub
		this.table = table;
		this.synEntry = entry;
		verdicts = new ArrayList<>();
		this.langCode = langCode;
	}
	@Override
	public void onClick(ClickEvent event) {
		// TODO Auto-generated method stub
		
		int rows = table.getRowCount();
		
		for (int i = 0; i < rows; i++){
			ListBox lb = (ListBox) table.getWidget(i, 1);
			List<String> words = synEntry.wordsInLanguage(langCode);
			Verdict verdict = new Verdict();
			verdict.setComment(((TextBox)table.getWidget(i, 2)).getText());
			verdict.setConceptId(synEntry.getConceptID());
			verdict.setLang(langCode.getCode());
			verdict.setWordInLang(words.get(i));
			

			if (lb.getItemText(lb.getSelectedIndex()).equalsIgnoreCase("Incorrect")){
			       verdict.setVerdict(Verdict.INCORRECT);   	
			} else if (lb.getItemText(lb.getSelectedIndex()).equalsIgnoreCase("Ambiguous")){
				verdict.setVerdict(Verdict.AMBIGUOUS);
			}
		}
		
	}
	
	public ArrayList<Verdict> getVerdicts() {
		return verdicts;
	}

	
	
}
