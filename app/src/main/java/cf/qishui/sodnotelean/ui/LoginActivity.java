package cf.qishui.sodnotelean.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.trello.rxlifecycle.android.ActivityEvent;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cf.qishui.sodnotelean.MainActivity;
import cf.qishui.sodnotelean.R;
import cf.qishui.sodnotelean.database.UserInfoTable;
import cf.qishui.sodnotelean.model.LoginModel;
import cf.qishui.sodnotelean.model.UserModel;
import cf.qishui.sodnotelean.network.ApiProvider;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

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

        UserInfoTable userInfoTable = SQLite.select()
                .from(UserInfoTable.class)
                .querySingle();
        if (userInfoTable != null && userInfoTable.Token != null) {
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

        UserInfoTable userInfo = new UserInfoTable();

        ApiProvider.login(email, password)
                .flatMap(new Func1<LoginModel, Observable<UserModel>>() {
                    @Override
                    public Observable<UserModel> call(LoginModel loginModel) {
                        userInfo.Token = loginModel.Token;
                        userInfo.UserId = loginModel.UserId;
                        userInfo.Email = loginModel.Email;
                        userInfo.Username = loginModel.Username;
                        return ApiProvider.userInfo(loginModel.UserId);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .compose(provider.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new Action1<UserModel>() {
                               @Override
                               public void call(UserModel userModel) {
                                   userInfo.Avatar = userModel.Logo;
                                   userInfo.Verified = userModel.Verified;
                                   userInfo.save();

                                   Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                   startActivity(intent);
                                   finish();
                               }
                           },
                        new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                Toast.makeText(LoginActivity.this, throwable.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }

                );
    }
}

