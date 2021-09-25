package com.adpumb.ads.display;

import android.app.Activity;
import android.util.Log;

import com.adpumb.ads.AdCompletionHandler;
import com.adpumb.ads.AdPumbAPI;
import com.adpumb.ads.KempaAd;
import com.adpumb.ads.KempaInterstitialAd;
import com.adpumb.ads.KempaNativeAd;
import com.adpumb.ads.KempaRewardedAd;
import com.adpumb.ads.error.PlacementDisplayStatus;
import com.adpumb.ads.mediation.KempaMediationAdapter;
import com.adpumb.ads.mediation.KempaMediationAdapterListener;
import com.adpumb.ads.util.Action;
import com.adpumb.ads.util.DataStore;
import com.adpumb.ads.util.ThreadFactory;
import com.adpumb.ads.util.Utils;
import com.adpumb.lifecycle.Adpumb;
import com.google.android.gms.ads.nativead.NativeAd;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class DisplayManager {
    private static DisplayManager displayManager = new DisplayManager();
    private Set<FullScreenPlacement> pendingAds;
    private AdpumbLoader adpumpLoader;
    private Set<NativePlacement> pendingNativeAds = new HashSet<>();
    private Set<String> runningNativeAds = new HashSet<>();

    private DisplayManager() {
        pendingAds = new TreeSet<>();
        KempaMediationAdapter.addInstanceReadyListener(new KempaMediationAdapterListener() {
            @Override
            public void adapterOnReady() {
                showPendingAd();
            }
        });

    }

    public static DisplayManager getInstance() {
        return displayManager;
    }

    public void contextListener(Activity activity) {
        showPendingAd();
        showPendingNativeAds();
    }



    public void nativeAdListener(){
        showPendingNativeAds();
    }
    public void interstitialAdListener() {
        showPendingAd();
    }

    private boolean isAdTooFrequent(FullScreenPlacement adType) {
        long lastTime = DataStore.getInstance().getAdShownTime(adType);
        long currentTime = System.currentTimeMillis();
        long secondsPassed = ((currentTime - lastTime) / (1000));
        if (secondsPassed < 0) {
            secondsPassed = Long.MAX_VALUE;
        }
        if (adType.getFrequency() > secondsPassed) {
            return true;
        }
        return false;
    }

    public void showAd(FullScreenPlacement fullScreenPlacement) {
        if(isAdTooFrequent(fullScreenPlacement)){
            return;
        }
        KempaMediationAdapter adapter = KempaMediationAdapter.getInstance();
        KempaAd kempaAd = null;
        if(adapter!=null){
            kempaAd = adapter.getAdFromMediation(fullScreenPlacement.getType());
        }
        if (kempaAd != null) {
            showAd(kempaAd, fullScreenPlacement);
        }else if(fullScreenPlacement.getShowLoader() && adpumpLoader ==null){//rewarded placements will always have loader, so it will pass this condition
            pendingAds.add(fullScreenPlacement);
            adpumpLoader = new AdpumbLoader(fullScreenPlacement.getMaxLoadingTime(),new Action(){
                @Override
                public void doAction() {
                    adpumpLoader = null;
                    pendingAds.remove(fullScreenPlacement);
                    fullScreenPlacement.getAfterAdCompletion().onAdCompletion(false, PlacementDisplayStatus.NO_AD_AVAILABLE);
                }
            });
            adpumpLoader.setLoaderSettings(fullScreenPlacement.getLoaderSettings());
            adpumpLoader.showLoading("Loading");
        }else{
            pendingAds.add(fullScreenPlacement);
        }
    }

    private boolean isProperContext(FullScreenPlacement fullScreenPlacement) {

        if (!fullScreenPlacement.isContraintToActivity()) {
            return true;
        }
        if (fullScreenPlacement.getConstraintToActivities().contains(Adpumb.getActivityContext())) {
            return true;
        }
        return false;
    }

    private void showAd(KempaAd kempaInterstitialAd, FullScreenPlacement fullScreenPlacement) {
        Activity activity = Adpumb.getActivityContext();
        if (activity == null) {
            pendingAds.add(fullScreenPlacement);
            return;
        }
        if (!isProperContext(fullScreenPlacement)) {
            //todo: listen for desired context
            //add to pending
            pendingAds.add(fullScreenPlacement);
            return;
        }
        displayAd(kempaInterstitialAd, activity, fullScreenPlacement);

    }
    private synchronized void showPendingNativeAds() {
        if(pendingNativeAds.isEmpty()){
            return;
        }
        Set<NativePlacement> adsToRemove = new HashSet<>();
        for(NativePlacement placement: pendingNativeAds){
            if(publishNativeAd(placement)){
               adsToRemove.add(placement);
            }
        }
        pendingNativeAds.removeAll(adsToRemove);
    }
    private synchronized void showPendingAd() {
        if (pendingAds.isEmpty()) {
            return;
        }
        KempaMediationAdapter adapter = KempaMediationAdapter.getInstance();
        KempaAd ad = null;

        FullScreenPlacement pendingReward = getPendingRewardPlacement();

        if(adapter!=null){
            if (pendingReward != null){
                ad = adapter.getAdFromMediation(KempaRewardedAd.class);
            }else {
                ad = adapter.getAdFromMediation(KempaInterstitialAd.class);
            }

        }
        Activity activity = Adpumb.getActivityContext();
        if (ad == null || activity == null) {
            return;
        }

        if (pendingReward != null){
            showPendingReward(pendingReward, ad, activity);
            return;
        }

        FullScreenPlacement usedFullScreenPlacement = null;
        Set<FullScreenPlacement> adsToRemove = new HashSet<>();
        for (FullScreenPlacement fullScreenPlacement : pendingAds) {
            if(isAdTooFrequent(fullScreenPlacement)){
                adsToRemove.add(fullScreenPlacement);
                continue;
            }
            if (isProperContext(fullScreenPlacement)) {
                displayAd(ad, activity, fullScreenPlacement);
                usedFullScreenPlacement = fullScreenPlacement;
                break;
            }
        }
        if (usedFullScreenPlacement != null) {
            pendingAds.remove(usedFullScreenPlacement);
        }
        pendingAds.removeAll(adsToRemove);

    }

    private void showPendingReward(FullScreenPlacement pendingReward, KempaAd ad, Activity activity) {
        if (isProperContext(pendingReward)) {
            displayAd(ad, activity, pendingReward);
            pendingAds.remove(pendingReward);
        }
    }

    private FullScreenPlacement getPendingRewardPlacement() {
        for (FullScreenPlacement pendingPlacement : pendingAds) {
            if (pendingPlacement instanceof RewardedPlacement){
                return pendingPlacement;
            }
        }
        return null;
    }

    private void displayAd(KempaAd ad, Activity activity, FullScreenPlacement fullScreenPlacement) {
        Utils.runOnUi(new Action() {
            @Override
            public void doAction() {
                if(adpumpLoader !=null){
                    adpumpLoader.hideLoading();
                    adpumpLoader = null;
                }
                DataStore.getInstance().setAdShownTime(fullScreenPlacement);
                ad.show(activity, new AdCompletionHandler() {
                    @Override
                    public void adCompleted(boolean success) {
                        AdPumbAPI.getInstance().logImpressionThread(ad, fullScreenPlacement,KempaMediationAdapter.getInstance().getConfigVersion());
                        PlacementDisplayStatus status = PlacementDisplayStatus.IMPRESSION_REGISTERED;
                        if(!success){
                            status = PlacementDisplayStatus.USER_CANCELLED;
                        }
                        fullScreenPlacement.getAfterAdCompletion().onAdCompletion(success,status);
                    }
                });
            }
        });
    }

    public void showNativeAd(NativePlacement placement){

        if (!isShowing(placement)){
            refreshNativeAd(placement);
        }else {
            Log.d(Adpumb.TAG, "Native Placement - "+placement.getPlacementName()+" - is already showing, ignoring the new request");
        }

    }

    private void refreshNativeAd(NativePlacement placement) {
        boolean isPublished = publishNativeAd(placement);
        if(!isPublished){
            pendingNativeAds.add(placement);
        }
    }

    private boolean isShowing(NativePlacement placement) {
        return runningNativeAds.contains(placement.getPlacementName());
    }


    private synchronized boolean publishNativeAd(NativePlacement placement){
        KempaMediationAdapter adapter = KempaMediationAdapter.getInstance();
        if(adapter==null){
            return false;
        }
        if(!isAllowedOnThisActivity(placement)){
            runningNativeAds.remove(placement.getPlacementName());
            return false;
        }
        KempaNativeAd kempaNativeAd = adapter.getAdFromMediation(KempaNativeAd.class);
        if(kempaNativeAd!=null){
            //todo:first load boolean is not handled
            placement.getNativeAdListener().onAdRecieved((NativeAd) kempaNativeAd.getNativeAd(),false);
            runningNativeAds.add(placement.getPlacementName());
            nativeAdPostPublish(placement, kempaNativeAd);
            return true;
        }
        return false;
    }

    private void nativeAdPostPublish(NativePlacement placement, KempaNativeAd kempaNativeAd) {
        kempaNativeAd.markAdAsUsed();
        AdPumbAPI.getInstance().logImpressionThread(kempaNativeAd, placement,KempaMediationAdapter.getInstance().getConfigVersion());
        ThreadFactory.getInstance().getHandler().postDelayed(() -> {
            if (!placement.isDisposed()){
                refreshNativeAd(placement);
            }else {
                Log.d(Adpumb.TAG, "disposed placement "+placement.getPlacementName());
            }
        }, (long) placement.getRefreshRateInSeconds() * 1000);
    }

    private boolean isAllowedOnThisActivity(NativePlacement placement) {
        return Adpumb.getCurrentOrLastContext()==placement.getDesiredActivity();
    }

    public void disposeNativePlacement(NativePlacement placement){
//  dispose method inside display manager to remove running
        if (placement != null){
            placement.setDisposed(true);
            runningNativeAds.remove(placement.getPlacementName());
        }
    }

}
