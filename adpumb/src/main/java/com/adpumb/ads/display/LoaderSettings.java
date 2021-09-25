package com.adpumb.ads.display;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.google.android.ads.nativetemplates.R;

public class LoaderSettings {

    int logoResID = 0;

    int msgStrokeColorResID = 0;
    int msgTextColorResID = 0;

    Sprite animationStyle = null;


    public int getLogoResID() {
        if (logoResID == 0){
            return R.drawable.ic_logo;
        }
        return logoResID;
    }

    public void setLogoResID(Integer logoResID) {
        this.logoResID = logoResID;
    }

    public void setMessageStyle(Integer strokeColorResID, Integer textColorResID){
        this.msgStrokeColorResID = strokeColorResID;
        this.msgTextColorResID = textColorResID;
    }

    public int getMsgStrokeColorResID() {

        return msgStrokeColorResID == 0 ? R.color.gnt_white : msgStrokeColorResID;
    }

    public int getMsgTextColorResID() {
        return msgTextColorResID == 0 ? R.color.gnt_red : msgTextColorResID;
    }

    public Sprite getAnimationStyle() {
        return animationStyle == null ? new DoubleBounce() : animationStyle;
    }

    public void setAnimationStyle(Sprite animationStyle) {
        this.animationStyle = animationStyle;
    }

}
