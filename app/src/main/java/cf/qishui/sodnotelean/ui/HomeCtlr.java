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
import android.widget.Toast;

import com.bluelinelabs.conductor.Controller;
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
import cf.qishui.sodnotelean.database.Notebook;
import cf.qishui.sodnotelean.network.ApiProvider;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by wangxiao on 2017/6/2.
 */

public class HomeCtlr extends RxController {
    private Unbinder unbinder;

    private LayoutInflater mInflater;

    @BindView(R.id.main_recycler_books)
    RecyclerView mRecyclerView;
    private BooksAdapter mAdapter = new BooksAdapter();
    private List<Notebook> mNotebooks = new ArrayList<>();

    public HomeCtlr() {
        initData();
    }

    public HomeCtlr(@Nullable Bundle args) {
        super(args);
    }

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        Logger.d("onCreateView");
        mInflater = inflater;
        View view = inflater.inflate(R.layout.activity_main, container, false);
        unbinder = ButterKnife.bind(this, view);

        initViews();
        return view;
    }

    private void initViews() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initData() {
        ApiProvider.getNotebooks()
                .observeOn(AndroidSchedulers.mainThread())
                .compose(bindUntilEvent(ControllerEvent.DESTROY))
                .subscribe(new Action1<List<Notebook>>() {
                               @Override
                               public void call(List<Notebook> notebooks) {
                                   if (notebooks != null) {
                                       Logger.d("获取数据" + notebooks.size());
                                       for (Notebook notebook : notebooks) {
                                           if (!notebook.delete()) {
                                               mNotebooks.add(notebook);
                                           }
                                       }
                                       mAdapter.notifyDataSetChanged();
                                   }
                               }
                           },
                        new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                Toast.makeText(getActivity(), throwable.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                )
        ;
    }

    @Override
    protected void onDestroyView(@NonNull View view) {
        super.onDestroyView(view);
        unbinder.unbind();
        unbinder = null;

        Logger.d("ondestroyView");
    }

    public class BooksViewHolder extends RecyclerView.ViewHolder {
        public TextView mTvTitle;

        public BooksViewHolder(View itemView) {
            super(itemView);
            mTvTitle = (TextView) itemView.findViewById(R.id.main_item_book_tv_title);
        }
    }

    public class BooksAdapter extends RecyclerView.Adapter<BooksViewHolder> {

        @Override
        public BooksViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = mInflater.inflate(R.layout.main_item_books, parent, false);
            return new BooksViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(BooksViewHolder holder, int position) {
            Notebook notebook = mNotebooks.get(position);
            holder.mTvTitle.setText(notebook.Title);
            holder.mTvTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Notebook notebook = mNotebooks.get(position);
                    String id = notebook.NotebookId;
                    getRouter().pushController(RouterTransaction.with(new NoteListCtrl(id))
                            .pushChangeHandler(new FadeChangeHandler())
                            .popChangeHandler(new FadeChangeHandler())
                    );
                }
            });

        }

        @Override
        public int getItemCount() {
            return mNotebooks.size();
        }
    }
}
