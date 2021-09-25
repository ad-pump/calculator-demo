package com.adpumb.ads;

import android.app.Activity;

import com.adpumb.ads.display.DisplayManager;
import com.adpumb.ads.mediation.FillRetryManager;

public abstract class KempaInterstitialAd extends KempaAd {

    public KempaInterstitialAd(Activity context, String unitId, float ecpm) {
        super(context, unitId, ecpm);
    }

    @Override
    public void show(Activity activity, AdCompletionHandler adCompletionHandler) {
        setAdCompletionHandler(adCompletionHandler);
        showAd();
    }

    @Override
    public void onAdLoaded() {
        isAdRequestCompleted = true;
        DisplayManager.getInstance().interstitialAdListener();
        FillRetryManager.getInstance().adLoaded(this);
    }


    protected abstract void showAd();

}
