package com.adpumb.ads.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.adpumb.ads.display.FullScreenPlacement;
import com.adpumb.lifecycle.Adpumb;

public class DataStore {
    private SharedPreferences preferences;
    private static DataStore store;
    private DataStore(Context context){
        preferences = context.getSharedPreferences("AUTH", Context.MODE_MULTI_PROCESS | Context.MODE_PRIVATE);
    }

    public static DataStore getInstance(){
        if(store==null){
            store = new DataStore(Adpumb.getCurrentOrLastContext());
        }
        return store;
    }

    public void setAdShownTime(FullScreenPlacement adType){
        String name = adType.getPlacementName();
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(name,System.currentTimeMillis());
        editor.apply();
    }

    public long getAdShownTime(FullScreenPlacement adType){
        return preferences.getLong(adType.getPlacementName(),0);
    }

}
