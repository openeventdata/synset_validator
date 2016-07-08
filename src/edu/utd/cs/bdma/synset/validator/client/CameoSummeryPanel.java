package edu.utd.cs.bdma.synset.validator.client;

import java.util.ArrayList;
import java.util.List;

import com.github.gwtbootstrap.client.ui.TextBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.utd.cs.bdma.synset.validator.shared.customevents.CameoCodeSelectedEvent;
import edu.utd.cs.bdma.synset.validator.shared.entity.CameoEntrySummery;

public class CameoSummeryPanel extends PopupPanel implements HasHandlers{

	private static final CameoServiceAsync cameoService = GWT.create(CameoService.class); 
	static EventBus eventBus = GWT.create(SimpleEventBus.class);
	
	
	
	private List<CameoEntrySummery> summeries;
	private List<CameoEntrySummery> filteredSummeries = new ArrayList<>() ;
	
	@UiField
	LoadingPanel loadingPanel;
	
	Widget parentWidget;
	
	private static CameoSummeryPanelUiBinder uiBinder = GWT.create(CameoSummeryPanelUiBinder.class);

	interface CameoSummeryPanelUiBinder extends UiBinder<Widget, CameoSummeryPanel> {
	}

	public CameoSummeryPanel() {
		setWidget(uiBinder.createAndBindUi(this));
		
			
	}

	@UiField
	FlexTable summeryTable;
	
	@UiField
	TextBox searchTextBox;
	
	@UiField
	VerticalPanel mainPanel;
	
		
	public void getAndShow(final Widget widget){
		if (summeries != null)
		            GWT.log("Num Summeries: "+summeries.size());
		parentWidget = widget;
		this.showRelativeTo(parentWidget);
		loadingPanel.show();
		if (summeries == null ){
			cameoService.getCameoSummery(new AsyncCallback<List<CameoEntrySummery>>() {

				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onSuccess(List<CameoEntrySummery> result) {
					// TODO Auto-generated method stub
					summeries = result;
					for (CameoEntrySummery s: summeries){
						filteredSummeries.add(s);
					}
					constructTable(summeries);
					loadingPanel.hide();
				}
			});
		} else {
			GWT.log("Testing Drawing");
			filterResult();
			constructTable(filteredSummeries);
			
			loadingPanel.hide();
		}
	}

	private void constructTable(List<CameoEntrySummery> summeries2) {
		// TODO Auto-generated method stub
	    summeryTable.clear();
		int i = 0;
		summeryTable.setCellPadding(10);
		for(CameoEntrySummery summery: summeries2){
			summeryTable.setWidget(i++, 0, new HTML(buildTableEntry(summery)));
		}
		GWT.log("Finished Processing");
		//this.showRelativeTo(widget);
	}
	
	
	private String buildTableEntry(CameoEntrySummery summery){
		return "<span style=\"font-weight: bold; color: green;\">"+summery.getCode()
		             +"</span><span style=\"font-weight: bold;\"> "
		                            +summery.getConcept()+"</span> "+summery.getDescription();
	}

	@UiHandler("summeryTable")
	void summeryClicked(ClickEvent e){
		int row = summeryTable.getCellForEvent(e).getRowIndex();
		GWT.log(""+row);
		String cameoCode = filteredSummeries.get(row).getCode();
		//eventBus.fireEvent(new CameoCodeSelectedEvent(cameoCode));
		this.hide();
		fireEvent(new CameoCodeSelectedEvent(cameoCode));
	}

	@UiHandler("hideButton")
	void handleClick(ClickEvent event){
		this.hide();
	}

    @UiHandler("searchTextBox")
    void handleKeyPress(KeyUpEvent event){
    	
         filterResult();
    	 constructTable(filteredSummeries);
    }
    
    void filterResult(){

        filteredSummeries.clear();    
        GWT.log("Length: "+searchTextBox.getText());
		for(CameoEntrySummery summery: summeries){
			if (summery.contains(searchTextBox.getText()))
			        filteredSummeries.add(summery);
		}
		GWT.log("Filtered Summeries: "+filteredSummeries.size());
		GWT.log("Finished Processing");
		
    }

    
	

}
