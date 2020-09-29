package com.jiantaokeji.answerliu.mvvm_core.base;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.jiantaokeji.answerliu.mvvm_core.R;
import com.jiantaokeji.answerliu.mvvm_core.utils.ClassUtil;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;


/**
 * 类描述：Fragment基类
 * 创建人：AnswerLiu QQ:1250360307
 * 创建时间： 2018/4/2 0002 17:45
 */
public abstract class BaseFragment<VM extends AndroidViewModel,SV extends ViewDataBinding>  extends Fragment {
    // ViewModel
    protected VM viewModel;
    // 布局view
    protected SV bindingView;
    // fragment是否显示了
    protected boolean mIsVisible = false;
    // 加载中
    private View loadingView;
    // 加载失败
    private View errorView;
    // 空布局
    private View emptyView;
    // 动画
    private AnimationDrawable mAnimationDrawable;
    private CompositeDisposable compositeDisposable;
    protected Activity activity;

    protected boolean mIsLoadedData = false;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (Activity) context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_base, null);
        bindingView = DataBindingUtil.inflate(activity.getLayoutInflater(), setContent(), null, false);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        bindingView.getRoot().setLayoutParams(params);
        RelativeLayout mContainer = inflate.findViewById(R.id.container);
        mContainer.addView(bindingView.getRoot());
        bindingView.getRoot().setVisibility(View.GONE);
        bindingView.setLifecycleOwner(this);
        return inflate;
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isResumed()) {
            handleOnVisibilityChangedToUser(isVisibleToUser);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getUserVisibleHint()) {
            handleOnVisibilityChangedToUser(true);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (getUserVisibleHint()) {
            handleOnVisibilityChangedToUser(false);
        }
    }

    /**
     * 处理对用户是否可见
     *
     * @param isVisibleToUser 是否对用户可见
     */
    private void handleOnVisibilityChangedToUser(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            // 对用户可见
            if (!mIsLoadedData) {
                mIsLoadedData = true;
                onLazyLoadOnce();
            }
            onVisibleToUser();
        } else {
            // 对用户不可见
            onInvisibleToUser();
        }
    }
    /**
     * 懒加载一次。如果只想在对用户可见时才加载数据，并且只加载一次数据，在子类中重写该方法
     */
    protected void onLazyLoadOnce() {
    }

    /**
     * 对用户可见时触发该方法。如果只想在对用户可见时才加载数据，在子类中重写该方法
     */
    protected void onVisibleToUser() {
    }

    /**
     * 对用户不可见时触发该方法
     */
    protected void onInvisibleToUser() {
    }


    protected void loadData() {
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViewModel();
    }

    /**
     * 初始化ViewModel
     */
    private void initViewModel() {
        final Class<VM> viewModelClass = ClassUtil.getViewModel(this);
        if (viewModelClass != null) {
            this.viewModel = new ViewModelProvider(this).get(viewModelClass);
        }
    }

    protected <T extends View> T getView(int id) {
        return (T) getView().findViewById(id);
    }


    /**
     * 获取布局文件根视图
     *
     * @return 根布局
     */
    protected abstract int setContent();


    /**
     * 加载失败后点击后的操作
     */
    protected void onRefresh() {

    }


    /**
     * 显示加载中状态
     */
    protected void showLoading() {
        ViewStub viewStub = getView(R.id.vs_loading);
        if (viewStub != null) {
            loadingView = viewStub.inflate();
            ImageView img = loadingView.findViewById(R.id.img_progress);
            mAnimationDrawable = (AnimationDrawable) img.getDrawable();
        }
        if (loadingView != null && loadingView.getVisibility() != View.VISIBLE) {
            loadingView.setVisibility(View.VISIBLE);
        }
        // 开始动画
        if (mAnimationDrawable != null && !mAnimationDrawable.isRunning()) {
            mAnimationDrawable.start();
        }
        if (bindingView.getRoot().getVisibility() != View.GONE) {
            bindingView.getRoot().setVisibility(View.GONE);
        }
        if (errorView != null) {
            errorView.setVisibility(View.GONE);
        }
        if (emptyView != null && emptyView.getVisibility() != View.GONE) {
            emptyView.setVisibility(View.GONE);
        }
    }

    /**
     * 加载完成的状态
     */
    protected void showContentView() {
        if (bindingView.getRoot().getVisibility() != View.VISIBLE) {
            bindingView.getRoot().setVisibility(View.VISIBLE);
        }
        if (loadingView != null && loadingView.getVisibility() != View.GONE) {
            loadingView.setVisibility(View.GONE);
        }
        // 停止动画
        if (mAnimationDrawable != null && mAnimationDrawable.isRunning()) {
            mAnimationDrawable.stop();
        }
        if (errorView != null) {
            errorView.setVisibility(View.GONE);
        }
        if (emptyView != null && emptyView.getVisibility() != View.GONE) {
            emptyView.setVisibility(View.GONE);
        }
    }

    /**
     * 加载失败点击重新加载的状态
     */
    protected void showError() {
        ViewStub viewStub = getView(R.id.vs_error_refresh);
        if (viewStub != null) {
            errorView = viewStub.inflate();
            // 点击加载失败布局
            errorView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showLoading();
                    onRefresh();
                }
            });
        }
        if (errorView != null) {
            errorView.setVisibility(View.VISIBLE);
        }
        if (loadingView != null && loadingView.getVisibility() != View.GONE) {
            loadingView.setVisibility(View.GONE);
        }
        // 停止动画
        if (mAnimationDrawable != null && mAnimationDrawable.isRunning()) {
            mAnimationDrawable.stop();
        }
        if (bindingView.getRoot().getVisibility() != View.GONE) {
            bindingView.getRoot().setVisibility(View.GONE);
        }
        if (emptyView != null && emptyView.getVisibility() != View.GONE) {
            emptyView.setVisibility(View.GONE);
        }
    }


    protected void showEmptyView(String text) {
        // 需要这样处理，否则二次显示会失败
        ViewStub viewStub = getView(R.id.vs_empty);
        if (viewStub != null) {
            emptyView = viewStub.inflate();
//            ((TextView) emptyView.findViewById(R.id.tv_tip_empty)).setText(text);
        }
        if (emptyView != null) {
            emptyView.setVisibility(View.VISIBLE);
        }

        if (loadingView != null && loadingView.getVisibility() != View.GONE) {
            loadingView.setVisibility(View.GONE);
        }
        // 停止动画
        if (mAnimationDrawable != null && mAnimationDrawable.isRunning()) {
            mAnimationDrawable.stop();
        }
        if (errorView != null) {
            errorView.setVisibility(View.GONE);
        }
        if (bindingView != null && bindingView.getRoot().getVisibility() != View.GONE) {
            bindingView.getRoot().setVisibility(View.GONE);
        }
    }





    public void addSubscription(Disposable disposable) {
        if (this.compositeDisposable == null) {
            this.compositeDisposable = new CompositeDisposable();
        }
        this.compositeDisposable.add(disposable);
    }

    private void dispose() {
        if (this.compositeDisposable != null &&!compositeDisposable.isDisposed()){
            this.compositeDisposable.dispose();
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        dispose();
    }
}
