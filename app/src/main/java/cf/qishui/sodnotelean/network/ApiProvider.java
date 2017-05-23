package cf.qishui.sodnotelean.network;

import com.orhanobut.logger.Logger;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;

import cf.qishui.sodnotelean.SodApp;
import cf.qishui.sodnotelean.database.UserInfoTable;
import cf.qishui.sodnotelean.model.LoginModel;
import cf.qishui.sodnotelean.database.Notebook;
import cf.qishui.sodnotelean.model.StateModel;
import cf.qishui.sodnotelean.model.UserModel;
import cf.qishui.sodnotelean.restapi.AuthService;
import cf.qishui.sodnotelean.restapi.NotebookService;
import cf.qishui.sodnotelean.restapi.UserService;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by wangxiao on 2017/5/21.
 */

public class ApiProvider {
    public static Observable<LoginModel> login(String email, String password) {
        AuthService authService = SodApp.getApp().retrofit().create(AuthService.class);
        return authService.login(email, password).compose(applyTransform());
    }

    public static Observable<StateModel> logout() {
        AuthService authService = SodApp.getApp().retrofit().create(AuthService.class);
        return authService.logout().compose(applyTransform());
    }

    public static Observable<StateModel> register(String email, String pwd) {
        AuthService authService = SodApp.getApp().retrofit().create(AuthService.class);
        return authService.register(email, pwd).compose(applyTransform());
    }

    public static Observable<UserModel> userInfo(String userId) {
        UserService user = SodApp.getApp().retrofit().create(UserService.class);
        return user.userInfo(userId).compose(applyTransform());
    }

    public static Observable<StateModel> updateUsername(String userName) {
        UserService user = SodApp.getApp().retrofit().create(UserService.class);
        return user.updateUsername(userName).compose(applyTransform());
    }

    public static Observable<StateModel> updatePwd(String oldPwd, String pwd) {
        UserService user = SodApp.getApp().retrofit().create(UserService.class);
        return user.updatePwd(oldPwd, pwd).compose(applyTransform());
    }

    public static Observable<List<Notebook>> getNotebooks() {
        NotebookService notebook = SodApp.getApp().retrofit().create(NotebookService.class);
        return notebook.getNotebooks().compose(applyTransform());
    }

    private static <T> Observable.Transformer<T, T> applyTransform() {
//        return observable -> observable.subscribeOn(Schedulers.io());
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> observable) {
                return observable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnError(new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                if ("NOTLOGIN".equals(throwable.getMessage())) {
                                    Logger.d("没有登录");
                                    UserInfoTable userInfoTable = SQLite.select()
                                            .from(UserInfoTable.class)
                                            .querySingle();
                                    if (userInfoTable != null) {
                                        userInfoTable.delete();
                                    }
                                    SodApp.getApp().finishAllActs();
                                    SodApp.getApp().startLoginActivity();
                                }
                            }
                        })
                        ;
            }
        };
    }
}
