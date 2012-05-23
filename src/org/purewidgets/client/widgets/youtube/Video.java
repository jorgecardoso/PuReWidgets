/**
 * 
 */
package org.purewidgets.client.widgets.youtube;

/**
 * @author Jorge C. S. Cardoso
 *
 */
public class Video {

	private String thumbnail;
	private String id;
	private String title;
	private String author;
	private String description;
	private Rating rating;
	private long favoriteCount;
	private long viewCount;
	private long duration;
	private String []keywords;
	
	/**
	 * Convenience field, not originated from youtube, that allows applications to 
	 * specify which search tags resulted in this video.
	 */
	private String originatingTags;
	
	/**
	 * 
	 * @param id
	 * @param title
	 * @param author
	 * @param thumbnail
	 */
	public Video(String id, String title, String author, String thumbnail) {
		this.setId(id);
		this.setTitle(title);
		this.setAuthor(author);
		this.setThumbnail(thumbnail);
	}
	
	
	


	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}


	public String getThumbnail() {
		return thumbnail;
	}


	
	public void setId(String id) {
		this.id = id;
	}


	public String getId() {
		return id;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public String getTitle() {
		return title;
	}


	public void setAuthor(String author) {
		this.author = author;
	}


	public String getAuthor() {
		return author;
	}

	public String getDescription() {
		return this.description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}


	/**
	 * @param rating the rating to set
	 */
	public void setRating(Rating rating) {
		this.rating = rating;
	}


	/**
	 * @return the rating
	 */
	public Rating getRating() {
		return rating;
	}


	/**
	 * @param favoriteCount the favoriteCount to set
	 */
	public void setFavoriteCount(long favoriteCount) {
		this.favoriteCount = favoriteCount;
	}


	/**
	 * @return the favoriteCount
	 */
	public long getFavoriteCount() {
		return favoriteCount;
	}


	/**
	 * @param viewCount the viewCount to set
	 */
	public void setViewCount(long viewCount) {
		this.viewCount = viewCount;
	}


	/**
	 * @return the viewCount
	 */
	public long getViewCount() {
		return viewCount;
	}


	/**
	 * @param keywords the keywords to set
	 */
	public void setKeywords(String [] keywords) {
		this.keywords = keywords;
	}


	/**
	 * @return the keywords
	 */
	public String [] getKeywords() {
		return keywords;
	}


	/**
	 * @return the originatingTags
	 */
	public String getOriginatingTags() {
		return originatingTags;
	}


	/**
	 * @param originatingTags the originatingTags to set
	 */
	public void setOriginatingTags(String originatingTags) {
		this.originatingTags = originatingTags;
	}


	/**
	 * @return the duration
	 */
	public long getDuration() {
		return duration;
	}


	/**
	 * @param duration the duration to set
	 */
	public void setDuration(long duration) {
		this.duration = duration;
	}
	
	
}
