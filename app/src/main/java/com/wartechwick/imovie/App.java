package com.wartechwick.imovie;

import android.app.Application;
import android.content.Context;

/**
 * Created by penghaitao on 2016/10/9.
 */
public class App extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    public static Context getApplication() {
        return mContext;
    }
}
