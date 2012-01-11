/**
 * 
 */
package org.jorgecardoso.purewidgets.demo.everybodyvotes.server.dao;

/**
 * @author "Jorge C. S. Cardoso"
 *
 */
public class EBVPollOptionDao {

	private String option;
	
	private int votes;

	@SuppressWarnings("unused")
	private EBVPollOptionDao() {}
	
	public EBVPollOptionDao(String option) {
		this.option = option;
		this.votes = 0;
	}

	/**
	 * @return the option
	 */
	public String getOption() {
		return option;
	}

	/**
	 * @param option the option to set
	 */
	public void setOption(String option) {
		this.option = option;
	}

	/**
	 * @return the votes
	 */
	public int getVotes() {
		return votes;
	}

	/**
	 * @param votes the votes to set
	 */
	public void setVotes(int votes) {
		this.votes = votes;
	}
}
