package com.jiantaokeji.answerliu.mvvm_core.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;


/**
 * 图片加载工具
 */
public class GlideUtil {

    /**
     * 加载图片
     * @param context
     * @param url
     * @param imageView
     */
    public static void load(Context context, String url, ImageView imageView) {
        RequestOptions options=new RequestOptions().error(null).placeholder(null);
        Glide.with(context)
                .load(url)
                .apply(options)
                .into(imageView);
    }

    /**
     * 加载圆形图片
     * @param context
     * @param url
     * @param imageView
     */
//    public static void loadCircle(Context context, String url, ImageView imageView) {
//        RequestOptions options=new RequestOptions().error(null).placeholder(null).transform(new GlideCircleTransform());
//        Glide.with(context)
//                .load(url)
//                .apply(options)
//                .into(imageView);
//    }


    /**
     * 加载圆形图片
     * @param context 上下文
     * @param resourceId 过渡图
     * @param url 图片地址
     * @param imageView 图片控件
     */
    public static void loadCircle(Context context, int resourceId,String url, ImageView imageView) {
        RequestOptions options=new RequestOptions().error(resourceId).placeholder(resourceId).transform(new GlideCircleTransform());
        Glide.with(context)
                .load(url)
                .apply(options)
                .into(imageView);
    }



    /**
     * 加载本地圆形图片
     * @param context
     * @param resourceId
     * @param imageView
     */
    public static void loadCircleLocal(Context context, int  resourceId, ImageView imageView) {
        RequestOptions options=new RequestOptions().error(null).placeholder(null).transform(new GlideCircleTransform());
        Glide.with(context)
                .load(resourceId)
                .apply(options)
                .into(imageView);
    }


    /**
     * 加载本地图片
     * @param context
     * @param resourceId
     * @param imageView
     */
    public static void loadLocal(Context context, int  resourceId, ImageView imageView) {
        Glide.with(context)
                .load(resourceId)
                .into(imageView);
    }

    /**
     * 加载圆角图片
     * @param context
     * @param url
     * @param imageView
     */
    public static void loadRound(Context context, String url, ImageView imageView) {
        RequestOptions options=new RequestOptions().error(null).placeholder(null).transform(new GlideRoundTransform());
        Glide.with(context)
                .load(url)
                .apply(options)
                .into(imageView);
    }



    public static void loadRound(Context context, String url, ImageView imageView,int borderWidth,int borderColor) {
        RequestOptions options=new RequestOptions().error(null).placeholder(null).transform(new GlideRoundTransform(borderWidth,borderColor));
        Glide.with(context)
                .load(url)
                .apply(options)
                .into(imageView);
    }
}
