package cf.qishui.sodnotelean.restapi;

import cf.qishui.sodnotelean.database.LoginModel;
import cf.qishui.sodnotelean.model.SodaBaseModel;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by wangxiao on 2017/5/20.
 */

public interface Auth {
    @GET("auth/login")
    Observable<LoginModel> login(@Query("email") String email, @Query("pwd") String pwd);

    @GET("auth/logout")
    Observable<SodaBaseModel> logout();

    @GET("auth/register")
    Observable<SodaBaseModel> register(@Query("email") String email, @Query("pwd") String pwd);
}
