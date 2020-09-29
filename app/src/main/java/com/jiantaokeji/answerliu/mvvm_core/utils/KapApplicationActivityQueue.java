package com.jiantaokeji.answerliu.mvvm_core.utils;

import android.app.Activity;

import java.util.Stack;

/**
 * @author AnswerLiu
 * @QQ 1250360307
 * @description: 管理 Activity 的 视图栈
 * @date :2019/12/3 9:30
 */
public class KapApplicationActivityQueue {
    private KapApplicationActivityQueue(){}
    private static KapApplicationActivityQueue queue = new KapApplicationActivityQueue();
    public static KapApplicationActivityQueue ShareActivityQueue(){
        return queue;
    }
    private Stack<Activity> activityStack = new Stack<Activity>();

    /** 获取当前的activity，不做任何操作 */
    public Activity currentActivity() {
        return activityStack.lastElement();
    }


    /**
     * 只有这俩方法 操作 activityStack不能手动调用（都是自动添加删除的）
     * addActivity 添加Activity到堆栈
     * popCurrentActivity 结束当前Activity
     * currentActivity
     */
    public void addActivity(Activity activity) {
        activityStack.push(activity);
    }
    public void popCurrentActivity(Activity activity) {
        activityStack.remove(activity);
    }

    /**
     * 下面的这些方法都是辅助方法 (注意防止当前activity结束当前的导致crash)
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    // 结束指定的Activity
    public  void finishOneActivity(Activity activity) {
        if (activity != null) {
            if(!activity.isFinishing()) {
                activity.finish();
            }
        }
    }
    // 结束指定类名的Activity
    public  void finishOneActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (!activity.getClass().equals(cls)) continue;
            finishOneActivity(activity);
            return;
        }
    }
    /** 结束至指定类名Activity(不包括该类名) */
    public void finishToActivity(Class<?> cls){
        while (!activityStack.lastElement().getClass().equals(cls)){
            activityStack.pop().finish();
            if (activityStack.size() == 0) return;
        }
    }

    /** 结束除指定类名的所有Activity */
    public  void finishExcludeActivityAllActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity == null) continue;
            if (activity.getClass().equals(cls)) continue;
            finishOneActivity(activity);
        }
    }
    /** 结束所有Activity */
    public  void finishAllActivity() {
        for (Activity activity : activityStack) {
            if (activity == null) continue;
            finishOneActivity(activity);
        }
    }
}
