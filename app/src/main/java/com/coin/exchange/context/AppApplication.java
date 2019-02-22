package com.coin.exchange.context;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.coin.exchange.cache.CacheHelper;
import com.coin.exchange.cache.PreferenceManager;
import com.coin.exchange.di.component.AppComponent;
import com.coin.exchange.di.component.DaggerAppComponent;
import com.coin.exchange.di.module.AppModule;
import com.coin.libbase.utils.ToastUtil;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/11/7
 * @description
 */
public class AppApplication extends MultiDexApplication {

    @SuppressLint("StaticFieldLeak")
    private static Context CONTEXT;

    @NonNull
    private static AppComponent appComponent;
    @Inject
    PreferenceManager preferenceManager;

    @Override
    public void onCreate() {
        super.onCreate();

        CONTEXT = this;
        ToastUtil.init(this);

        appComponent = DaggerAppComponent.builder().appModule(new AppModule(this)).build();
        appComponent.inject(this);

        CacheHelper.init(this);
    }

    public static Context getContext() {
        return CONTEXT;
    }

    public static AppComponent getAppComponent() {
        return appComponent;
    }
}
