package com.example.wushop.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.slider.library.SliderLayout;
import com.example.wushop.R;
import com.example.wushop.activity.WaresListActivity;
import com.example.wushop.adapter.HomeWaresAdapter;
import com.example.wushop.bean.Banner;
import com.example.wushop.bean.Goods;
import com.example.wushop.bean.HomeWares;
import com.example.wushop.net.ServiceGenerator;
import com.example.wushop.net.SubscriberCallBack;
import com.example.wushop.widget.Constants;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 主页
 */

public class HomeFragment extends BaseFragment implements Thread.UncaughtExceptionHandler{

    @ViewInject(R.id.slider)
    private SliderLayout mSliderLayout;

    @ViewInject(R.id.recyclerView)
    private RecyclerView mRecyclerView;

    private HomeWaresAdapter mHomeWaresAdapter;


    @Override
    public void setToolbar() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    public void init() {

        initAdapter();
        ServiceGenerator.getRetrofit(getActivity())
                .getBanner(1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SubscriberCallBack<List<Banner>>(getActivity(),false) {
                    @Override
                    public void onSuccess(List<Banner> result) {
                        Log.e("onSuccess",""+result.size());
                        mHomeWaresAdapter.setBanner(result);

                    }
                });

        ServiceGenerator.getRetrofit(getActivity())
                .getHome("")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SubscriberCallBack<List<HomeWares>>(getActivity(),true) {
                    @Override
                    public void onSuccess(List<HomeWares> result) {
                        mHomeWaresAdapter.setWares(result);

                    }
                });
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
//        Thread.setDefaultUncaughtExceptionHandler(this);
//        View view = inflater.inflate(R.layout.fragment_home,container,false);
//        ViewUtils.inject(this,view);
//        initAdapter();
//        ServiceGenerator.getRetrofit(getActivity())
//                .getBanner(1)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new SubscriberCallBack<List<Banner>>(getActivity(),false) {
//                    @Override
//                    public void onSuccess(List<Banner> result) {
//                        Log.e("onSuccess",""+result.size());
//                        mHomeWaresAdapter.setBanner(result);
//
//                    }
//                });
//
//        ServiceGenerator.getRetrofit(getActivity())
//                .getHome("")
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new SubscriberCallBack<List<HomeWares>>(getActivity(),true) {
//                    @Override
//                    public void onSuccess(List<HomeWares> result) {
//                        mHomeWaresAdapter.setWares(result);
//
//                    }
//                });
//
//
//        return view;
//
//    }

    private void initAdapter() {

        mHomeWaresAdapter = new HomeWaresAdapter(getContext());

        mHomeWaresAdapter.setOnGoodsClickListener(new HomeWaresAdapter.GoodsClickListener() {
            @Override
            public void setOnClick(View view, Goods goods) {
                Intent intent = new Intent(getActivity(),WaresListActivity.class);
                intent.putExtra(Constants.GOODS_ID,goods.getId());
                startActivity(intent);
            }
        });

        mRecyclerView.setAdapter(mHomeWaresAdapter);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSliderLayout.stopAutoCycle();
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        //在此处理异常， arg1即为捕获到的异常
        Log.i("AAA", "uncaughtException   " + throwable);
    }
}
