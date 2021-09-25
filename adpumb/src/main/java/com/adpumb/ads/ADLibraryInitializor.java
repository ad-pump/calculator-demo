package com.adpumb.ads;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import android.util.Log;

import com.adpumb.ads.config.AdConfigRepository;
import com.adpumb.ads.util.Utils;
import com.adpumb.lifecycle.AdConfiguration;
import com.facebook.ads.AdSettings;
import com.facebook.ads.AudienceNetworkAds;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.adpumb.ads.mediation.KempaMediationAdapter;
import com.adpumb.lifecycle.Adpumb;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;


public class ADLibraryInitializor {
    private static AtomicBoolean initCalled = new AtomicBoolean(false);
    public synchronized static void initialize(Activity activity, AdConfigRepository configRepository) {
        if(initCalled.get()){
            return;
        }
        if (Adpumb.isDebugMode()) {
            RequestConfiguration configuration = new RequestConfiguration.Builder().setTestDeviceIds(Arrays.asList("C53453B224A27F7AECD8727557216B8C", "7C7A4756520AF5C1682D157B66612D99","C17DD0E199F64A1B9E249AC867D5AA9F")).build();
            MobileAds.setRequestConfiguration(configuration);
            AdSettings.addTestDevice("ae9745b9-6f04-4f17-9e11-00ef98327a6a");
            AdSettings.addTestDevice("d70639bf-3fb3-4db1-8624-f91efd50beb6");
        }
        if (KempaMediationAdapter.getInstance() == null) {
            MobileAds.initialize(activity);
            AudienceNetworkAds.initialize(activity);
        }
        new Thread(){
            @Override
            public void run() {
                String config = configRepository.getAdConfig();
                while (config==null){
                    try {
                        sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    config = configRepository.getAdConfig();
                }
                if(config!=null){
                    Handler handler = new Handler(Looper.getMainLooper());
                    String adConfig = decode(config);
                    handler.post(() -> KempaMediationAdapter.initialize(adConfig));
                }
            }
        }.start();


    }

    private static String decode(String data){
        String decoded = new String( Base64.decode(Base64.decode(data,Base64.DEFAULT),Base64.DEFAULT));
        return decoded;
    }



}
