package cf.qishui.sodnotelean.network;

import java.util.List;

import cf.qishui.sodnotelean.SodApp;
import cf.qishui.sodnotelean.model.LoginModel;
import cf.qishui.sodnotelean.database.Notebook;
import cf.qishui.sodnotelean.model.StateModel;
import cf.qishui.sodnotelean.model.UserModel;
import cf.qishui.sodnotelean.restapi.AuthService;
import cf.qishui.sodnotelean.restapi.NotebookService;
import cf.qishui.sodnotelean.restapi.UserService;
import rx.Observable;
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
        return notebook.getNotebooks().subscribeOn(Schedulers.io());
    }

    private static <T> Observable.Transformer<T, T> applyTransform() {
        return observable -> observable.subscribeOn(Schedulers.io());
    }
}
