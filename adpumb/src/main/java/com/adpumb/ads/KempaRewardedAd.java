package com.adpumb.ads;

import android.app.Activity;

public abstract class KempaRewardedAd extends KempaAd{

    public KempaRewardedAd(Activity context, String unitId, float ecpm) {
        super(context, unitId, ecpm);
    }

    @Override
    public void show(Activity activity, AdCompletionHandler adCompletionHandler) {
        setAdCompletionHandler(adCompletionHandler);
        showAd();
    }

    protected abstract void showAd();
}
