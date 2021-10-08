package com.example.new_sample;

public class NativeHolderRegistry {

    MyRecyclerViewAdapter.NativeAdViewHolder nativeAdViewHolder;
    long lastRefreshedOn;


    public NativeHolderRegistry(MyRecyclerViewAdapter.NativeAdViewHolder nativeAdViewHolder, long lastRefreshedOn) {
        this.nativeAdViewHolder = nativeAdViewHolder;
        this.lastRefreshedOn = lastRefreshedOn;
    }

    public MyRecyclerViewAdapter.NativeAdViewHolder getNativeAdViewHolder() {
        return nativeAdViewHolder;
    }

    public void setNativeAdViewHolder(MyRecyclerViewAdapter.NativeAdViewHolder nativeAdViewHolder) {
        this.nativeAdViewHolder = nativeAdViewHolder;
    }

    public long getLastRefreshedOn() {
        return lastRefreshedOn;
    }

    public void setLastRefreshedOn(long lastRefreshedOn) {
        this.lastRefreshedOn = lastRefreshedOn;
    }
}
