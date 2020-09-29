package com.jiantaokeji.answerliu.mvvm_core.base;

import android.content.Context;
import android.view.View;

import razerdp.basepopup.BasePopupWindow;

/**
 * @author AnswerLiu
 * @QQ 1250360307
 * @description: 弹框基类
 * @date :2019/12/30 11:33
 */
public abstract class BasePopup extends BasePopupWindow {

    protected View popupView;



    public BasePopup(Context context) {
        super(context);
    }

    @Override
    public View onCreateContentView() {
        popupView = createPopupById(getRootLayoutResID());
        return popupView;
    }


    protected abstract int getRootLayoutResID();

}
