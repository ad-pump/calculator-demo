package com.adpumb.ads;

import static com.google.android.gms.ads.nativead.NativeAdOptions.ADCHOICES_BOTTOM_RIGHT;
import static com.google.android.gms.ads.nativead.NativeAdOptions.NATIVE_MEDIA_ASPECT_RATIO_LANDSCAPE;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;

import com.adpumb.ads.error.ADError;
import com.adpumb.ads.mediation.KempaMediationAdapter;
import com.adpumb.lifecycle.Adpumb;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;

import org.jetbrains.annotations.NotNull;

public class GoogleNativeAd extends KempaNativeAd {

    private KempaAdListener adListener;
    private long loadTime=0;

    public GoogleNativeAd(Activity context, String unitId, float ecpm) {
        super(context, unitId, ecpm);
    }

    @Override
    protected void initialize(Context context, String unitId) {
        if (Adpumb.isDebugMode()) {
            adUnitId="ca-app-pub-3940256099942544/2247696110";
        }
    }

    public void showAd() { }

    @Override
    public NativeAd getNativeAd() {
        return nativeAd;
    }


    @Override
    public void loadAd() {
        AdLoader.Builder builder = new AdLoader.Builder(getActivity(), getAdUnitId());
        AdLoader loader = builder.forNativeAd(new NativeAd.OnNativeAdLoadedListener() {

            @Override
            public void onNativeAdLoaded(@NonNull @NotNull NativeAd loadedAd) {
                loadTime = System.currentTimeMillis();
                nativeAd = loadedAd;
                adListener.onAdLoaded();
            }
        }).withAdListener(new AdListener() {
            @Override
            public void onAdImpression() {
//                long refreshInterval = Configuration.getRemoteConfig().getLong(Configuration.GOOGLE_NATIVE_AD_REFRESH_TIMEOUT);
//                ThreadFactory.getInstance().getHandler().postDelayed(() -> {
                    onAdCompleted(true);
//                }, refreshInterval);
            }

            @Override
            public void onAdFailedToLoad(@NonNull @NotNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                System.out.println("native ad failed to load");
                if (nativeAd != null) {
                    nativeAd.destroy();
                }
                nativeAd = null;

                int errorCode = loadAdError.getCode();
                if (errorCode == AdRequest.ERROR_CODE_MEDIATION_NO_FILL || errorCode == AdRequest.ERROR_CODE_NO_FILL) {
                    adListener.onError(ADError.NO_FIIL);
                } else if (errorCode == AdRequest.ERROR_CODE_NETWORK_ERROR || errorCode == AdRequest.ERROR_CODE_INTERNAL_ERROR) {
                    adListener.onError(ADError.NETWORK);
                } else {
                    adListener.onError(ADError.FATAL);
                }
            }
        }).withNativeAdOptions(new NativeAdOptions.Builder().setMediaAspectRatio(NATIVE_MEDIA_ASPECT_RATIO_LANDSCAPE).setAdChoicesPlacement(ADCHOICES_BOTTOM_RIGHT).build())
                .build();
        loader.loadAd(new AdRequest.Builder().build());

    }

    @Override
    public boolean isAdLoaded() {
        return nativeAd != null;
    }

    @Override
    public boolean isAdValid() {
        if (nativeAd == null){
            return false;
        }

        long timeSinceAdLoad = System.currentTimeMillis()-loadTime;
        long timeSinceAdLoadInMinutes = timeSinceAdLoad/(1000*60);
        int googleInterstitialAdReload = KempaMediationAdapter.getInstance().getGoogleNativeAdReload();
        if(timeSinceAdLoadInMinutes>googleInterstitialAdReload){
            return false;
        }

        return true;
    }

    @Override
    protected void addListener(KempaAdListener adListener) {
        this.adListener = adListener;
    }

}

