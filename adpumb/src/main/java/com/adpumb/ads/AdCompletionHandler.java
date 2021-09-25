package com.adpumb.ads;

public interface AdCompletionHandler {
    /*
    Ad completed will be called when the user watched the ad or the ad failed to load.
    When the ad failed to load, sucess will false and when the user watched and closed the ad, the success will be true
     */
    public void adCompleted(boolean success);
}
