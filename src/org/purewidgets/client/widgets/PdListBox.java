/**
 * 
 */
package org.purewidgets.client.widgets;

import java.util.ArrayList;

import org.purewidgets.client.feedback.InputFeedback;
import org.purewidgets.client.feedback.MessagePattern;
import org.purewidgets.shared.events.ActionEvent;
import org.purewidgets.shared.events.WidgetInputEvent;
import org.purewidgets.shared.im.WidgetOption;
import org.purewidgets.shared.logging.Log;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * A widget that presents a list of choices to the user. 
 * When an option is selected, it triggers an ActionEvent. 
 * By default, a PdListBox has graphical vertical orientation displaying the name of the option and the reference
 * code for the option in a single line.
 *  
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
public class PdListBox extends PdWidget {
	private final String USER_INPUT_FEEDBACK_PATTERN = MessagePattern.PATTERN_USER_NICKNAME + ": " + MessagePattern.PATTERN_WIDGET_OPTION_SHORT_DESCRIPTION + "(10) " + MessagePattern.PATTERN_INPUT_AGE;
	private final String USER_SHARED_TITLE_INPUT_FEEDBACK_PATTERN = MessagePattern.PATTERN_USER_NICKNAME + " " + MessagePattern.PATTERN_INPUT_AGE;
	private final String USER_SHARED_INFO_INPUT_FEEDBACK_PATTERN = MessagePattern.PATTERN_WIDGET_OPTION_SHORT_DESCRIPTION+"(10)";
	
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
	 * Creates a new PdListBox with the specified id, title, and options.
	 * 
	 * @param widgetId The widget id
	 * @param title the title of the listbox
	 * @param options The options for the listbox
	 */
	public PdListBox(String widgetId, String title, ArrayList<String> options) {
		super(widgetId);
		initWidget(uiBinder.createAndBindUi(this));
		
		/*
		 * Set the default user feedback pattern
		 */
		this.setOnScreenFeedbackInfo(USER_INPUT_FEEDBACK_PATTERN);
		this.setOffScreenFeedbackInfo(USER_SHARED_INFO_INPUT_FEEDBACK_PATTERN);
		this.setOffScreenFeedbackTitle(USER_SHARED_TITLE_INPUT_FEEDBACK_PATTERN);
		
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

	/**
	 * Updates the graphical representations of the reference codes.
	 */		
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
	
	/**
	 * Handles input from the user, creating the ActionEvent that will be sent to the application
	 * and the InputFeedback that will be displayed on the public display.
	 * 
	 * @return InputFeedback<PdButton> the InputFeedback that will be displayed on the public display.
	 */	
	@Override
	public InputFeedback<PdListBox> handleInput(WidgetInputEvent ie) {
		ActionEvent<PdListBox> ae = new ActionEvent<PdListBox>(ie, this, null);
		
		InputFeedback<PdListBox> feedback = new InputFeedback<PdListBox>(this, ie, InputFeedback.Type.ACCEPTED, ae);
		
//		feedback.setType(InputFeedback.Type.ACCEPTED);
//		feedback.setActionEvent(ae);
		
		this.generateUserInputFeedbackMessage(ie, feedback);
		return feedback;
	}

//	@Override
//	public void onClick(ClickEvent event) {
////		Cell cell = ((Grid) event.getSource()).getCellForEvent(event);
////
////		int i = cell.getRowIndex();
////
////		// Simulate an input event
////		InputEvent e;
////		if (this.internalOptionIDs) {
////			// the command uses (i+1) because, to the user, options start at 1.
////			e = new InputEvent(new Identity("Click"), new Command(this
////					.getReferenceCode(0)
////					+ "." + (i + 1)), this.getOptionID(i));
////		} else {
////			e = new InputEvent(new Identity("Click"), new Command(""), this
////					.getOptionID(i));
////		}
////
////		this.onInput(e);
//
//	}

	

}
