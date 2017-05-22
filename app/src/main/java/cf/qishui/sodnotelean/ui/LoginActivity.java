package cf.qishui.sodnotelean.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.trello.rxlifecycle.android.ActivityEvent;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cf.qishui.sodnotelean.MainActivity;
import cf.qishui.sodnotelean.R;
import cf.qishui.sodnotelean.database.LoginModel;
import cf.qishui.sodnotelean.restapi.ApiProvider;
import rx.android.schedulers.AndroidSchedulers;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseAct {
    @BindView(R.id.email)
    AutoCompleteTextView mEtEmail;

    @BindView(R.id.password)
    EditText mEtPasswd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        LoginModel loginModel = SQLite.select()
                .from(LoginModel.class)
                .querySingle();
        if (loginModel != null && loginModel.Token != null) {
            //todo how to check token validation
            jumpToMainAct();
            finish();
        }
    }

    private void jumpToMainAct() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.email_sign_in_button)
    public void login() {
        String email = mEtEmail.getText().toString();
        String password = mEtPasswd.getText().toString();

        ApiProvider.login(email, password)
                .doOnNext(loginModel -> {
                    if (loginModel.Ok) {
                        loginModel.save();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .compose(provider.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(loginModel -> {
                            if (!loginModel.Ok) {
                                if (!TextUtils.isEmpty(loginModel.Msg)) {
                                    Toast.makeText(LoginActivity.this, loginModel.Msg, Toast.LENGTH_SHORT).show();
                                }
                                return;
                            }

                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        },
                        throwable -> Logger.e(throwable.toString()),
                        () -> {

                        }

                );
    }
}

