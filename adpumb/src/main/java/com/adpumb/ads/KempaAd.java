package com.adpumb.ads;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;

import com.adpumb.ads.display.DisplayManager;
import com.adpumb.ads.error.ADError;
import com.adpumb.ads.error.ErrorReport;
import com.adpumb.ads.error.FatalAdUnit;
import com.adpumb.ads.mediation.FillRetryManager;
import com.adpumb.ads.mediation.RetryableAd;
import com.adpumb.ads.util.ThreadFactory;

public abstract class KempaAd implements RetryableAd<KempaInterstitialAd>,KempaAdListener {


    protected String adUnitId;
    private AdCompletionHandler completionCallBack;
    private Activity context;
    private float ecpm;
    private boolean isInvalid = false;
    protected boolean isAdRequestCompleted = false;
    private boolean shouldReload = false;
    private Handler handler;

    public KempaAd(Activity context, String unitId, float ecpm) {
        this.context = context;
        this.adUnitId = unitId;
        this.ecpm = ecpm;
        this.handler = ThreadFactory.getInstance().getHandler();
        initialize(context, unitId);
        addListener(this);
        reload();

    }

    public abstract void show(Activity activity,AdCompletionHandler adCompletionHandler) ;

    protected Activity getActivity() {
        return this.context;
    }


    public String getAdUnitId() {
        return adUnitId;
    }

    @Override
    public boolean shouldReload() {
        return shouldReload;
    }

    public void reload() {
        shouldReload = false;
        loadAd();
    }
    @Override
    public void onAdLoaded() {
        isAdRequestCompleted = true;
        DisplayManager.getInstance().interstitialAdListener();
        FillRetryManager.getInstance().adLoaded(KempaAd.this);
    }

    @Override
    public void onError(ADError error) {
        isAdRequestCompleted = true;
        if (isInvalid) {
            return;
        }
        if (error == ADError.FATAL) {
            ErrorReport.getInstance().report(new FatalAdUnit(adUnitId));
            return;
        }

        FillRetryManager retryManager = FillRetryManager.getInstance();
        if (error != ADError.NO_FIIL || retryManager.canRetry(KempaAd.this)) {
            long delay = retryManager.retryDelay(KempaAd.this, error);
            handler.removeCallbacksAndMessages(null);//remove any existing reloads
            handler.postDelayed(() -> reload(), delay);
        } else {
            shouldReload = true;
        }
    }

    @Override
    public void onAdCompleted(boolean sucess) {
        callback(sucess);
        reload();
    }


    protected AdCompletionHandler getCompletionCallBack() {
        return completionCallBack;
    }

    protected void setAdCompletionHandler(AdCompletionHandler completionCallBack) {
        this.completionCallBack = completionCallBack;
    }

    private void callback(boolean success) {

        if (getCompletionCallBack() != null) {
            getCompletionCallBack().adCompleted(success);
            setAdCompletionHandler(null);
        }
    }


    protected abstract void initialize(Context context, String unitId);


    public abstract void loadAd();

    public abstract boolean isAdLoaded();

    public abstract boolean isAdValid();

    protected abstract void addListener(KempaAdListener adListener);

    public float getEcpm() {
        return ecpm;
    }

    public Integer getEcpmAsInt() {
        return (int) (ecpm * 100);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        KempaInterstitialAd that = (KempaInterstitialAd) o;

        if (Float.compare(that.getEcpm(), getEcpm()) != 0) return false;
        return getAdUnitId().equals(that.getAdUnitId());
    }

    @Override
    public int hashCode() {
        int result = getAdUnitId().hashCode();
        result = 31 * result + (getEcpm() != +0.0f ? Float.floatToIntBits(getEcpm()) : 0);
        return result;
    }

    @Override
    public int compareTo(KempaInterstitialAd other) {
        int diff = Float.compare(this.getEcpm(), other.getEcpm());
        if (diff == 0) {
            return this.getAdUnitId().compareTo(other.getAdUnitId());
        } else {
            return diff;
        }
    }

    public void setInvalid(boolean invalid) {
        isInvalid = invalid;
    }

    public boolean isAdRequestCompleted() {
        return isAdRequestCompleted;
    }
}
