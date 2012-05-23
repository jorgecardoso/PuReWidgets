package org.purewidgets.shared.widgetmanager;

public interface Callback<T> {

	public void onSuccess(T returnValue);
	public void onFailure(Throwable exception);
}
