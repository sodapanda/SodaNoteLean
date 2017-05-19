package cf.qishui.sodnotelean.account;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.orhanobut.logger.Logger;

import cf.qishui.sodnotelean.ui.LoginActivity;

/**
 * Created by wangxiao on 2017/5/17.
 */

public class Authenticator extends AbstractAccountAuthenticator {
    private Context mContext;
    private FakeHttpServer mHttpServer;

    public static final String ACCOUNT_TYPE = "account_type";
    public static final String AUTH_TYPE = "auth_type";
    public static final String IS_ADD_NEW = "is_adding_new";
    public static final String AUTH_TOKEN_FULL = "full_token";

    public Authenticator(Context context) {
        super(context);
        mContext = context;

        mHttpServer = new FakeHttpServer();
    }

    @Override
    public Bundle editProperties(AccountAuthenticatorResponse response, String accountType) {
        return null;
    }

    @Override
    public Bundle addAccount(AccountAuthenticatorResponse response, String accountType, String authTokenType, String[] requiredFeatures, Bundle options) throws NetworkErrorException {
        Logger.d("add account");
        Logger.d("%s %s %s %s %s", response, accountType, authTokenType, requiredFeatures, options);
        Intent intent = new Intent(mContext, LoginActivity.class);
        intent.putExtra(ACCOUNT_TYPE, accountType);
        intent.putExtra(AUTH_TYPE, authTokenType);
        intent.putExtra(IS_ADD_NEW, true);
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);

        Bundle bundle = new Bundle();
        bundle.putParcelable(AccountManager.KEY_INTENT, intent);

        return bundle;
    }

    @Override
    public Bundle confirmCredentials(AccountAuthenticatorResponse response, Account account, Bundle options) throws NetworkErrorException {
        return null;
    }

    @Override
    public Bundle getAuthToken(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
        Logger.d("getAuthToken");
        AccountManager am = AccountManager.get(mContext);
        String authToken = am.peekAuthToken(account, authTokenType);

        if (TextUtils.isEmpty(authToken)) {
            String passwd = am.getPassword(account);
            if (passwd != null) {
                authToken = mHttpServer.login(account.name, passwd);
            }
        }

        if (!TextUtils.isEmpty(authToken)) {
            Bundle result = new Bundle();
            result.putString(AccountManager.KEY_ACCOUNT_NAME, account.name);
            result.putString(AccountManager.KEY_ACCOUNT_TYPE, account.type);
            result.putString(AccountManager.KEY_AUTHTOKEN, authToken);
            return result;
        }

        Intent intent = new Intent(mContext, LoginActivity.class);
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
        intent.putExtra(ACCOUNT_TYPE, account.type);
        intent.putExtra(AUTH_TYPE, authTokenType);
        Bundle bundle = new Bundle();
        bundle.putParcelable(AccountManager.KEY_INTENT, intent);

        return bundle;
    }

    @Override
    public String getAuthTokenLabel(String authTokenType) {
        return null;
    }

    @Override
    public Bundle updateCredentials(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
        return null;
    }

    @Override
    public Bundle hasFeatures(AccountAuthenticatorResponse response, Account account, String[] features) throws NetworkErrorException {
        return null;
    }
}
