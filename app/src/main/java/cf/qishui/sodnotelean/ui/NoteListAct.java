package cf.qishui.sodnotelean.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cf.qishui.sodnotelean.R;
import cf.qishui.sodnotelean.model.NoteModel;
import cf.qishui.sodnotelean.network.ApiProvider;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class NoteListAct extends BaseAct {
    @BindView(R.id.notelist_recycler)
    RecyclerView mRecyclerView;

    private NoteAdapter mAdapter = new NoteAdapter();

    private String mId;

    private List<NoteModel> mNotes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);
        ButterKnife.bind(this);
        mId = getIntent().getStringExtra("id");

        initView();
        initData();
    }

    private void initData() {
        ApiProvider.getNotes(mId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Action1<List<NoteModel>>() {
                            @Override
                            public void call(List<NoteModel> noteModels) {
                                if (noteModels != null) {
                                    for (NoteModel note : noteModels) {
                                        if (!note.IsDeleted) {
                                            mNotes.add(note);
                                        }
                                    }
                                    mAdapter.notifyDataSetChanged();
                                }
                            }
                        },
                        new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                Logger.d(throwable);
                            }
                        }
                );

    }

    private void initView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder {
        public TextView mTv;

        public NoteViewHolder(View itemView) {
            super(itemView);
            mTv = (TextView) itemView.findViewById(R.id.main_item_book_tv_title);
        }
    }

    public class NoteAdapter extends RecyclerView.Adapter<NoteViewHolder> {

        @Override
        public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = getLayoutInflater();
            View itemView = inflater.inflate(R.layout.main_item_books, parent, false);
            return new NoteViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(NoteViewHolder holder, int position) {
            NoteModel note = mNotes.get(position);
            holder.mTv.setText(note.Title);
        }

        @Override
        public int getItemCount() {
            return mNotes.size();
        }
    }
}
