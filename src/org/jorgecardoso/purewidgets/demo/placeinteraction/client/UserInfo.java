package org.jorgecardoso.purewidgets.demo.placeinteraction.client;

public class UserInfo {

	private static String userIdentity;

	/**
	 * @return the userIdentity
	 */
	public static String getUserIdentity() {
		return userIdentity;
	}

	/**
	 * @param userIdentity the userIdentity to set
	 */
	public static void setUserIdentity(String userIdentity) {
		UserInfo.userIdentity = userIdentity;
	}
	
	
}
