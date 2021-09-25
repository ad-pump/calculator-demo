package com.adpumb.ads.display;

import android.app.Activity;
import android.util.Log;

import com.adpumb.ads.util.Utils;
import com.adpumb.lifecycle.Adpumb;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class InterstitialPlacementBuilder extends PlacementBuilder {


    public InterstitialPlacementBuilder() {

    }

    public InterstitialPlacement build() {
        if (Utils.isBlank(placementName)) {
            throw new RuntimeException("No placement name given. This will impact the report. Please set a name for the placement ");
        }
        if (frequency == null) {
            Log.w(Adpumb.TAG, "No frequency cap for interstial. Using defaul value 180000 (3 minutes)");
            frequency = 180l;
        }
        if (frequency < 30l) {
            Log.w(Adpumb.TAG, "Given frequency %l is very low. We advice use to increase it, especially if you are using it from onResume");
        }
        if (showLoader == null) {
            Log.d(Adpumb.TAG, "No instruction given about loader animation. Using default which is no loader animation");
            showLoader = false;
        }
        if (maxLoadingTime == null && showLoader) {
            Log.w(Adpumb.TAG, "No max loading time given. Loader animation will pay time ad is ready. This will onbstruct user. Using 10 as default, ie 10 seconds");
            maxLoadingTime = 10L;
        } else if (maxLoadingTime == null) {
            Log.d(Adpumb.TAG, "No loader and no max loading time. Hence will keep trying the ad till its ready");
            maxLoadingTime = 3600L;//one hour
        }
        if (onAdCompletion == null) {
            Log.d(Adpumb.TAG, "onAdCompletion method is not given. Hence you won't recieve any call backs");
        }

        if (constraintToActivities == null) {
            Log.d(Adpumb.TAG, "The placement is allowed to load on top of any activity as constraint ativity is not given");
        }
        if (priority == null) {
            Log.d(Adpumb.TAG, "No priority is set. Using default priority, ie 10");
            this.priority = 10;
        }
        InterstitialPlacement placement = new InterstitialPlacement();
        placement.setPlacementName(this.placementName);
        placement.setFrequency(this.frequency);
        placement.setShowLoader(this.showLoader);
        placement.setMaxLoadingTime(this.maxLoadingTime);
        placement.setAfterAdCompletion(this.onAdCompletion);
        placement.setConstraintToActivities(constraintToActivities);
        placement.setPriority(priority);
        placement.setLoaderSettings(loaderSettings);
        return placement;
    }

    public InterstitialPlacementBuilder name(String name) {
        this.placementName = name;
        return this;
    }

    public InterstitialPlacementBuilder loaderUISetting(LoaderSettings loaderSettings){
        this.loaderSettings = loaderSettings;
        return this;
    }

    public InterstitialPlacementBuilder frequencyCapInSeconds(long frequencyCapping) {
        this.frequency = frequencyCapping;
        return this;
    }

    public InterstitialPlacementBuilder showLoaderTillAdIsReady(boolean showLoader) {
        this.showLoader = showLoader;
        return this;
    }

    public InterstitialPlacementBuilder loaderTimeOutInSeconds(long maxLoadTime) {
        this.maxLoadingTime = maxLoadTime;
        return this;
    }

    public InterstitialPlacementBuilder onAdCompletion(AdCompletion onAdCompletion) {
        this.onAdCompletion = onAdCompletion;
        return this;
    }


    public InterstitialPlacementBuilder constraintToActivities(Class<Activity>... activities) {
        this.constraintToActivities = new HashSet<>(Arrays.asList(activities));
        return this;
    }

    public InterstitialPlacementBuilder priority(Integer priority) {
        this.priority = priority;
        return this;
    }

}
