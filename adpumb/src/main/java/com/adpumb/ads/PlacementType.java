package com.adpumb.ads;


import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


public enum PlacementType {
    EXECUTOR_RESUME(false, 5 * 60),
    LAUNCH_RESUME(false, 5 * 60),
    RESUME(false, 5 * 60),
    REWARD(true, 0),
    CONNECT(true, 5 * 60);
    //EXECUTOR_NATIVE(false,15, AdmobNativeAd.class, ExecutorActivity.class);


    private boolean showLoader;
    private long adInterval;
    private Class<? extends KempaAd> implType;
    private Set<Class> allowedActivity;

    PlacementType(boolean showLoader, long intervalInSeconds, Class<? extends KempaAd> type, Class... allowed) {
        this.showLoader = showLoader;
        this.adInterval = intervalInSeconds;
        implType = type;
        allowedActivity = new HashSet<>(Arrays.asList(allowed));
    }

    PlacementType(boolean showLoader, long intervalInSeconds) {
        this(showLoader, intervalInSeconds, KempaInterstitialAd.class);
    }

    public boolean isAllowedForAcitivty(Class currentActivity) {
        if (allowedActivity.isEmpty()) {
            //allowed none is allowed all
            return true;
        }
        return allowedActivity.contains(currentActivity);
    }


    public Class<? extends KempaAd> getType() {
        return implType;
    }

    public boolean isShowLoader() {
        return showLoader;
    }

    public long getAdInterval() {
        return adInterval;
    }
    }
