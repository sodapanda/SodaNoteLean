package cf.qishui.sodnotelean.restapi;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by wangxiao on 2017/5/21.
 */

public interface User {
    @GET("user/info")
    Observable<cf.qishui.sodnotelean.model.User> userInfo(@Query("userId") String userId);
}
