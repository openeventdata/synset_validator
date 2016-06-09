package edu.utd.cs.bdma.synset.validator.client;
import com.github.gwtbootstrap.client.ui.Heading;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import edu.utd.cs.bdma.synset.validator.shared.LanguageCode;
import edu.utd.cs.bdma.synset.validator.shared.SynsetEntry;

public class Test extends Composite implements HasText {

	private static TestUiBinder uiBinder = GWT.create(TestUiBinder.class);

	interface TestUiBinder extends UiBinder<Widget, Test> {
	}

	public Test() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@UiField
	HTML meaningLabel;
	
	@UiField
	Heading conceptHeader;
	
	@UiField
	Label engWordsLabel;

	public Test(SynsetEntry entry) {
		initWidget(uiBinder.createAndBindUi(this));
		conceptHeader.setText(entry.getConcept());
		StringBuilder sb = new StringBuilder();
		if (entry.wordsInLanguage(LanguageCode.ENGLISH) != null) {
            GWT.log("Inside");
			for (String word : entry.wordsInLanguage(LanguageCode.ENGLISH)) {
				sb.append(" " + word + ",");
			}
			if (sb.length() > 0)
				sb.deleteCharAt(sb.length() - 1);
			else
				sb.append("No words found in record");
		} else
			sb.append("Error null found");
        meaningLabel.setHTML("<br/>" + contextInMultiline(entry.getGloss()));
		engWordsLabel.setText(sb.toString());
	}

	@Override
	public String getText() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setText(String text) {
		// TODO Auto-generated method stub
		
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
