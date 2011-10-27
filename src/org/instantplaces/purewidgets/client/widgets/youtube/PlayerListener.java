package org.instantplaces.purewidgets.client.widgets.youtube;


/**
 * 
 * @author Jorge C. S. Cardoso
 *
 */
public interface PlayerListener {
    public void onReady();
    public void onStateChange(PlayerState state);
    public void onLoading(int percent);
    public void onError(PlayerError error);
}
