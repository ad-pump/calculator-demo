package com.adpumb.ads.mediation;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class KempaAdConfig {
    private Boolean enableRetryManager;
    private String version;
    private float validationEcpm;
    private Set<String> disabledPlacements;
    private Set<KempaAdNetwork> adNetworks;
    private int googleInterstitialAdReload = 30;

    private int googleRewardedAdReload = 30;
    private int googleNativeAdReload = 30;

    public Set<KempaAdNetwork> getAdNetworks() {
        return ( adNetworks != null ) ? adNetworks : new HashSet<KempaAdNetwork>();
    }

    public void setGoogleInterstitialAdReload(int googleInterstitialAdReload) {
        this.googleInterstitialAdReload = googleInterstitialAdReload;
    }

    public int getGoogleInterstitialAdReload() {
        return googleInterstitialAdReload;
    }

    public void setGoogleRewardedAdReload(int googleRewardedAdReload) {
        this.googleRewardedAdReload = googleRewardedAdReload;
    }
    public int getGoogleRewardedAdReload() {
        return googleRewardedAdReload;
    }

    public void setGoogleNativeAdReload(int googleNativeAdReload) {
        this.googleNativeAdReload = googleNativeAdReload;
    }
    public int getGoogleNativeAdReload() {
        return googleNativeAdReload;
    }

    public void setAdNetworks(Set<KempaAdNetwork> adNetworks) {
        this.adNetworks = adNetworks;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public float getValidationEcpm() {
        return validationEcpm;
    }

    public void setValidationEcpm(Float validationEcpm) {
        if(validationEcpm==null){
            this.validationEcpm = 0f;
        }else{
            this.validationEcpm = validationEcpm;
        }

    }

    public Set<String> getDisabledPlacements() {
        if(disabledPlacements==null){
            return Collections.EMPTY_SET;
        }
        return disabledPlacements;
    }

    public void setDisabledPlacements(Set<String> disabledPlacements) {
        this.disabledPlacements = disabledPlacements;
    }

    public boolean isEnableRetryManager() {
        if(enableRetryManager==null){
            return false;
        }
        return enableRetryManager;
    }

    public void setEnableRetryManager(boolean enableRetryManager) {
        this.enableRetryManager = enableRetryManager;
    }
}
