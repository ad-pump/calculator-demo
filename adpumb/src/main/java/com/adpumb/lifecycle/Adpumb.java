package com.adpumb.lifecycle;

import android.app.Activity;
import android.util.Log;

import com.adpumb.ads.ADLibraryInitializor;
import com.adpumb.ads.analytics.AdPumbAnalyticsListener;
import com.adpumb.ads.config.AdConfigRepository;
import com.adpumb.ads.config.HttpAdConfigRepository;
import com.adpumb.ads.mediation.KempaMediationAdapter;
import com.adpumb.ads.util.ApiData;

public class Adpumb {
    public static String TAG = "adpumb";
    private static Activity activityContext;
    private static Activity lastContext;
    private static boolean isDebug;
    private static String packageName;
    private static AdPumbAnalyticsListener externalAnalytics = null;

    public static void register(Activity activity, boolean isDebugMode, AdPumbAnalyticsListener adPumbAnalyticsListener, AdConfigRepository adConfigRepository) {
        isDebug = isDebugMode;
        externalAnalytics = adPumbAnalyticsListener;
        Log.w(Adpumb.TAG, "Registering adpumb");
        activity.getApplication().registerActivityLifecycleCallbacks(new ActivityListener());
        packageName = activity.getApplication().getPackageName();
        Log.w(Adpumb.TAG, "package name recieved "+packageName);
        setActivityContext(activity);
        ADLibraryInitializor.initialize(activity,adConfigRepository == null ? new HttpAdConfigRepository() : adConfigRepository);
        KempaMediationAdapter.getInstance();
        ApiData.initialize();
    }
    public static void register(Activity activity, boolean isDebugMode, AdConfigRepository adConfigRepository) {
        register(activity,isDebugMode,null, adConfigRepository);
    }

    public static void register(Activity activity, boolean isDebugMode, AdPumbAnalyticsListener adPumbAnalyticsListener) {
        register(activity,isDebugMode,adPumbAnalyticsListener, null);
    }

    public static void register(Activity activity, boolean isDebugMode) {
        register(activity,isDebugMode,null, null);
    }

    public static boolean isDebugMode() {
        return isDebug;
    }

    public static String getAdConfiguration() {
        return AdConfiguration.CONFIG;
    }

    public static long retryDelayWithManager() {
        return 3000;
    }

    public static long defaultRetryDelay() {
        return 3000l;
    }

    public static boolean isAdAllowed() {
        return true;
    }

    public static Activity getActivityContext() {
        return activityContext;
    }

    public static void setActivityContext(Activity activityContext) {
        if (activityContext != null) {
            lastContext = activityContext;
        }
        Adpumb.activityContext = activityContext;
    }

    public static Activity getCurrentOrLastContext() {
        return lastContext;
    }

    public static String getPackageName() {
        return packageName;
    }

    public static AdPumbAnalyticsListener getExternalAnalytics() {
        return externalAnalytics;
    }
}
