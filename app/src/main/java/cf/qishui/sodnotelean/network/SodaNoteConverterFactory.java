package cf.qishui.sodnotelean.network;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonQualifier;
import com.squareup.moshi.Moshi;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

import static java.util.Collections.unmodifiableSet;

/**
 * Created by wangxiao on 2017/5/22.
 */

public class SodaNoteConverterFactory extends Converter.Factory {
    /**
     * Create an instance using a default {@link Moshi} instance for conversion.
     */
    public static SodaNoteConverterFactory create() {
        return create(new Moshi.Builder().build());
    }

    /**
     * Create an instance using {@code moshi} for conversion.
     */
    @SuppressWarnings("ConstantConditions") // Guarding public API nullability.
    public static SodaNoteConverterFactory create(Moshi moshi) {
        if (moshi == null) throw new NullPointerException("moshi == null");
        return new SodaNoteConverterFactory(moshi, false, false, false);
    }

    private final Moshi moshi;
    private final boolean lenient;
    private final boolean failOnUnknown;
    private final boolean serializeNulls;

    private SodaNoteConverterFactory(Moshi moshi, boolean lenient, boolean failOnUnknown,
                                     boolean serializeNulls) {
        this.moshi = moshi;
        this.lenient = lenient;
        this.failOnUnknown = failOnUnknown;
        this.serializeNulls = serializeNulls;
    }

    /**
     * Return a new factory which uses {@linkplain JsonAdapter#lenient() lenient} adapters.
     */
    public SodaNoteConverterFactory asLenient() {
        return new SodaNoteConverterFactory(moshi, true, failOnUnknown, serializeNulls);
    }

    /**
     * Return a new factory which uses {@link JsonAdapter#failOnUnknown()} adapters.
     */
    public SodaNoteConverterFactory failOnUnknown() {
        return new SodaNoteConverterFactory(moshi, lenient, true, serializeNulls);
    }

    /**
     * Return a new factory which includes null values into the serialized JSON.
     */
    public SodaNoteConverterFactory withNullSerialization() {
        return new SodaNoteConverterFactory(moshi, lenient, failOnUnknown, true);
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations,
                                                            Retrofit retrofit) {
        JsonAdapter<?> adapter = moshi.adapter(type, jsonAnnotations(annotations));
        if (lenient) {
            adapter = adapter.lenient();
        }
        if (failOnUnknown) {
            adapter = adapter.failOnUnknown();
        }
        if (serializeNulls) {
            adapter = adapter.serializeNulls();
        }
        return new SodaNoteResponseBodyConverter<>(adapter, annotations,moshi);
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type,
                                                          Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        JsonAdapter<?> adapter = moshi.adapter(type, jsonAnnotations(parameterAnnotations));
        if (lenient) {
            adapter = adapter.lenient();
        }
        if (failOnUnknown) {
            adapter = adapter.failOnUnknown();
        }
        if (serializeNulls) {
            adapter = adapter.serializeNulls();
        }
        return new SodaNoteRequestBodyConverter<>(adapter);
    }

    private static Set<? extends Annotation> jsonAnnotations(Annotation[] annotations) {
        Set<Annotation> result = null;
        for (Annotation annotation : annotations) {
            if (annotation.annotationType().isAnnotationPresent(JsonQualifier.class)) {
                if (result == null) result = new LinkedHashSet<>();
                result.add(annotation);
            }
        }
        return result != null ? unmodifiableSet(result) : Collections.<Annotation>emptySet();
    }
}
