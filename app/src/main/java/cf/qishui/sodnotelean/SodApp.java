package cf.qishui.sodnotelean;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import cf.qishui.sodnotelean.model.LoginModel;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;

/**
 * Created by wangxiao on 2017/5/10.
 * <p>
 * Global Application
 */

public class SodApp extends Application {
    private static SodApp INSTANTS;

    private Retrofit mRetrofit;

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
    }

    private void initHttp() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(new StethoInterceptor())
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        LoginModel loginModel = SQLite.select()
                                .from(LoginModel.class)
                                .querySingle();

                        HttpUrl newUrl = chain.request().url();
                        if (loginModel != null) {
                            newUrl = chain.request().url().newBuilder().addQueryParameter("token", loginModel.Token).build();
                        }

                        Request request = chain.request().newBuilder().url(newUrl).build();
                        return chain.proceed(request);
                    }
                })
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(MoshiConverterFactory.create());
        mRetrofit = builder.client(okHttpClient).build();
    }

    public Retrofit retrofit() {
        return mRetrofit;
    }

}
