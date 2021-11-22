package com.example.new_sample;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.adpumb.ads.display.DisplayManager;
import com.adpumb.ads.display.NativeAdListener;
import com.adpumb.ads.display.NativePlacement;
import com.adpumb.ads.display.NativePlacementBuilder;
import com.adpumb.lifecycle.Adpumb;
import com.google.android.ads.nativetemplates.NativeTemplateStyle;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.nativead.NativeAd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<String> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    static final int TYPE_CONTENT = 0;
    static final int TYPE_NATIVE_AD = 1;

    Map<Integer, NativeHolderRegistry> nativeHolderRegistryMap = new HashMap<>();
    ArrayList<NativePlacement> nativePlacements = new ArrayList<>();

    Activity activity;

    // data is passed into the constructor
    MyRecyclerViewAdapter(Activity activity, List<String> data) {
        this.mInflater = LayoutInflater.from(activity);
        this.activity = activity;
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = mInflater.inflate(R.layout.recyclerview_row, parent, false);

        switch (viewType){
            case TYPE_CONTENT:
                view = mInflater.inflate(R.layout.recyclerview_row, parent, false);
                return new ContentViewHolder(view);
            case TYPE_NATIVE_AD:
                view = mInflater.inflate(R.layout.lyt_native_ad, parent, false);
                return new NativeAdViewHolder(view);
            default:
                view = mInflater.inflate(R.layout.recyclerview_row, parent, false);
        }

        return new ContentViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        switch (holder.getItemViewType()){
            case TYPE_CONTENT:
                String animal = mData.get(position);
                ContentViewHolder contentViewHolder = (ContentViewHolder) holder;
                contentViewHolder.myTextView.setText(animal);
                break;

        }


    }

    @Override
    public void onViewAttachedToWindow(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);

        if (holder.getItemViewType() == TYPE_NATIVE_AD){
            registerNativeHolder((NativeAdViewHolder) holder);
        }
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        if (holder.getItemViewType() == TYPE_NATIVE_AD){
            unRegisterNativeHolder((NativeAdViewHolder) holder);
        }
    }

    private void unRegisterNativeHolder(NativeAdViewHolder holder) {
        nativeHolderRegistryMap.remove(holder.getLayoutPosition());

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                adjustNativePlacements();
            }
        }, 3000); //wait 2 seconds to avoid fast disposal request


    }

    private void registerNativeHolder(NativeAdViewHolder holder) {
        nativeHolderRegistryMap.put(holder.getLayoutPosition(), new NativeHolderRegistry(holder, 0));
        adjustNativePlacements();
    }

    private void adjustNativePlacements() {
        int requiredPlacementCount = nativeHolderRegistryMap.size() - nativePlacements.size();
        for (int count = 0; count < requiredPlacementCount; count++) {
            nativePlacements.add(createNativePlacement("placement - "+nativePlacements.size()));
        }

        int abundantPlacementCount = nativePlacements.size() - nativeHolderRegistryMap.size();
        for (int i = 0; i < abundantPlacementCount; i++) {
            DisplayManager.getInstance().disposeNativePlacement(nativePlacements.get(nativePlacements.size()-1));
            nativePlacements.remove(nativePlacements.get(nativePlacements.size()-1));
        }
    }

    private NativePlacement createNativePlacement(String placementName) {
        NativePlacement nativePlacement = new NativePlacementBuilder()
                .name(placementName)
                .toBeShownOnActivity(activity)
                .refreshRateInSeconds(5)
                .adListener(new NativeAdListener() {
                    @Override
                    public void onAdRecieved(NativeAd nativeAd, boolean b) {
                        Log.d(Adpumb.TAG, "refreshing placement - "+placementName);
                        MyRecyclerViewAdapter.this.refreshNativeAd(nativeAd);
                    }
                })
                .build();

        DisplayManager.getInstance().showNativeAd(nativePlacement);

        return nativePlacement;
    }

    private void refreshNativeAd(NativeAd nativeAd){


        if (activity.isDestroyed() || activity.isFinishing()) {
            //framework make sure this case never happen
            //need to put crashlytics here to verify that claim
            return;
        }

        NativeTemplateStyle styles = new
                NativeTemplateStyle.Builder().withMainBackgroundColor(new ColorDrawable(Color.parseColor("#f3f3f6"))).build();

        NativeAdViewHolder holder = null;
        try {
            System.out.println("adpumb next holder key = "+getNextHolderKey());
            NativeHolderRegistry nativeHolderRegistry = nativeHolderRegistryMap.get(getNextHolderKey());
            holder = nativeHolderRegistry.getNativeAdViewHolder();
            holder.nativeTemplate.setVisibility(View.VISIBLE);

            holder.nativeTemplate.setStyles(styles);
            holder.nativeTemplate.setNativeAd(nativeAd);

            nativeHolderRegistry.setLastRefreshedOn(System.currentTimeMillis());
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private int getNextHolderKey() throws Exception{

        long lowestUpdateTime = System.currentTimeMillis();
        int nextHolderKey = -1;

        for (Map.Entry<Integer, NativeHolderRegistry> entry : nativeHolderRegistryMap.entrySet()) {
            if (entry.getValue().lastRefreshedOn < lowestUpdateTime){
                lowestUpdateTime = entry.getValue().lastRefreshedOn;
                nextHolderKey = entry.getKey();
            }
        }

        if (nextHolderKey == -1){
            throw new IllegalStateException();
        }

        return nextHolderKey;
    }


    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ContentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myTextView;

        ContentViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.tvAnimalName);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // stores and recycles views as they are scrolled off screen
    public class NativeAdViewHolder extends RecyclerView.ViewHolder {

        TemplateView nativeTemplate;

        NativeAdViewHolder(View itemView) {
            super(itemView);
            nativeTemplate = itemView.findViewById(R.id.native_ad_view_list);
        }

    }

    // convenience method for getting data at click position
    String getItem(int id) {
        return mData.get(id);
    }

    @Override
    public int getItemViewType(int position) {
        if (mData.get(position).contains("native_ad")){
            return TYPE_NATIVE_AD;
        }

        return TYPE_CONTENT;
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
