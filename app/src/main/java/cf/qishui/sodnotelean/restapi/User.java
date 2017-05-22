package cf.qishui.sodnotelean.restapi;

import cf.qishui.sodnotelean.model.SodaBaseModel;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by wangxiao on 2017/5/21.
 */

public interface User {
    @GET("user/info")
    Observable<cf.qishui.sodnotelean.model.User> userInfo(@Query("userId") String userId);

    @FormUrlEncoded
    @POST("user/updateUsername")
    Observable<SodaBaseModel> updateUsername(@Field("username") String username);

    @FormUrlEncoded
    @POST("user/updatePwd")
    Observable<SodaBaseModel> updatePwd(@Field("oldPwd") String oldPwd, @Field("pwd") String pwd);
}
