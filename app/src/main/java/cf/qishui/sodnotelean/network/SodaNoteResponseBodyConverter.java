package cf.qishui.sodnotelean.network;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonDataException;
import com.squareup.moshi.Moshi;

import java.io.IOException;
import java.lang.annotation.Annotation;

import cf.qishui.sodnotelean.model.StateModel;
import okhttp3.ResponseBody;
import okio.ByteString;
import retrofit2.Converter;

/**
 * Created by wangxiao on 2017/5/22.
 */

public class SodaNoteResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private static final ByteString UTF8_BOM = ByteString.decodeHex("EFBBBF");
    private Annotation[] annotations;

    private final JsonAdapter<T> adapter;
    private Moshi moshi;

    SodaNoteResponseBodyConverter(JsonAdapter<T> adapter, Annotation[] annotations, Moshi moshi) {
        this.adapter = adapter;
        this.annotations = annotations;
        this.moshi = moshi;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        String jsonStr = value.string();
        try {
            return adapter.fromJson(jsonStr);
        } catch (JsonDataException e) {
            JsonAdapter<StateModel> errorAdapter = moshi.adapter(StateModel.class);
            StateModel stateModel = errorAdapter.fromJson(jsonStr);
            String errorMsg = "common error";
            if (stateModel != null && stateModel.Msg != null) {
                errorMsg = stateModel.Msg;
            }
            throw new IllegalStateException(errorMsg);
        } finally {
            value.close();
        }
    }
}
