package com.sharebicycle.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.library.BaseRecyclerAdapter;
import com.github.library.BaseViewHolder;
import com.github.library.listener.OnRecyclerItemClickListener;
import com.sharebicycle.MyApplication;
import com.sharebicycle.api.ApiLock;
import com.sharebicycle.been.RidingOrder;
import com.sharebicycle.utils.Consts;
import com.sharebicycle.utils.TimeUtil;
import com.sharebicycle.utils.WWToast;
import com.sharebicycle.utils.WWViewUtil;
import com.sharebicycle.www.R;
import com.sharebicycle.xutils.WWXCallBack;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ZXJ on 2017/5/16.
 */

public class RidingHistoryActivity extends FatherActivity {
    @BindView(R.id.tv_0)
    TextView tv0;
    @BindView(R.id.tv_1)
    TextView tv1;
    @BindView(R.id.tv_2)
    TextView tv2;
    @BindView(R.id.lv_data)
    RecyclerView lvData;
    @BindView(R.id.swipe_layout)
    SwipeRefreshLayout swipeLayout;
    private ArrayList<RidingOrder> list = new ArrayList<RidingOrder>();
    private BaseRecyclerAdapter mAdapter;
    // 分页数据
    private int mCurrentPage = 0;
    private int totalCount = 0;

    @Override
    protected int getLayoutId() {
        return R.layout.act_riding_history;
    }

    @Override
    protected void initValues() {
        initDefautHead("我的行程", true);
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


        mAdapter = new BaseRecyclerAdapter<RidingOrder>(this, list, R.layout.list_riding_history) {
            @Override
            protected void convert(BaseViewHolder helper, RidingOrder item) {
                helper.setText(R.id.tv_time, TimeUtil.getTimeToS(item.BeginTime * 1000));
                helper.setText(R.id.tv_ridingtime,"骑行时间："+( item.EndTime-item.BeginTime)/60+"分钟");
                helper.setText(R.id.tv_price,WWViewUtil.numberFormatPrice(item.PayAmt));
                helper.setText(R.id.tv_len,"骑行距离："+item.WayLen+"KM");
                helper.setText(R.id.tv_no,"编号："+item.LockId);
            }
        };
        mAdapter.setOnRecyclerItemClickListener(new OnRecyclerItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                startActivity(new Intent(RidingHistoryActivity.this, RidingDetailActivity.class).putExtra(Consts.KEY_DATA, JSONObject.toJSONString(list.get(position))));
            }
        });
        lvData.setHasFixedSize(true);
        lvData.setLayoutManager(new LinearLayoutManager(this));
        lvData.setItemAnimator(new DefaultItemAnimator());
        mAdapter.openLoadAnimation(false);
        lvData.setAdapter(mAdapter);

    }

    private void requestData() {
        showWaitDialog();
        RequestParams params = new RequestParams(ApiLock.Orders());
        params.addBodyParameter(Consts.KEY_SESSIONID, MyApplication
                .getInstance().getSessionId());
        params.addBodyParameter("pageSize", 20 + "");
        params.addBodyParameter("pageIndex", mCurrentPage + "");
        x.http().get(params, new WWXCallBack("Orders") {

            @Override
            public void onAfterSuccessOk(JSONObject data) {
                mCurrentPage++;
                list = (ArrayList<RidingOrder>) JSONArray.parseArray(
                        data.getString("Data"), RidingOrder.class);
                totalCount = data.getIntValue("PageCount");
                tv0.setText(data.getString("WayLen"));
                tv1.setText(data.getString("Carbon"));
                tv2.setText(data.getString("Calorie"));
                if (mCurrentPage > 1) {
                    mAdapter.addData(list);
                } else {
                    mAdapter.setData(list);
                }

            }

            @Override
            public void onAfterFinished() {
                dismissWaitDialog();
                if (mCurrentPage < 2)
                    swipeLayout.setRefreshing(false);
            }
        });
    }

    @Override
    protected void doOperate() {

        requestData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
