package cf.qishui.sodnotelean.ui;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cf.qishui.sodnotelean.R;
import cf.qishui.sodnotelean.model.NoteContentModel;
import cf.qishui.sodnotelean.network.ApiProvider;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class NoteDetailAct extends BaseAct {
    private String mContent;
    private String mId;

    @BindView(R.id.detail_tv_content)
    TextView mTvContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);
        ButterKnife.bind(this);

        mId = getIntent().getStringExtra("id");

        initData();

        initViews();
    }

    private void initData() {
        ApiProvider.getNoteContent(mId)
                .observeOn(AndroidSchedulers.mainThread())
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

    private void initViews() {
    }
}
