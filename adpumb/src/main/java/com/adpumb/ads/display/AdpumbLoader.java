package com.adpumb.ads.display;

import android.app.Activity;
import android.app.Dialog;
import android.util.Log;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.ads.nativetemplates.R;
import com.adpumb.ads.util.Action;
import com.adpumb.ads.util.Utils;
import com.adpumb.lifecycle.Adpumb;

import java.util.Timer;
import java.util.TimerTask;


public class AdpumbLoader {

    private String tag="KempaLoader";
    private Dialog dialog;
    private TextView tvLoadingMessage;
    private Timer timer;
    private Long delay;
    private Action onFailure;
    LoaderSettings loaderSettings = null;

    public AdpumbLoader(Long timeOut, Action onFailure) {
        this.delay = timeOut;
        this.onFailure = onFailure;
    }

    private void initTimer() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Utils.runOnUi(new Action() {
                    @Override
                    public void doAction() {
                        Utils.disposeDialog(dialog);
                        onFailure.doAction();
                    }
                });
            }
        }, delay*1000);
    }

    public LoaderSettings getLoaderSettings() {
        return loaderSettings;
    }

    public void setLoaderSettings(LoaderSettings loaderSettings) {
        this.loaderSettings = loaderSettings;
    }

    public void showLoading(String message) throws NullPointerException {
        Activity activity = Adpumb.getActivityContext();
        if(activity==null||activity.isFinishing()) {
            Log.e(tag,"Activity invalid : "+message);
            return;
        }
        dialog  = new Dialog(activity);
        dialog.setOwnerActivity(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //...set cancelable false so that it's never get hidden
        dialog.setCancelable(false);
        //...that's the layout i told you will inflate later
        dialog.setContentView(R.layout.adpump_loading);
        applyLoaderSettings(dialog);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        dialog.setOwnerActivity(Adpumb.getActivityContext());

        //...initialize the imageView form infalted layout
        tvLoadingMessage = dialog.findViewById(R.id.tv_loader_message);

        //...finaly show it
        dialog.show();
        initTimer();

        if (message != null){
            setMessage(message);
        }
    }

    private void applyLoaderSettings(Dialog dialog) {
        if (loaderSettings != null){
            ImageView logo = dialog.findViewById(R.id.img_logo);
            logo.setImageResource(loaderSettings.getLogoResID());

            MagicTextView loadingMsg = dialog.findViewById(R.id.tv_loader_message);
            loadingMsg.setTextColor(dialog.getOwnerActivity().getResources().getColor(loaderSettings.getMsgTextColorResID()));
            loadingMsg.setStroke(1, dialog.getOwnerActivity().getResources().getColor(loaderSettings.getMsgStrokeColorResID()));

            SpinKitView spinKitView = dialog.findViewById(R.id.av_connection);
            spinKitView.setIndeterminateDrawable(loaderSettings.getAnimationStyle());

        }
    }

    //..also create a method which will hide the dialog when some work is done
    public void hideLoading(){
        if(timer!=null){
            timer.cancel();
        }
        Utils.disposeDialog(dialog);
    }

    public void setMessage(String message){
        if(dialog == null) {
            Log.i(tag,"Dialog in null");
            return;
        }
        Activity activityContext = Adpumb.getActivityContext();
        if( (activityContext == null) || activityContext.isFinishing() ){
            return;
        }
        Utils.runOnUi(new Action(){
            @Override
            public void doAction() {
                try {
                    tvLoadingMessage.setText(message);
                }catch (Exception e){
                    e.printStackTrace();
                    Log.e(tag,"Wrong thread maybe");
                }
            }
        });

    }
}
