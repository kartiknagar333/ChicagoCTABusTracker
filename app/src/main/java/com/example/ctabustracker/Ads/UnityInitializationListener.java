package com.example.ctabustracker.Ads;

import android.util.Log;
import com.unity3d.ads.IUnityAdsInitializationListener;
import com.unity3d.ads.UnityAds;

public class UnityInitializationListener implements IUnityAdsInitializationListener {

    private static final String TAG = "UnityInitializationList";
    private final AdsCallback adsCallback;

    public UnityInitializationListener(AdsCallback adsCallback) {
        this.adsCallback = adsCallback;
    }

    @Override
    public void onInitializationComplete() {
        Log.d(TAG, "onInitializationComplete: ");
        adsCallback.showBanner();
    }

    @Override
    public void onInitializationFailed(UnityAds.UnityAdsInitializationError error, String message) {
        Log.d(TAG, "onInitializationFailed: " + message);
        adsCallback.initFailed(message);
    }
}
