package com.adpumb.ads.util;


import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.Set;

public class Utils {
    public static boolean isBlank(String string){
        return string==null || "".equals(string.trim());
    }
    public static void disposeDialog(Dialog dialog){
        if(dialog==null){
            return;
        }
        Activity ownerActivity = dialog.getOwnerActivity();
        if(ownerActivity==null){
            // never reach here
            Log.i("dialog","missed ownerActivity");
            try{
                if(dialog.isShowing()){ dialog.dismiss(); }
            } catch (Exception e){
                //todo:log crashz
            }
            return;
        }
        if(ownerActivity.isFinishing() ){
            return;
        }
        if(dialog.isShowing()){ dialog.dismiss(); }
    }

    public static void runOnUi(Action action) {

        android.os.Handler osHandler = new android.os.Handler(Looper.getMainLooper());
        osHandler.post(new Runnable() {
            @Override
            public void run() {
                action.doAction();
            }
        });
    }
}
