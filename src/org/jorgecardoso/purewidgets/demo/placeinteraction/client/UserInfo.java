package org.jorgecardoso.purewidgets.demo.placeinteraction.client;

public class UserInfo {

	private static String identity;
	
	private static String username;

	/**
	 * @return the userIdentity
	 */
	public static String getUsername() {
		return username;
	}

	/**
	 * @param username the userIdentity to set
	 */
	public static void setUsername(String username) {
		UserInfo.username = username;
	}

	/**
	 * @return the identity
	 */
	public static String getIdentity() {
		return identity;
	}

	/**
	 * @param identity the identity to set
	 */
	public static void setIdentity(String identity) {
		UserInfo.identity = identity;
	}
	
	
}
