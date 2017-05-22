package cf.qishui.sodnotelean.restapi;

import java.util.List;

import cf.qishui.sodnotelean.database.LoginModel;
import cf.qishui.sodnotelean.database.Notebook;
import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by wangxiao on 2017/5/22.
 */

public interface Notebooks {
    @GET("notebook/getNotebooks")
    Observable<List<Notebook>> getNotebooks();
}
