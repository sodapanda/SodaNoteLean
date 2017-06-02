package cf.qishui.sodnotelean.ui;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bluelinelabs.conductor.Controller;
import com.bluelinelabs.conductor.rxlifecycle.ControllerEvent;
import com.bluelinelabs.conductor.rxlifecycle.RxController;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cf.qishui.sodnotelean.R;
import cf.qishui.sodnotelean.model.NoteContentModel;
import cf.qishui.sodnotelean.network.ApiProvider;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by wangxiao on 2017/6/2.
 */

public class NoteContentCtlr extends RxController {
    private String mContent;
    private String mId;
    private Unbinder unbinder;

    @BindView(R.id.detail_tv_content)
    TextView mTvContent;

    public NoteContentCtlr(String id) {
        this.mId = id;
        initData();
    }

    public NoteContentCtlr(@Nullable Bundle args) {
        super(args);
    }

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        View view = inflater.inflate(R.layout.activity_note_detail, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    private void initData() {
        ApiProvider.getNoteContent(mId)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(bindUntilEvent(ControllerEvent.DESTROY))
                .subscribe(new Action1<NoteContentModel>() {
                    @Override
                    public void call(NoteContentModel noteContentModel) {
                        mContent = noteContentModel.Content;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            mTvContent.setText(Html.fromHtml(mContent, Html.FROM_HTML_MODE_COMPACT));
                        } else {
                            mTvContent.setText(Html.fromHtml(mContent));
                        }
                    }
                });
    }

    @Override
    protected void onDestroyView(@NonNull View view) {
        super.onDestroyView(view);
        unbinder.unbind();
        unbinder = null;
    }
}
