/**
 * 
 */
package org.instantplaces.purewidgets.client.widgets;

import java.util.ArrayList;

import java.util.Collections;
import java.util.Set;

import org.instantplaces.purewidgets.shared.Log;
import org.instantplaces.purewidgets.shared.events.ActionEvent;
import org.instantplaces.purewidgets.shared.events.ActionListener;
import org.instantplaces.purewidgets.shared.widgets.TagCloud;
import org.instantplaces.purewidgets.shared.widgets.TagCloud.Tag;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.SimplePanel;


/**
 * The tag cloud widget is a visual representation of a tag cloud.
 * 
 * 
 * <h3>CSS Style Rules</h3>
 * <dl>
 * <dt>.instantplaces-GuiTagCloud</dt>
 * <dd>the outer &lt;div&gt; element</dd>
 * <dt>.instantplaces-GuiTagCloud .tag</dt>
 * <dd>the &lt;span&gt; element for each word</dd>
 * <dt>.instantplaces-GuiTagCloud .tagspanel</dt>
 * <dd>the panel that holds the list of tags and the textbox</dd>
 * 
 * </dl>
 * 
 * 
 * @author Jorge C. S. Cardoso
 * 
 */
public class GuiTagCloud extends GuiWidget implements ActionListener {
	
	
	
	public static final String DEFAULT_STYLENAME = "instantplaces-GuiTagCloud";
	public static final String TAGS_PANEL_STYLENAME = "tagspanel";
	//public static final String 
	public static final String TAG_STYLENAME = "tag";
	
	/**
	 * Possible ordering of tags in the tag cloud.
	 * 
	 * @author "Jorge C. S. Cardoso"
	 *
	 */
	public enum ORDER {ALPHA, FREQUENCY}; 
	
	
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
	
	private GuiTextBox guiTextBox;
	
	private boolean allowUserInput;
	
	private int currentFontSize;
	
	public GuiTagCloud( String widgetId ) {
		this(widgetId, null, null, true);
	}
	
	public GuiTagCloud( String widgetId, boolean allowUserInput ) {
		this(widgetId, null, null, allowUserInput);
	}
	
	/**
	 * The TagCloud is actually a composite widget with just one component -- the TextBox -- so, although,
	 * we give it a widgetId, the TagCloud itself is never sent to the Widgetmanager, only the TextBox.
	 * 
	 * We never call sendToServer() in the constructor.
	 * 
	 * @param widgetId
	 * @param tags
	 * @param frequency
	 * @param allowUserInput
	 */
	public GuiTagCloud( String widgetId, String tags[], int frequency[], boolean allowUserInput ) {
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
			guiTextBox = new GuiTextBox( this.tagCloud.getTextBox(), null );
			guiTextBox.setLongDescription("Enter a word to contribute to the video tag cloud");
			textBoxPanel = new SimplePanel();
			textBoxPanel.add(guiTextBox);
			textBoxPanel.getElement().getStyle().setProperty("textAlign", "center");
		}
		
		this.setWidget(this.tagCloud);
		
		/*
		 * We listen for action events from the TagCloud and send them to the app
		 */
		this.tagCloud.addActionListener(this);
		
		panel = new SimplePanel();
		
		initWidget(panel);
		this.setStyleName(DEFAULT_STYLENAME);
		
		/*
		 * Create the Gui for the tag cloud.
		 */
		Log.warn(this, "finishing constructor");
		this.updateGui();
		
	}
	

	@Override
	public void setWidth(String w) {
		super.setWidth(w);
		this.panel.setWidth(w);
	}

	public void updateGui() {
		Log.warn(this, "updateGUI");
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
		int count = 0;
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
				count++;
			}
			
		} else {
			while (current < 100 && tagsPanel.getOffsetWidth() <= maxWidth && tagsPanel.getOffsetHeight() < maxHeight) {
				current++;
				tagsPanel.getElement().getStyle().setFontSize(current, Unit.PX);
				
//				Log.warn("tags panel width: " + tagsPanel.getOffsetWidth());
//				Log.warn("tags panel height: " + tagsPanel.getOffsetHeight());
				count++;
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
	
	
	public ArrayList<Tag> getTagList() {
		return this.tagCloud.getTagList();
	}
	
	public void setTagList(ArrayList<Tag> tagList) {
		this.tagCloud.setTagList(tagList);
		
		//Log.warn(this, "set tag list");
		//this.updateGui();
		//this.addTagsToPanel();
	}
	
	public ArrayList<Tag> getTagListSortedByAlpha() {
		
		return this.tagCloud.getTagListSortedByAlpha();
	}
	
	public void addTag(String word, int frequency) {
		this.tagCloud.addTag(word, frequency);
		//Log.warn(this, "add tag");
		//this.updateGui();
	}
	
	public ArrayList<Tag> getTagListSortedByFrequency() {
		
		return this.tagCloud.getTagListSortedByFrequency();
	}
	
	public TagCloud.Tag getTag(String keyword) {
		return this.tagCloud.getTag(keyword);
	}
	
	public boolean contains( String keyword ) {
		return this.tagCloud.contains(keyword);
	}
	
	public Set<String> getKeywords() {
		return this.tagCloud.getKeywords();
	}
	
	public ArrayList<Tag> getTagListSortedByFrequency(boolean desc) {
		
		return this.tagCloud.getTagListSortedByFrequency(desc);
	}
	
	public ORDER getOrder() {
		return order;
	}

	public void setOrder(ORDER order) {
		this.order = order;
	}

	@Override
	public void onAction(ActionEvent<?> e) {
		ActionEvent<GuiTagCloud> ae = new ActionEvent<GuiTagCloud>(e.getPersona(), this, null, e.getParam());
		this.updateGui();
		this.fireActionEvent(ae);
	}
}
