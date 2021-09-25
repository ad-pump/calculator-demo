package com.adpumb.ads.analytics;

import com.adpumb.ads.util.ApiData;

public class ImpressionData {

    String clientID;
    String adUnit;
    float ecpm;
    String placementName;
    String adConfigVersion;
    long time;

    public ImpressionData(String clientID, String adUnit, float ecpm, String placementName, String adConfigVersion, long time) {
        this.clientID = clientID;
        this.adUnit = adUnit;
        this.ecpm = ecpm;
        this.placementName = placementName;
        this.adConfigVersion = adConfigVersion;
        this.time = time;
    }

    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    public String getAdUnit() {
        return adUnit;
    }

    public void setAdUnit(String adUnit) {
        this.adUnit = adUnit;
    }

    public float getEcpm() {
        return ecpm;
    }

    public void setEcpm(float ecpm) {
        this.ecpm = ecpm;
    }

    public String getPlacementName() {
        return placementName;
    }

    public void setPlacementName(String placementName) {
        this.placementName = placementName;
    }

    public String getAdConfigVersion() {
        return adConfigVersion;
    }

    public void setAdConfigVersion(String adConfigVersion) {
        this.adConfigVersion = adConfigVersion;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
