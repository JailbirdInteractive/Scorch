package com.jailbird.scorch;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.os.StrictMode;
import android.support.multidex.MultiDex;

//import com.facebook.FacebookSdk;
//import com.facebook.appevents.AppEventsLogger;
import com.digits.sdk.android.Digits;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import io.fabric.sdk.android.Fabric;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by adria on 10/4/2016.
 */

public class MyApplication extends Application {
    public static Resources resources;

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "sRlB5BXTZLyh79IAJSokQBK2Z";
    private static final String TWITTER_SECRET = "dc0p5ZGzeHFkTzWaIyRPHClb2p3eJUTBZhic61nyRYVXk6JYp3";

    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Comfortaa-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        resources=getResources();
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);

        Fabric.with(this, new TwitterCore(authConfig), new Digits.Builder().build());
        //Digits.enableSandbox();
        //LoginApplication.startSocialLogin(this);

        //FacebookSdk.sdkInitialize(getApplicationContext());
        //AppEventsLogger.activateApp(this);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}
