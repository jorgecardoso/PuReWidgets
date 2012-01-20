/**
 * 
 */
package org.instantplaces.purewidgets.client.widgets;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimpleCheckBox;
import com.google.gwt.user.client.ui.SimpleRadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.HTMLTable.Cell;
import com.google.gwt.user.client.ui.HasHorizontalAlignment.HorizontalAlignmentConstant;

/**
 * A widget that presents a list of choices to the user. The ListBox may allow
 * multiple selections or provide a mutually-exclusive set of choices.
 * 
 * <h3>CSS Style Rules</h3>
 * <dl>
 * <dt>.instantplaces-ListBox</dt>
 * <dd>the outer element</dd>
 * <dt>.instantplaces-ListBox-disabled</dt>
 * <dd>the outer element, when disabled</dd> *
 * <dt>.instantplaces-ListBox-item</dt>
 * <dd>the item outer element</dd>
 * <dt>.instantplaces-ListBox-item-selected</dt>
 * <dd>the selected item outer element</dd>
 * <dt>.instantplaces-ListBox-item-label</dt>
 * <dd>the label element</dd>
 * <dt>.instantplaces-ListBox-item-referencecode</dt>
 * <dd>the reference code element</dd>
 * <dt>.instantplaces-ListBox-item-feedback</dt>
 * <dd>the feedback element</dd>
 * </dl>
 * 
 * @author Jorge C. S. Cardoso
 * 
 */
public class ListBox extends AbstractWidget implements ClickHandler {
	/**
	 * The default style name for the ListBox.
	 */
	public final String DEFAULT_STYLENAME = "instantplaces-ListBox";

	/**
	 * The suffix for the item of the listbox.
	 */
	public static final String ITEM_STYLENAME_SUFFIX = "-item";

	/**
	 * The suffix for the item of the listbox.
	 */
	public static final String TOPITEM_STYLENAME_SUFFIX = "-topitem";

	/**
	 * The suffix for the item button (radiobutton, checkbox) of the listbox.
	 */
	public static final String ITEMBUTTON_STYLENAME_SUFFIX = "-item-button";

	/**
	 * The suffix for the selected item.
	 */
	public static final String ITEMSELECTED_STYLENAME_SUFFIX = "-item-selected";

	/**
	 * The suffix for the label associated with the listbox item.
	 */
	public static final String ITEMLABEL_STYLENAME_SUFFIX = "-item-label";

	/**
	 * The suffix for the reference code associated with the listbox item.
	 */
	public static final String ITEMREFERENCECODE_STYLENAME_SUFFIX = "-item-referencecode";

	/**
	 * The suffix for the feedback associated with the listbox item.
	 */
	public static final String ITEMFEEDBACK_STYLENAME_SUFFIX = "-item-feedback";

	/**
	 * Indicates whether the ListBox can have multiple items selected at the
	 * same time.
	 */
	private boolean multiSelect;

	/**
	 * The main panel for the widget.
	 */
	private VerticalPanel vPanel;

	/**
	 * The listbox is arranged in a grid with 4 columns (button, option label,
	 * reference code and feedback).
	 */
	private Grid grid;

	/**
	 * The panel used to display feedback.
	 */
	private HorizontalPanel hPanelTop;

	/**
	 * The top feedback label.
	 */
	private InlineLabel feedback;

	/**
	 * The top reference code. Used only when internaloptions is true.
	 */
	private InlineLabel referenceCode;

	/**
	 * The check boxes, or radio buttons to use next to each item (optional)
	 */
	private com.google.gwt.user.client.ui.SimpleCheckBox checkBoxes[];

	/**
	 * The labels for each of the radio buttons.
	 */
	private String labels[];

	private boolean selected[];

	private boolean internalOptionIDs = false;

	private boolean showButtons;

	/**
	 * @param widgetID
	 * @param labels
	 * @param optionsIDs
	 * @param suggestedReferences
	 * @param internalOptionIDs
	 */
	public ListBox(String widgetID, String labels[], String optionsIDs[],
			String suggestedReferences[], boolean multiSelect,
			boolean showButtons, boolean internalOptionIDs) {
		this.setWidgetID(widgetID);
		this.internalOptionIDs = internalOptionIDs;

		this.setLabels(labels);
		this.setMultiSelect(multiSelect);
		this.setShowButtons(showButtons);
		this.selected = new boolean[labels.length];
		for (int i = 0; i < selected.length; i++) {
			this.selected[i] = false;
		}

		/*
		 * Create the WidgetOptions
		 */
		WidgetOptionID[] options;
		if (internalOptionIDs) {
			options = new WidgetOptionID[1];
			options[0] = new WidgetOptionID(this.getWidgetID(), null);
		} else {
			options = new WidgetOptionID[labels.length];
			for (int i = 0; i < labels.length; i++) {
				options[i] = new WidgetOptionID(optionsIDs[i],
						suggestedReferences != null ? suggestedReferences[i]
								: null);
			}
		}
		// Add the options (and register the widget in the Interaction Manager,
		// and get the reference codes.
		this.addWidgetOptionID(options);

		/*
		 * Create the visual elements
		 */

		// the top row
		this.hPanelTop = new HorizontalPanel();
		this.feedback = new InlineLabel("");
		this.referenceCode = new InlineLabel("");
		this.hPanelTop.add(this.feedback);
		this.hPanelTop.add(this.referenceCode);

		// the listbox items
		grid = new Grid(labels.length, 3);
		grid.setCellSpacing(0);
		grid.addClickHandler(this);
		if (this.isShowButtons()) {
			this.checkBoxes = new com.google.gwt.user.client.ui.SimpleCheckBox[labels.length];
		} else {
			this.checkBoxes = null;
		}

		/*
		 * fill in the rows
		 */

		for (int i = 0; i < labels.length; i++) {
			if (this.isShowButtons()) {
				if (!this.isMultiSelect()) {
					this.checkBoxes[i] = new com.google.gwt.user.client.ui.SimpleRadioButton(
							this.getWidgetID());
					// this.checkBoxes[i].setStyleName(this.getStyleName()+this.ITEMBUTTON_STYLENAME_SUFFIX);
				} else {
					this.checkBoxes[i] = new com.google.gwt.user.client.ui.SimpleCheckBox();
					// this.checkBoxes[i].setStyleName(this.getStyleName()+this.ITEMBUTTON_STYLENAME_SUFFIX);
				}
			}

			// hPanelItems[i].setStyleName(this.getStyleName()+this.ITEM_STYLENAME_SUFFIX);
			if (this.checkBoxes != null) {
				grid.setWidget(i, 0, this.checkBoxes[i]);
			}
			grid.setText(i, 1, this.labels[i]);
		}

		vPanel = new VerticalPanel();
		/*
		 * if (this.internalOptionIDs) { vPanel.add(this.hPanelTop); }
		 */
		vPanel.add(grid);

		// fill the reference labels by simulating an update from the
		// interaction manager.
		onReferenceCodesUpdated();

		initWidget(vPanel);
		// vPanel.setStyleName(this.getStyleName());

		this.setStyleName(this.DEFAULT_STYLENAME);
		this.setInputFeedbackDisplay(new ListBoxFeedback(this));
		// this.getLblFeedback()[0].setText("TESte");
	}

	@Override
	public void setStyleName(String style) {
		super.setStyleName(style);
		this.vPanel.setStyleName(style);
		// this.grid.setStyleName(style);
		HTMLTable.RowFormatter rowFormatter = grid.getRowFormatter();
		HTMLTable.CellFormatter cellFormatter = grid.getCellFormatter();

		for (int i = 0; i < this.grid.getRowCount(); i++) {
			rowFormatter.setStyleName(i, this.getStylePrimaryName()
					+ this.ITEM_STYLENAME_SUFFIX);
			if (this.checkBoxes != null) {
				this.checkBoxes[i].setStyleName(this.getStylePrimaryName()
						+ this.ITEMBUTTON_STYLENAME_SUFFIX);
			}

			cellFormatter.setStyleName(i, 1, this.getStylePrimaryName()
					+ this.ITEMLABEL_STYLENAME_SUFFIX);

			cellFormatter.setStyleName(i, 2, this.getStylePrimaryName()
					+ this.ITEMREFERENCECODE_STYLENAME_SUFFIX);

			// cellFormatter.setStyleName(i, 3, this.getStyleName()+
			// this.ITEMFEEDBACK_STYLENAME_SUFFIX);
		}

		this.hPanelTop.setStyleName(this.getStylePrimaryName()
				+ this.TOPITEM_STYLENAME_SUFFIX);
		this.feedback.setStyleName(this.getStylePrimaryName()
				+ this.ITEMFEEDBACK_STYLENAME_SUFFIX);
		this.referenceCode.setStyleName(this.getStylePrimaryName()
				+ this.ITEMREFERENCECODE_STYLENAME_SUFFIX);
	}

	@Override
	public void onReferenceCodesUpdated() {
		if (this.grid != null) {
			if (this.internalOptionIDs) {
				for (int i = 0; i < this.labels.length; i++) {
					this.grid.setText(i, 2, "(" + this.getReferenceCode(0)
							+ " " + (i + 1) + ")");
				}
				// this.grid.setText(0, 2, "("+this.getReferenceCode(0)+")");
				// this.referenceCode.setText("("+this.getReferenceCode(0)+")");
			} else {
				for (int i = 0; i < this.labels.length; i++) {
					this.grid.setText(i, 2, "(" + this.getReferenceCode(i)
							+ ")");
				}
			}
		}
	}

	@Override
	public void onClick(ClickEvent event) {
		Cell cell = ((Grid) event.getSource()).getCellForEvent(event);

		int i = cell.getRowIndex();

		// Simulate an input event
		InputEvent e;
		if (this.internalOptionIDs) {
			// the command uses (i+1) because, to the user, options start at 1.
			e = new InputEvent(new Identity("Click"), new Command(this
					.getReferenceCode(0)
					+ "." + (i + 1)), this.getOptionID(i));
		} else {
			e = new InputEvent(new Identity("Click"), new Command(""), this
					.getOptionID(i));
		}

		this.onInput(e);

	}

	public void deSelectAll() {
		for (int i = 0; i < this.selected.length; i++) {
			this.selected[i] = false;
			this.setVisuallySelected(i, false);
		}
	}

	public void change(int i) {
		Log.debug("");
		this.setSelected(i, !this.isSelected(i));
	}

	public void changeVisually(int i) {
		Log.debug("");
		this.setVisuallySelected(i, !this.isSelected(i));
	}

	public void setSelected(int i, boolean selected) {
		if (!this.isMultiSelect() && selected == true) {
			this.deSelectAll();
		}
		this.selected[i] = selected;
		this.setVisuallySelected(i, selected);
	}

	public void setVisuallySelected(int i, boolean selected) {
		if (this.checkBoxes != null) {
			this.checkBoxes[i].setChecked(selected);
		}

		HTMLTable.RowFormatter rowFormatter = grid.getRowFormatter();
		// HTMLTable.CellFormatter cellFormatter = grid.getCellFormatter();

		if (selected) {
			rowFormatter.setStyleName(i, this.getStyleName()
					+ this.ITEMSELECTED_STYLENAME_SUFFIX);
		} else {
			rowFormatter.setStyleName(i, this.getStyleName()
					+ this.ITEM_STYLENAME_SUFFIX);
		}
	}

	public boolean isSelected(int i) {
		return this.selected[i];
	}

	public int getSelected() {
		for (int i = 0; i < selected.length; i++) {
			if (this.selected[i]) {
				return i;
			}
		}
		return -1;
	}

	@Override
	public void onInput(InputEvent ie) {
		InputFeedback f = new InputFeedback(ie);
		this.getFeedbackSequencer().add(f);
	}

	/**
	 * Determines the user selected options' indexes, based on the InputEvent.
	 * 
	 * @param ie
	 * @return An array of indexes to the options of this listbox.
	 */
	public int[] getOptions(InputEvent ie) {
		Command cmd = ie.getCommand();
		Log.debug("" + cmd);
		int options[];
		boolean error = false;

		// user cannot make multiple selections on a single-selection listbox
		if (!this.isMultiSelect() && cmd.getParameters().length > 1) {
			return null;

			// command must have at least one paramter is internalOptionsIDs is
			// enabled
		} else if (this.internalOptionIDs && cmd.getParameters().length < 1) {
			return null;
		}
		Log.debug("1");
		if (this.internalOptionIDs) {
			// parse parameters for integers

			options = new int[cmd.getParameters().length];

			for (int i = 0; i < cmd.getParameters().length; i++) {

				try {
					// subtract 1 because the reference codes start at 1
					options[i] = Integer.parseInt(cmd.getParameter(i)) - 1;
					if (options[i] < 0 || options[i] >= this.getLabels().length) {
						error = true;
					}
				} catch (NumberFormatException e) {
					options[i] = -1;
					error = true;
				}
			}
		} else {
			options = new int[1];
			options[0] = this.getIndexOfWidgetOptionID(ie.getWidgetOptionID());
		}
		if (error) {
			return null;
		} else {
			return options;
		}
	}

	@Override
	public void start(InputFeedback inputFeedback) {
		Log.debug("");
		if (this.isInputEnabled()) {
			inputFeedback.setType(InputFeedback.Type.ACCEPTED);

			/*
			 * Determine which options were selected
			 */

			InputEvent ie = inputFeedback.getInputEvent();

			int options[] = this.getOptions(ie);

			if (options == null) {
				inputFeedback.setType(InputFeedback.Type.NOT_ACCEPTED);
				inputFeedback.setInfo(inputFeedback.getInputEvent()
						.getIdentity().getName());
			} else {

				/*
				 * Create string with list of selected options
				 */
				StringBuilder sb = new StringBuilder();

				for (int i = 0; i < options.length; i++) {
					// options start at zero, internally, but for the user
					// they start at 1, so add 1.
					sb.append(String.valueOf(options[i] + 1));
					if (i < options.length - 1) {
						sb.append(", ");
					}
				}

				/*
				 * If the listbox is single select it does not seem necessary
				 * to display the option numbers. There is only one
				 */
				if (this.isMultiSelect()) {
					inputFeedback.setInfo(inputFeedback.getInputEvent()
							.getIdentity().getName()
							+ ": " + sb.toString());
				} else {
					inputFeedback.setInfo(inputFeedback.getInputEvent()
							.getIdentity().getName());
				}
			}

			// Show the feedback
			this.getInputFeedbackDisplay().show(inputFeedback);

			/*
			 * Trigger application event only if the input was accepted.
			 */
			if (inputFeedback.getType() == InputFeedback.Type.ACCEPTED) {
				ActionEvent ae = new ActionEvent(ie.getIdentity(), this, ie
						.getWidgetOptionID(), options);
				if (this.isMultiSelect()) {
					for (int i = 0; i < options.length; i++) {
						this.change(options[i]);
					}
				} else {
					this.deSelectAll();
					this.setSelected(options[0], true);
				}
				this.fireActionEvent(ae);
			}
			// super.onInput(inputFeedback.getInputEvent());

		} else {
			inputFeedback.setType(InputFeedback.Type.NOT_ACCEPTED);
			inputFeedback.setInfo(inputFeedback.getInputEvent().getIdentity()
					.getName());

			this.getInputFeedbackDisplay().show(inputFeedback);
		}

	}

	public void stop(InputFeedback inputFeedback, boolean noMore) {
		Log.debug("");
		this.getInputFeedbackDisplay().hide(inputFeedback, noMore);
		this.setEnabled(this.isInputEnabled());
		for (int i = 0; i < this.labels.length; i++) {
			this.setSelected(i, this.isSelected(i));
		}
	}

	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		if (this.checkBoxes != null) {
			for (SimpleCheckBox c : this.checkBoxes) {
				if (c != null) {
					c.setEnabled(enabled);
				}
			}
		}
	}

	/**
	 * @param lblFeedback
	 *            the lblFeedback to set
	 */
	public void showFeedback(int index, String feedback) {
		if (index == -1) {
			this.feedback.setText(feedback);

			// if the listbox is not using internal options, then attach the top
			// row to display it.
			// if (!this.internalOptionIDs) {
			this.vPanel.insert(this.hPanelTop, 0);
			// }
		} else {
			HTMLTable.CellFormatter cellFormatter = grid.getCellFormatter();
			cellFormatter.setStyleName(index, 1, this.getStyleName()
					+ this.ITEMFEEDBACK_STYLENAME_SUFFIX);
			this.grid.setText(index, 1, feedback);
		}
	}

	public void hideFeedback(int index) {
		if (index == -1) {
			this.feedback.setText("");

			// remove the top row
			// if (!this.internalOptionIDs) {
			this.vPanel.remove(this.hPanelTop);
			// }
		} else {
			HTMLTable.CellFormatter cellFormatter = grid.getCellFormatter();
			cellFormatter.setStyleName(index, 1, this.getStyleName()
					+ this.ITEMLABEL_STYLENAME_SUFFIX);
			this.grid.setText(index, 1, this.labels[index]);
		}
	}

	/**
	 * @param multiSelect
	 *            the multiSelect to set
	 */
	public void setMultiSelect(boolean multiSelect) {
		this.multiSelect = multiSelect;
	}

	/**
	 * @return the multiSelect
	 */
	public boolean isMultiSelect() {
		return multiSelect;
	}

	/**
	 * @param labels
	 *            the labels to set
	 */
	public void setLabels(String labels[]) {
		this.labels = labels;
	}

	/**
	 * @return the labels
	 */
	public String[] getLabels() {
		return labels;
	}

	public void setShowButtons(boolean showButtons) {
		this.showButtons = showButtons;
	}

	public boolean isShowButtons() {
		return showButtons;
	}

}
