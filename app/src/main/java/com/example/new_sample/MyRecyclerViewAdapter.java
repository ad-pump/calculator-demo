package com.example.new_sample;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import com.adpumb.ads.nativetemplates.NativeTemplateStyle;
import com.adpumb.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.nativead.NativeAd;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<String> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    static final int TYPE_CONTENT = 0;
    static final int TYPE_NATIVE_AD = 1;

    Activity activity;
    Map<String, NativePlacement> nativePlacements = new HashMap<>();

    // data is passed into the constructor
    MyRecyclerViewAdapter(Activity activity, List<String> data) {
        this.mInflater = LayoutInflater.from(activity);
        this.activity = activity;
        this.mData = prepareData(data);
    }

    private List<String> prepareData(List<String> data) {
        for (int index = 0; index < data.size() / 3; index++) {
            data.add(index*3, "native_ad_"+index);
        }
        return data;
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
//            case TYPE_NATIVE_AD:
//                displayNativeAd(mData.get(position), (NativeAdViewHolder) holder);
//                break;
        }


    }

    @Override
    public void onViewAttachedToWindow(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);

        if (holder.getItemViewType() == TYPE_NATIVE_AD){
            displayNativeAd(mData.get(holder.getLayoutPosition()), (NativeAdViewHolder) holder);
        }
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        if (holder.getItemViewType() == TYPE_NATIVE_AD){
            DisplayManager.getInstance().disposeNativePlacement(nativePlacements.get(mData.get(holder.getLayoutPosition())));
            nativePlacements.remove(mData.get(holder.getLayoutPosition()));
            Log.d("Adpumb", "disposed native placement = "+mData.get(holder.getLayoutPosition()));
        }
    }



    private void displayNativeAd(String placementName, NativeAdViewHolder holder) {

        NativePlacement nativePlacement = new NativePlacementBuilder()
                .name(placementName)
                .toBeShownOnActivity(activity)
                .refreshRateInSeconds(5)
                .adListener(new NativeAdListener() {
                    @Override
                    public void onAdRecieved(NativeAd nativeAd, boolean b) {
                        Log.d("Adpumb", "refreshing placement - "+placementName);
                        MyRecyclerViewAdapter.this.showNativeAd(nativeAd, holder);
                    }
                })
                .build();

        DisplayManager.getInstance().showNativeAd(nativePlacement,activity);
        nativePlacements.put(placementName, nativePlacement);
    }

    private void showNativeAd(NativeAd nativeAd, NativeAdViewHolder holder){


        if (activity.isDestroyed() || activity.isFinishing()) {
            //framework make sure this case never happen
            //need to put crashlytics here to verify that claim
            return;
        }

        NativeTemplateStyle styles = new
                NativeTemplateStyle.Builder().withMainBackgroundColor(new ColorDrawable(Color.parseColor("#f3f3f6"))).build();

        holder.nativeTemplate.setVisibility(View.VISIBLE);

        holder.nativeTemplate.setStyles(styles);
        holder.nativeTemplate.setNativeAd(nativeAd);
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
