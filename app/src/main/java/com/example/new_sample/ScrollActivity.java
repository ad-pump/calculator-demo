package com.example.new_sample;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.adpumb.ads.banner.BannerView;
import com.adpumb.ads.display.BannerEvent;
import com.adpumb.ads.display.BannerPlacement;
import com.adpumb.ads.display.BannerPlacementBuilder;
import com.adpumb.ads.display.DisplayManager;

public class ScrollActivity extends AppCompatActivity implements BannerEvent {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.long_page);
        Button button = findViewById(R.id.btn_act);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ScrollActivity.this.finish();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        BannerView container1 = findViewById(R.id.bannerContainer1);
        BannerView container2 = findViewById(R.id.bannerContainer2);
        BannerPlacement bannerOne = new BannerPlacementBuilder().activity(this)
                .size(BannerPlacementBuilder.INLINE)
                .name("banner_one")
                .refreshRateInSeconds(5)
                .build();
        BannerPlacement bannerTwo = new BannerPlacementBuilder()
                .activity(this)
                .name("banner_two")
                .refreshRateInSeconds(5)
                .size(BannerPlacementBuilder.INLINE)
                .build();

        BannerPlacement staticPlacement = new BannerPlacementBuilder().name("top_banner")
                .activity(this)
                .size(BannerPlacementBuilder.ANCHORED)
                .refreshRateInSeconds(5)
                .build();
        DisplayManager.getInstance().showBannerAd(staticPlacement,findViewById(R.id.bannerContainer));

        DisplayManager.getInstance().showBannerAd(bannerTwo, container2, this);
        DisplayManager.getInstance().showBannerAd(bannerOne,container1);
    }

    @Override
    public void onImpressionLogged(BannerPlacement placement) {
        Log.d("calc","Impression for "+placement.getPlacementName());
    }

    @Override
    public void onAdRefreshed(BannerPlacement placement) {
        Log.d("calc","Ad about to refresh for "+placement.getPlacementName());
    }
}
