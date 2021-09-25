package com.adpumb.ads;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.adpumb.ads.error.ADError;
import com.adpumb.ads.mediation.KempaMediationAdapter;
import com.adpumb.lifecycle.Adpumb;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import java.util.concurrent.atomic.AtomicBoolean;

import androidx.annotation.NonNull;

public class GoogleInterstitialAd extends KempaInterstitialAd {

    private InterstitialAd interstitialAd;
    private KempaAdListener adListener;
    private long loadTime = 0;
    private AtomicBoolean isLoading = new AtomicBoolean(false);

    public GoogleInterstitialAd(Activity context, String unitId, float ecpm) {
        super(context, unitId, ecpm);
    }

    @Override
    protected void initialize(Context context, String unitId) {
        if (Adpumb.isDebugMode()) {
            adUnitId = "ca-app-pub-3940256099942544/1033173712";
        }
        isLoading = new AtomicBoolean(false);


    }

    @Override
    protected void showAd() {
        if (interstitialAd != null) {
            interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                    super.onAdFailedToShowFullScreenContent(adError);
                    if (adListener != null) {
                        adListener.onAdCompleted(false);
                    }
                    interstitialAd = null;
                }

                @Override
                public void onAdShowedFullScreenContent() {
                    super.onAdShowedFullScreenContent();
                    interstitialAd = null;
                }

                @Override
                public void onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent();
                    if (adListener != null) {
                        adListener.onAdCompleted(true);
                    }
                    interstitialAd = null;
                }

                @Override
                public void onAdImpression() {
                    super.onAdImpression();
                }
            });
            interstitialAd.show(Adpumb.getActivityContext());
            interstitialAd = null;
        }
    }

    @Override
    public void loadAd() {
        if (isLoading.get()) {
            return;
        }
        Log.w(Adpumb.TAG, "Request ad");
        isLoading.set(true);
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(Adpumb.getCurrentOrLastContext(), adUnitId, adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd loadedAd) {
                Log.w(Adpumb.TAG, "Adloaded");
                super.onAdLoaded(loadedAd);
                System.out.println("retry loading success " + getEcpm());
                loadTime = System.currentTimeMillis();
                interstitialAd = loadedAd;
                isLoading.set(false);
                adListener.onAdLoaded();
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                Log.w(Adpumb.TAG, "Adload failed "+loadAdError.getCode());
                System.out.println("retry loading failed  " + getEcpm() + ":" + loadAdError.getMessage());
                interstitialAd = null;
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
        return interstitialAd != null;
    }

    @Override
    public boolean isAdValid() {
        if (interstitialAd == null) {
            return false;
        }
        long timeSinceAdLoad = System.currentTimeMillis() - loadTime;
        long timeSinceAdLoadInMinutes = timeSinceAdLoad / (1000 * 60);
        int googleInterstitialAdReload = KempaMediationAdapter.getInstance().getGoogleInterstitialAdReload();
        if (timeSinceAdLoadInMinutes > googleInterstitialAdReload) {
            return false;
        }
        return true;
    }

    @Override
    protected void addListener(KempaAdListener adListener) {
        this.adListener = adListener;
    }
}
