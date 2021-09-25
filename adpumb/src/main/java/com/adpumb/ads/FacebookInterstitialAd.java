package com.adpumb.ads;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.adpumb.ads.error.ADError;
import com.adpumb.lifecycle.Adpumb;


public class FacebookInterstitialAd extends KempaInterstitialAd {

    public static final String PLACEMENT_ID = "652051188886687_660669281358211";
    private static final int FB_NO_FILL = 1001;
    private static final int FB_FREQUENT_LOAD = 1002;
    private InterstitialAd interstitialAd;
    private KempaAdListener kempaAdListener;


    public FacebookInterstitialAd(Activity context,  String placementId,float ecpm) {
        super(context,placementId,ecpm);

    }


    @Override
    protected void initialize(Context context,String placementId){
        if(Adpumb.isDebugMode()){
            interstitialAd = new InterstitialAd(context,"VID_HD_9_16_39S_APP_INSTALL#"+placementId);
        }
        else{
            interstitialAd = new InterstitialAd(context, placementId);
        }


    }

    @Override
    protected void showAd() {
        interstitialAd.show();
    }

    @Override
    public void loadAd() {
        if(Adpumb.isDebugMode()){
            Log.d(Adpumb.TAG, "not loading fb ads as they slow down debugging");
            return;
        }
        InterstitialAdListener interstitialAdListener = new InterstitialAdListener() {

            @Override
            public void onError(Ad ad, AdError adError) {
                if(kempaAdListener!=null){
                    int errorCode = adError.getErrorCode();
                    boolean noFill = errorCode==FB_NO_FILL || errorCode== FB_FREQUENT_LOAD || errorCode ==1203;
                    if(noFill){
                        kempaAdListener.onError(ADError.NO_FIIL);
                    }else if(errorCode==1011 || errorCode == 1012){
                        kempaAdListener.onError(ADError.FATAL);
                    }else{
                        kempaAdListener.onError(ADError.NETWORK);
                    }

                }
            }

            @Override
            public void onAdLoaded(Ad ad) {
                if(kempaAdListener!=null){
                    kempaAdListener.onAdLoaded();
                }
            }

            @Override
            public void onAdClicked(Ad ad) {

            }

            @Override
            public void onLoggingImpression(Ad ad) {

            }

            @Override
            public void onInterstitialDisplayed(Ad ad) {

            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                if(kempaAdListener!=null){
                    kempaAdListener.onAdCompleted(true);
                }
            }
        };
        interstitialAd.loadAd(
                interstitialAd.buildLoadAdConfig()
                        .withAdListener(interstitialAdListener)
                        .build());

    }

    @Override
    public boolean isAdLoaded() {
        return interstitialAd.isAdLoaded();
    }

    @Override
    public boolean isAdValid() {
        return !interstitialAd.isAdInvalidated();
    }

    @Override
    public void addListener(KempaAdListener adListener) {
        this.kempaAdListener = adListener;
    }


}
