/**
 * 
 */
package org.jorgecardoso.purewidgets.demo.everybodyvotes.shared.dao;

import java.io.Serializable;
import java.util.ArrayList;
import javax.persistence.Embedded;
import javax.persistence.Id;

import org.instantplaces.purewidgets.shared.Log;

/**
 * @author "Jorge C. S. Cardoso"
 *
 */
public class EBVPollDao implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2L;


	/**
	 * the poll id in the datastore
	 */
	@Id 
    private Long pollId;
	
	
	/**
	 * The placeId
	 */
	private String placeId;
	
	
	/**
	 * Show this poll after this date
	 */
	private long showAfter;
	
	/**
	 * This poll closes on this date (should be between showAfter and before showUntil)
	 */
	private long closesOn;
	
	/**
	 * Show this poll until this date
	 */
	private long showUntil;
	
	
	/**
	 * The poll's question
	 */
	private String pollQuestion;
	
	/**
	 * The list of options for the poll
	 */
	 @Embedded
	private ArrayList<EBVPollOptionDao> pollOptions;
	
	/**
	 * This simulates a hashmap
	 * The votes for this poll in the form of userid->pollOptionId
	 */
	private ArrayList<String> voters;
	private ArrayList<String> votersOptions;
	
	
	
	public EBVPollDao() {
		//this.pollId = Math.random()*100000000+"";
		pollOptions = new ArrayList<EBVPollOptionDao>();
		voters = new ArrayList<String>();
		votersOptions = new ArrayList<String>();
	}

	
	public void vote(String userId, String optionId) {
	
		/*
		 * Get the voted option
		 */
		EBVPollOptionDao option = null;
		for (EBVPollOptionDao o : this.pollOptions) {
			if ( o.getOption().equals(optionId) ) {
				option = o;
				break;
			}
		}
		
		if ( null == option ) {
			Log.warn(this, "Invalid poll option id: " + optionId);
			return;
		}
		
		String existingOptionVoted = null;
		int indexVoter = this.voters.indexOf(userId);
		if ( indexVoter > -1 ) {
			existingOptionVoted = this.votersOptions.get( indexVoter );
		}
		if ( null == existingOptionVoted ) { // new user voting
			this.voters.add(userId);
			this.votersOptions.add(optionId);
			
			option.setVotes(option.getVotes()+1);
			
		} else { //user already voted
			/*
			 * Delete old vote
			 */
			/*
			 * Get the old voted option
			 */
			EBVPollOptionDao oldOption = null;
			for (EBVPollOptionDao o : this.pollOptions) {
				if ( o.getOption().equals(existingOptionVoted) ) {
					oldOption = o;
					break;
				}
			}
			if ( null != oldOption ) {
				oldOption.setVotes(oldOption.getVotes()-1);
			} 
			
			/*
			 * Set the new vote
			 */
			//this.votersToOptions.put(userId, optionId);
			
			//this.voters.add(userId);
			this.votersOptions.set(indexVoter, optionId);
			option.setVotes(option.getVotes()+1);
			
			
		}
	}

	/**
	 * @return the placeId
	 */
	public String getPlaceId() {
		return placeId;
	}


	/**
	 * @param placeId the placeId to set
	 */
	public void setPlaceId(String placeId) {
		this.placeId = placeId;
	}


	/**
	 * @return the pollQuestion
	 */
	public String getPollQuestion() {
		return pollQuestion;
	}


	/**
	 * @param pollQuestion the pollQuestion to set
	 */
	public void setPollQuestion(String pollQuestion) {
		this.pollQuestion = pollQuestion;
	}


	/**
	 * @return the pollOptions
	 */
	public ArrayList<EBVPollOptionDao> getPollOptions() {
		return pollOptions;
	}


	/**
	 * @param pollOptions the pollOptions to set
	 */
	public void setPollOptions(ArrayList<EBVPollOptionDao> pollOptions) {
		this.pollOptions = pollOptions;
	}


	/**
	 * @return the pollId
	 */
	public Long getPollId() {
		return pollId;
	}


	/**
	 * @param pollId the pollId to set
	 */
	public void setPollId(Long pollId) {
		this.pollId = pollId;
	}


	/**
	 * @return the showAfter
	 */
	public long getShowAfter() {
		return showAfter;
	}


	/**
	 * @param showAfter the showAfter to set
	 */
	public void setShowAfter(long showAfter) {
		this.showAfter = showAfter;
	}


	/**
	 * @return the showUntil
	 */
	public long getShowUntil() {
		return showUntil;
	}


	/**
	 * @param showUntil the showUntil to set
	 */
	public void setShowUntil(long showUntil) {
		this.showUntil = showUntil;
	}


	/**
	 * @return the closesOn
	 */
	public long getClosesOn() {
		return closesOn;
	}


	/**
	 * @param closesOn the closesOn to set
	 */
	public void setClosesOn(long closesOn) {
		this.closesOn = closesOn;
	}


	
	
}
