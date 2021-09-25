package com.adpumb.ads;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.adpumb.ads.error.ADError;
import com.adpumb.ads.mediation.KempaMediationAdapter;
import com.adpumb.lifecycle.Adpumb;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

import java.util.concurrent.atomic.AtomicBoolean;

public class GoogleRewardedAd extends KempaRewardedAd{

    private RewardedAd mRewardedAd;
    private KempaAdListener adListener;
    private long loadTime = 0;
    private AtomicBoolean isLoading = new AtomicBoolean(false);
    private boolean rewardEarned = false;

    public GoogleRewardedAd(Activity context, String unitId, float ecpm) {
        super(context, unitId, ecpm);
    }

    @Override
    protected void initialize(Context context, String unitId) {
        if (Adpumb.isDebugMode()) {
            adUnitId = "ca-app-pub-3940256099942544/5224354917";
        }
        isLoading = new AtomicBoolean(false);
    }

    @Override
    public void loadAd() {
        if (isLoading.get()) {
            return;
        }
        Log.w(Adpumb.TAG, "Request ad");
        isLoading.set(true);
        rewardEarned = false;
        AdRequest adRequest = new AdRequest.Builder().build();
        RewardedAd.load(Adpumb.getCurrentOrLastContext(), adUnitId,
                adRequest, new RewardedAdLoadCallback() {

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                        Log.d(Adpumb.TAG, "Google Rewarded Ad loaded");
                        super.onAdLoaded(rewardedAd);
                        System.out.println("retry loading success " + getEcpm());
                        loadTime = System.currentTimeMillis();
                        mRewardedAd = rewardedAd;
                        isLoading.set(false);
                        adListener.onAdLoaded();
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error.
                        super.onAdFailedToLoad(loadAdError);
                        Log.w(Adpumb.TAG, "Google Rewarded Ad load failed "+loadAdError.getCode());
                        System.out.println("retry loading failed  " + getEcpm() + ":" + loadAdError.getMessage());
                        mRewardedAd = null;
                        isLoading.set(false);
                        int errorCode = loadAdError.getCode();
                        if (errorCode == AdRequest.ERROR_CODE_MEDIATION_NO_FILL || errorCode == AdRequest.ERROR_CODE_NO_FILL) {
                            adListener.onError(ADError.NO_FIIL);
                        } else if (errorCode == AdRequest.ERROR_CODE_NETWORK_ERROR || errorCode == AdRequest.ERROR_CODE_INTERNAL_ERROR) {
                            adListener.onError(ADError.NETWORK);
                        } else {
                            adListener.onError(ADError.FATAL);
                        }
                    }
                });


    }

    @Override
    public boolean isAdLoaded() {
        return mRewardedAd != null;
    }

    @Override
    public boolean isAdValid() {
        if (mRewardedAd == null) {
            return false;
        }
        long timeSinceAdLoad = System.currentTimeMillis() - loadTime;
        long timeSinceAdLoadInMinutes = timeSinceAdLoad / (1000 * 60);
        int googleRewardedAdReload = KempaMediationAdapter.getInstance().getGoogleRewardedAdReload();
        if (timeSinceAdLoadInMinutes > googleRewardedAdReload) {
            return false;
        }
        return true;
    }

    @Override
    protected void addListener(KempaAdListener adListener) {
        this.adListener = adListener;
    }

    @Override
    protected void showAd() {
        if (mRewardedAd != null) {
            mRewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                    super.onAdFailedToShowFullScreenContent(adError);
                    if (adListener != null) {
                        adListener.onAdCompleted(false);
                    }
                    mRewardedAd = null;
                }

                @Override
                public void onAdShowedFullScreenContent() {
                    super.onAdShowedFullScreenContent();
                    mRewardedAd = null;
                }

                @Override
                public void onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent();
                    mRewardedAd = null;
//                    may not be closing after watching full
                    if (adListener != null) {
                        adListener.onAdCompleted(rewardEarned);
                    }
                }

                @Override
                public void onAdImpression() {
                    super.onAdImpression();
                }
            });

            mRewardedAd.show(Adpumb.getActivityContext(), new OnUserEarnedRewardListener() {
                @Override
                public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                    rewardEarned = true;
                    // Handle the reward.
                    Log.d(Adpumb.TAG, "The user earned the reward.");
                    int rewardAmount = rewardItem.getAmount();
                    String rewardType = rewardItem.getType();
                }
            });

            mRewardedAd = null;
        }
    }
}
