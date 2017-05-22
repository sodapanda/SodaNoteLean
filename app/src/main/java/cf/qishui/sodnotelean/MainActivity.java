package cf.qishui.sodnotelean;

import android.os.Bundle;

import com.orhanobut.logger.Logger;

import java.util.List;

import butterknife.ButterKnife;
import cf.qishui.sodnotelean.database.Notebook;
import cf.qishui.sodnotelean.network.ApiProvider;
import cf.qishui.sodnotelean.ui.BaseAct;
import rx.functions.Action0;
import rx.functions.Action1;

public class MainActivity extends BaseAct {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        ApiProvider
                .getNotebooks()
                .subscribe(new Action1<List<Notebook>>() {
                               @Override
                               public void call(List<Notebook> books) {
                                   for (Notebook notebook : books) {
                                       notebook.save();
                                   }
                               }
                           }, new Action1<Throwable>() {
                               @Override
                               public void call(Throwable throwable) {
                                   Logger.d("error " + throwable.getMessage());
                               }
                           }, new Action0() {
                               @Override
                               public void call() {
                                   Logger.d("finshed");
                               }
                           }
                );
    }


}
