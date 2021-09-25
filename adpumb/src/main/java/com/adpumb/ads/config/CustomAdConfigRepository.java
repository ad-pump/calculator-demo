package com.adpumb.ads.config;

public class CustomAdConfigRepository implements AdConfigRepository{

    String adConfig;

    public CustomAdConfigRepository(String adConfig) {
        this.adConfig = adConfig;
    }

    @Override
    public String getAdConfig() {
        return this.adConfig;
    }
}
