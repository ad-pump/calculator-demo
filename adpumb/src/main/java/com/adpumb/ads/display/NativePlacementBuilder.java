package com.adpumb.ads.display;

import android.app.Activity;
import android.util.Log;

import com.adpumb.ads.util.Utils;
import com.adpumb.lifecycle.Adpumb;

public class NativePlacementBuilder{

    protected String placementName;
    protected Integer priority;
    protected Activity desiredActivity;
    protected int refreshRateInSeconds;
    public NativeAdListener nativeAdListener;

    public NativePlacementBuilder() {
    }

    public NativePlacement build() {
        if (Utils.isBlank(placementName)) {
            throw new RuntimeException("No placement name given. This will impact the report. Please set a name for the placement ");
        }

        if (desiredActivity == null) {
            throw new RuntimeException("You should provide value for `toBeShownOnActivity` on NativePlacementBuilder");
        }
        if (priority == null) {
            Log.d(Adpumb.TAG, "No priority is set. Using default priority, ie 10");
            this.priority = 10;
        }
        if(nativeAdListener==null){
            throw new RuntimeException("There is no listener written for native ad. Without it the native ads loaded will be unused");
        }
        NativePlacement placement = new NativePlacement();
        placement.setPlacementName(this.placementName);
        placement.setDesiredActivity(desiredActivity);
        placement.setPriority(priority);
        placement.setNativeAdListener(nativeAdListener);
        placement.setRefreshRateInSeconds(refreshRateInSeconds);
        return placement;
    }

    public NativePlacementBuilder name(String name) {
        this.placementName = name;
        return this;
    }

    public NativePlacementBuilder toBeShownOnActivity(Activity activity) {
        this.desiredActivity = activity;
        return this;
    }

    public NativePlacementBuilder adListener(NativeAdListener nativeAdListener){
        this.nativeAdListener = nativeAdListener;
        return this;
    }

    public NativePlacementBuilder priority(Integer priority) {
        this.priority = priority;
        return this;
    }

    public NativePlacementBuilder refreshRateInSeconds(Integer refreshRate) {
        this.refreshRateInSeconds = refreshRate;
        return this;
    }
}
