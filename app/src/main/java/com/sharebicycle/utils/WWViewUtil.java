package com.sharebicycle.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.InputFilter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;


import com.sharebicycle.www.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WWViewUtil {
    /**
     * 给listView设置emptyView
     */
    public static void setEmptyView(ListView listView, String text) {
        View view = LayoutInflater.from(listView.getContext()).inflate(
                R.layout.emptyview, listView, false);
        if (!TextUtils.isEmpty(text)) {
            ((TextView) view.findViewById(R.id.tv_info)).setText(text);
        }
        listView.setEmptyView(view);
    }


    /**
     * 给listView设置emptyView
     * <p>
     * SpannableStringBuilder builder = new SpannableStringBuilder("您还没有购买商品，去逛逛");
     * <p>
     * //用下划线标记文本
     * builder.setSpan(new UnderlineSpan(), 9, 12,
     * Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
     * //用颜色标记
     * builder.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.yellow_ww)), 9, 12,
     * Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
     */
    public static void setEmptyView(ListView listView, SpannableStringBuilder builder, View.OnClickListener listener) {
        View view = LayoutInflater.from(listView.getContext()).inflate(
                R.layout.emptyview, listView, false);
        if (!TextUtils.isEmpty(builder)) {
            TextView tv_info = ((TextView) view.findViewById(R.id.tv_info));
            tv_info.setText(builder);
            if (listener != null) {
                tv_info.setOnClickListener(listener);
            }
        }
        listView.setEmptyView(view);
    }

    /**
     * 给listView设置emptyView
     */
    public static void setEmptyView(ListView listView) {
        View view = LayoutInflater.from(listView.getContext()).inflate(
                R.layout.emptyview, listView, false);
        listView.setEmptyView(view);
    }

    /**
     * 给listView设置emptyView
     */
    public static void setEmptyViewNew(ListView listView) {
        View emptyView = View.inflate(listView.getContext(), R.layout.emptyview, null);
        emptyView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        ((ViewGroup) listView.getParent()).addView(emptyView);
        listView.setEmptyView(emptyView);
    }

    /**
     * 给gridView设置emptyView
     */
    public static void setEmptyView(GridView gridView) {
        View view = LayoutInflater.from(gridView.getContext()).inflate(
                R.layout.emptyview, gridView, false);
        gridView.setEmptyView(view);
    }

    /***
     * <p>
     * Description:设置空字符
     * </p>
     *
     * @author ZXJ
     * @date 2016-3-31
     * @param text
     * @param old
     * @param change
     ***/
    public static void setNullText(TextView text, String old, String change) {
        if (old == null || old.isEmpty()) {
            text.setText(change);
        } else {
            text.setText(old);
        }
    }

    /***
     * Description:输出两位小数
     * @author ZXJ
     * @date 2016-8-6
     * @param d
     * @return
     */
    public static String numberFormatWithTwo(double d) {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(d);
    }

    /***
     * Description:输出s小数
     * @author ZXJ
     * @date 2016-8-6
     * @param d ,s
     * @return
     */
    public static String numberFormatWithTwo(double d, String s) {
        DecimalFormat df = new DecimalFormat(s);
        return df.format(d);
    }

    /***
     * Description:输出s小数
     * @author ZXJ
     *设置下划线
     * @return
     */
    public static void viewUnderLine(TextView view) {
        //设置下划线
        view.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        view.getPaint().setAntiAlias(true);//抗锯齿
    }

    /***
     * Description:输出两位小数价格带符号
     * @author ZXJ
     * @date 2016-8-6
     * @param d
     * @return
     */
    public static String numberFormatPrice(double d) {
        DecimalFormat df = new DecimalFormat("0.00");
        return "¥" + df.format(d);
    }

    /**
     * 获取状态栏高度
     *
     * @param context context
     * @return 状态栏高度
     */
    public static int getStatusBarHeight(Context context) {
        // 获得状态栏高度
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return context.getResources().getDimensionPixelSize(resourceId);
    }

    /**
     * showAsDropDown适配7.0
     *
     * @param p
     * @param v
     */
    public static void setPopInSDK7(PopupWindow p, View v) {
        if (Build.VERSION.SDK_INT == 24) {
            Rect rect = new Rect();
            v.getGlobalVisibleRect(rect);
            p.showAtLocation(v, Gravity.NO_GRAVITY, rect.left, rect.bottom);
        } else {
            p.showAsDropDown(v);
        }

    }


    /**
     * 限制输入框金额输入
     *
     * @param s,view,afterPointNum
     * @param afterPointNum        限制小数点后几位
     * @param afterPointNum
     */
    public static void limitEditMoneyRule(CharSequence s, EditText view, int afterPointNum) {
        if (s.toString().contains(".")) {
            if (s.length() - 1 - s.toString().indexOf(".") > afterPointNum) {
                s = s.toString().subSequence(0,
                        s.toString().indexOf(".") + afterPointNum + 1);
                view.setText(s);
                view.setSelection(s.length());
            }
        }
        if (s.toString().trim().substring(0).equals(".")) {
            s = "0" + s;
            view.setText(s);
            view.setSelection(2);
        }
        if (s.toString().startsWith("0")
                && s.toString().trim().length() > 1) {
            if (!s.toString().substring(1, 2).equals(".")) {
                view.setText(s.subSequence(0, 1));
                view.setSelection(1);
                return;
            }
        }
    }

    /**
     * 输入限制  整数限制  小数点后位数限制
     *
     * @param view
     */
    public static void inputLimit(final EditText view, final double maxVALUE, final int pointerLength) {
        view.setFilters(new InputFilter[]{new InputFilter() {
            Pattern mPattern = Pattern.compile("([0-9]|\\.)*");

            //输入的最大金额
            private double MAX_VALUE = maxVALUE;
            //小数点后的位数
            private int POINTER_LENGTH = pointerLength;

            private final String POINTER = ".";

            private final String ZERO = "0";

            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                String sourceText = source.toString();
                String destText = dest.toString();
                //验证删除等按键
                if (TextUtils.isEmpty(sourceText)) {
                    return "";
                }

                Matcher matcher = mPattern.matcher(source);
                //已经输入小数点的情况下，只能输入数字
                if (destText.contains(POINTER)) {
                    if (!matcher.matches()) {
                        return "";
                    } else {
                        if (POINTER.equals(source)) {  //只能输入一个小数点
                            return "";
                        }
                    }
                    //验证小数点精度，保证小数点后只能输入两位
                    int index = destText.indexOf(POINTER);
                    int length = destText.length() - index;
                    //如果长度大于2，并且新插入字符在小数点之后
                    if (length > POINTER_LENGTH && index < dstart) {
                        //超出2位返回null
                        return "";
                    }
                } else {
                    //没有输入小数点的情况下，只能输入小数点和数字，但首位不能输入小数点
                    if (!matcher.matches()) {
                        return "";
                    } else {
                        if ((POINTER.equals(source)) && TextUtils.isEmpty(destText)) {
                            return "";
                        }
                    }
                    if (view.getText().toString().startsWith(ZERO) && !sourceText.equals(POINTER)) {
                        return "";
                    }
                }
                //验证输入金额的大小
                double sumText = Double.parseDouble(destText + sourceText);
                if (sumText > MAX_VALUE) {
                    return dest.subSequence(dstart, dend);
                }
                return dest.subSequence(dstart, dend) + sourceText;
            }
        }});
    }

}
