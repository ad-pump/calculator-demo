package com.adpumb.ads.display;

import android.app.Activity;

import com.adpumb.ads.KempaAd;
import com.adpumb.ads.KempaInterstitialAd;
import com.adpumb.ads.KempaRewardedAd;
import com.adpumb.ads.error.PlacementDisplayStatus;

import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class FullScreenPlacement implements Comparable{

    private String placementName;
    private Long frequency;
    private Boolean showLoader;
    private Long maxLoadingTime;
    private AdCompletion afterAdCompletion;
    private Integer priority;
    private Set<Class<Activity>> constraintToActivities;
    private LoaderSettings loaderSettings;

    public FullScreenPlacement() {

    }

    public void setLoaderSettings(LoaderSettings loaderSettings) {
        this.loaderSettings = loaderSettings;
    }

    public LoaderSettings getLoaderSettings() {
        return loaderSettings;
    }

    public String getPlacementName() {
        return placementName;
    }

    public void setPlacementName(String placementName) {
        this.placementName = placementName;
    }

    public Long getFrequency() {
        return frequency;
    }

    public void setFrequency(Long frequency) {
        this.frequency = frequency;
    }

    public Boolean getShowLoader() {
        return showLoader;
    }

    public void setShowLoader(Boolean showLoader) {
        this.showLoader = showLoader;
    }

    public Long getMaxLoadingTime() {
        return maxLoadingTime;
    }

    public void setMaxLoadingTime(Long maxLoadingTime) {
        this.maxLoadingTime = maxLoadingTime;
    }



    public Set<Class<Activity>> getConstraintToActivities() {
        return constraintToActivities;
    }

    public void setConstraintToActivities(Set<Class<Activity>> constraintToActivities) {
        if (constraintToActivities == null) {
            this.constraintToActivities = new HashSet<>();
        } else {
            this.constraintToActivities = constraintToActivities;
        }
    }

    public boolean isMaxLoadingTimeEnabled() {
        if (maxLoadingTime == null || maxLoadingTime <= 0) {
            return false;
        }
        return true;
    }

    public boolean isContraintToActivity() {
        if (this.constraintToActivities.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    @Override
    public int compareTo(Object o) {
        FullScreenPlacement other = (FullScreenPlacement) o;
        int diff = Integer.compare(this.priority,other.priority);
        if(diff==0){
            return this.placementName.compareTo(other.placementName);
        }
        return diff;
    }

    public AdCompletion getAfterAdCompletion() {
        if(afterAdCompletion==null){
            return emptyHandler();
        }
        return afterAdCompletion;
    }

    @NotNull
    private AdCompletion emptyHandler() {
        return new AdCompletion(){
            @Override
            public void onAdCompletion(boolean isSuccess, PlacementDisplayStatus status) {

            }
        };
    }

    public void setAfterAdCompletion(AdCompletion afterAdCompletion) {
        this.afterAdCompletion = afterAdCompletion;
    }

    public Class<? extends KempaAd> getType() {

        return this instanceof InterstitialPlacement ? KempaInterstitialAd.class : KempaRewardedAd.class;
    }
}
