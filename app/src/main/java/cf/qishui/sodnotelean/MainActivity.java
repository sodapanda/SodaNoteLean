package cf.qishui.sodnotelean;

import android.os.Bundle;

import com.orhanobut.logger.Logger;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.trello.rxlifecycle.android.ActivityEvent;

import butterknife.ButterKnife;
import butterknife.OnClick;
import cf.qishui.sodnotelean.model.LoginModel;
import cf.qishui.sodnotelean.restapi.ApiProvider;
import cf.qishui.sodnotelean.ui.BaseAct;

public class MainActivity extends BaseAct {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
    }

    @OnClick(R.id.main_btn_login)
    public void login() {
        ApiProvider.login("", "")
                .compose(provider.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(BaseModel::save);
    }

    @OnClick(R.id.main_btn_userinfo)
    public void clickUserInfo() {
        LoginModel loginModel = SQLite.select()
                .from(LoginModel.class)
                .querySingle();
        if (loginModel == null) {
            return;
        }

        ApiProvider.userInfo(loginModel.UserId)
                .compose(provider.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(Logger::d);
    }


}
