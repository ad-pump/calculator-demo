package com.adpumb.ads.mediation;

import java.util.Set;

public class KempaAdNetwork {
    private Set<KempaAdUnit> kempaAdUnits;
    private String adVendor;

    public Set<KempaAdUnit> getKempaAdUnits() {
        return kempaAdUnits;
    }

    public String getAdVendor() {
        return this.adVendor;
    }

    public void setKempaAdUnits(Set<KempaAdUnit> kempaAdUnits) {
        this.kempaAdUnits = kempaAdUnits;
    }

    public void setAdVendor(String adVendor){
        this.adVendor = adVendor;
    }
}
