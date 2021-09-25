package com.adpumb.ads.display;

import android.app.Activity;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class PlacementBuilder {

    protected String placementName;
    protected Long frequency;
    protected Boolean showLoader;
    protected Long maxLoadingTime;
    protected AdCompletion onAdCompletion;
    protected LoaderSettings loaderSettings;


    protected Integer priority;
    protected Set<Class<Activity>> constraintToActivities;

    public FullScreenPlacement build() {
        return null;
    }

    public PlacementBuilder name(String name) {
        this.placementName = name;
        return this;
    }

    public PlacementBuilder loaderUISetting(LoaderSettings loaderSettings){
        this.loaderSettings = loaderSettings;
        return this;
    }

    public PlacementBuilder frequencyCapInSeconds(Long frequencyCapping) {
        this.frequency = frequencyCapping;
        return this;
    }

    public PlacementBuilder showLoaderTillAdIsReady(Boolean showLoader) {
        this.showLoader = showLoader;
        return this;
    }

    public PlacementBuilder loaderTimeOutInSeconds(Long maxLoadTime) {
        this.maxLoadingTime = maxLoadTime;
        return this;
    }

    public PlacementBuilder onAdCompletion(AdCompletion onAdCompletion) {
        this.onAdCompletion = onAdCompletion;
        return this;
    }


    public PlacementBuilder constraintToActivities(Class<Activity>... activities) {
        this.constraintToActivities = new HashSet<>(Arrays.asList(activities));
        return this;
    }

    public PlacementBuilder priority(Integer priority) {
        this.priority = priority;
        return this;
    }
}
