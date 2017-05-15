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
import android.widget.LinearLayout;
import android.widget.Toast;

import com.flyco.tablayout.SlidingTabLayout;
import com.github.library.BaseRecyclerAdapter;
import com.github.library.BaseViewHolder;
import com.github.library.listener.OnRecyclerItemClickListener;
import com.sharebicycle.been.SmartLockHistory;
import com.sharebicycle.utils.WWToast;
import com.sharebicycle.www.R;

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
    private LinearLayout ll_container;
    @Override
    protected int getLayoutId() {
        return R.layout.frag_real_estate;
    }

    @Override
    protected void initView() {

            mFragments.add(RealEstateClassFragment.getInstance(RealEstateClassFragment.buy));
            mFragments.add(RealEstateClassFragment.getInstance(RealEstateClassFragment.rent));
            mFragments.add(RealEstateClassFragment.getInstance(RealEstateClassFragment.sell));


        ViewPager vp = (ViewPager) mGroup.findViewById(R.id.vp);
        mvAdapter = new MyPagerAdapter(getActivity().getSupportFragmentManager());
        vp.setAdapter(mvAdapter);
        /** indicator圆角色块 */
        SlidingTabLayout tabLayout_9 = (SlidingTabLayout) mGroup.findViewById(R.id.tl_9);
        tabLayout_9.setViewPager(vp);

        ll_container = (LinearLayout)  mGroup.findViewById(R.id.ll_container);

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
