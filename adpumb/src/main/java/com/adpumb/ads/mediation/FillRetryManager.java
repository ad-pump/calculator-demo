package com.adpumb.ads.mediation;

import com.adpumb.ads.error.ADError;
import com.adpumb.ads.util.Utils;
import com.adpumb.lifecycle.Adpumb;

import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;



public class FillRetryManager {

    private static FillRetryManager instance = new FillRetryManager(true);
    private Map<String, Set<RetryableAd>> adMap;
    private Map<RetryableAd, String> vendorMap;
    private boolean isEnabled;


    public FillRetryManager(boolean isEnabled) {
        adMap = new ConcurrentHashMap<>();
        vendorMap = new ConcurrentHashMap<>();
        this.isEnabled = isEnabled;
    }

    public static FillRetryManager getInstance() {
        return instance;
    }

    public static void setInstance(FillRetryManager retryManager){
        instance = retryManager;
    }

    public void setEnable(boolean enable) {
        this.isEnabled = enable;
        if(enable==false){
            reloadAllAds();
        }
    }

    public long retryDelay(RetryableAd retryableAd,ADError error){
        if(error==ADError.NETWORK){
            return 1000;//retry network error in a second.
        }
        if (error == ADError.TOO_FREQUENT){
            return 10000;//retry after 10 seconds
        }
        String vendor = vendorMap.get(retryableAd);
        if(Utils.isBlank(vendor)){
            return Adpumb.defaultRetryDelay()*1000;
        }
        return Adpumb.retryDelayWithManager()*1000;
    }


    private void reloadAllAds() {
        for(RetryableAd ad: vendorMap.keySet()){
            if(ad.shouldReload()){
                ad.loadAd();
            }
        }
    }

    public synchronized void add(String subvendor, RetryableAd retryableAd) {
        if(Utils.isBlank(subvendor)){
            return;
        }
        Set<RetryableAd> retryableAds = adMap.get(subvendor);
        if (retryableAds == null) {
            retryableAds = new TreeSet<>();
            adMap.put(subvendor, retryableAds);
        }
        vendorMap.put(retryableAd, subvendor);
        retryableAds.add(retryableAd);
    }

    public void adLoaded(RetryableAd retryableAd) {
        String vendor = vendorMap.get(retryableAd);
        if(Utils.isBlank(vendor)){
            return;
        }
        Set<RetryableAd> ads = adMap.get(vendor);
        RetryableAd nextAdToLoad = nextAdToLoad(retryableAd,ads);
        if(nextAdToLoad!=null){
            nextAdToLoad.loadAd();
        }
    }

    private RetryableAd nextAdToLoad(RetryableAd loadedAd, Set<RetryableAd> adList) {
        boolean retryNext = false;
        for (RetryableAd ad : adList) {
            if(retryNext){
                if(ad.shouldReload()){
                    return ad;
                }
            }
            if (ad.equals(loadedAd)) {
                retryNext = true;
            }
        }
        return null;
    }

    //todo:should rety network error
    public boolean canRetry(RetryableAd retryableAd) {
        if(!isEnabled){
            System.out.println("retry manager not enabled "+retryableAd.getEcpm());
            return true;
        }
        String subvendor = vendorMap.get(retryableAd);
        if(Utils.isBlank(subvendor)){

            return true;
        }
        Set<RetryableAd> ads = adMap.get(subvendor);
        for (RetryableAd ad : ads) {
            if (isSame(retryableAd.getEcpm(), ad.getEcpm())) {
                System.out.println("retry allowed"+retryableAd.getEcpm());
                return true;
            }
            if (!ad.isAdLoaded()) {
                System.out.println("retry rejected"+retryableAd.getEcpm());
                return false;
            }
        }
        System.out.println("retry allowed"+retryableAd.getEcpm());
        return true;
    }

    public boolean isSame(float a, float b) {
        return Math.abs(a - b) < 0.1;
    }


}
