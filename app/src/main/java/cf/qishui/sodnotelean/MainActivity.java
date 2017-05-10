package cf.qishui.sodnotelean;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.functions.Action1;

public class MainActivity extends Activity {
    @BindView(R.id.test_id)
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        tv.setText("Hello");

        Observable.just("a").subscribe(s -> Log.i("test", "test string " + s));
    }
}
