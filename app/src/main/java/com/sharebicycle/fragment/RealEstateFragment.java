package com.sharebicycle.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.flyco.tablayout.SlidingTabLayout;
import com.github.library.BaseRecyclerAdapter;
import com.github.library.BaseViewHolder;
import com.github.library.listener.OnRecyclerItemClickListener;
import com.sharebicycle.MyApplication;
import com.sharebicycle.activity.FatherActivity;
import com.sharebicycle.api.ApiUser;
import com.sharebicycle.been.Message;
import com.sharebicycle.been.SmartLockHistory;
import com.sharebicycle.utils.Consts;
import com.sharebicycle.utils.TimeUtil;
import com.sharebicycle.utils.WWToast;
import com.sharebicycle.www.R;
import com.sharebicycle.xutils.WWXCallBack;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZXJ on 2017/5/10.
 */

public class RealEstateFragment extends FatherFragment {


    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private final String[] mTitles = {
            "我要买房", "我要租房", "我是房主"
    };
    private MyPagerAdapter mvAdapter;


    private RecyclerView lvData;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ArrayList<Message> list = new ArrayList<Message>();
    private BaseRecyclerAdapter mAdapter;
    // 分页数据
    private int mCurrentPage = 0;
    private int totalCount = 0;


    @Override
    protected int getLayoutId() {
        return R.layout.frag_real_estate;
    }

    @Override
    protected void initView() {
        View headerView = mInflater.inflate(R.layout.real_estate_header, null);
        headerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));

        mFragments.add(RealEstateClassFragment.getInstance(RealEstateClassFragment.buy));
        mFragments.add(RealEstateClassFragment.getInstance(RealEstateClassFragment.rent));
        mFragments.add(RealEstateClassFragment.getInstance(RealEstateClassFragment.sell));


        ViewPager vp = (ViewPager) headerView.findViewById(R.id.vp);
        mvAdapter = new MyPagerAdapter(getActivity().getSupportFragmentManager());
        vp.setAdapter(mvAdapter);
        /** indicator圆角色块 */
        SlidingTabLayout tabLayout_9 = (SlidingTabLayout) headerView.findViewById(R.id.tl_9);
        tabLayout_9.setViewPager(vp);


        lvData = (RecyclerView) mGroup.findViewById(R.id.lv_data);
        mSwipeRefreshLayout = (SwipeRefreshLayout) mGroup.findViewById(R.id.swipe_layout);
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

        mAdapter = new BaseRecyclerAdapter<Message>(getActivity(), list, R.layout.real_eatate_list) {
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
        lvData.setLayoutManager(new LinearLayoutManager(getActivity()));
        lvData.setItemAnimator(new DefaultItemAnimator());
        mAdapter.openLoadAnimation(false);

        mAdapter.addHeaderView(headerView);
        lvData.setAdapter(mAdapter);

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


    private class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }
    }
}
