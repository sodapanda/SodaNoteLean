package cf.qishui.sodnotelean;

import android.os.Bundle;
import android.view.ViewGroup;

import com.bluelinelabs.conductor.Conductor;
import com.bluelinelabs.conductor.Router;
import com.bluelinelabs.conductor.RouterTransaction;

import cf.qishui.sodnotelean.ui.BaseAct;
import cf.qishui.sodnotelean.ui.HomeCtlr;

public class MainActivity extends BaseAct {
    private Router router;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_holder_layout);

        ViewGroup holder = (ViewGroup) findViewById(R.id.main_holder_view);

        router = Conductor.attachRouter(this, holder, savedInstanceState);
        if (!router.hasRootController()) {
            router.setRoot(RouterTransaction.with(new HomeCtlr()));
        }
    }

    @Override
    public void onBackPressed() {
        if (!router.handleBack()) {
            super.onBackPressed();
        }
    }
}
