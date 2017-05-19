package cf.qishui.sodnotelean.account;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.orhanobut.logger.Logger;

/**
 * Created by wangxiao on 2017/5/17.
 */

public class AuthenticatorService extends Service {
    private Authenticator mAuthenticator;

    @Override
    public void onCreate() {
        mAuthenticator = new Authenticator(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Logger.d("onBind " + mAuthenticator);
        return mAuthenticator.getIBinder();
    }
}
