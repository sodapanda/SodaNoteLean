package cf.qishui.sodnotelean;

import android.app.Application;

import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;

/**
 * Created by wangxiao on 2017/5/10.
 * <p>
 * Global Application
 */

public class SodApp extends Application {
    private static SodApp INSTANTS;

    @Override
    public void onCreate() {
        super.onCreate();

        INSTANTS = this;

        initApp();
    }

    public static SodApp getAppContext() {
        return INSTANTS;
    }

    private void initApp() {
        if (BuildConfig.DEBUG) {
            Logger.init("SODLog").logLevel(LogLevel.FULL);
        } else {
            Logger.init().logLevel(LogLevel.NONE);
        }
    }
}
