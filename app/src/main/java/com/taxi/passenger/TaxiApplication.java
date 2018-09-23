package com.taxi.passenger;

import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import io.fabric.sdk.android.Fabric;


public  class TaxiApplication extends MultiDexApplication
{

    @Override
    public void onCreate()
    {
        super.onCreate();
        MultiDex.install(this);
        TwitterAuthConfig authConfig = new TwitterAuthConfig("qARyLKqVjnbJ69aDKuCsiOBfo", "bJGtMyN2v0DXqBJPt5AHQkdSUglsRLwBhxhv2Nto4aiP5hY1VD");
        Fabric.with(this, new Twitter(authConfig));
    }
}

