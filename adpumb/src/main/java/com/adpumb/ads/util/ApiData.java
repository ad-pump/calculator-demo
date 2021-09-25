package com.adpumb.ads.util;

import com.adpumb.lifecycle.Adpumb;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.gson.JsonObject;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ApiData {
    static ApiData apiData = new ApiData();
    JsonObject data ;
    public static  ApiData getInstance(){
        return apiData;
    }
    public static void initialize(){getInstance().getAdvertisingId();}
    ApiData(){
        data = new JsonObject();
        collectData();
    }

    private void collectData() {
        data.addProperty("adUnit","");
        data.addProperty("ecpm","");
        data.addProperty("isDebug",false);
        data.addProperty("advertisingId","");
        data.addProperty("time",System.currentTimeMillis());
        collectEnvData();
    }
    private void getAdvertisingId() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    AdvertisingIdClient.Info adInfo = AdvertisingIdClient.getAdvertisingIdInfo(Adpumb.getCurrentOrLastContext());
                    String myId = adInfo != null ? adInfo.getId() : null;
                    data.addProperty("advertisingId",myId);
                } catch (Exception e) {
                }
            }
        });
    }
    private void collectEnvData() {
        JsonObject env = new JsonObject();
        env.addProperty("IS_DEBUG", Adpumb.isDebugMode());
        env.addProperty("OSVERSION", System.getProperty("os.version"));
        env.addProperty("RELEASE", android.os.Build.VERSION.RELEASE);
        env.addProperty("DEVICE", android.os.Build.DEVICE);
        env.addProperty("MODEL", android.os.Build.MODEL);
        env.addProperty("PRODUCT", android.os.Build.PRODUCT);
        env.addProperty("BRAND", android.os.Build.BRAND);
        env.addProperty("DISPLAY", android.os.Build.DISPLAY);
        env.addProperty("CPU_ABI", android.os.Build.CPU_ABI);
        env.addProperty("CPU_ABI2", android.os.Build.CPU_ABI2);
        //data.addProperty("UNKNOWN",android.os.Build.UNKNOWN);
        env.addProperty("HARDWARE", android.os.Build.HARDWARE);
        env.addProperty("ID", android.os.Build.ID);
        env.addProperty("MANUFACTURER", android.os.Build.MANUFACTURER);
        env.addProperty("SERIAL", android.os.Build.SERIAL);
        env.addProperty("USER", android.os.Build.USER);
        env.addProperty("HOST", android.os.Build.HOST);
        data.add("device",env);
    }

    public JsonObject getData() {
        data.addProperty("adUnit","");
        data.addProperty("ecpm","");
        data.addProperty("isDebug", Adpumb.isDebugMode());
        data.addProperty("placementName","");
        return data;
    }
}
