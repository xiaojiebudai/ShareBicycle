package com.sharebicycle.fragment;

import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.github.library.BaseRecyclerAdapter;
import com.github.library.BaseViewHolder;
import com.github.library.listener.OnRecyclerItemClickListener;
import com.sharebicycle.activity.SmartLockClassActivity;
import com.sharebicycle.activity.SmartLockOpenActivity;
import com.sharebicycle.been.SmartLockHistory;
import com.sharebicycle.utils.WWToast;
import com.sharebicycle.www.R;

import java.util.ArrayList;


/**
 * Created by ZXJ on 2017/5/10.
 */

public class SmartLockFragment extends FatherFragment implements View.OnClickListener{

    private   RecyclerView lvData;

    private ArrayList<SmartLockHistory> list = new ArrayList<SmartLockHistory>();
    private BaseRecyclerAdapter mAdapter;
    @Override
    protected int getLayoutId() {
        return R.layout.frag_smart_lock;
    }

    @Override
    protected void initView() {
        mGroup.findViewById(R.id.rl_head_left).setOnClickListener(this);
        mGroup.findViewById(R.id.rl_head_right).setOnClickListener(this);

        lvData=(RecyclerView)mGroup.findViewById(R.id.lv_data);


        for (int i = 0; i <3 ; i++) {
            list.add(new SmartLockHistory());
        }
        mAdapter = new BaseRecyclerAdapter<SmartLockHistory>(getActivity(), list, R.layout.list_lock_history) {
            @Override
            protected void convert(BaseViewHolder helper, SmartLockHistory item) {

            }
        };
        mAdapter.setOnRecyclerItemClickListener(new OnRecyclerItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                WWToast.showShort("演示例子，敬请期待");
            }
        });
        lvData.setHasFixedSize(true);
        lvData.setLayoutManager(new LinearLayoutManager(getActivity()));
        lvData.setItemAnimator(new DefaultItemAnimator());
        mAdapter.openLoadAnimation(false);
        lvData.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_head_left:
                getActivity().startActivity(new Intent(getActivity(),SmartLockClassActivity.class));
                break;
            case R.id.rl_head_right:
                getActivity().startActivity(new Intent(getActivity(),SmartLockOpenActivity.class));
                break;
        }
    }
}
