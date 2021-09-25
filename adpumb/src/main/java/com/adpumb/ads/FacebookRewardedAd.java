package com.adpumb.ads;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.adpumb.ads.error.ADError;
import com.adpumb.lifecycle.Adpumb;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.RewardedVideoAd;
import com.facebook.ads.RewardedVideoAdListener;

public class FacebookRewardedAd extends KempaRewardedAd{

    public static final String PLACEMENT_ID = "652051188886687_660669281358211";
    private static final int FB_NO_FILL = 1001;
    private static final int FB_FREQUENT_LOAD = 1002;
    private RewardedVideoAd rewardedVideoAd;;
    private KempaAdListener kempaAdListener;
    private boolean rewardEarned = false;

    public FacebookRewardedAd(Activity context, String unitId, float ecpm) {
        super(context, unitId, ecpm);
    }

    @Override
    protected void initialize(Context context, String placementId) {
        if(Adpumb.isDebugMode()){
            rewardedVideoAd = new RewardedVideoAd(context,"VID_HD_9_16_39S_APP_INSTALL#"+placementId);
        }
        else{
            rewardedVideoAd = new RewardedVideoAd(context, placementId);
        }
    }

    @Override
    public void loadAd() {
        RewardedVideoAdListener rewardedVideoAdListener = new RewardedVideoAdListener() {
            @Override
            public void onError(Ad ad, AdError adError) {
                // Rewarded video ad failed to load
                Log.d(Adpumb.TAG, "FB reward load failed = "+adError.getErrorMessage());
                if(kempaAdListener!=null){
                    int errorCode = adError.getErrorCode();
                    if(errorCode==FB_NO_FILL){
                        kempaAdListener.onError(ADError.NO_FIIL);
                    }else if(errorCode== FB_FREQUENT_LOAD){
                        kempaAdListener.onError(ADError.TOO_FREQUENT);
                    }else{
                        kempaAdListener.onError(ADError.NETWORK);
                    }

                }
            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Rewarded video ad is loaded and ready to be displayed
                Log.d(Adpumb.TAG, "FB rewarded Loaded");
                if(kempaAdListener!=null){
                    kempaAdListener.onAdLoaded();
                }
            }

            @Override
            public void onAdClicked(Ad ad) {
                // Rewarded video ad clicked
                Log.d(Adpumb.TAG, "Rewarded video ad clicked!");
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Rewarded Video ad impression - the event will fire when the
                // video starts playing
                Log.d(Adpumb.TAG, "Rewarded video ad impression logged!");
            }

            @Override
            public void onRewardedVideoCompleted() {
                // Rewarded Video View Complete - the video has been played to the end.
                // You can use this event to initialize your reward
                Log.d(Adpumb.TAG, "Rewarded video completed!");

                // Call method to give reward
                // giveReward();
                rewardEarned = true;
            }

            @Override
            public void onRewardedVideoClosed() {
                // The Rewarded Video ad was closed - this can occur during the video
                // by closing the app, or closing the end card.
                if(kempaAdListener!=null){
                    kempaAdListener.onAdCompleted(rewardEarned);
                    rewardEarned = false;
                }
            }
        };

        rewardedVideoAd.loadAd(
                rewardedVideoAd.buildLoadAdConfig()
                        .withAdListener(rewardedVideoAdListener)
                        .build());
    }

    @Override
    public boolean isAdLoaded() {
        return rewardedVideoAd.isAdLoaded();
    }

    @Override
    public boolean isAdValid() {
        return !rewardedVideoAd.isAdInvalidated();
    }

    @Override
    protected void addListener(KempaAdListener adListener) {
        this.kempaAdListener = adListener;
    }

    @Override
    protected void showAd() {
        rewardedVideoAd.show();
    }


}
