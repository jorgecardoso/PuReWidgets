package org.purewidgets.client.widgets.youtube;

public enum PlayerError {
	/*This event is fired when an error in the player occurs. The possible error codes are 2, 100, 101, and 150:
		The 2 error code is broadcast when a request contains an invalid parameter. For example, this error occurs if you specify a video ID that does not have 11 characters, or if the video ID contains invalid characters, such as exclamation points or asterisks.
		The 100 error code is broadcast when the video requested is not found. This occurs when a video has been removed (for any reason), or it has been marked as private.
		The 101 error code is broadcast when the video requested does not allow playback in the embedded players.
		The error code 150 is the same as 101, it's just 101 in disguise!*/
		INVALID_PARAMETER,
	    NOT_FOUND,
	    NO_EMBED
}
