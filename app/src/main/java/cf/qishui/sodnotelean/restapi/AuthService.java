package cf.qishui.sodnotelean.restapi;

import cf.qishui.sodnotelean.model.LoginModel;
import cf.qishui.sodnotelean.model.StateModel;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by wangxiao on 2017/5/20.
 */

public interface AuthService {
    @GET("auth/login")
    Observable<LoginModel> login(@Query("email") String email, @Query("pwd") String pwd);

    @GET("auth/logout")
    Observable<StateModel> logout();

    @GET("auth/register")
    Observable<StateModel> register(@Query("email") String email, @Query("pwd") String pwd);
}
