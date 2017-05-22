package cf.qishui.sodnotelean.network;

import com.orhanobut.logger.Logger;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonDataException;
import com.squareup.moshi.Moshi;

import java.io.IOException;
import java.lang.annotation.Annotation;

import cf.qishui.sodnotelean.model.ErrorModel;
import okhttp3.ResponseBody;
import okio.BufferedSource;
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
            Logger.d("before parse");
            return adapter.fromJson(jsonStr);
        } catch (JsonDataException e) {
            JsonAdapter<ErrorModel> errorAdapter = moshi.adapter(ErrorModel.class);
            Logger.d("before error parse "+jsonStr);
            ErrorModel errorModel = errorAdapter.fromJson(jsonStr);
            String errorMsg = "common error";
            if (errorModel != null && errorModel.Msg != null) {
                errorMsg = errorModel.Msg;
            }
            throw new IllegalStateException(errorMsg);
        } finally {
            value.close();
        }
    }
}
