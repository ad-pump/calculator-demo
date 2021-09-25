package com.adpumb.ads.display;

import com.adpumb.ads.KempaAd;
import com.google.android.gms.ads.nativead.NativeAd;

public interface NativeAdListener {

    void onAdRecieved(NativeAd nativeAd, boolean firstLoad);
}
