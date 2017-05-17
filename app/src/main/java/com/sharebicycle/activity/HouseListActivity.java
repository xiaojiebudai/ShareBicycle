package com.sharebicycle.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.library.BaseRecyclerAdapter;
import com.github.library.BaseViewHolder;
import com.github.library.listener.OnRecyclerItemClickListener;
import com.sharebicycle.been.Message;
import com.sharebicycle.been.SmartLockHistory;
import com.sharebicycle.utils.WWToast;
import com.sharebicycle.utils.WWViewUtil;
import com.sharebicycle.www.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ZXJ on 2017/5/14.
 */

public class HouseListActivity extends FatherActivity {
    @BindView(R.id.edt_search)
    EditText edtSearch;
    @BindView(R.id.iv_search_clear)
    ImageView ivSearchClear;
    @BindView(R.id.tv_head_right)
    TextView tvHeadRight;
    @BindView(R.id.rl_head_right)
    RelativeLayout rlHeadRight;
    @BindView(R.id.tv_area)
    TextView tvArea;
    @BindView(R.id.ll_area)
    LinearLayout llArea;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.ll_price)
    LinearLayout llPrice;
    @BindView(R.id.tv_style)
    TextView tvStyle;
    @BindView(R.id.ll_style)
    LinearLayout llStyle;
    @BindView(R.id.ll_more)
    LinearLayout llMore;
    @BindView(R.id.lv_data)
    RecyclerView lvData;
    private ArrayList<SmartLockHistory> list = new ArrayList<SmartLockHistory>();
    private BaseRecyclerAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.act_house_list;
    }

    @Override
    protected void initValues() {
        initHeadBack();
        initTextHeadRigth("查询", new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    protected void initView() {
        for (int i = 0; i < 20; i++) {
            list.add(new SmartLockHistory());
        }
        mAdapter = new BaseRecyclerAdapter<SmartLockHistory>(this, list, R.layout.real_eatate_list) {
            @Override
            protected void convert(BaseViewHolder helper, SmartLockHistory item) {

            }
        };
        mAdapter.setOnRecyclerItemClickListener(new OnRecyclerItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                startActivity(new Intent(HouseListActivity.this,HouseDetailActivity.class));
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

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.iv_search_clear)
    public void onViewClicked() {
    }

    @OnClick({R.id.ll_area, R.id.ll_price, R.id.ll_style, R.id.ll_more})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_area:
                showAreaPop(view);
                break;
            case R.id.ll_price:
                showPricePop(view);
                break;
            case R.id.ll_style:
                showStylePop(view);
                break;
            case R.id.ll_more:
                showMorePop(view);
                break;
        }
    }
    private PopupWindow popupMore;
    private void showMorePop(View view) {
        if (popupMore != null) {
            WWViewUtil.setPopInSDK7(popupMore, view);
        } else {
            View inflate = View.inflate(this,
                    R.layout.layout_pop_house_more, null);

            View ok = inflate.findViewById(R.id.tv_ok);

            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popupMore.dismiss();
                }
            });
            inflate.findViewById(R.id.iv_alpha_bg).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popupMore.dismiss();
                }
            });
            popupMore = new PopupWindow(inflate, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
            popupMore.setBackgroundDrawable(new BitmapDrawable());
            popupMore.setOutsideTouchable(true);
            WWViewUtil.setPopInSDK7(popupMore, view);
        }



    }
    private PopupWindow popupStyle;
    private void showStylePop(View view) {
        if (popupStyle != null) {
            WWViewUtil.setPopInSDK7(popupStyle, view);
        } else {
            View inflate = View.inflate(this,
                    R.layout.layout_pop_listview_alpha, null);
            RecyclerView lv_pop = (RecyclerView) inflate
                    .findViewById(R.id.lv_data);
            View ok=inflate.findViewById(R.id.tv_ok);
            ok.setVisibility(View.VISIBLE);
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popupStyle.dismiss();
                }
            });
            inflate.findViewById(R.id.iv_alpha_bg).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popupStyle.dismiss();
                }
            });
            popupStyle = new PopupWindow(inflate, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
            popupStyle.setBackgroundDrawable(new BitmapDrawable());
            popupStyle.setOutsideTouchable(true);
            final ArrayList<Message> list = new ArrayList<Message>();
            final BaseRecyclerAdapter mAdapter;
            for (int i = 0; i <10 ; i++) {
                list.add(new Message());
            }

            mAdapter = new BaseRecyclerAdapter<Message>(this, list, R.layout.list_min_text_item) {
                @Override
                protected void convert(BaseViewHolder helper, Message item) {
                    helper.setText(R.id.tv_info,"100万");
                    if (item.isSelect) {
                        helper.setTextColor(R.id.tv_info,mContext.getResources().getColor(R.color.color_main));
                        helper.getView(R.id.iv_gou).setVisibility(View.VISIBLE);
                    } else {
                        helper.setTextColor(R.id.tv_info,mContext.getResources().getColor(R.color.text_f7));
                        helper.getView(R.id.iv_gou).setVisibility(View.GONE);

                    }
                }
            };
            mAdapter.setOnRecyclerItemClickListener(new OnRecyclerItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                   list.get(position).isSelect=  ! list.get(position).isSelect;
                    mAdapter.notifyDataSetChanged();
                }
            });
            lv_pop.setHasFixedSize(true);
            lv_pop.setLayoutManager(new LinearLayoutManager(this));
            lv_pop.setItemAnimator(new DefaultItemAnimator());
            mAdapter.openLoadAnimation(false);
            lv_pop.setAdapter(mAdapter);
            WWViewUtil.setPopInSDK7(popupStyle, view);
        }

    }
    private PopupWindow popupPrice;
    private  Message selectPrice;
    private void showPricePop(View view) {
        if (popupPrice != null) {
            WWViewUtil.setPopInSDK7(popupPrice, view);
        } else {
            View inflate = View.inflate(this,
                    R.layout.layout_pop_listview_alpha, null);
            RecyclerView lv_pop = (RecyclerView) inflate
                    .findViewById(R.id.lv_data);
            inflate.findViewById(R.id.iv_alpha_bg).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popupPrice.dismiss();
                }
            });
            popupPrice = new PopupWindow(inflate, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
            popupPrice.setBackgroundDrawable(new BitmapDrawable());
            popupPrice.setOutsideTouchable(true);
            final ArrayList<Message> list = new ArrayList<Message>();
             final BaseRecyclerAdapter mAdapter;
            for (int i = 0; i <10 ; i++) {
                list.add(new Message());
            }
            selectPrice=list.get(0);
            mAdapter = new BaseRecyclerAdapter<Message>(this, list, R.layout.list_min_text_item) {
                @Override
                protected void convert(BaseViewHolder helper, Message item) {
                    helper.setText(R.id.tv_info,"100万");
                    if (selectPrice == item) {
                        helper.setTextColor(R.id.tv_info,mContext.getResources().getColor(R.color.color_main));
                                            helper.getView(R.id.iv_gou).setVisibility(View.VISIBLE);
                                      } else {
                        helper.setTextColor(R.id.tv_info,mContext.getResources().getColor(R.color.text_f7));
                        helper.getView(R.id.iv_gou).setVisibility(View.GONE);

                    }
                }
            };
            mAdapter.setOnRecyclerItemClickListener(new OnRecyclerItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    selectPrice=list.get(position);
                    mAdapter.notifyDataSetChanged();
                }
            });
            lv_pop.setHasFixedSize(true);
            lv_pop.setLayoutManager(new LinearLayoutManager(this));
            lv_pop.setItemAnimator(new DefaultItemAnimator());
            mAdapter.openLoadAnimation(false);
            lv_pop.setAdapter(mAdapter);


            WWViewUtil.setPopInSDK7(popupPrice, view);
        }
    }
    private PopupWindow popupArea;
    private  Message selectAreaLeft;
    private  Message selectAreaLeft1;
    private void showAreaPop(View view) {
        if (popupArea != null) {
            WWViewUtil.setPopInSDK7(popupArea, view);
        } else {
            View inflate = View.inflate(this,
                    R.layout.pop_erjiselect, null);

            inflate.findViewById(R.id.iv_alpha_bg).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popupArea.dismiss();
                }
            });
            popupArea = new PopupWindow(inflate, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
            popupArea.setBackgroundDrawable(new BitmapDrawable());
            popupArea.setOutsideTouchable(true);
            RecyclerView rootcategory = (RecyclerView) inflate
                    .findViewById(R.id.rootcategory);
            final ArrayList<Message> list = new ArrayList<Message>();
            final BaseRecyclerAdapter mAdapter;

            for (int i = 0; i <10 ; i++) {
                list.add(new Message());
            }
            selectAreaLeft=list.get(0);
            mAdapter = new BaseRecyclerAdapter<Message>(this, list, R.layout.list_min_text_item) {
                @Override
                protected void convert(BaseViewHolder helper, Message item) {
                    helper.setText(R.id.tv_info,"100万");
                    if (selectAreaLeft == item) {
                        helper.setTextColor(R.id.tv_info,mContext.getResources().getColor(R.color.color_main));

                    } else {
                        helper.setTextColor(R.id.tv_info,mContext.getResources().getColor(R.color.text_f7));
                    }
                }
            };
            RecyclerView childcategory = (RecyclerView) inflate
                    .findViewById(R.id.childcategory);
            final ArrayList<Message> list1 = new ArrayList<Message>();
            final BaseRecyclerAdapter mAdapter1;
            mAdapter1 = new BaseRecyclerAdapter<Message>(this, list1, R.layout.list_min_text_item) {
                @Override
                protected void convert(BaseViewHolder helper, Message item) {
                    helper.setText(R.id.tv_info,"100万");
                    if (selectAreaLeft1 == item) {
                        helper.setTextColor(R.id.tv_info,mContext.getResources().getColor(R.color.color_main));

                    } else {
                        helper.setTextColor(R.id.tv_info,mContext.getResources().getColor(R.color.text_f7));
                    }
                }
            };
            rootcategory.setHasFixedSize(true);
            rootcategory.setLayoutManager(new LinearLayoutManager(this));
            rootcategory.setItemAnimator(new DefaultItemAnimator());
            mAdapter.openLoadAnimation(false);
            rootcategory.setAdapter(mAdapter);

            mAdapter.setOnRecyclerItemClickListener(new OnRecyclerItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    selectAreaLeft=list.get(position);
                    mAdapter.notifyDataSetChanged();
                    list1.clear();
                    for (int i = 0; i <10 ; i++) {

                        list1.add(new Message());
                    }
                    selectAreaLeft1=list1.get(0);
                    mAdapter1.setData(list1);
                }
            });
            mAdapter1.setOnRecyclerItemClickListener(new OnRecyclerItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    selectAreaLeft1=list1.get(position);
                    mAdapter1.notifyDataSetChanged();
                }
            });
            childcategory.setHasFixedSize(true);
            childcategory.setLayoutManager(new LinearLayoutManager(this));
            childcategory.setItemAnimator(new DefaultItemAnimator());
            mAdapter1.openLoadAnimation(false);
            childcategory.setAdapter(mAdapter1);


            WWViewUtil.setPopInSDK7(popupArea, view);
        }
    }
}
