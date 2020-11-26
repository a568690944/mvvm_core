package com.jiantaokeji.answerliu.mvvm_core.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.jiantaokeji.answerliu.mvvm_core.R;
import com.jiantaokeji.answerliu.mvvm_core.utils.DensityUtils;


/**
 * 没有动画的底部导航
 */
public class NormalTabView extends FrameLayout {
    private int mTextNormalColor;
    private int mTextSelectColor;
    private float mTextSize;
    private String mTabName;
    private Drawable mIconNormal;
    private Drawable mIconSelected;
//    private String mAnimationPath;
//    private LottieAnimationView mLottieView;
    private ImageView mTabView;
    private TextView mTabNameView;
    private TextView mMsgView;
    private boolean isSelected;

    public NormalTabView(Context context) {
        super(context);
    }

    public NormalTabView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public NormalTabView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }


    private void init(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.NormalTabView);
        mTextNormalColor = ta.getColor(R.styleable.NormalTabView_ntv_text_normal_color,getResources().getColor(R.color.color_262626));
        mTextSelectColor = ta.getColor(R.styleable.NormalTabView_ntv_text_selected_color, getResources().getColor(R.color.white));
        mTextSize = ta.getDimension(R.styleable.NormalTabView_ntv_text_size, DensityUtils.dp2px(context, 5));
        mIconNormal = ta.getDrawable(R.styleable.NormalTabView_ntv_icon_normal);
        mIconSelected=ta.getDrawable(R.styleable.NormalTabView_ntv_icon_selected);
        mTabName = ta.getString(R.styleable.NormalTabView_ntv_tab_name);
        isSelected = ta.getBoolean(R.styleable.NormalTabView_ntv_tab_selected, false);
        ta.recycle();
        initView(context);
    }

    private void initView(Context context) {
        View containView = LayoutInflater.from(context).inflate(R.layout.normal_tab_view, null, false);
        mTabView = containView.findViewById(R.id.tab_view);
        mTabNameView = containView.findViewById(R.id.tab_name);
        mTabNameView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
        mTabNameView.setTextColor(mTextNormalColor);
        mTabNameView.setText(mTabName);
        mMsgView = containView.findViewById(R.id.msg_view);
        this.addView(containView);
        if (isSelected) {
            selected();
        } else {
            unSelected();
        }
    }

    public void selected() {
        mTabView.setImageDrawable(mIconSelected);
        mTabNameView.setTextColor(mTextSelectColor);
    }

    public void unSelected() {
        mTabView.setImageDrawable(mIconNormal);
        mTabNameView.setTextColor(mTextNormalColor);
    }


    public void showDot(boolean isShow){
        if (isShow){
            mMsgView.setVisibility(VISIBLE);
        }else {
            mMsgView.setVisibility(GONE);
        }
    }


    //在右上角显示消息提示数量
    @SuppressLint("SetTextI18n")
    public void showMsg(int num) {
        if (num > 0 && num <= 99) {
            mMsgView.setVisibility(VISIBLE);
            mMsgView.setText(num + "");
        } else if (num > 99) {
            mMsgView.setVisibility(VISIBLE);
            mMsgView.setText("99+");
        } else if (num == 0) {
            mMsgView.setVisibility(GONE);
        } else {
            mMsgView.setVisibility(View.GONE);
        }
    }
}