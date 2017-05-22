package cf.qishui.sodnotelean.restapi;

import cf.qishui.sodnotelean.model.StateModel;
import cf.qishui.sodnotelean.model.UserModel;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by wangxiao on 2017/5/21.
 */

public interface UserService {
    @GET("user/info")
    Observable<UserModel> userInfo(@Query("userId") String userId);

    @FormUrlEncoded
    @POST("user/updateUsername")
    Observable<StateModel> updateUsername(@Field("username") String username);

    @FormUrlEncoded
    @POST("user/updatePwd")
    Observable<StateModel> updatePwd(@Field("oldPwd") String oldPwd, @Field("pwd") String pwd);
}
