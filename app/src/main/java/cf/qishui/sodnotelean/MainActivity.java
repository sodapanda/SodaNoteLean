package cf.qishui.sodnotelean;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.orhanobut.logger.Logger;

import butterknife.BindView;
import cf.qishui.sodnotelean.RestAPI.Auth;
import cf.qishui.sodnotelean.network.SodaNetwork;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends Activity {
    @BindView(R.id.test_id)
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getData();
    }

    private void getData() {
        Auth auth = SodaNetwork.retrofit.create(Auth.class);
        auth.login("sodapanda20@gmail.com", "wangxiao7Q")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Logger::d, Logger::d, () -> Logger.d("finished"));
    }
}
