/**
 * 
 */
package org.jorgecardoso.purewidgets.demo.client.placeinteractionwebpage;

/**
 * @author "Jorge C. S. Cardoso"
 *
 */

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("login")
public interface LoginService extends RemoteService {
  public LoginInfo login(String requestUri);
}