package com.example.new_sample;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.adpumb.ads.analytics.AdPumbAnalyticsListener;
import com.adpumb.ads.analytics.ImpressionData;
import com.adpumb.ads.display.AdCompletion;
import com.adpumb.ads.display.BannerPlacement;
import com.adpumb.ads.display.BannerPlacementBuilder;
import com.adpumb.ads.display.DisplayManager;
import com.adpumb.ads.display.InterstitialPlacement;
import com.adpumb.ads.display.InterstitialPlacementBuilder;
import com.adpumb.ads.display.LoaderSettings;
import com.adpumb.ads.display.NativeAdListener;
import com.adpumb.ads.display.NativePlacement;
import com.adpumb.ads.display.NativePlacementBuilder;
import com.adpumb.ads.display.RewardedPlacement;
import com.adpumb.ads.display.RewardedPlacementBuilder;
import com.adpumb.ads.error.PlacementDisplayStatus;
import com.adpumb.ads.nativetemplates.NativeTemplateStyle;
import com.adpumb.ads.nativetemplates.TemplateView;
import com.adpumb.lifecycle.AdPumbConfiguration;
import com.google.android.gms.ads.nativead.NativeAd;


public class MainActivity extends AppCompatActivity {

    private Button b1;
    private Button b2;
    private Button b3;
    private Button b4;
    private Button b5;
    private Button b6;
    private Button b7;
    private Button b8;
    private Button b9;
    private Button b0;
    private Button b_equal;
    private Button b_multi;
    private Button b_divide;
    private Button b_add;
    private Button b_sub;
    private Button b_clear;
    private Button b_dot;
    private Button b_para1;
    private Button b_para2;
    private TextView t1;
    private TextView t2;
    private final char ADDITION = '+';
    private final char SUBTRACTION = '-';
    private final char MULTIPLICATION = '*';
    private final char DIVISION = '/';
    private final char EQU = '=';
    private final char EXTRA = '@';
    private final char MODULUS = '%';
    private char ACTION;
    private double val1 = Double.NaN;
    private double val2;

    private Activity mActivity;

    private AdPumbAnalyticsListener adPumbAnalyticsListener = new AdPumbAnalyticsListener() {
        @Override
        public void onEvent(ImpressionData impressionData) {
            if (t2 != null){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        t2.setText(impressionData.getPlacementName() + " shown");
                    }
                });
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        BannerPlacement banner = new BannerPlacementBuilder()
                .name("first_banner")
                .activity(this)
                .size(BannerPlacementBuilder.ANCHORED)
                .refreshRateInSeconds(10)
                .build();
        DisplayManager.getInstance().showBannerAd(banner,findViewById(R.id.bannerContainer));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AdPumbConfiguration.getInstance().setExternalAnalytics(adPumbAnalyticsListener);
        setContentView(R.layout.activity_main);
        mActivity = this;
        viewSetup();

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.this.startActivity(new Intent(MainActivity.this,ScrollActivity.class));
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ifErrorOnOutput();
                exceedLength();
                t1.setText(t1.getText().toString() + "2");
            }
        });

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ifErrorOnOutput();
                exceedLength();
                t1.setText(t1.getText().toString() + "3");
            }
        });

        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ifErrorOnOutput();
                exceedLength();
                t1.setText(t1.getText().toString() + "4");
            }
        });

        b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ifErrorOnOutput();
                exceedLength();
                t1.setText(t1.getText().toString() + "5");
            }
        });

        b6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ifErrorOnOutput();
                exceedLength();
                t1.setText(t1.getText().toString() + "6");
            }
        });

        b7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ifErrorOnOutput();
                exceedLength();
                t1.setText(t1.getText().toString() + "7");
            }
        });

        b8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ifErrorOnOutput();
                exceedLength();
                t1.setText(t1.getText().toString() + "8");
            }
        });

        b9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ifErrorOnOutput();
                exceedLength();
                t1.setText(t1.getText().toString() + "9");
            }
        });

        b0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ifErrorOnOutput();
                exceedLength();
                t1.setText(t1.getText().toString() + "0");
            }
        });

        b_dot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exceedLength();
                t1.setText(t1.getText().toString() + ".");
            }
        });

        b_para1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (t1.getText().length() > 0) {
                    ACTION = MODULUS;
                    operation();
                    if (!ifReallyDecimal()) {
                        t2.setText(val1 + "%");
                    } else {
                        t2.setText((int) val1 + "%");
                    }
                    t1.setText(null);
                } else {
                    t2.setText("Error");
                }

                showNativeAdsInRecycler();
            }
        });

        b_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DisplayManager.getInstance().showAd(getRewardedPlacement("addition_rewarded_ad", mActivity, new AdCompletion() {
                    @Override
                    public void onAdCompletion(boolean success, PlacementDisplayStatus placementDisplayStatus) {
                        if (success){
                            Toast.makeText(mActivity, "You have successfully watched Rewarded Ad", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(mActivity, "please watch Rewarded Ad - "+placementDisplayStatus.name(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }) );


                if (t1.getText().length() > 0) {
                    ACTION = ADDITION;
                    operation();
                    if (!ifReallyDecimal()) {
                        t2.setText(val1 + "+");
                    } else {
                        t2.setText((int) val1 + "+");
                    }
                    t1.setText(null);
                } else {
                    t2.setText("Error");
                }
            }
        });

        b_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DisplayManager.getInstance().showAd(getInterstitialWithCustomLoader("subtraction_interstitial_custom_loader", new AdCompletion() {
                    @Override
                    public void onAdCompletion(boolean success, PlacementDisplayStatus placementDisplayStatus) {
                        if (success){
                            Toast.makeText(mActivity, "You have successfully watched Interstial Ad", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(mActivity, "please watch Ad - "+placementDisplayStatus.name(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }));

                if (t1.getText().length() > 0) {
                    ACTION = SUBTRACTION;
                    operation();
                    if (t1.getText().length() > 0)
                        if (!ifReallyDecimal()) {
                            t2.setText(val1 + "-");
                        } else {
                            t2.setText((int) val1 + "-");
                        }
                    t1.setText(null);
                } else {
                    t2.setText("Error");
                }

            }
        });

        b_multi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DisplayManager.getInstance().showAd(getInterstitialPlacement("multiplication_interstitial_with_callback", new AdCompletion() {
                    @Override
                    public void onAdCompletion(boolean success, PlacementDisplayStatus placementDisplayStatus) {
                        if (success){
                            Toast.makeText(mActivity, "You have successfully watched Ad", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(mActivity, "please watch Ad"+placementDisplayStatus.name(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }));
                if (t1.getText().length() > 0) {
                    ACTION = MULTIPLICATION;
                    operation();
                    if (!ifReallyDecimal()) {
                        t2.setText(val1 + "×");
                    } else {
                        t2.setText((int) val1 + "×");
                    }
                    t1.setText(null);
                } else {
                    t2.setText("Error");
                }
            }
        });

        b_divide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DisplayManager.getInstance().showAd(getInterstitialPlacement("divide_simple_interstitial", null));
                if (t1.getText().length() > 0) {
                    ACTION = DIVISION;
                    operation();
                    if (ifReallyDecimal()) {
                        t2.setText((int) val1 + "/");
                    } else {
                        t2.setText(val1 + "/");
                    }
                    t1.setText(null);
                } else {
                    t2.setText("Error");
                }
            }
        });

        b_para2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!t2.getText().toString().isEmpty() || !t1.getText().toString().isEmpty()) {
                    val1 = Double.parseDouble(t1.getText().toString());
                    ACTION = EXTRA;
                    t2.setText("-" + t1.getText().toString());
                    t1.setText("");
                } else {
                    t2.setText("Error");
                }
            }
        });

        b_equal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                displayNativeAd("equals_native_ad");

                if (t1.getText().length() > 0) {
                    operation();
                    ACTION = EQU;
                    if (!ifReallyDecimal()) {
                        t2.setText(/*t2.getText().toString() + String.valueOf(val2) + "=" + */String.valueOf(val1));
                    } else {
                        t2.setText(/*t2.getText().toString() + String.valueOf(val2) + "=" + */String.valueOf((int) val1));
                    }
                    t1.setText(null);
                } else {
                    t2.setText("Error");
                }
            }
        });

        b_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                hideNativeAd();

                if (t1.getText().length() > 0) {
                    CharSequence name = t1.getText().toString();
                    t1.setText(name.subSequence(0, name.length() - 1));
                } else {
                    val1 = Double.NaN;
                    val2 = Double.NaN;
                    t1.setText("");
                    t2.setText("");
                }
            }
        });

        // Empty text views on long click.
        b_clear.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                val1 = Double.NaN;
                val2 = Double.NaN;
                t1.setText("");
                t2.setText("");
                return true;
            }
        });
    }

    private void viewSetup() {
        b1 = findViewById(R.id.button1);
        b2 = findViewById(R.id.button2);
        b3 = findViewById(R.id.button3);
        b4 = findViewById(R.id.button4);
        b5 = findViewById(R.id.button5);
        b6 = findViewById(R.id.button6);
        b7 = findViewById(R.id.button7);
        b8 = findViewById(R.id.button8);
        b9 = findViewById(R.id.button9);
        b0 = findViewById(R.id.button0);
        b_equal = findViewById(R.id.button_equal);
        b_multi = findViewById(R.id.button_multi);
        b_divide = findViewById(R.id.button_divide);
        b_add = findViewById(R.id.button_add);
        b_sub = findViewById(R.id.button_sub);
        b_clear = findViewById(R.id.button_clear);
        b_dot = findViewById(R.id.button_dot);
        b_para1 = findViewById(R.id.button_para1);
        b_para2 = findViewById(R.id.button_para2);
        t1 = findViewById(R.id.input);
        t2 = findViewById(R.id.output);
    }

    private boolean isDebug(){
        return ( 0 != ( getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE ) );
    }

    private void operation() {
        if (!Double.isNaN(val1)) {
            if (t2.getText().toString().charAt(0) == '-') {
                val1 = (-1) * val1;
            }
            val2 = Double.parseDouble(t1.getText().toString());

            switch (ACTION) {
                case ADDITION:
                    val1 = val1 + val2;
                    break;
                case SUBTRACTION:
                    val1 = val1 - val2;
                    break;
                case MULTIPLICATION:
                    val1 = val1 * val2;
                    break;
                case DIVISION:
                    val1 = val1 / val2;
                    break;
                case EXTRA:
                    val1 = (-1) * val1;
                    break;
                case MODULUS:
                    val1 = val1 % val2;
                    break;
                case EQU:
                    break;
            }
        } else {
            val1 = Double.parseDouble(t1.getText().toString());
        }
    }

    // Remove error message that is already written there.
    private void ifErrorOnOutput() {
        if (t2.getText().toString().equals("Error")) {
            t2.setText("");
        }
    }

    // Whether value if a double or not
    private boolean ifReallyDecimal() {
        return val1 == (int) val1;
    }

    private void noOperation() {
        String inputExpression = t2.getText().toString();
        if (!inputExpression.isEmpty() && !inputExpression.equals("Error")) {
            if (inputExpression.contains("-")) {
                inputExpression = inputExpression.replace("-", "");
                t2.setText("");
                val1 = Double.parseDouble(inputExpression);
            }
            if (inputExpression.contains("+")) {
                inputExpression = inputExpression.replace("+", "");
                t2.setText("");
                val1 = Double.parseDouble(inputExpression);
            }
            if (inputExpression.contains("/")) {
                inputExpression = inputExpression.replace("/", "");
                t2.setText("");
                val1 = Double.parseDouble(inputExpression);
            }
            if (inputExpression.contains("%")) {
                inputExpression = inputExpression.replace("%", "");
                t2.setText("");
                val1 = Double.parseDouble(inputExpression);
            }
            if (inputExpression.contains("×")) {
                inputExpression = inputExpression.replace("×", "");
                t2.setText("");
                val1 = Double.parseDouble(inputExpression);
            }
        }
    }


    private InterstitialPlacement getInterstitialWithCustomLoader(String placementName, AdCompletion adCompletion){
        LoaderSettings loaderSettings = new LoaderSettings();
        loaderSettings.setLogoResID(R.drawable.arithmatic_button);
        loaderSettings.setMessageStyle(R.color.colorAccent, R.color.colorPrimary);

        InterstitialPlacement customLoaderPlacement = new InterstitialPlacementBuilder()
                .name(placementName)  // placement name is considered as id.
                .loaderUISetting(loaderSettings)
                .showLoaderTillAdIsReady(true) //this will show loader anima
                .loaderTimeOutInSeconds(10) //after time out, the loader will be hidden and onAdCompletion will get called.
                .frequencyCapInSeconds(10) // N calls within 10 seconds will endup showing only one ad for the placment with name 'multiplication'
                .onAdCompletion(adCompletion)
                .build();

        return customLoaderPlacement;
    }

    private RewardedPlacement getRewardedPlacement(String placementName, Context mContext, AdCompletion adCompletion){
        RewardedPlacement rewardedPlacement = new RewardedPlacementBuilder()
                .name(placementName)
                .loaderTimeOutInSeconds(5)
                .onAdCompletion(adCompletion)
                .build();

        return rewardedPlacement;
    }

    private InterstitialPlacement getInterstitialPlacement(String placementName, AdCompletion adCompletion){
        InterstitialPlacement addition = new InterstitialPlacementBuilder()
                .name(placementName)
                .showLoaderTillAdIsReady(true)
                .loaderTimeOutInSeconds(10)
                .frequencyCapInSeconds(5)
                .onAdCompletion(adCompletion)
                .build();

        return addition;
    }

    private void displayNativeAd(String placementName){

        NativePlacement nativePlacement = new NativePlacementBuilder()
                .name(placementName)
                .toBeShownOnActivity(this)
                .refreshRateInSeconds(15)
                .adListener(new NativeAdListener() {
                    @Override
                    public void onAdRecieved(NativeAd nativeAd, boolean b) {
                        showNativeAd(nativeAd);
                    }
                })
                .build();

        DisplayManager.getInstance().showNativeAd(nativePlacement,this);

    }

    private void hideNativeAd(){
        mActivity.findViewById(R.id.native_ad_template).setVisibility(View.INVISIBLE);
    }

    private void showNativeAd(NativeAd nativeAd){

        mActivity.findViewById(R.id.native_ad_template).setVisibility(View.VISIBLE);

        if (mActivity.isDestroyed() || mActivity.isFinishing()) {
            //framework make sure this case never happen
            //need to put crashlytics here to verify that claim
            return;
        }

        NativeTemplateStyle styles = new
                NativeTemplateStyle.Builder().withMainBackgroundColor(new ColorDrawable(Color.parseColor("#f3f3f6"))).build();

        TemplateView template = mActivity.findViewById(R.id.native_ad_template);
        template.setVisibility(View.VISIBLE);

        template.setStyles(styles);
        template.setNativeAd(nativeAd);
    }


    private void showNativeAdsInRecycler() {

        Intent recyclerActivity = new Intent(mActivity, ListActivity.class);
        mActivity.startActivity(recyclerActivity);
    }

    // Make text small if too many digits.
    private void exceedLength() {
        if (t1.getText().toString().length() > 10) {
            t1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        }
    }
}