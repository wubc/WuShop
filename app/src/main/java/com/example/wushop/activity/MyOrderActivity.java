package com.example.wushop.activity;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.example.wushop.MyApplication;
import com.example.wushop.R;
import com.example.wushop.adapter.BaseAdapter;
import com.example.wushop.adapter.MyOrderAdapter;
import com.example.wushop.adapter.decoration.CardViewtemDecortion;
import com.example.wushop.bean.Order;
import com.example.wushop.net.ServiceGenerator;
import com.example.wushop.net.SubscriberCallBack;
import com.example.wushop.utils.ToastUtils;
import com.example.wushop.widget.Constants;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.io.Serializable;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 我的订单
 */

public class MyOrderActivity extends BaseActivity implements TabLayout.OnTabSelectedListener{

    public static final int STATUS_ALL = 1000;
    public static final int STATUS_SUCCESS = 1; //支付成功的订单
    public static final int STATUS_PAY_FAIL = -2; //支付失败的订单
    public static final int STATUS_PAY_WAIT = 0; //：待支付的订单
    private int status = STATUS_ALL;

    @ViewInject(R.id.tab_layout)
    private TabLayout mTabLayout;

    @ViewInject(R.id.recycle_view)
    private RecyclerView mRecyclerView;

    private MyOrderAdapter orderAdapter;

    @Override
    public void init() {

        //初始化tab
        initTab();
        //获取订单数据
        getOrders();
    }

    //获取订单数据
    private void getOrders() {
        String userId = MyApplication.getmApplication().getUser().getId() + "";
        if (!TextUtils.isEmpty(userId)){
            ServiceGenerator.getRetrofit(this)
                    .orderList(Long.parseLong(userId), status, MyApplication.getmApplication().getToken())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new SubscriberCallBack<List<Order>>(this, true) {
                        @Override
                        public void onSuccess(List<Order> result) {
                            showOrders(result);
                        }
                    });
        }
    }

    private void showOrders(List<Order> result) {
        if (orderAdapter == null){
            orderAdapter = new MyOrderAdapter(MyOrderActivity.this, result, new MyOrderAdapter.OnItemWaresClickListener() {
                @Override
                public void onItemWaresClickListener(View view, Order order) {
                    /**
                     * 再次购买按钮 点击事件，跳转到支付页面
                     * 将商品和地址以及总金额传入
                     */
                    Intent intent = new Intent(MyOrderActivity.this, NewOrderActivity.class);
                    intent.putExtra("order", (Serializable) order.getItems());
                    intent.putExtra("sign", Constants.ORDER);

                    intent.putExtra("price", order.getAmount());
                    startActivity(intent, true);
                }
            });

            mRecyclerView.setAdapter(orderAdapter);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            mRecyclerView.addItemDecoration(new CardViewtemDecortion());

            orderAdapter.setOnItemClickListenner(new BaseAdapter.OnItemClickListenner() {
                @Override
                public void onItemClick(View view, int position) {
                    ToastUtils.show(MyOrderActivity.this, "功能正在完善...");
//                    toDetailActivity(position);
                }
            });
        } else {
            orderAdapter.refrenshData(result);
            mRecyclerView.setAdapter(orderAdapter);
        }

    }

    //初始化tab
    private void initTab() {
        TabLayout.Tab tab = mTabLayout.newTab();
        tab.setText("全部");
        tab.setTag(STATUS_ALL);
        mTabLayout.addTab(tab);

        tab = mTabLayout.newTab();
        tab.setText("支付成功");
        tab.setTag(STATUS_SUCCESS);
        mTabLayout.addTab(tab);

        tab = mTabLayout.newTab();
        tab.setText("待支付");
        tab.setTag(STATUS_PAY_WAIT);
        mTabLayout.addTab(tab);

        tab = mTabLayout.newTab();
        tab.setText("支付失败");
        tab.setTag(STATUS_PAY_FAIL);
        mTabLayout.addTab(tab);

        mTabLayout.addOnTabSelectedListener(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_my_order;
    }

    @Override
    public void setToolbar() {
        getToolbar().setleftButtonIcon(R.drawable.icon_back_32px);
        getToolbar().setTitle("我的订单");

    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        status = (int) tab.getTag();
        getOrders();
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
