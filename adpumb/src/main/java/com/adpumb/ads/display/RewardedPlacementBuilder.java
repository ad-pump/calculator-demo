package com.adpumb.ads.display;

import android.app.Activity;
import android.util.Log;

import com.adpumb.ads.util.Utils;
import com.adpumb.lifecycle.Adpumb;

import java.util.Arrays;
import java.util.HashSet;

public class RewardedPlacementBuilder extends PlacementBuilder{

    public RewardedPlacementBuilder() { }

    public RewardedPlacement build() {
        if (Utils.isBlank(placementName)) {
            throw new RuntimeException("No placement name given. This will impact the report. Please set a name for the placement ");
        }

        if (maxLoadingTime == null) {
            Log.w(Adpumb.TAG, "No max loading time given. Loader animation will pay time ad is ready. This will onbstruct user. Using 10 as default, ie 10 seconds");
            maxLoadingTime = 10L;
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
        RewardedPlacement placement = new RewardedPlacement();
        placement.setPlacementName(this.placementName);
        placement.setFrequency(0L);//rewarded placements don't have frequency capping
        placement.setShowLoader(true);//loader is always enabled for rewarded placements
        placement.setMaxLoadingTime(this.maxLoadingTime);
        placement.setAfterAdCompletion(this.onAdCompletion);
        placement.setConstraintToActivities(constraintToActivities);
        placement.setPriority(priority);
        placement.setLoaderSettings(loaderSettings);
        return placement;
    }

    public RewardedPlacementBuilder name(String name) {
        this.placementName = name;
        return this;
    }

    public RewardedPlacementBuilder loaderUISetting(LoaderSettings loaderSettings){
        this.loaderSettings = loaderSettings;
        return this;
    }

    public RewardedPlacementBuilder loaderTimeOutInSeconds(long maxLoadTime) {
        this.maxLoadingTime = maxLoadTime;
        return this;
    }

    public RewardedPlacementBuilder onAdCompletion(AdCompletion onAdCompletion) {
        this.onAdCompletion = onAdCompletion;
        return this;
    }


    public RewardedPlacementBuilder constraintToActivities(Class<Activity>... activities) {
        this.constraintToActivities = new HashSet<>(Arrays.asList(activities));
        return this;
    }

    public RewardedPlacementBuilder priority(Integer priority) {
        this.priority = priority;
        return this;
    }
}
