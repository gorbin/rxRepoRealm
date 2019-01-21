package com.punicapp.sample;

import android.app.Application;

import io.realm.Realm;

/**
 * Created by Evgeny on 24.01.18.
 */

public class SampleApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
    }
}
