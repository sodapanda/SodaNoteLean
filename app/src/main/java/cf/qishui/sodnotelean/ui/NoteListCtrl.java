package cf.qishui.sodnotelean.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bluelinelabs.conductor.RouterTransaction;
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler;
import com.bluelinelabs.conductor.rxlifecycle.ControllerEvent;
import com.bluelinelabs.conductor.rxlifecycle.RxController;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cf.qishui.sodnotelean.R;
import cf.qishui.sodnotelean.model.NoteModel;
import cf.qishui.sodnotelean.network.ApiProvider;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by wangxiao on 2017/6/2.
 */

public class NoteListCtrl extends RxController {
    private Unbinder unbinder;
    private LayoutInflater mInflater;

    @BindView(R.id.notelist_recycler)
    RecyclerView mRecyclerView;

    private NoteAdapter mAdapter = new NoteAdapter();

    private String mId;

    private List<NoteModel> mNotes = new ArrayList<>();

    public NoteListCtrl(String mId) {
        this.mId = mId;
        initData();
    }

    public NoteListCtrl(@Nullable Bundle args) {
        super(args);
    }

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        this.mInflater = inflater;
        View view = inflater.inflate(R.layout.activity_note_list, container, false);
        unbinder = ButterKnife.bind(this, view);

        initView();

        return view;
    }

    private void initView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initData() {
        ApiProvider.getNotes(mId)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(bindUntilEvent(ControllerEvent.DESTROY))
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
            View itemView = mInflater.inflate(R.layout.main_item_books, parent, false);
            return new NoteViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(NoteViewHolder holder, int position) {
            NoteModel note = mNotes.get(position);
            holder.mTv.setText(note.Title);

            holder.mTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getRouter().pushController(RouterTransaction.with(new NoteContentCtlr(note.NoteId))
                            .pushChangeHandler(new FadeChangeHandler())
                            .popChangeHandler(new FadeChangeHandler()));
                }
            });
        }

        @Override
        public int getItemCount() {
            return mNotes.size();
        }
    }

    @Override
    protected void onDestroyView(@NonNull View view) {
        super.onDestroyView(view);
        unbinder.unbind();
        unbinder = null;
    }
}
