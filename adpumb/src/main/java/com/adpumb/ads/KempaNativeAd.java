package com.adpumb.ads;

import android.app.Activity;

import com.adpumb.ads.display.DisplayManager;
import com.adpumb.ads.mediation.FillRetryManager;
import com.google.android.gms.ads.nativead.NativeAd;

public abstract class KempaNativeAd extends KempaAd{

    protected NativeAd nativeAd;

    public KempaNativeAd(Activity context, String unitId, float ecpm) {
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
        DisplayManager.getInstance().nativeAdListener();
        FillRetryManager.getInstance().adLoaded(this);
    }

    public void markAdAsUsed(){
        this.nativeAd = null;
        reload();
    }
    protected abstract void showAd();

    public abstract Object getNativeAd();
}
