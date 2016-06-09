package edu.utd.cs.bdma.synset.validator.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;

public class CameoRulesWidget extends Composite{

	private static CameoRulesWidgetUiBinder uiBinder = GWT.create(CameoRulesWidgetUiBinder.class);

	interface CameoRulesWidgetUiBinder extends UiBinder<Widget, CameoRulesWidget> {
	}

	public CameoRulesWidget() {
		initWidget(uiBinder.createAndBindUi(this));
	}

//	@UiField
//	FlexTable rulesTable;
//
//	public CameoRulesWidget(String str) {
//		initWidget(uiBinder.createAndBindUi(this));
//		
//	}



}
