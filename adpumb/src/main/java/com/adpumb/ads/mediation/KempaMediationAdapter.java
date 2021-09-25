package com.adpumb.ads.mediation;

import android.util.Log;

import com.adpumb.ads.GoogleInterstitialAd;
import com.adpumb.ads.KempaAd;
import com.adpumb.ads.KempaAdFactory;
import com.adpumb.ads.KempaInterstitialAd;
import com.adpumb.ads.util.Utils;
import com.adpumb.lifecycle.Adpumb;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.TestOnly;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;


public class KempaMediationAdapter {


    private static boolean isApplovinInitiated = false;
    private static KempaMediationAdapter mediationAdapter;
    private static long updateTime;
    private static boolean isConfigLoaded = false;
    private static Set<KempaMediationAdapterListener> listeners = new HashSet<>();
    private KempaAdConfig kempaAdConfig;


    private Map<Integer, ArrayList<KempaAd>> ads;
    private String config;

    private Map<Integer, Integer> pointer = new HashMap<>();

    public KempaMediationAdapter(String configuration) {

        this.config = configuration;
//        Log.w(Adpumb.TAG, "using config "+config);
        this.kempaAdConfig = new Gson().fromJson(configuration, KempaAdConfig.class);
        Log.w(Adpumb.TAG, "json parsed "+this.kempaAdConfig.getVersion());
        FillRetryManager.getInstance().setEnable(kempaAdConfig.isEnableRetryManager());
        Log.w(Adpumb.TAG, "retry manager is "+kempaAdConfig.isEnableRetryManager());
        createAdUnits();
    }

    public static void addInstanceReadyListener(KempaMediationAdapterListener listener) {
        listeners.add(listener);
    }

    public static boolean getIsConfigLoaded() {
        return KempaMediationAdapter.isConfigLoaded;
    }

    public static void setIsConfigLoaded(boolean isConfigLoaded) {
        KempaMediationAdapter.isConfigLoaded = isConfigLoaded;
    }

    public static synchronized KempaMediationAdapter initialize(String configuration) {
        updateTime = System.currentTimeMillis();
        if (mediationAdapter == null) {
            if (Utils.isBlank(configuration)) {
                return null;
            }
            Log.w(Adpumb.TAG, "init mediation adapter");
            mediationAdapter = new KempaMediationAdapter(configuration);
            Log.w(Adpumb.TAG, "mediation adapter created");
            return mediationAdapter;
        }
        if (configuration == null) {
            configuration = "{}";
        } else {
            KempaMediationAdapter.isConfigLoaded = true;
        }
        if (configuration.hashCode() != mediationAdapter.config.hashCode()) {
            applyConfigChange(configuration);
        }
        reloadInvalidAd(mediationAdapter);
        fireListeners();
        return mediationAdapter;
    }

    private static void fireListeners() {
        for (KempaMediationAdapterListener listener : listeners) {
            listener.adapterOnReady();
        }
        listeners.clear();
    }

    private static void reloadInvalidAd(KempaMediationAdapter adapter) {
        for (Collection<KempaAd> adlist : adapter.ads.values()) {
            for (KempaAd ad : adlist) {
                if (ad instanceof GoogleInterstitialAd && !ad.isAdValid()) {
                    ad.loadAd();
                }
            }
        }
    }

    public static synchronized KempaMediationAdapter getInstance() {
        return mediationAdapter;
    }

    private static boolean isStale() {
        return System.currentTimeMillis() - updateTime > 1000 * 60;
    }

    @TestOnly
    public static void setUpdateTime(long newTime) {
        updateTime = newTime;
    }

    private static void applyConfigChange(String config) {
        KempaAdConfig newConfig = new Gson().fromJson(config, KempaAdConfig.class);
        FillRetryManager.getInstance().setEnable(newConfig.isEnableRetryManager());
        Map<String, KempaAd> oldAdHash = createAdHash(mediationAdapter.ads);
        Map<Integer, ArrayList<KempaAd>> newAds = new TreeMap<>(Collections.reverseOrder());

        Set<String> newKeys = new HashSet<>();
        for (KempaAdNetwork network : newConfig.getAdNetworks()) {
            for (KempaAdUnit newUnit : network.getKempaAdUnits()) {
                int index = (int) (newUnit.getEcpm() * 100);
                ArrayList<KempaAd> kempaAds = newAds.get(index);
                if (kempaAds == null) {
                    kempaAds = new ArrayList<>();
                }
                String newKey = createAdHash(newUnit.getAdUnit(), newUnit.getEcpm());
                if (oldAdHash.containsKey(newKey)) {
                    KempaAd oldAd = oldAdHash.get(newKey);
                    kempaAds.add(oldAd);
                } else {
                    KempaAd newAd = KempaAdFactory.getInstance().createAd(Adpumb.getCurrentOrLastContext(), network.getAdVendor(), newUnit);
                    kempaAds.add(newAd);
                }
                newAds.put(index, kempaAds);
                newKeys.add(newKey);
            }
        }
        for (String oldKey : oldAdHash.keySet()) {
            if (newKeys.contains(oldKey)) {
                continue;
            }
            KempaAd oldAd = oldAdHash.get(oldKey);
            oldAd.setInvalid(true);
        }
        mediationAdapter.ads = newAds;
    }

    private static Map<String, KempaAd> createAdHash(Map<Integer, ArrayList<KempaAd>> allAds) {
        Map<String, KempaAd> adSet = new HashMap();

        for (Map.Entry<Integer, ArrayList<KempaAd>> entry : allAds.entrySet()) {
            // int key = entry.getKey();
            ArrayList<KempaAd> kempaAds = entry.getValue();
            for (KempaAd adx : kempaAds) {
                adSet.put(createAdHash(adx.getAdUnitId(), adx.getEcpm()), adx);
            }
        }
        return adSet;
    }

    private static String createAdHash(String adunitId, float ecpm) {
        DecimalFormat df = new DecimalFormat("#.00");
        return adunitId + ":" + df.format(ecpm);
    }

    public static boolean isApplovinInitiated() {
        return isApplovinInitiated;
    }

    public static void setIsApplovinInitiated(boolean isApplovinInitiated) {
        KempaMediationAdapter.isApplovinInitiated = isApplovinInitiated;
    }


    public String getConfigVersion() {
        return kempaAdConfig.getVersion();
    }

    public int getGoogleInterstitialAdReload() {
        return kempaAdConfig.getGoogleInterstitialAdReload();
    }

    public int getGoogleRewardedAdReload() {
        return kempaAdConfig.getGoogleRewardedAdReload();
    }

    public int getGoogleNativeAdReload() {
        return kempaAdConfig.getGoogleNativeAdReload();
    }

    public boolean isPlacementDisAllowed(String placementName) {
        return kempaAdConfig.getDisabledPlacements().contains(placementName);
    }

    public KempaInterstitialAd getAd() { return getAdFromMediation(KempaInterstitialAd.class); }

    public <T> T getAd(Class<T> t) {
        return (T) getAdFromMediation(t);
    }

    private KempaAd getAdFromList(Integer multipliedEcpm, ArrayList<KempaAd> adList) {
        ArrayList<KempaAd> loadedAds = new ArrayList<>();
        for (KempaAd ad : adList) {
            if (isAdReady(ad)) {
                loadedAds.add(ad);
            }
        }
        if (loadedAds.isEmpty()) {
            return null;
        }
        if (loadedAds.size() == 1) {
            return loadedAds.get(0);
        }
        Integer currentIndex = pointer.get(multipliedEcpm);
        if ((currentIndex == null) || (currentIndex >= loadedAds.size())) {
            currentIndex = 0;
        }
        KempaAd selectedAd = loadedAds.get(currentIndex++);
        pointer.put(multipliedEcpm, currentIndex);
        return selectedAd;
    }

    //this method ensures all the ads with ecpn greater or equal are loaded or failed before picking up an ad
    //this will prevent loading a low value ad which has better load time compated to an high value ad
    private boolean isCpmCutOffCompleted(List<KempaAd> adlist) {
        float cutoffEcpm = kempaAdConfig.getValidationEcpm();
        if (adlist.isEmpty()) {
            //strange case
            return false;
        }
        KempaAd firstAd = adlist.get(0);
        if (firstAd.getEcpm() <= cutoffEcpm) {
            //low value ads, no need to check cutoff
            return true;
        }
        for (KempaAd ad : adlist) {
            if (!ad.isAdRequestCompleted()) {
                return false;
            }
        }
        return true;
    }

    public <T> T getAdFromMediation(Class<T> type) {
        for (Map.Entry<Integer, ArrayList<KempaAd>> listOfAds : ads.entrySet()) {
            if (!isCpmCutOffCompleted(listOfAds.getValue())) {
                return null;
            }
            KempaAd selectedAd = getAdFromList(listOfAds.getKey(), listOfAds.getValue());
            if (selectedAd != null && type.isInstance(selectedAd)) {
                return (T) selectedAd;
            }
        }
        return null;
    }

    private boolean isAdReady(KempaAd ad) {
        if (!ad.isAdLoaded()) {
            return false;
        }
        if (!ad.isAdValid()) {

            ad.loadAd();
            return false;
        }
        return true;
    }


    private void createAdUnits() {
        ads = new TreeMap<>(Collections.reverseOrder());
        KempaAdFactory adFactory = KempaAdFactory.getInstance();
        for (KempaAdNetwork adNetwork : kempaAdConfig.getAdNetworks()) {
            Log.w(Adpumb.TAG, "creating units for "+adNetwork.getAdVendor());
            String adVendor = adNetwork.getAdVendor();
            for (KempaAdUnit adUnit : adNetwork.getKempaAdUnits()) {
                Log.w(Adpumb.TAG, "creating units for "+adUnit.getAdUnit());
                KempaAd kempaAd = adFactory.createAd(Adpumb.getCurrentOrLastContext(), adVendor, adUnit);
                if (kempaAd == null) {
                    continue;
                }
                FillRetryManager.getInstance().add(adUnit.getSubVendor(), kempaAd);
                ArrayList<KempaAd> adList = ads.get(kempaAd.getEcpmAsInt());
                if (adList == null) {
                    adList = new ArrayList<>();
                    ads.put(kempaAd.getEcpmAsInt(), adList);
                }
                adList.add(kempaAd);
            }
        }

    }

    @NotNull
    private Comparator<KempaAd> ecpmCoparator() {
        return new Comparator<KempaAd>() {
            @Override
            public int compare(KempaAd ad1, KempaAd ad2) {
                return Float.compare(ad2.getEcpm(), ad1.getEcpm());
            }
        };
    }

    public KempaAdConfig getKempaAdConfig() {
        return kempaAdConfig;
    }

    public Map<Integer, ArrayList<KempaAd>> getAllAds() {
        return ads;
    }


    public void setAllAds(Map<Integer, ArrayList<KempaAd>> newAds) {
        ads = newAds;
    }
}
