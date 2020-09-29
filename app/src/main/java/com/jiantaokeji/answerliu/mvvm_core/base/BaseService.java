package com.jiantaokeji.answerliu.mvvm_core.base;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * @author: AnswerLiu
 * @date: 2020/8/14
 * @description
 */
public class BaseService extends Service {

    private CompositeDisposable mCompositeDisposable;


    protected void addDisposable(Disposable disposable) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        this.mCompositeDisposable.add(disposable);
    }


    @Override
    public void onDestroy() {
        if (mCompositeDisposable != null && !mCompositeDisposable.isDisposed()) {
            mCompositeDisposable.dispose();
        }
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
