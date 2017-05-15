package com.sharebicycle.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.github.library.BaseRecyclerAdapter;
import com.github.library.BaseViewHolder;
import com.github.library.listener.OnRecyclerItemClickListener;
import com.sharebicycle.been.Message;
import com.sharebicycle.utils.WWToast;
import com.sharebicycle.www.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ZXJ on 2017/5/15.
 */

public class BussnessDetailActivity extends FatherActivity {
    @BindView(R.id.lv_data)
    RecyclerView lvData;
    @BindView(R.id.swipe_layout)
    SwipeRefreshLayout swipeLayout;
    private ArrayList<Message> list = new ArrayList<Message>();
    private BaseRecyclerAdapter mAdapter;
    // 分页数据
    private int mCurrentPage = 0;
    private int totalCount = 0;

    @Override
    protected int getLayoutId() {
        return R.layout.act_ticketlist;
    }

    @Override
    protected void initValues() {
        initDefautHead("交易明细", true);
    }

    @Override
    protected void initView() {

        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mCurrentPage = 0;
                requestData();
            }
        });
        // 添加滚动监听。
        lvData.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (!recyclerView.canScrollVertically(1)) {// 手指不能向上滑动了
                    // TODO 这里有个注意的地方，如果你刚进来时没有数据，但是设置了适配器，这个时候就会触发加载更多，需要开发者判断下是否有数据，如果有数据才去加载更多。
                    if (mCurrentPage >= totalCount) {
                        WWToast.showShort(R.string.nomore_data);
                    } else {
                        requestData();
                    }
                }
            }
        });

        for (int i = 0; i <20 ; i++) {
            list.add(new Message());
        }

        mAdapter = new BaseRecyclerAdapter<Message>(this, list, R.layout.list_bus_detail_item) {
            @Override
            protected void convert(BaseViewHolder helper, Message item) {

            }
        };
        mAdapter.setOnRecyclerItemClickListener(new OnRecyclerItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }
        });
        lvData.setHasFixedSize(true);
        lvData.setLayoutManager(new LinearLayoutManager(this));
        lvData.setItemAnimator(new DefaultItemAnimator());
        mAdapter.openLoadAnimation(false);
        lvData.setAdapter(mAdapter);
    }

    private void requestData() {
    }

    @Override
    protected void doOperate() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
