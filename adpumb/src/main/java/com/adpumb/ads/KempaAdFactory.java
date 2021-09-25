package com.adpumb.ads;

import static com.adpumb.ads.ADVendor.ADMOB;
import static com.adpumb.ads.ADVendor.FACEBOOK;
import static com.adpumb.ads.ADVendor.GOOGLE;

import android.app.Activity;

import com.adpumb.ads.mediation.KempaAdUnit;
import com.adpumb.ads.util.Utils;

public class KempaAdFactory {

    private static KempaAdFactory kempaAdFactory;
    public static final String FB_DEBUG_AD = "VID_HD_16_9_46S_LINK#652051188886687_730542811037524";
    public static final String FB_LAUNCH_AD = "652051188886687_784497688975369";
    public static final String FB_EXECUTOR_AD = "652051188886687_784498778975260";
    public static final String ADX_INTERSTITIAL = "/419163168/com.secure.cryptovpn.interstitial";
    public static final String ADX_REWARD = "/419163168/com.secure.cryptovpn.rewarded";
    private static final String NATIVE="native";
    private static final String INTERSTITIAL="interstitial";
    private static final String REWARD = "reward";

    public KempaAdFactory(){

    }
    public static void setKempaAdFactory(KempaAdFactory adFactory){
        kempaAdFactory = adFactory;
    }
    public static KempaAdFactory getInstance(){
        if(kempaAdFactory==null){
            kempaAdFactory = new KempaAdFactory();
        }
        return kempaAdFactory;
    }

    public KempaAd createAd(Activity activity, String adVendor,KempaAdUnit adUnit){
        if(adVendor==null){
            return null;
        }
        String type = adUnit.getType();
        if(Utils.isBlank(type)){
            type = INTERSTITIAL;
        }
        switch (type){
            case NATIVE:
                return createNativeAd(adUnit,adVendor,activity);
            case INTERSTITIAL:
                return createInterstitialAd(adUnit,adVendor,activity);
            case REWARD:
                return createRewardedAd(adUnit, adVendor, activity);
        }
        return null;
    }

    private GoogleNativeAd createNativeAd(KempaAdUnit adUnit, String vendor, Activity activity){
        switch (vendor){
            case GOOGLE:
            case ADMOB:
                return new GoogleNativeAd(activity,adUnit.getAdUnit(),adUnit.getEcpm());
            default:
                return null;
        }

    }

    private KempaRewardedAd createRewardedAd(KempaAdUnit adUnit,String adVendor,Activity activity){
        switch (adVendor) {
            case ADMOB:
            case GOOGLE:
                return new GoogleRewardedAd(activity, adUnit.getAdUnit(), adUnit.getEcpm());
            case FACEBOOK:
                return new FacebookRewardedAd(activity, adUnit.getAdUnit(), adUnit.getEcpm());
        }
        return null;
    }

    private KempaInterstitialAd createInterstitialAd(KempaAdUnit adUnit,String adVendor,Activity activity){
        switch (adVendor) {
            case ADMOB:
            case ADVendor.GOOGLE:
                return new GoogleInterstitialAd(activity, adUnit.getAdUnit(), adUnit.getEcpm());
            case FACEBOOK:
                return new FacebookInterstitialAd(activity, adUnit.getAdUnit(), adUnit.getEcpm());
        }
        return null;
    }
}
