package com.sharebicycle.activity;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.library.BaseRecyclerAdapter;
import com.github.library.BaseViewHolder;
import com.github.library.listener.OnRecyclerItemClickListener;
import com.sharebicycle.MyApplication;
import com.sharebicycle.api.ApiUser;
import com.sharebicycle.been.Message;
import com.sharebicycle.utils.Consts;
import com.sharebicycle.utils.TimeUtil;
import com.sharebicycle.utils.WWToast;
import com.sharebicycle.www.R;
import com.sharebicycle.xutils.WWXCallBack;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;

/***
 * Description:消息列表 Company: wangwanglife Version：1.0
 *
 * @author zxj
 * @date 2016-7-30
 */
public class MessageListActivity extends FatherActivity {

    private RecyclerView lvData;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ArrayList<Message> list = new ArrayList<Message>();
    private BaseRecyclerAdapter mAdapter;
    // 分页数据
    private int mCurrentPage = 0;
    private int totalCount = 0;


    @Override
    protected int getLayoutId() {
        // 与收藏用同一个
        return R.layout.act_message;
    }

    @Override
    protected void initValues() {
        initDefautHead(R.string.message, true);

    }

    @Override
    protected void initView() {

        lvData = (RecyclerView) findViewById(R.id.lv_data);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
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

        mAdapter = new BaseRecyclerAdapter<Message>(this, list, R.layout.list_message_item) {
            @Override
            protected void convert(BaseViewHolder helper, Message item) {

                helper.setText(R.id.title, item.SendName);
                helper.setText(R.id.time, TimeUtil.getTimeToS(item.CreateTime * 1000));
                helper.setText(R.id.info, item.Content);

                if (item.ReadFlag) {
                    helper.setTextColor(R.id.title,getResources().getColor(R.color.text_gray_s));
                    helper.setTextColor(R.id.info,getResources().getColor(R.color.text_gray_s));

                } else {
                    helper.setTextColor(R.id.title,getResources().getColor(R.color.text_f7));
                    helper.setTextColor(R.id.info,getResources().getColor(R.color.text_f7));
                }
            }
        };
        mAdapter.setOnRecyclerItemClickListener(new OnRecyclerItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (!((Message) mAdapter.getItem(position)).ReadFlag) {
                    readMessage(position);
                }
            }
        });
        lvData.setHasFixedSize(true);
        lvData.setLayoutManager(new LinearLayoutManager(this));
        lvData.setItemAnimator(new DefaultItemAnimator());
        mAdapter.openLoadAnimation(false);
        lvData.setAdapter(mAdapter);

    }




    @Override
    protected void doOperate() {
        requestData();
    }


    private void requestData() {
        showWaitDialog();
        RequestParams params = new RequestParams(ApiUser.getInterMessage());
        params.addBodyParameter(Consts.KEY_SESSIONID, MyApplication
                .getInstance().getSessionId());
        params.addBodyParameter("pageSize", 20 + "");
        params.addBodyParameter("pageIndex", mCurrentPage + "");
        x.http().get(params, new WWXCallBack("InterMessage") {

            @Override
            public void onAfterSuccessOk(JSONObject data) {
                mCurrentPage++;
                list = (ArrayList<Message>) JSONArray.parseArray(
                        data.getString("Data"), Message.class);
                totalCount = data.getIntValue("PageCount");
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
                    mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }


    /**
     * 消息已读
     *
     * @param i
     */
    private void readMessage(final int i) {
        showWaitDialog();
        RequestParams params = new RequestParams(ApiUser.InterMessageRead());
        params.addBodyParameter(Consts.KEY_SESSIONID, MyApplication
                .getInstance().getSessionId());
        params.addBodyParameter("flowId", ((Message) mAdapter.getData().get(i)).FlowId + "");
        x.http().get(params, new WWXCallBack("InterMessageRead") {

            @Override
            public void onAfterSuccessOk(JSONObject data) {
                ((Message) mAdapter.getData().get(i)).ReadFlag = true;
                mAdapter.notifyDataSetChanged();
                WWToast.showShort(R.string.message_read);
            }

            @Override
            public void onAfterFinished() {
                dismissWaitDialog();
            }
        });

    }

}
