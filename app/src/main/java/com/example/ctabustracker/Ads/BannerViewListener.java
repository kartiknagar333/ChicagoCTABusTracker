package com.example.ctabustracker.Ads;

import android.util.Log;
import com.unity3d.services.banners.BannerErrorInfo;
import com.unity3d.services.banners.BannerView;

public class BannerViewListener implements BannerView.IListener {

    private static final String TAG = "BannerViewListener";
    private final AdsCallback adsCallback;

    public BannerViewListener(AdsCallback adsCallback) {
        this.adsCallback = adsCallback;
    }

    @Override
    public void onBannerLoaded(BannerView bannerView) {
        Log.d(TAG, "onBannerLoaded: ");
        // You could optionally notify via adsCallback if needed.
    }

    @Override
    public void onBannerShown(BannerView bannerAdView) {
        Log.d(TAG, "onBannerShown: ");
    }

    @Override
    public void onBannerClick(BannerView bannerView) {
        Log.d(TAG, "onBannerClick: ");
    }

    @Override
    public void onBannerFailedToLoad(BannerView bannerView, BannerErrorInfo errorInfo) {
        Log.d(TAG, "onBannerFailedToLoad: " + errorInfo);
        // You could call adsCallback.initFailed(errorInfo.toString()) or another method if desired.
    }

    @Override
    public void onBannerLeftApplication(BannerView bannerView) {
        Log.d(TAG, "onBannerLeftApplication: ");
    }
}
