package com.jiantaokeji.answerliu.mvvm_core.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by liuc on 2014/5/20.
 */
public final class ToastUtils {

    private static Toast mToast;

    public static void showToast(Context context, String msg, boolean isLong) {
        if (mToast == null) {
            mToast = Toast.makeText(context, msg, isLong ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT);
        } else {
            mToast.setText(msg);
            mToast.setDuration(isLong ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT);
        }
        mToast.show();
    }

    public static void show(Context context, String msg) {
        showToast(context, msg, false);
    }

    public static void showNetWorkError(Context context) {
        showToast(context, "网络连接超时，请稍后再试！", true);
    }
    public static void showError(Context context) {
        showToast(context, "出现异常，请稍后再试！", false);
    }
}
