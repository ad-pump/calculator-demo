package com.adpumb.ads.mediation;

public interface RetryableAd<T> extends Comparable<T> {
    float getEcpm();
    boolean isAdLoaded();
    void loadAd();
    boolean shouldReload();
}
