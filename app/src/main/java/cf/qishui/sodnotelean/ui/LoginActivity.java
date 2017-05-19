package cf.qishui.sodnotelean.ui;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.orhanobut.logger.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;
import cf.qishui.sodnotelean.R;
import cf.qishui.sodnotelean.account.Authenticator;
import cf.qishui.sodnotelean.account.FakeHttpServer;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity {
    @BindView(R.id.email)
    AutoCompleteTextView mEtEmail;

    @BindView(R.id.password)
    EditText mEtPasswd;

    @BindView(R.id.email_sign_in_button)
    Button mBtSignin;

    private String mAccountType;
    private String mAuthType;
    boolean isNew;
    private AccountManager mAccountManager;
    private AccountAuthenticatorResponse mAccountAuthenticatorResponse = null;

    private Bundle mResultBundle = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        mAccountType = getIntent().getStringExtra(Authenticator.ACCOUNT_TYPE);
        mAuthType = getIntent().getStringExtra(Authenticator.AUTH_TYPE);
        if (TextUtils.isEmpty(mAuthType)) {
            mAuthType = Authenticator.AUTH_TOKEN_FULL;
        }
        isNew = getIntent().getBooleanExtra(Authenticator.IS_ADD_NEW, true);

        mAccountManager = AccountManager.get(this);
        mAccountAuthenticatorResponse =
                getIntent().getParcelableExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE);

        if (mAccountAuthenticatorResponse != null) {
            mAccountAuthenticatorResponse.onRequestContinued();
        }

        mBtSignin.setOnClickListener(v -> {
            String email = mEtEmail.getText().toString();
            String passwd = mEtPasswd.getText().toString();

            Logger.d("clicked %s %s", email, passwd);

            FakeHttpServer httpServer = new FakeHttpServer();
            String token = httpServer.login(email, passwd);

            Account account = new Account(email, mAccountType);
            mAccountManager.addAccountExplicitly(account, passwd, null);
            mAccountManager.setAuthToken(account, mAuthType, token);

            mResultBundle = new Bundle();
            mResultBundle.putString(AccountManager.KEY_ACCOUNT_NAME, email);
            mResultBundle.putString(AccountManager.KEY_ACCOUNT_TYPE, mAccountType);
            mResultBundle.putString(AccountManager.KEY_AUTHTOKEN, token);
            mResultBundle.putString(AccountManager.KEY_PASSWORD, passwd);

            if (mAccountAuthenticatorResponse != null) {
                // send the result bundle back if set, otherwise send an error.
                if (mResultBundle != null) {
                    mAccountAuthenticatorResponse.onResult(mResultBundle);
                } else {
                    mAccountAuthenticatorResponse.onError(AccountManager.ERROR_CODE_CANCELED,
                            "canceled");
                }
                mAccountAuthenticatorResponse = null;
            }

            finish();
        });
    }
}

