package cf.qishui.sodnotelean.restapi;

import java.util.List;

import cf.qishui.sodnotelean.SodApp;
import cf.qishui.sodnotelean.database.LoginModel;
import cf.qishui.sodnotelean.database.Notebook;
import cf.qishui.sodnotelean.model.SodaBaseModel;
import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Created by wangxiao on 2017/5/21.
 */

public class ApiProvider {
    public static Observable<LoginModel> login(String email, String password) {
        Auth auth = SodApp.getApp().retrofit().create(Auth.class);
        return auth.login(email, password).compose(applyTransform(LoginModel.class));
    }

    public static Observable<SodaBaseModel> logout() {
        Auth auth = SodApp.getApp().retrofit().create(Auth.class);
        return auth.logout().compose(applyTransform(SodaBaseModel.class));
    }

    public static Observable<SodaBaseModel> register(String email, String pwd) {
        Auth auth = SodApp.getApp().retrofit().create(Auth.class);
        return auth.register(email, pwd).compose(applyTransform(SodaBaseModel.class));
    }

    public static Observable<cf.qishui.sodnotelean.model.User> userInfo(String userId) {
        UserService user = SodApp.getApp().retrofit().create(UserService.class);
        return user.userInfo(userId).compose(applyTransform(cf.qishui.sodnotelean.model.User.class));
    }

    public static Observable<SodaBaseModel> updateUsername(String userName) {
        UserService user = SodApp.getApp().retrofit().create(UserService.class);
        return user.updateUsername(userName).compose(applyTransform(SodaBaseModel.class));
    }

    public static Observable<SodaBaseModel> updatePwd(String oldPwd, String pwd) {
        UserService user = SodApp.getApp().retrofit().create(UserService.class);
        return user.updatePwd(oldPwd, pwd).compose(applyTransform(SodaBaseModel.class));
    }

    public static Observable<List<Notebook>> getNotebooks() {
        Notebooks notebook = SodApp.getApp().retrofit().create(Notebooks.class);
        return notebook.getNotebooks().subscribeOn(Schedulers.io());
    }

    private static <T extends SodaBaseModel> Observable.Transformer<T, T> applyTransform(Class<T> clazz) {
        return observable -> observable
                .subscribeOn(Schedulers.io())
                .onErrorReturn(throwable -> {
                    T fallBackData = null;
                    try {
                        fallBackData = clazz.newInstance();
                        SodaBaseModel baseModel = fallBackData;
                        baseModel.Ok = false;
                        baseModel.Msg = throwable.toString();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return fallBackData;
                });
    }
}
