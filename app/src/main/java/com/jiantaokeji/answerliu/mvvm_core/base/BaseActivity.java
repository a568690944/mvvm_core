package com.jiantaokeji.answerliu.mvvm_core.base;

import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import androidx.annotation.LayoutRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.jakewharton.rxbinding2.view.RxView;
import com.jiantaokeji.answerliu.mvvm_core.R;
import com.jiantaokeji.answerliu.mvvm_core.databinding.ActivityBaseBinding;
import com.jiantaokeji.answerliu.mvvm_core.utils.ClassUtil;
import java.util.concurrent.TimeUnit;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import static com.jiantaokeji.answerliu.mvvm_core.constant.ComParamContact.WINDOW_DURATION;


/**
 * @author AnswerLiu
 * @date Activity基类
 */
public abstract class BaseActivity<VM extends AndroidViewModel, SV extends ViewDataBinding> extends AppCompatActivity {

    protected String TAG;
    // ViewModel
    protected VM viewModel;
    // 布局view
    protected SV bindingView;
    private View errorView;
    private View loadingView;
    protected ActivityBaseBinding mBaseBinding;
    private AnimationDrawable mAnimationDrawable;
    private CompositeDisposable mCompositeDisposable;
    protected Activity me;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG = this.getClass().getSimpleName();
        this.me = this;//引用me自己，便于子类调用
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {

        mBaseBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.activity_base, null, false);
        bindingView = DataBindingUtil.inflate(LayoutInflater.from(this), layoutResID, null, false);

        // content
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        bindingView.getRoot().setLayoutParams(params);
        RelativeLayout mContainer = (RelativeLayout) mBaseBinding.getRoot().findViewById(R.id.container);
        mContainer.addView(bindingView.getRoot());
        getWindow().setContentView(mBaseBinding.getRoot());

        loadingView = ((ViewStub) findViewById(R.id.vs_loading)).inflate();
        ImageView img = loadingView.findViewById(R.id.img_progress);

        // 加载动画
        mAnimationDrawable = (AnimationDrawable) img.getDrawable();
        // 默认进入页面就开启动画
        if (!mAnimationDrawable.isRunning()) {
            mAnimationDrawable.start();
        }
        setToolBar();
        bindingView.getRoot().setVisibility(View.GONE);
        bindingView.setLifecycleOwner(this);
        initViewModel();
    }


    /**
     * 初始化ViewModel
     */
    private void initViewModel() {
        Class<VM> viewModelClass = ClassUtil.getViewModel(this);
        if (viewModelClass != null) {
            this.viewModel = new ViewModelProvider(this).get(viewModelClass);
        }
    }


    /**
     * 设置titlebar
     */
    public void setToolBar() {
        addSubscription(RxView.clicks(mBaseBinding.ivBack)
                .throttleFirst(WINDOW_DURATION, TimeUnit.MILLISECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> finish()));
    }

    public void setTitle(CharSequence text) {
        mBaseBinding.toolbarTitle.setText(text);
    }

    public void setNoTitle() {
        mBaseBinding.toolBar.setVisibility(View.GONE);
    }

    public void setToolBarRight(int icon) {
        mBaseBinding.ivRight.setImageResource(icon);
        addSubscription(RxView.clicks(mBaseBinding.ivRight)
                .throttleFirst(WINDOW_DURATION, TimeUnit.MILLISECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> onClickToolBarRight()));
    }


    protected void showLoading() {
        if (loadingView != null && loadingView.getVisibility() != View.VISIBLE) {
            loadingView.setVisibility(View.VISIBLE);
        }
        // 开始动画
        if (!mAnimationDrawable.isRunning()) {
            mAnimationDrawable.start();
        }
        if (bindingView.getRoot().getVisibility() != View.GONE) {
            bindingView.getRoot().setVisibility(View.GONE);
        }
        if (errorView != null) {
            errorView.setVisibility(View.GONE);
        }
    }

    protected void showContentView() {
        if (loadingView != null && loadingView.getVisibility() != View.GONE) {
            loadingView.setVisibility(View.GONE);
        }
        // 停止动画
        if (mAnimationDrawable.isRunning()) {
            mAnimationDrawable.stop();
        }
        if (errorView != null) {
            errorView.setVisibility(View.GONE);
        }
        if (bindingView.getRoot().getVisibility() != View.VISIBLE) {
            bindingView.getRoot().setVisibility(View.VISIBLE);
        }
    }

    protected void showError() {
        if (loadingView != null && loadingView.getVisibility() != View.GONE) {
            loadingView.setVisibility(View.GONE);
        }
        // 停止动画
        if (mAnimationDrawable.isRunning()) {
            mAnimationDrawable.stop();
        }
        if (errorView == null) {
            ViewStub viewStub = (ViewStub) findViewById(R.id.vs_error_refresh);
            errorView = viewStub.inflate();
            // 点击加载失败布局
            errorView.setOnClickListener(v -> {
                showLoading();
                initView();
            });
        } else {
            errorView.setVisibility(View.VISIBLE);
        }
        if (bindingView.getRoot().getVisibility() != View.GONE) {
            bindingView.getRoot().setVisibility(View.GONE);
        }
    }

    protected void initView(){}

    protected void onClickToolBarRight(){}


    public void addSubscription(Disposable s) {
        if (this.mCompositeDisposable == null) {
            this.mCompositeDisposable = new CompositeDisposable();
        }
        this.mCompositeDisposable.add(s);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.fontScale != 1) {
            getResources();
        }
    }

    /**
     * 禁止改变字体大小
     */
    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (this.mCompositeDisposable != null && !mCompositeDisposable.isDisposed()) {
            // clear 和 dispose的区别是：  disposed = true;
            this.mCompositeDisposable.clear();
        }
    }

    public void removeDisposable() {
        if (this.mCompositeDisposable != null && !mCompositeDisposable.isDisposed()) {
            this.mCompositeDisposable.dispose();
        }
    }
}
