package com.adpumb.ads.mediation;

public class KempaAdUnit {
    private String adUnit;
    private float ecpm;
    private String subVendor;
    private String type;

    public String getAdUnit() {
        return adUnit;
    }

    public Float getEcpm() {
        return ecpm;
    }

    public void setAdUnit(String adUnit) {
        this.adUnit = adUnit;
    }

    public void setEcpm(float ecpm) {
        this.ecpm = ecpm;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        KempaAdUnit adUnit1 = (KempaAdUnit) o;

        if (Float.compare(adUnit1.ecpm, ecpm) != 0) return false;
        return adUnit.equals(adUnit1.adUnit);
    }

    @Override
    public int hashCode() {
        int result = adUnit.hashCode();
        result = 31 * result + (ecpm != +0.0f ? Float.floatToIntBits(ecpm) : 0);
        return result;
    }

    public String getSubVendor() {
        return subVendor;
    }

    public void setSubVendor(String subVendor) {
        this.subVendor = subVendor;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

/*


 */