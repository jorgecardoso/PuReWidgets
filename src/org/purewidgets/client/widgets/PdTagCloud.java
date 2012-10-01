/**
 * 
 */
package org.purewidgets.client.widgets;

import java.util.ArrayList;

import java.util.Collections;
import java.util.Set;

import org.purewidgets.shared.events.ActionEvent;
import org.purewidgets.shared.events.ActionListener;
import org.purewidgets.shared.widgets.TagCloud;
import org.purewidgets.shared.widgets.TagCloud.Tag;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.SimplePanel;


/**
 * A visual representation of a tag cloud that allows users to add new tags.
 * 
 * 
 * <h3>CSS Style Rules</h3>
 * <dl>
 * <dt>.pw-GuiTagCloud</dt>
 * <dd>the outer &lt;div&gt; element</dd>
 * <dt>.pw-GuiTagCloud .tag</dt>
 * <dd>the &lt;span&gt; element for each word</dd>
 * <dt>.pw-GuiTagCloud .tagspanel</dt>
 * <dd>the panel that holds the list of tags and the textbox</dd>
 * 
 * </dl>
 * 
 * 
 * @author Jorge C. S. Cardoso
 * 
 */
public class PdTagCloud extends PdWidget {
	
	
	
	public static final String DEFAULT_STYLENAME = "pw-GuiTagCloud";
	public static final String TAGS_PANEL_STYLENAME = "tagspanel";

	public static final String TAG_STYLENAME = "tag";
	
	/**
	 * Possible ordering of tags in the tag cloud.
	 * 
	 * @author "Jorge C. S. Cardoso"
	 *
	 */
	public enum ORDER {
		/**
		 * Tags are ordered by name.
		 */
		ALPHA, 
		/**
		 * Tags are ordered by frequency.
		 */
		FREQUENCY}; 
	
	
	/**
	 * The outer panel that holds the tags
	 */
	private SimplePanel panel;
	
	/**
	 * The panel that holds the textbox for user tag suggestions
	 */
	private SimplePanel textBoxPanel;
	
	/**
	 * The order of the tags in the tag cloud.
	 */
	private ORDER order;
	
	/**
	 * The underlying tagcloud that holds the data for this GuiTagCloud.
	 */
	private TagCloud tagCloud;
	
	private PdTextBox guiTextBox;
	
	private boolean allowUserInput;
	
	private int currentFontSize;
	
	/**
	 * Creates a new tag cloud with the specified widget id.
	 * @param widgetId The widget id.
	 */
	@UiConstructor
	public PdTagCloud( String widgetId ) {
		this(widgetId, null, null, false);
	}
	
	/**
	 * Creates a new tag cloud with the specified widget id and control over whether users
	 * can add new tags.
	 * 
	 * @param widgetId The widget id
	 * @param allowUserInput if true, users can add new tags; if false, they cannot.
	 */
	public PdTagCloud( String widgetId, boolean allowUserInput ) {
		this(widgetId, null, null, allowUserInput);
	}
	
	/**
	 * 
	 * 
	 * Creates a new tag cloud with the specified widget id, initial tag and frequency sets, and control over whether users
	 * can add new tags.
	 * 
	 * @param widgetId The widget id
	 * @param tags The initial set of tags
	 * @param frequency  The initial frequency for each tag
	 * @param allowUserInput if true, users can add new tags; if false, they cannot.
	 */
	public PdTagCloud( String widgetId, String tags[], int frequency[], boolean allowUserInput ) {
		super(widgetId);
		this.order = ORDER.ALPHA;
		this.allowUserInput = allowUserInput;
		
		/*
		 * Start with a font size of 15px
		 */
		this.currentFontSize = 15;
		
		/*
		 * The TagCloud that holds the data
		 */
		this.tagCloud = new TagCloud( widgetId, tags, frequency, this.allowUserInput );
		
		if ( this.allowUserInput ) {
			/*
			 * The GuiTextBox that allows users to input tags
			 */
			guiTextBox = new PdTextBox(widgetId+"textbox", this.tagCloud.getTextBox().getShortDescription(), null );
			guiTextBox.setLongDescription("Enter a word to contribute to the video tag cloud");
			textBoxPanel = new SimplePanel();
			textBoxPanel.add(guiTextBox);
			textBoxPanel.getElement().getStyle().setProperty("textAlign", "center");
		}
		
		this.setWidget(this.tagCloud);
		
		/*
		 * We listen for action events from the TagCloud and send them to the app
		 */
		this.tagCloud.addActionListener(new ActionListener() {

			@Override
			public void onAction(ActionEvent<?> e) {
				ActionEvent<PdTagCloud> ae = new ActionEvent<PdTagCloud>(e.getUserId(), e.getNickname(), PdTagCloud.this, null, e.getParam());
				PdTagCloud.this.updateGui();
				PdTagCloud.this.fireActionEvent(ae);
			}
			
		});
		
		panel = new SimplePanel();
		
		initWidget(panel);
		this.setStyleName(DEFAULT_STYLENAME);
		
		/*
		 * Create the Gui for the tag cloud.
		 */
		
		this.updateGui();
	}
	

	/**
	 * Sets the width of the tag cloud box.
	 * @param w The width of the tag cloud box
	 */
	@Override
	public void setWidth(String w) {
		super.setWidth(w);
		this.panel.setWidth(w);
	}

	/**
	 * Updates the representation of tags in the tag box
	 */
	public void updateGui() {
		
		this.panel.clear();
		//this.panel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		
		/*
		 * We need an intermediate panel so that we can calculate the font size to fill the main panel.
		 */
		FlowPanel tagsPanel = new FlowPanel();
		tagsPanel.addStyleName(TAGS_PANEL_STYLENAME);
		
		//this.panel.add(tagsPanel);
		
		
		this.panel.add(tagsPanel);
		
		ArrayList<TagCloud.Tag> tagList = this.tagCloud.getTagList();
		
		switch ( this.order ) {
		case ALPHA:
			Collections.sort(tagList, new TagCloud.AlphaTagComparator(false));
			break;
		case FREQUENCY:
			Collections.sort(tagList, new TagCloud.FrequencyTagComparator(true));
			break;
		}
		
		
		for (TagCloud.Tag t : tagList) {
			InlineLabel l = new InlineLabel(t.getWord()+ " ");
			l.addStyleName(TAG_STYLENAME);
			l.getElement().getStyle().setFontSize((t.getNormalizedFrequency()*(3-1)+1), Unit.EM);
			//l.getElement().getStyle().setFontSize((int)(t.getNormalizedFrequency()*(this.maxFontSize-this.minFontSize)+this.minFontSize), Unit.PX);
			tagsPanel.add(l);
		}
		
		if ( this.allowUserInput ) {
			tagsPanel.add( this.textBoxPanel );
			
			//tagsPanel.addStyleName(TEXTBOX_PANEL_STYLE_SUFFIX);
		}
		
		
//		int current = 15;
//		
//		
//		int maxHeight = this.panel.getOffsetHeight();
//		int maxWidth = this.panel.getOffsetWidth();
//		int count = 0;
//		int old = current;
//		boolean change = true;
//		int difW = 0;
//		int difH = 0;
//		while(change && maxHeight > 0 && maxWidth > 0) {
//			
//			tagsPanel.getElement().getStyle().setFontSize(current, Unit.PX);
//			difW = maxWidth-tagsPanel.getOffsetWidth();
//			difH = maxHeight-tagsPanel.getOffsetHeight();
//			
//			if ( difW < 1 || difH < 1 ) {
//				//tagsPanel.getElement().getStyle().setFontSize(current-1, Unit.PX);
//				break;
//			}
//			
//			/*
//			 * Calculate the ratio of the size of the max dif to the size of the tags panel
//			 */
//			float percent;
//			if (difW > difH ) {
//				percent = difW*1.0f/maxWidth;
//			} else {
//				percent = difH*1.0f/maxHeight;
//			}
//			
//			old = current;
//			current = current+(int)(current*percent);
//			
//			if (current == old) {
//				change = false;
//			}
//			
//			count++;
//		} 
//		
//
//		
//		Log.warn("tags panel width: " + tagsPanel.getOffsetWidth());
//		Log.warn("tags panel height: " + tagsPanel.getOffsetHeight());
//		Log.warn("panel width: " + this.panel.getOffsetWidth());
//		Log.warn("panel height: " + this.panel.getOffsetHeight());
//		Log.warn("count:" + count);
		
		
//		int min = 15;
//		int max = 50;
//		
//		Log.warn("tags panel width: " + tagsPanel.getOffsetWidth());
//		Log.warn("tags panel height: " + tagsPanel.getOffsetHeight());
//		Log.warn("panel width: " + this.panel.getOffsetWidth());
//		Log.warn("panel height: " + this.panel.getOffsetHeight());
//		int maxHeight = this.panel.getOffsetHeight();
//		int maxWidth = this.panel.getOffsetWidth();
//		
//		int count = 0;
//		while ( max-min > 1 ) {
//			int half = (max-min)/2+min;
//			tagsPanel.getElement().getStyle().setFontSize(half, Unit.PX);
//			
//			if (tagsPanel.getOffsetWidth() > maxWidth || tagsPanel.getOffsetHeight() > maxHeight) {
//				max = half;
//			} else {
//				min = half;
//			}
//			Log.warn("half: "  + half);
//			
//			count++;
//		}
//		tagsPanel.getElement().getStyle().setFontSize(min, Unit.PX);
//		Log.warn("tags panel width: " + tagsPanel.getOffsetWidth());
//		Log.warn("tags panel height: " + tagsPanel.getOffsetHeight());
//		Log.warn("panel width: " + this.panel.getOffsetWidth());
//		Log.warn("panel height: " + this.panel.getOffsetHeight());
//		Log.warn("count:" + count);
		
		tagsPanel.getElement().getStyle().setFontSize(this.currentFontSize, Unit.PX);
		
		int maxHeight = this.panel.getOffsetHeight();
		int maxWidth = this.panel.getOffsetWidth();
		int current = this.currentFontSize;
//		int count = 0;
//		Log.warn("tags panel width: " + tagsPanel.getOffsetWidth());
//		Log.warn("tags panel height: " + tagsPanel.getOffsetHeight());
//		Log.warn("panel width: " + this.panel.getOffsetWidth());
//		Log.warn("panel height: " + this.panel.getOffsetHeight());

		if ( maxHeight < tagsPanel.getOffsetHeight() || maxWidth < tagsPanel.getOffsetWidth() ) { // we need to decrease
			while (current > 5 && (tagsPanel.getOffsetWidth() >= maxWidth || tagsPanel.getOffsetHeight() >= maxHeight) ) {
				current--;
				tagsPanel.getElement().getStyle().setFontSize(current, Unit.PX);
				
//				Log.warn("tags panel width: " + tagsPanel.getOffsetWidth());
//				Log.warn("tags panel height: " + tagsPanel.getOffsetHeight());
//				count++;
			}
			
		} else {
			while (current < 100 && tagsPanel.getOffsetWidth() <= maxWidth && tagsPanel.getOffsetHeight() < maxHeight) {
				current++;
				tagsPanel.getElement().getStyle().setFontSize(current, Unit.PX);
				
//				Log.warn("tags panel width: " + tagsPanel.getOffsetWidth());
//				Log.warn("tags panel height: " + tagsPanel.getOffsetHeight());
//				count++;
			}
			/*
			 * Revert the last change: current-1
			 */
			current--;
			
			tagsPanel.getElement().getStyle().setFontSize(current, Unit.PX);
		}
		this.currentFontSize = current;
//		Log.warn("tags panel width: " + tagsPanel.getOffsetWidth());
//		Log.warn("tags panel height: " + tagsPanel.getOffsetHeight());
//		Log.warn("panel width: " + this.panel.getOffsetWidth());
//		Log.warn("panel height: " + this.panel.getOffsetHeight());
//		Log.warn("count:" + count);
		
	}
	
	/**
	 * Gets the current tag list.
	 * 
	 * @return The tag list.
	 */
	public ArrayList<Tag> getTagList() {
		return this.tagCloud.getTagList();
	}
	
	/**
	 * Sets the tag list for this tag cloud.
	 * 
	 * @param tagList The tag list to set.
	 */
	public void setTagList(ArrayList<Tag> tagList) {
		this.tagCloud.setTagList(tagList);
		
		//Log.warn(this, "set tag list");
		//this.updateGui();
		//this.addTagsToPanel();
	}
	
	/**
	 * Gets the tag list, ordered by name of the tag.
	 * @return The tag list, ordered by name.
	 */
	public ArrayList<Tag> getTagListSortedByAlpha() {
		
		return this.tagCloud.getTagListSortedByAlpha();
	}
	
	/**
	 * Adds a new tag to the tag cloud.
	 * 
	 * @param word The word to add.
	 * @param frequency The frequency of that word.
	 */
	public void addTag(String word, int frequency) {
		this.tagCloud.addTag(word, frequency);
		//Log.warn(this, "add tag");
		//this.updateGui();
	}
	
	/**
	 * Gets the tag list, sorted by frequency of the tag.
	 * @return The tag list, sorted by frequency of the tag.
	 */
	public ArrayList<Tag> getTagListSortedByFrequency() {
		
		return this.tagCloud.getTagListSortedByFrequency();
	}
	
	/**
	 * Gets a tag from the tag cloud.
	 * 
	 * @param keyword The word to get.
	 * @return The tag associated with the specified word.
	 */
	public TagCloud.Tag getTag(String keyword) {
		return this.tagCloud.getTag(keyword);
	}
	
	/**
	 * Checks if this tag cloud contains a given word.
	 * @param keyword The word to check.
	 * @return true if the tag cloud contains the specified word, false otherwise.
	 */
	public boolean contains( String keyword ) {
		return this.tagCloud.contains(keyword);
	}
	
	/**
	 * Gets a set with all the words of this tag cloud.
	 * 
	 * @return A set with all the words of this tag cloud.
	 */
	public Set<String> getKeywords() {
		return this.tagCloud.getKeywords();
	}
	
	/**
	 * Gets a list of tags ordered by frequency.
	 * 
	 * @param desc if true, the tags are ordered in descending manner; if false, the tags are ordered in ascending manner.
	 * @return
	 */
	public ArrayList<Tag> getTagListSortedByFrequency(boolean desc) {
		
		return this.tagCloud.getTagListSortedByFrequency(desc);
	}
	
	/**
	 * Gets the ordering for the tags in the visual representation.
	 * 
	 * @return The ordering for the tags in the visual representation.
	 */
	public ORDER getOrder() {
		return order;
	}

	/**
	 * Sets the ordering for the tags in the visual representation.
	 * 
	 * @param order The ordering for the tags in the visual representation.
	 */	
	public void setOrder(ORDER order) {
		this.order = order;
	}


}
