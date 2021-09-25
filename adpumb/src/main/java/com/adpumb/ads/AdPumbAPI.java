package com.adpumb.ads;

import com.adpumb.ads.analytics.ImpressionData;
import com.adpumb.ads.display.FullScreenPlacement;
import com.adpumb.ads.display.NativePlacement;
import com.adpumb.ads.util.ApiData;
import com.adpumb.ads.util.HttpConnection;
import com.adpumb.ads.util.HttpResponse;
import com.adpumb.lifecycle.Adpumb;
import com.google.gson.JsonObject;


public class AdPumbAPI {
    private static final String BASE_URL = "https://api.adpumb.com/";
    private static final String API_PATH_AD_IMPRESSION = "adApi";
    private static AdPumbAPI adPumbAPI = null;
    private String clientId;

    AdPumbAPI() {

    }

    public static AdPumbAPI getInstance() {
        if (adPumbAPI == null) {
            adPumbAPI = new AdPumbAPI();
            adPumbAPI.setCredentials(Adpumb.getPackageName());
        }
        return adPumbAPI;
    }

    public void setCredentials(String clientId) {
        this.clientId = clientId;
    }

    public JsonObject logImpression(String adUnit, float ecpm,String placementName,String configVersion) {
        JsonObject req = new JsonObject();
        req.addProperty("clientId", clientId);
        JsonObject object = ApiData.getInstance().getData();
        object.addProperty("adUnit", adUnit);
        object.addProperty("ecpm", ecpm);
        object.addProperty("placementName", placementName);
        object.addProperty("adConfigversion", configVersion);
        object.addProperty("time",System.currentTimeMillis());
        req.add("data", object);

        if (Adpumb.getExternalAnalytics() != null){
            Adpumb.getExternalAnalytics().onEvent(new ImpressionData(clientId, adUnit, ecpm, placementName, configVersion, System.currentTimeMillis()));
        }

        return doPost(BASE_URL + API_PATH_AD_IMPRESSION, req);
    }

    public void logImpressionThread(KempaAd ad, FullScreenPlacement fullScreenPlacement, String configVersion) {
        new Thread(() -> {
            logImpression(ad.getAdUnitId(), ad.getEcpm(), fullScreenPlacement.getPlacementName(),configVersion);
        }).start();
    }

    public void logImpressionThread(KempaAd ad, NativePlacement nativePlacement, String configVersion) {
        new Thread(() -> {
            logImpression(ad.getAdUnitId(), ad.getEcpm(), nativePlacement.getPlacementName(),configVersion);
        }).start();
    }

    public JsonObject doPost(String api_url, JsonObject data) {
        try {
            HttpConnection httpConnection = new HttpConnection(api_url);
            httpConnection.setVerb(HttpConnection.POST);
            httpConnection.addHeader("Content-Type", "application/json; utf-8");
            httpConnection.addHeader("Accept", "application/json");
            HttpResponse httpResponse = httpConnection.connect(data.toString());
            return httpResponse.responseObject(JsonObject.class);

        } catch (Throwable e) {
            return null;
        }
    }

}
