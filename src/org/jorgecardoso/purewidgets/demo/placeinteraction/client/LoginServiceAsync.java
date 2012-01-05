/**
 * 
 */
package org.jorgecardoso.purewidgets.demo.placeinteraction.client;

/**
 * @author "Jorge C. S. Cardoso"
 *
 */


import com.google.gwt.user.client.rpc.AsyncCallback;

public interface LoginServiceAsync {
  public void login(String requestUri, AsyncCallback<LoginInfo> async);
}