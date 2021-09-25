package com.adpumb.ads;


import com.adpumb.ads.error.ADError;

public interface KempaAdListener {
     void onAdLoaded();
     void onError(ADError errorType);
     void onAdCompleted(boolean success);
}
