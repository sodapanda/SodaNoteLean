package cf.qishui.sodnotelean.RestAPI;

import cf.qishui.sodnotelean.model.LoginModel;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by wangxiao on 2017/5/20.
 */

public interface Auth {
    @GET("auth/login")
    Observable<LoginModel> login(@Query("email") String email, @Query("pwd") String pwd);

}
