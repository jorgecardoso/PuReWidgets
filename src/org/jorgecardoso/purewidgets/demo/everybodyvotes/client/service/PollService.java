/**
 * 
 */
package org.jorgecardoso.purewidgets.demo.everybodyvotes.client.service;
import java.util.List;

import org.jorgecardoso.purewidgets.demo.everybodyvotes.shared.dao.EBVPollDao;

import com.google.gwt.user.client.rpc.RemoteService;

/**
 * @author "Jorge C. S. Cardoso"
 *
 */
public interface PollService extends RemoteService {
	public EBVPollDao getPoll(String pollId);
	
	public List<EBVPollDao> getPolls(String placeId);
	public List<EBVPollDao> getActivePolls(String placeId);
	public List<EBVPollDao> getClosedPolls(String placeId);
	
	public void savePoll( EBVPollDao poll );
	
	public void deletePoll( EBVPollDao poll);
	
	public void updatePolls(String placeId, String applicationId);
}
