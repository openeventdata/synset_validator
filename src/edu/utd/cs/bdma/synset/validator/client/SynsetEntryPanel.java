package edu.utd.cs.bdma.synset.validator.client;

import java.util.ArrayList;
import java.util.Iterator;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.utd.cs.bdma.synset.validator.shared.entity.SynsetEntryWithWords;
import edu.utd.cs.bdma.synset.validator.shared.entity.SynsetExample;
import edu.utd.cs.bdma.synset.validator.shared.entity.SynsetWord;

public class SynsetEntryPanel extends Composite implements HasWidgets{ 
	
	private PopupPanel pPanel;
	private SynsetEntryWithWords entryWithWords;
	private TextArea descriptionTA;
	private TextArea examplesTA;
	private TextArea wordsTA;
	
	
	public SynsetEntryPanel(SynsetEntryWithWords entryWithWords) {
		// TODO Auto-generated constructor stub\
		pPanel = new PopupPanel();
		initWidget(pPanel);
		this.entryWithWords = entryWithWords;
		design();
	}
	
	public boolean isNewEntry(){
		return entryWithWords == null;
	}
	
	public SynsetEntryPanel(){
		this(null);
	}
	
	public void show(Widget widget){
		pPanel.showRelativeTo(widget);
	}
	
	private void design(){
		VerticalPanel vPanel = new VerticalPanel();
		
		HTML label = new HTML("<h4>Description</h4>");
		
		vPanel.add(label);
		
		descriptionTA = new TextArea();
		
		vPanel.add(descriptionTA);
		
		vPanel.add(new HTML("<h4>Examples</h4><br/>(one per line)"));
		
		examplesTA = new TextArea();
		
		vPanel.add(examplesTA);
		
		vPanel.add(new HTML("<h4>Words in Synonym set</h4><br/>(comma seperated)"));
		
		wordsTA = new TextArea();
		
		vPanel.add(wordsTA);
		
		Button saveButton = new Button("Save");
		Button cancelButton = new Button("Cancel"); 
		
		cancelButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				pPanel.hide();
			}
		});
		
		saveButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				
			}
		});
		
		if (!isNewEntry()){
			descriptionTA.setText(entryWithWords.getEntry().getGloss().substring(0, entryWithWords.getEntry().getGloss().indexOf("\"")));
			
			ArrayList<SynsetExample> examples = entryWithWords.getExamples();
			StringBuilder sb =new StringBuilder();
			
			for (SynsetExample ex: examples){
				sb.append(ex.getExample()+"\n");
			}
			
			examplesTA.setText(sb.toString());
			ArrayList<SynsetWord> words = entryWithWords.getWords();
			
			if (words.size() != 0){
			sb.delete(0, sb.length());
			
			for (SynsetWord sw: words){
				sb.append(sw.getWord()+",");
			}
			wordsTA.setText(sb.substring(0, sb.length()-1).toString());
			}	
		}	
		
		pPanel.add(vPanel);
	}

	@Override
	public void add(Widget w) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Iterator<Widget> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean remove(Widget w) {
		// TODO Auto-generated method stub
		return false;
	}
	

}
