package cf.qishui.sodnotelean.restapi;

import java.util.List;

import cf.qishui.sodnotelean.model.NoteModel;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by wangxiao on 2017/5/23.
 */

public interface NoteService {
    @GET("note/getNotes")
    Observable<List<NoteModel>> getNotes(@Query("notebookId") String notebookId);
}
