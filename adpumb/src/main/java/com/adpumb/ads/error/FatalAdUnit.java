package com.adpumb.ads.error;

public class FatalAdUnit extends Exception {
    private String unit;
    public FatalAdUnit(String adUnit){
        this.unit = adUnit;
    }
}
