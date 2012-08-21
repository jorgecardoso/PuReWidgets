/**
 * 
 */
package org.purewidgets.client.widgets;

import java.util.ArrayList;

import org.purewidgets.client.feedback.InputFeedback;
import org.purewidgets.shared.events.ActionEvent;
import org.purewidgets.shared.events.WidgetInputEvent;
import org.purewidgets.shared.im.WidgetOption;
import org.purewidgets.shared.logging.Log;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * A widget that presents a list of choices to the user. The ListBox may allow
 * multiple selections or provide a mutually-exclusive set of choices.
 * 
 * <h3>CSS Style Rules</h3>
 * <dl>
 * <dt>.pwListbox</dt>
 * <dd>the outer element. </dd>
 * 
 * <dt>.pwListboxTitle</dt>
 * <dd>the title of the listbox</dd>
 * 
 * <dt>.pwListboxItem</dt>
 * <dd>An item of the listbox</dd>
 *
 * <dt>.pwListboxItemLabel</dt>
 * <dd>The label of the item of the listbox</dd>
 * 
 * <dt>.pwListboxItemReferencecode</dt>
 * <dd>The reference of the item of the listbox</dd> 
 * </dl> 
 * 
 * @author Jorge C. S. Cardoso
 * 
 */
public class PdListBox extends PdWidget implements ClickHandler {
	protected final String DEFAULT_USER_INPUT_FEEDBACK_PATTERN = "%U%: %WOS%";
	protected final String DEFAULT_USER_SHARED_TITLE_INPUT_FEEDBACK_PATTERN = "%U%";
	protected final String DEFAULT_USER_SHARED_INFO_INPUT_FEEDBACK_PATTERN = "%WOS%";
	
	interface Style extends CssResource {
		
	    String pwListbox();
	
	    String pwListboxItem();
		
        String pwListboxItemLabel();

	    String pwListboxItemReferencecode();
		
	    String pwListboxTitle();		
	  }
	
	
	@UiTemplate("PdListbox.ui.xml")
	interface PdListboxUiBinder extends UiBinder<Widget, PdListBox> {	}
	private static PdListboxUiBinder uiBinder = GWT.create(PdListboxUiBinder.class);


	@UiField Style style;

	/**
	 * The main panel for the widget.
	 */
	@UiField
	VerticalPanel uiVerticalPanelMain;
	
	/**
	 * The Label with the list title
	 */
	private Label titleLabel;
	
	/**
	 * A list of HPanels with the options for the list
	 */
	private ArrayList<HorizontalPanel> optionsHorizontalPanel;

	/**
	 * The list widget behind this Gui Widget
	 */
	private org.purewidgets.shared.widgets.ListBox widgetList;
	
	/**
	 * @param widgetID
	 * @param labels
	 * @param optionsIDs
	 * @param suggestedReferences
	 * @param internalOptionIDs
	 */
	public PdListBox(String widgetId, String title, ArrayList<String> options) {
		super(widgetId);
		initWidget(uiBinder.createAndBindUi(this));
		
		/*
		 * Set the default user feedback pattern
		 */
		this.setUserInputFeedbackPattern(DEFAULT_USER_INPUT_FEEDBACK_PATTERN);
		this.setUserSharedInfoInputFeedbackPattern(DEFAULT_USER_SHARED_INFO_INPUT_FEEDBACK_PATTERN);
		this.setUserSharedTitleInputFeedbackPattern(DEFAULT_USER_SHARED_TITLE_INPUT_FEEDBACK_PATTERN);
		
		this.widgetList = new org.purewidgets.shared.widgets.ListBox(widgetId, title, options);
		this.setWidget(this.widgetList);
		//this.sendToServer();
		
		
		/* Gui stuff 
		 * 
		 */
		
		this.titleLabel = new Label(title);
		this.titleLabel.addStyleName(style.pwListboxItem());
		this.titleLabel.addStyleName(style.pwListboxTitle());
		this.uiVerticalPanelMain.add(titleLabel);
		
		
		this.optionsHorizontalPanel = new ArrayList<HorizontalPanel>();
		for ( String option : this.widgetList.getListOptions() ) {
			HorizontalPanel hPanel = new HorizontalPanel();
			hPanel.addStyleName(style.pwListboxItem());
			
			this.optionsHorizontalPanel.add(hPanel);
			
			Label optionText = new Label(option);
			optionText.addStyleName(style.pwListboxItemLabel());
			hPanel.add(optionText);
			
			Label optionReferenceCode = new Label();
			optionReferenceCode.addStyleName(style.pwListboxItemReferencecode());
			hPanel.add(optionReferenceCode);
			
			this.uiVerticalPanelMain.add(hPanel);
		}
		
	
		this.sendToServer();
		this.onReferenceCodesUpdated();
	}

	@Override
	public void onReferenceCodesUpdated() {
		Log.error(this, "Updating " + this.getWidgetOptions().size() + " reference codes");
		ArrayList<WidgetOption> widgetOptions = this.getWidgetOptions();
		for (int i = 0; i < widgetOptions.size(); i++ ) {
			HorizontalPanel hPanel = this.optionsHorizontalPanel.get(i);
			
			Label refCode = (Label)hPanel.getWidget(1);
			
			refCode.setText( ReferenceCodeFormatter.format(widgetOptions.get(i).getReferenceCode()) );
		}
		super.onReferenceCodesUpdated();
	}
	
	@Override
	public InputFeedback<PdListBox> handleInput(WidgetInputEvent ie) {
		InputFeedback<PdListBox> feedback = new InputFeedback<PdListBox>(this, ie);
		feedback.setType(InputFeedback.Type.ACCEPTED);

		ActionEvent<PdListBox> ae = new ActionEvent<PdListBox>(ie, this, null);
		feedback.setActionEvent(ae);
		
		this.generateUserInputFeedbackMessage(ie, feedback);
		return feedback;
	}

	@Override
	public void onClick(ClickEvent event) {
//		Cell cell = ((Grid) event.getSource()).getCellForEvent(event);
//
//		int i = cell.getRowIndex();
//
//		// Simulate an input event
//		InputEvent e;
//		if (this.internalOptionIDs) {
//			// the command uses (i+1) because, to the user, options start at 1.
//			e = new InputEvent(new Identity("Click"), new Command(this
//					.getReferenceCode(0)
//					+ "." + (i + 1)), this.getOptionID(i));
//		} else {
//			e = new InputEvent(new Identity("Click"), new Command(""), this
//					.getOptionID(i));
//		}
//
//		this.onInput(e);

	}

	

}
