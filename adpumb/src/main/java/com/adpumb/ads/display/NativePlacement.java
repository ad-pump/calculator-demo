package com.adpumb.ads.display;

import android.app.Activity;

import com.adpumb.ads.KempaAd;
import com.adpumb.ads.KempaNativeAd;

public class NativePlacement implements Comparable {

    private String placementName;
    private Integer priority;
    private Activity activityClass;
    private int refreshRateInSeconds;
    private NativeAdListener nativeAdListener;
    private boolean isDisposed;

    public void setNativeAdListener(NativeAdListener adListener){
        this.nativeAdListener = adListener;
    }

    public NativeAdListener getNativeAdListener(){
        return this.nativeAdListener;
    }

    public int getRefreshRateInSeconds() {
        return refreshRateInSeconds;
    }

    public void setRefreshRateInSeconds(int refreshRateInSeconds) {
        this.refreshRateInSeconds = refreshRateInSeconds;
    }

    public String getPlacementName() {
        return placementName;
    }

    public void setPlacementName(String placementName) {
        this.placementName = placementName;
    }

    public Activity getDesiredActivity() {
        return activityClass;
    }

    public void setDesiredActivity(Activity activity) {
        activityClass = activity;
    }

    public boolean isContraintToActivity() {
        return true;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    @Override
    public int compareTo(Object o) {
        NativePlacement other = (NativePlacement) o;
        int diff = Integer.compare(this.priority,other.priority);
        if(diff==0){
            return this.placementName.compareTo(other.placementName);
        }
        return diff;
    }

    public Class<? extends KempaAd> getType() {

        return KempaNativeAd.class;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NativePlacement that = (NativePlacement) o;
        return placementName.equals(that.placementName);
    }

    @Override
    public int hashCode() {
        return placementName.hashCode();
    }

    public boolean isDisposed() {
        return isDisposed;
    }

    public void setDisposed(boolean disposed) {
        isDisposed = disposed;
    }
}
