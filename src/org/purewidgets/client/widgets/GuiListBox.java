/**
 * 
 */
package org.purewidgets.client.widgets;

import java.util.ArrayList;

import org.purewidgets.client.feedback.InputFeedback;
import org.purewidgets.client.im.ReferenceCodeFormatter;
import org.purewidgets.shared.events.ActionEvent;
import org.purewidgets.shared.events.InputEvent;
import org.purewidgets.shared.im.WidgetOption;
import org.purewidgets.shared.logging.Log;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * A widget that presents a list of choices to the user. The ListBox may allow
 * multiple selections or provide a mutually-exclusive set of choices.
 * 
 * <h3>CSS Style Rules</h3>
 * <dl>
 * <dt>.instantplaces-GuiListBox</dt>
 * <dd>the outer element</dd>
 * <dt>.instantplaces-GuiListBox-disabled</dt>
 * <dd>the outer element, when disabled</dd> 
 * <dt>.instantplaces-GuiListBox .item</dt>
 * <dd>the item outer element</dd>
 * <dt>.instantplaces-ListBox .item .title</dt>
 * <dd>the title element</dd>
 * <dt>.instantplaces-ListBox .item .label</dt>
 * <dd>the label of the option</dd>
 * <dt>.instantplaces-ListBox .item .referencecode</dt>
 * <dd>the reference code of the option</dd>
 * </dl>
 * 
 * @author Jorge C. S. Cardoso
 * 
 */
public class GuiListBox extends GuiWidget implements ClickHandler {
	protected final String DEFAULT_USER_INPUT_FEEDBACK_PATTERN = "%U%: %WOS%";
	
	/**
	 * The default style name for the ListBox.
	 */
	public final String DEFAULT_STYLENAME = "instantplaces-GuiListBox";

	/**
	 * The suffix for the item of the listbox.
	 */
	public static final String ITEM_STYLENAME_SUFFIX = "item";

	/**
	 * The suffix for the item of the listbox.
	 */
	public static final String TITLE_STYLENAME_SUFFIX = "title";


	/**
	 * The suffix for the label associated with the listbox item.
	 */
	public static final String ITEMLABEL_STYLENAME_SUFFIX = "label";

	/**
	 * The suffix for the reference code associated with the listbox item.
	 */
	public static final String ITEMREFERENCECODE_STYLENAME_SUFFIX = "referencecode";

	

	/**
	 * The main panel for the widget.
	 */
	private VerticalPanel mainVerticalPanel;
	
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
	public GuiListBox(String widgetId, String title, ArrayList<String> options) {
		super();
		
		/*
		 * Set the default user feedback pattern
		 */
		this.userInputFeedbackPattern = DEFAULT_USER_INPUT_FEEDBACK_PATTERN;
		
		this.widgetList = new org.purewidgets.shared.widgets.ListBox(widgetId, title, options);
		this.setWidget(this.widgetList);
		//this.sendToServer();
		
		
		/* Gui stuff 
		 * 
		 */
		
		this.mainVerticalPanel = new VerticalPanel();
		this.mainVerticalPanel.setStyleName(DEFAULT_STYLENAME);
		
		this.titleLabel = new Label(title);
		this.titleLabel.addStyleName(ITEM_STYLENAME_SUFFIX);
		this.titleLabel.addStyleName(TITLE_STYLENAME_SUFFIX);
		this.mainVerticalPanel.add(titleLabel);
		
		
		this.optionsHorizontalPanel = new ArrayList<HorizontalPanel>();
		for ( String option : this.widgetList.getListOptions() ) {
			HorizontalPanel hPanel = new HorizontalPanel();
			hPanel.addStyleName(ITEM_STYLENAME_SUFFIX);
			
			this.optionsHorizontalPanel.add(hPanel);
			
			Label optionText = new Label(option);
			optionText.addStyleName(ITEMLABEL_STYLENAME_SUFFIX);
			hPanel.add(optionText);
			
			Label optionReferenceCode = new Label();
			optionReferenceCode.addStyleName(ITEMREFERENCECODE_STYLENAME_SUFFIX);
			hPanel.add(optionReferenceCode);
			
			this.mainVerticalPanel.add(hPanel);
		}
		
		this.initWidget(this.mainVerticalPanel);
		this.sendToServer();
	}

	@Override
	public void onReferenceCodesUpdated() {
		Log.debug(this, "Updating " + this.getWidgetOptions().size() + " reference codes");
		ArrayList<WidgetOption> widgetOptions = this.getWidgetOptions();
		for (int i = 0; i < widgetOptions.size(); i++ ) {
			HorizontalPanel hPanel = this.optionsHorizontalPanel.get(i);
			
			Label refCode = (Label)hPanel.getWidget(1);
			
			refCode.setText( ReferenceCodeFormatter.format(widgetOptions.get(i).getReferenceCode()) );
		}
		
	}
	
	@Override
	public InputFeedback<GuiListBox> handleInput(InputEvent ie) {
		InputFeedback<GuiListBox> feedback = new InputFeedback<GuiListBox>(this, ie);
		feedback.setType(InputFeedback.Type.ACCEPTED);

		ActionEvent<GuiListBox> ae = new ActionEvent<GuiListBox>(this, // source
																		// widget
				ie, // input event
				null);
		feedback.setActionEvent(ae);
		
		feedback.setInfo(this.generateUserInputFeedbackMessage(ie));
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
