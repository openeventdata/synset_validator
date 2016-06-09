package edu.utd.cs.bdma.synset.validator.client;

import java.util.HashMap;

import com.github.gwtbootstrap.client.ui.Button;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.utd.cs.bdma.synset.validator.shared.entity.CameoEntry;

public class CameoPopUpPanel extends PopupPanel {

	private static CameoPopUpPanelUiBinder uiBinder = GWT.create(CameoPopUpPanelUiBinder.class);
	
	private static final CameoServiceAsync cameoService = GWT.create(CameoService.class);  

	private CameoEntry entry;
	
	
	interface CameoPopUpPanelUiBinder extends UiBinder<Widget, CameoPopUpPanel> {
	}

	@UiField
	FlexTable cameoTable;
	
	@UiField
	Button hideButton;
	
	public CameoPopUpPanel() {
		setWidget(uiBinder.createAndBindUi(this));
		cameoTable.setWidget(0, 0, new Label("Cameo Code"));
		cameoTable.setWidget(1, 0, new Label("Name"));
		cameoTable.setWidget(2, 0, new Label("Description"));
		cameoTable.setWidget(3, 0, new Label("Usage Notes"));
		cameoTable.setWidget(4, 0, new Label("Examples"));
		cameoTable.setCellPadding(10);
	}
	
	public CameoPopUpPanel(CameoEntry entry){
		this();
		this.entry = entry;
	}
	
	public void setCemeoEntry(CameoEntry entry){
		this.entry = entry;
	}
	
	public void getAndShow(String cameoCode, final Widget widget){
		if (entry == null || !entry.getId().equals(cameoCode)){
			cameoService.getCameoInfo(cameoCode, new AsyncCallback<CameoEntry>() {
				
				@Override
				public void onSuccess(CameoEntry result) {
					// TODO Auto-generated method stub
					constructTable(result, widget);
					
				}
				
				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
					
				}
			});
		} else {
			constructTable(entry, widget);
		}
	}
	
	protected void constructTable(CameoEntry result, Widget widget) {
		// TODO Auto-generated method stub
		entry = result;
		cameoTable.setStylePrimaryName("regulartext");
		cameoTable.setWidget(0, 1, new Label(entry.getCode()));
		cameoTable.setWidget(1, 1, new Label(entry.getName()));
		cameoTable.setWidget(2, 1, new Label(entry.getDescription()));
		cameoTable.setWidget(3, 1, new Label(entry.getUsageNotes()));
		cameoTable.setWidget(4, 1, new HTML(examplesAsHTML()));
		cameoTable.setBorderWidth(2);
		cameoTable.getRowFormatter().addStyleName(0,"paddedLeft");
		this.setWidth("400px");
		this.showRelativeTo(widget);
	}
	
	private String examplesAsHTML(){
		StringBuilder sb = new StringBuilder();
		
		for (String ex: entry.getExamples()){
			ex = ex.replace("<source>", "<span title=\"Source\" style=\"color: red;\">");
			ex = ex.replace("<target>", "<span title=\"Target\" style=\"color: blue;\">");
			ex = ex.replace("<verb>", "<span title=\"Verb\" style=\"color: green;\">");
			ex = ex.replace("</source>","</span>");
			ex = ex.replace("</target>","</span>");
			ex = ex.replace("</verb>","</span>");
			sb.append(ex+"<br/><br/>");
		}
		return sb.toString();
	}

	@UiHandler("hideButton")
	void handleClick(ClickEvent event){
		this.hide();
	}





	

}
