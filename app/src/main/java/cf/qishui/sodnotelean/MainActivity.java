package cf.qishui.sodnotelean;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cf.qishui.sodnotelean.database.Notebook;
import cf.qishui.sodnotelean.network.ApiProvider;
import cf.qishui.sodnotelean.ui.BaseAct;
import cf.qishui.sodnotelean.ui.NoteListAct;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class MainActivity extends BaseAct {
    @BindView(R.id.main_recycler_books)
    RecyclerView mRecyclerView;
    private BooksAdapter mAdapter = new BooksAdapter();
    private List<Notebook> mNotebooks = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initViews();

        initData();
    }

    private void initData() {
        ApiProvider.getNotebooks()
                .observeOn(AndroidSchedulers.mainThread())
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
                                Toast.makeText(MainActivity.this, throwable.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                )
        ;
    }

    private void initViews() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
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
            LayoutInflater layoutInflater = getLayoutInflater();
            View itemView = layoutInflater.inflate(R.layout.main_item_books, parent, false);
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

                    Intent intent = new Intent(MainActivity.this, NoteListAct.class);
                    intent.putExtra("id", id);
                    startActivity(intent);
                }
            });

        }

        @Override
        public int getItemCount() {
            return mNotebooks.size();
        }
    }

}
