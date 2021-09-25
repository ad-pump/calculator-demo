package com.adpumb.ads.config;

import com.adpumb.ads.util.HttpConnection;
import com.adpumb.ads.util.HttpResponse;
import com.adpumb.lifecycle.Adpumb;

public class HttpAdConfigRepository implements AdConfigRepository {
    private static String AD_CONFIG_END_POINT = "https://api.adpumb.com/%s";

    @Override
    public String getAdConfig() {
        HttpConnection connection = new HttpConnection(getEndPoint());
        connection.setVerb(HttpConnection.GET);
        HttpResponse response = connection.connect(null);
        if (response.isSucess()) {
            return response.responseString();
        } else {
            return null;
        }
    }

    private String getEndPoint() {
        return String.format(AD_CONFIG_END_POINT, Adpumb.getPackageName());
    }
}
