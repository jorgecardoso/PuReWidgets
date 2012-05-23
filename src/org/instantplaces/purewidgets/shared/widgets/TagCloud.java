/**
 * 
 */
package org.instantplaces.purewidgets.shared.widgets;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Set;


import org.instantplaces.purewidgets.shared.Log;
import org.instantplaces.purewidgets.shared.events.ActionEvent;
import org.instantplaces.purewidgets.shared.events.ActionListener;



/**
 * 
 * @author "Jorge C. S. Cardoso"
 */

public class TagCloud extends Widget implements ActionListener {

	/**
	 * Compares two Tag objects alphabetically.
	 * 
	 * @author "Jorge C. S. Cardoso"
	 *
	 */
	public static class AlphaTagComparator implements Comparator<Tag>, Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		boolean desc;
		public AlphaTagComparator() {
			this(false);
		}
		
		public AlphaTagComparator(boolean desc) {
			this.desc = desc;
		}
		
		@Override
		public int compare(Tag arg0, Tag arg1) {
			
				int r = String.CASE_INSENSITIVE_ORDER.compare( arg0.getWord(), arg1.getWord() );
				return desc ? -r : r;
			
		}
		
	}
	
	/**
	 * Compares two Tag objects by their frequencies.
	 * 
	 * @author "Jorge C. S. Cardoso"
	 *
	 */
	public static class FrequencyTagComparator implements Comparator<Tag>, Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		boolean desc;
		public FrequencyTagComparator() {
			this(false);
		}
		
		public FrequencyTagComparator(boolean desc) {
			this.desc = desc;
		}
		@Override
		public int compare(Tag arg0, Tag arg1) {
			
				int r = arg0.getFrequency() - arg1.getFrequency();
				return desc ? -r : r;
			
		}
		
	}
	
	
	/**
	 * Represents a tag in the GuiTagCloud. A tag has a word, a real frequency (integer, non-negative number), and a normalized frequency in the tag cloud.
	 *  
	 * @author Jorge C. S. Cardoso
	 *
	 */
	public static class Tag {
		private String word;
		private int frequency;
		private float normalizedFrequency;
		
		public Tag(String word, int frequency, float normalizedFrequency) {
			this.setWord(word);
			this.setFrequency(frequency);
			this.setNormalizedFrequency(normalizedFrequency);
		}

		/**
		 * @return the frequency
		 */
		public int getFrequency() {
			return frequency;
		}

		/**
		 * @return the normalizedFrequency
		 */
		public float getNormalizedFrequency() {
			return normalizedFrequency;
		}

		/**
		 * @return the word
		 */
		public String getWord() {
			return word;
		}

		/**
		 * @param frequency the frequency to set
		 */
		public void setFrequency(int frequency) {
			this.frequency = frequency;
		}

		/**
		 * @param normalizedFrequency the normalizedFrequency to set
		 */
		public void setNormalizedFrequency(float normalizedFrequency) {
			this.normalizedFrequency = normalizedFrequency;
		}

		/**
		 * @param word the word to set
		 */
		public void setWord(String word) {
			this.word = word;
		}
		
		@Override
		public String toString() {
			return "Tag: " + this.word + ": " + this.frequency + ": " + this.normalizedFrequency;
		}
	}
	

	/**
	 * The current tags in this tag cloud.
	 */
	private HashMap <String, Tag> tags;
	
	private int minMaxFrequency = 10;
	
	/**
	 * The calculated maximum frequency on the current list of tags.
	 */
	private int maxFrequency;
	
	/**
	 * The calculated minimum frequency on the current list of tags.
	 */	
	private int minFrequency;
	
	private TextBox textBox;
	
	private boolean allowUserInput;
	
	public TagCloud( String widgetId ) {
		
		this(widgetId, null, null, true);
	}
	
	
	public TagCloud(String widgetId, String tags[], int frequency[], boolean allowUserInput) {
		super(widgetId, null, null);
		
		this.allowUserInput = allowUserInput;
		
		if ( this.allowUserInput ) {
			this.textBox = new TextBox(widgetId + "-textbox", "Suggest a tag");
			this.textBox.addActionListener(this);
			this.addDependentWidget( this.textBox );
		}
		
		this.createTags(tags, frequency);
		
	}
	
	public void addTag(String word, int frequency) {
		word = cleanTag(word);
		
		if ( this.tags.containsKey( word ) ) {
			this.tags.get( word ).setFrequency( frequency );
		} else {
			this.tags.put( word, new Tag(word, frequency, 0) );
		}
		this.recalculateNormalizedFrequencies();
		//this.addTagsToPanel();
	}
	
	public void addTag( TagCloud.Tag tag ) {
		this.tags.put( tag.getWord() , tag );
	}
	
	public Set<String> getKeywords() {
		return this.tags.keySet();
	}
	public boolean contains( String keyword ) {
		return this.tags.keySet().contains(this.cleanTag(keyword));
	}

	public TagCloud.Tag getTag(String keyword) {
		if ( null == keyword ) {
			return null;
		}
		
		keyword = this.cleanTag(keyword);
		if ( this.tags.containsKey( keyword ) ) {
			return this.tags.get( keyword );
		} else {
			return null;
		}
	}
	
	public ArrayList<Tag> getTagList() {
		return new ArrayList<Tag>(this.tags.values());
	}
	
	public ArrayList<Tag> getTagListSortedByAlpha() {
			
		ArrayList<Tag> tagList = new ArrayList<Tag>(this.tags.values());
		
		Collections.sort(tagList, new AlphaTagComparator(true));
		
		return tagList;
	}

	
	public ArrayList<Tag> getTagListSortedByFrequency() {
		
		ArrayList<Tag> tagList = new ArrayList<Tag>(this.tags.values());
		
		Collections.sort(tagList, new FrequencyTagComparator(true));
		
		return tagList;
	}
	
	
	
	public ArrayList<Tag> getTagListSortedByFrequency(boolean desc) {
		
		ArrayList<Tag> tagList = new ArrayList<Tag>(this.tags.values());
		
		Collections.sort(tagList, new FrequencyTagComparator(desc));
		
		return tagList;
	}
	
	public void setTagList(ArrayList<Tag> tagList) {
		this.tags.clear();
		for ( Tag tag : tagList ) {
			this.tags.put(tag.getWord(), tag);
		}
		this.recalculateNormalizedFrequencies();
		//this.addTagsToPanel();
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for ( String s : this.tags.keySet() ) {
			sb.append( this.tags.get(s).toString() ).append("\n");
		}
		return sb.toString();
	}
	
	private void calculateMinMaxFrequencies() {
		this.maxFrequency = 0;
		this.minFrequency = Integer.MAX_VALUE;
		for (Tag tag : this.tags.values()) {
			if (tag.getFrequency() > this.maxFrequency) {
				this.maxFrequency = tag.getFrequency();
			}
			if (tag.getFrequency() < this.minFrequency) {
				this.minFrequency = tag.getFrequency();
			}
		}
		
		
	//	Log.debug(this, "Max frequency: " + this.maxFrequency);
	//	Log.debug(this, "Min frequency: " + this.minFrequency);
	}
	
	/**
	 * @param word
	 * @return
	 */
	private String cleanTag(String word) {
		/*
		 * Trim and convert to lowercase, first to make sure there aren't tags that just differ
		 * by a space or upper/lower case
		 */
		word = word.trim().toLowerCase();
		return word;
	}
	
	
	
	private void createTags(String tags[], int frequency[]) {
		this.tags = new HashMap<String, Tag>();
		
		if ( null == tags || null == frequency ) {
			return;
		}
		//this.calculateMinMaxFrequencies(frequency);
		for (int i = 0; i < tags.length; i++) {
			
			tags[i] = this.cleanTag(tags[i]);
			
			// normalized frequency = frequency/(maxFrequency-minFrequency)
			Tag t = new Tag(tags[i], frequency[i], 0);//frequency[i]*1.0f/(this.maxFrequency-this.minFrequency));
			this.tags.put(t.getWord(), t);
		}
		
		this.recalculateNormalizedFrequencies();
	}
	

	private void recalculateNormalizedFrequencies() {
		this.calculateMinMaxFrequencies();
		
		if ( this.maxFrequency == this.minFrequency ) {
			for ( Tag tag : this.tags.values() ) {
				tag.setNormalizedFrequency( 0.5f );
			}
		} else {
			if ( this.maxFrequency < this.minMaxFrequency ) {
				this.maxFrequency = this.minMaxFrequency;
			}
			for ( Tag tag : this.tags.values() ) {
				
				tag.setNormalizedFrequency( (tag.getFrequency()-this.minFrequency)*1.0f/(this.maxFrequency-this.minFrequency) );
				
				//Log.debug(this, "Setting normalized frequency: " + tag.getWord() + ":" +tag.getFrequency() + ": " + tag.getNormalizedFrequency());
			}
		}
	}


	@Override
	public void onAction(ActionEvent<?> e) {
		Log.debug(this, "Received event from textbox");
		String text = this.cleanTag( (String)e.getParam() );
		
		if ( this.tags.containsKey( text ) ) {
			this.tags.get( text ).setFrequency( this.tags.get( text ).getFrequency() + 1 );
		} else {
			this.addTag(text, 1);
		}
		
		/*
		 * Fire app event
		 */
		ActionEvent<TagCloud> ae = new ActionEvent<TagCloud>(e.getPersona(), this, e.getSelection(), text); 
		
		this.fireActionEvent(ae);
	}


	/**
	 * @return the textBox
	 */
	public TextBox getTextBox() {
		return textBox;
	}



	

}
