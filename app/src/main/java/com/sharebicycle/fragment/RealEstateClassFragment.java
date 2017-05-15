package com.sharebicycle.fragment;

import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.github.library.BaseRecyclerAdapter;
import com.github.library.BaseViewHolder;
import com.github.library.listener.OnRecyclerItemClickListener;
import com.sharebicycle.activity.HouseListActivity;
import com.sharebicycle.been.SmartLockHistory;
import com.sharebicycle.utils.WWToast;
import com.sharebicycle.www.R;

import java.util.ArrayList;

/**
 * Created by ZXJ on 2017/5/14.
 */

public class RealEstateClassFragment extends FatherFragment {
    private int model;
    public static final int buy=0;
    public static final int rent=1;
    public static final int sell=2;
    private RecyclerView lvData;
    private ArrayList<SmartLockHistory> list = new ArrayList<SmartLockHistory>();
    private BaseRecyclerAdapter mAdapter;
    @Override
    protected int getLayoutId() {
        return R.layout.frag_real_eatate_class;
    }


    public static RealEstateClassFragment getInstance(int model) {
        RealEstateClassFragment sf = new RealEstateClassFragment();
        sf.model = model;
        return sf;
    }
    @Override
    protected void initView() {
        lvData= (RecyclerView) mGroup.findViewById(R.id.lv_data);
        for (int i = 0; i <8 ; i++) {
            list.add(new SmartLockHistory());
        }
        mAdapter = new BaseRecyclerAdapter<SmartLockHistory>(getActivity(), list, R.layout.grid_item) {
            @Override
            protected void convert(BaseViewHolder helper, SmartLockHistory item) {

            }
        };
        mAdapter.setOnRecyclerItemClickListener(new OnRecyclerItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                startActivity(new Intent(getActivity(), HouseListActivity.class));
            }
        });

        lvData.setHasFixedSize(true);
        lvData.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        lvData.setItemAnimator(new DefaultItemAnimator());
        mAdapter.openLoadAnimation(false);
        lvData.setAdapter(mAdapter);
    }
}
