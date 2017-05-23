package cf.qishui.sodnotelean;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;

import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cf.qishui.sodnotelean.database.UserInfoTable;
import cf.qishui.sodnotelean.network.SodaNoteConverterFactory;
import cf.qishui.sodnotelean.ui.LoginActivity;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

/**
 * Created by wangxiao on 2017/5/10.
 * <p>
 * Global Application
 */

public class SodApp extends Application {
    private static SodApp INSTANTS;

    private Retrofit mRetrofit;

    private List<Activity> mActs = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();

        INSTANTS = this;

        initApp();
    }

    public static SodApp getApp() {
        return INSTANTS;
    }

    private void initApp() {
        if (BuildConfig.DEBUG) {
            Logger.init("SODLog").logLevel(LogLevel.FULL);
        } else {
            Logger.init().logLevel(LogLevel.NONE);
        }

        FlowManager.init(this);

        Stetho.initializeWithDefaults(this);

        initHttp();

        initLifeCallback();
    }

    private void initHttp() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(new StethoInterceptor())
                .addInterceptor(chain -> {
                    UserInfoTable userInfoTable = SQLite.select()
                            .from(UserInfoTable.class)
                            .querySingle();

                    HttpUrl newUrl = chain.request().url();
                    if (userInfoTable != null) {
                        newUrl = chain.request().url()
                                .newBuilder()
                                .addQueryParameter("token", userInfoTable.Token)
                                .build();
                    }

                    Request request = chain.request().newBuilder().url(newUrl).build();
                    return chain.proceed(request);
                })
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(SodaNoteConverterFactory.create().failOnUnknown());
        mRetrofit = builder.client(okHttpClient).build();
    }

    public Retrofit retrofit() {
        return mRetrofit;
    }

    private void initLifeCallback() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                mActs.add(activity);
            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                mActs.remove(activity);
            }
        });
    }

    public void finishAllActs() {
        for (Activity act : mActs) {
            act.finish();
        }

        mActs.clear();
    }

    public void startLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

}
