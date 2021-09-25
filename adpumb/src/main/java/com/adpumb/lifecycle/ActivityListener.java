package com.adpumb.lifecycle;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.adpumb.ads.display.DisplayManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ActivityListener implements Application.ActivityLifecycleCallbacks {
    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle bundle) {
        Adpumb.setActivityContext(activity);
        System.out.println("life :created");
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        Adpumb.setActivityContext(activity);
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
        Adpumb.setActivityContext(activity);
        DisplayManager.getInstance().contextListener(activity);
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {
        if(Adpumb.getActivityContext()==activity){
            Adpumb.setActivityContext(null);
        }

    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
        if(Adpumb.getActivityContext()==activity){
            Adpumb.setActivityContext(null);
        }
    }
}
