# AdPumb android integration guide
## Prerequisite ##
You need to register with us before starting the integration. You can drop a mail to sales@adpumb.com
## Steps ##
1) Add your admob publisher id to the android-manifest.xml of your app: Adpump doesn't use your admob adunits, however adpump uses the underlying admob APIs for which publisher id is mandatory. Even those admob accounts which are having ad limitation will work fine with Adpump.

2) Add library dependency: Adpump is currently not hosted in maven central, hence you need to add the repository details to your gradle script to get the Adpump dependency resolved.

Please add the following to you build.gradle file of your app
```gradle
repositories {
    maven {
        url 'https://maven.adpumb.com/repository/adpumb/'
    }
}
dependencies {
    implementation 'com.adpumb:bidmachine:1.9.2' 
    *********************

```
3)  Adding config key to AndroidManifest
    On AndroidManifest.xml add meta data with name 'com.adpumb.config.key' values given by us.
```
   <meta-data 
   android:name="com.adpumb.config.key" 
   android:value="adpumb,AIzaSyBdR63r0eiZi6_wvGNlToNnVfVCm7sffwk,1:476641212837:android:440c356d4a8598bcda904c" />
    
```
> Adpump will initialize automatically as the application launches. You do not need to register/init manually.

## Type of ads supported by Adpumb
[1.Interstial](#interstial) </br>
[2.Reward](#reward) </br>
[3.Native](#native) </br>
[4.Banner](#banner)



### Interstial
Create Interstitial placement: Adpump is designed on the concept of placement rather than adunit. A placement is a predefined action sequence which ends up in showing an Ad. Consider the example of a calculator, where a user presses the addition (+) button and an ad is shown. Here we can consider the addition button click as a placement.
```java
private void onAdditionButtonClick() {
   InterstitialPlacement addition = new InterstitialPlacementBuilder()
            .name("addition") //Name of the placement is very important. Revenue dashboard will track the placement based on the name given. 
            .showLoaderTillAdIsReady(true)
            .loaderTimeOutInSeconds(5)
            .frequencyCapInSeconds(1)
            .build();            
   DisplayManager.getInstance().showAd(addition);
}            
```

In this example if the user presses the addition button before the ad is loaded/received from the server, then a loader will be shown. If the ad is not ready within 5 seconds then the loader will be removed. However if the ad is already loaded or it got loaded while the loader is shown, then ad loader will be hidden and ad will be shown to user.
For a particular placement you need to create only one placement object, which can be used to show multiple ads.

>For example:
```java
private static InterstitialPlacement addition = new InterstitialPlacementBuilder()
            .name("addition").build();
            
public void onResume(){
  DisplayManager.getInstance().showAd(addition);
}
```

>Callbacks: You can register callbacks to the placement

```java
InterstitialPlacement placement = new InterstitialPlacementBuilder()
                .name("division")
                .frequencyCapInSeconds(0)
                .showLoaderTillAdIsReady(true)
                .loaderTimeOutInSeconds(10000)
                .onAdCompletion(new AdCompletion() {
                    @Override
                    public void onAdCompletion(boolean isSuccess, PlacementDisplayStatus status) {
                        if(isSuccess){
                            Toast.makeText(MainActivity.this, "Thank you for watch the ad", Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(MainActivity.this, "Why you didnt watch the ad?", Toast.LENGTH_LONG).show();
                        }
                    }
                }).build();
        DisplayManager.getInstance().showAd(placement);
```

Customising loader animation: You can customize the loader using the loader settings for each placement
```java
        LoaderSettings loaderSettings = new LoaderSettings();
        loaderSettings.setLogoResID(R.drawable.arithmatic_button);
        loaderSettings.setMessageStyle(R.color.colorAccent, R.color.colorPrimary);
        //there are more options in loader settings which you can try
        InterstitialPlacement buttonPlacement = new InterstitialPlacementBuilder()
                .name("multiplication")  
                .loaderUISetting(loaderSettings)
                .showLoaderTillAdIsReady(true) //this will show loader anima
                .build();
        DisplayManager.getInstance().showAd(buttonPlacement);
```

<p align="center">
  <img src="https://github.com/ad-pump/calculator-demo/blob/nlshad-patch-2/Interstial-ad-demo.png">
</p>

### Reward
Rewarded Placement Implementation:
```java
private void onAdditionButtonClick() {
   RewardedPlacement rewardedPlacement = new RewardedPlacementBuilder()
                .name("placementName")
                .loaderTimeOutInSeconds(5)
                .onAdCompletion(new AdCompletion() {
                    @Override
                    public void onAdCompletion(boolean success, PlacementDisplayStatus placementDisplayStatus) {
                        if (success){
                            Toast.makeText(mActivity, "You have successfully watched Rewarded Ad", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(mActivity, "please watch Rewarded Ad - "+placementDisplayStatus.name(), Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .build();
   DisplayManager.getInstance().showAd(rewardedPlacement);
}            
```

Customising loader animation:
>You can customize the loader using the loader settings for each placement
```java
        LoaderSettings loaderSettings = new LoaderSettings();
        loaderSettings.setLogoResID(R.drawable.arithmatic_button);
        loaderSettings.setMessageStyle(R.color.colorAccent, R.color.colorPrimary);
        //there are more options in loader settings which you can try
        InterstitialPlacement buttonPlacement = new InterstitialPlacementBuilder()
                .name("multiplication")  
                .loaderUISetting(loaderSettings)
                .showLoaderTillAdIsReady(true) //this will show loader anima
                .build();
        DisplayManager.getInstance().showAd(buttonPlacement);
```
### Native
Native Ad Implementation
```java
NativePlacement nativePlacement = new NativePlacementBuilder()
                .name("placement_name_here")
                .toBeShownOnActivity(this)//Activity context
                .refreshRateInSeconds(15) //native ad will refresh in 15 seconds
                .adListener(new NativeAdListener() {
                    @Override
                    public void onAdRecieved(NativeAd nativeAd, boolean b) { //refreshed units will be returned here
                        //show native ads to your layout
                        //do something
                    }
                })
                .build();

        DisplayManager.getInstance().showNativeAd(nativePlacement,activity);
 ```

### Banner
Adpumb support banner ads of various types and handle the load and refresh. Since there are multiple type, its mandatory to mention the types you are going to use on the app in the manifest. It helps adpumb to cache the given type before you actually calls to show the ad.
If its not defined, then system assumes you are going to use the default type, ie ANCHORED .
You can define multiple types on the manifesto, but adding more types will end up in slow loading. So its better to limit it 1 or 2.


Define the banner type in android-manifest. 
```xml
        <meta-data
            android:name="com.adpumb.config.banner.types"
            android:value="INLINE,ANCHORED" />
        <meta-data
            android:name="com.adpumb.config.key"
            android:value="adpumb-test,AIzaSyAx4dxazFButNiZU4_rXT8hgaJNSREfmrw,1:693299279464:android:07e16d50af2a5719e6addd" />
    </application>
```
In the above example two type of banners INLINE and ANCHORED are defined.

#### Supported BannerType
- ANCHORED
- INLINE
- LARGE_BANNER
- MEDIUM_RECTANGLE
- WIDE_SKYSCRAPER

#### Anchored Banner
Typical use case of anchored banner is to show ad wisget on top or bottom of the screen. 
First step is to add BannerView on your layout
```xml
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="#000"
    tools:context=".MainActivity">
<!--    <Whatever goes here-->
    <com.adpumb.ads.banner.BannerView
        android:id="@+id/bannerContainer"
        android:layout_width="match_parent"
        android:gravity="center|top"
        android:layout_height="match_parent">
    </com.adpumb.ads.banner.BannerView>

</androidx.constraintlayout.widget.ConstraintLayout>
```
```java
 BannerPlacement banner = new BannerPlacementBuilder()
                .name("first_banner")
                .activity(this)
                .size(BannerPlacementBuilder.ANCHORED)
                .refreshRateInSeconds(15)
                .build();
        DisplayManager.getInstance().showBannerAd(banner, findViewById(R.id.bannerContainer), new BannerEvent() {
            @Override
            public void onImpressionLogged(BannerPlacement bannerPlacement) {
                //this will be called within few seconds a banner is shown to user
            }

            @Override
            public void onAdRefreshed(BannerPlacement bannerPlacement) {
                //this will be called after 10 seconds(given refresh rate) the onImpression is called
            }
        });
```
<p align="center">
  <img src="https://github.com/ad-pump/calculator-demo/blob/nlshad-patch-2/anchored.png">
</p>

#### Inline Banner
As the name implies, inline banners are shown generally between the content. Unlike a smart banner, Inline banner has dynamic heights, meaning different ads of Inline can be of different heights. This help Inline to grow or shrink based on the ad content. 
It is also possible to set the max height of Inline banner. However it should be set upfront on the manifest.
```xml
        <meta-data
            android:name="com.adpumb.config.banner.types"
            android:value="INLINE" />
        <meta-data
            android:name="com.adpumb.config.key"
            android:value="adpumb-test,AIzaSyAx4dxazFButNiZU4_rXT8hgaJNSREfmrw,1:693299279464:android:07e16d50af2a5719e6addd" />
        <meta-data
            android:name="com.adpumb.config.banner.inline.maxHeight"
            android:value="400" />
    </application>
</manifest>
```
<p align="center">
  <img src="https://github.com/ad-pump/calculator-demo/blob/nlshad-patch-2/Inline-banner.png">
</p>

```java
BannerPlacement bannerOne = new BannerPlacementBuilder().activity(this)
                .size(BannerPlacementBuilder.INLINE)
                .name("banner_one")
                .refreshRateInSeconds(15)
                .build();
BannerView container1 = findViewById(R.id.bannerContainer1);
DisplayManager.getInstance().showBannerAd(bannerOne,container1);
```

#### Other banner types
There is no additional steps required to other banner types. You can try them by adding it on the manifest and setting it as size on the banner placement.
<p align="center">
  <img src="https://github.com/ad-pump/calculator-demo/blob/nlshad-patch-2/others.png">
</p>

#### Ideal Refresh rate
There is no ideal refresh rate, however we recommend you using 15 seconds. You can alos try diffrent values by keeping the refresh rate as a Firebase remote config parameter and check which ones are giving you better yeild. Setting a very lower values such as 5 seconds might end up in low ecpm.


PS:**Please make sure you use different placement name for the placement created for different BannerView**
