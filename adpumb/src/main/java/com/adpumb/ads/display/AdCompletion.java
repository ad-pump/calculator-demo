package com.adpumb.ads.display;

import com.adpumb.ads.error.PlacementDisplayStatus;

public interface AdCompletion {
    void onAdCompletion(boolean isSuccess, PlacementDisplayStatus status);
}
