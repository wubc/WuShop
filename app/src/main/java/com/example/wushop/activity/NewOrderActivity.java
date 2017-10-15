package com.example.wushop.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.wushop.MyApplication;
import com.example.wushop.R;
import com.example.wushop.adapter.OrderItemAdapter;
import com.example.wushop.adapter.WareOrderAdapter;
import com.example.wushop.adapter.layoutmanager.FullyLinearLayoutManager;
import com.example.wushop.bean.Address;
import com.example.wushop.bean.Charge;
import com.example.wushop.bean.Order;
import com.example.wushop.bean.OrderItem;
import com.example.wushop.bean.ShoppingCart;
import com.example.wushop.bean.User;
import com.example.wushop.msg.CreateOrderRespMsg;
import com.example.wushop.net.ServiceGenerator;
import com.example.wushop.net.SubscriberCallBack;
import com.example.wushop.utils.CartOperator;
import com.example.wushop.utils.JSONUtil;
import com.example.wushop.utils.ToastUtils;
import com.example.wushop.widget.Constants;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.pingplusplus.android.PaymentActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 创建订单
 */

public class NewOrderActivity extends BaseActivity implements View.OnClickListener{
    //银联支付
    private static final String CHANNEL_UPACP = "upcap";
    //微信支付
    private static final String CHANNEL_WECHAT = "wx";
    //支付宝支付
    private static final String CHANNEL_ALIPAY = "alipay";
    //百度支付渠道
    private static final String CHANNEL_BFB = "bfb";
    //京东支付渠道
    private static final String CHANNEL_JDPAY_WAP = "jdpay_wap";

    @ViewInject(R.id.tv_name)
    private TextView mTvName;

    @ViewInject(R.id.tv_addr)
    private TextView mTvAddr;

    @ViewInject(R.id.img_add)
    private ImageView mImgAdd;

    @ViewInject(R.id.recycle_view)
    private RecyclerView mRecyclerView;

    @ViewInject(R.id.tv_order)
    private TextView mTvOrderList;

    @ViewInject(R.id.tv_total)
    private TextView mTvTotal;

    @ViewInject(R.id.btn_createOrder)
    private Button mBtnOrder;

    @ViewInject(R.id.rl_alipay)
    private RelativeLayout mRLAlipay;

    @ViewInject(R.id.rl_wechat)
    private RelativeLayout mRLWechat;

    @ViewInject(R.id.rl_bd)
    private RelativeLayout mRLBaidu;

    @ViewInject(R.id.rl_addr)
    private RelativeLayout mRLAddr;

    @ViewInject(R.id.rb_alipay)
    private RadioButton mRbAlipay;

    @ViewInject(R.id.rb_wechat)
    private RadioButton mRbWechat;

    @ViewInject(R.id.rb_bd)
    private RadioButton mRbBaidu;

    private HashMap<String, RadioButton> channels = new HashMap<>(3);
    private WareOrderAdapter wareOrderAdapter;
    private OrderItemAdapter orderItemAdapter;

    private String payChannel = CHANNEL_ALIPAY;//默认途径为支付宝

    private float amount;
    private String orderNum;
    private int SIGN;

    @Override
    public void init() {

        //显示订单数据
        showOrder();
        //地址更换
        mImgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NewOrderActivity.this, AddressListActivity.class);
                startActivityForResult(intent, Constants.REQUEST_CODE);
            }
        });
        //初始化地址
        initAddress();

        //初始化支付渠道
        initPayChannels();
    }

    private void initPayChannels() {
        //保存RadioButton
        channels.put(CHANNEL_ALIPAY, mRbAlipay);
        channels.put(CHANNEL_WECHAT, mRbWechat);
        channels.put(CHANNEL_BFB, mRbBaidu);

        mRbAlipay.setOnClickListener(this);
        mRbWechat.setOnClickListener(this);
        mRbBaidu.setOnClickListener(this);

        if (SIGN == Constants.CART){
            amount = wareOrderAdapter.getTotalPrice();
        }else if (SIGN == Constants.ORDER){
            amount = orderItemAdapter.getTotalPrice();
        }

        mTvTotal.setText("应付款：￥" + amount);
    }

    //从服务器端获取地址，每次打开页面就实时更新
    private void initAddress() {
        String userId = MyApplication.getmApplication().getUser().getId() + "";
        if (!TextUtils.isEmpty(userId)){

            ServiceGenerator.getRetrofit(this)
                    .getAddrList(Long.parseLong(userId), MyApplication.getmApplication().getToken())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new SubscriberCallBack<List<Address>>(this, true) {
                        @Override
                        public void onSuccess(List<Address> result) {
                            showAddress(result);
                        }
                    });
        }else {
            ToastUtils.show(this, "加载错误...");
        }

    }

    //显示默认地址
    private void showAddress(List<Address> result) {
        //购物车页面传递的数据显示地址
        if (SIGN == Constants.CART){
            for (Address address : result){
                if (address.getIsDefault()){
                    mTvName.setText(address.getConsignee() + "(" + address.getPhone() + ")");
                    mTvAddr.setText(address.getAddr());
                }
            }
            //我的订单页面显示地址
        }else if (SIGN == Constants.ORDER){
            Address addressOrder = (Address) getIntent().getSerializableExtra("address");

            if (addressOrder != null){
                mTvName.setText(addressOrder.getConsignee() + "(" + addressOrder.getPhone() + ")");
                mTvAddr.setText(addressOrder.getAddr());
            }else {//该页面跳转到地址列表界面又返回时显示默认地址
                for (Address address : result) {
                    if (address.getIsDefault()) {
                        mTvName.setText(address.getConsignee() + "(" + address.getPhone() + ")");
                        mTvAddr.setText(address.getAddr());
                    }
                }
            }
        }
    }

    //显示订单数据
    private void showOrder() {
        SIGN = getIntent().getIntExtra("sign",-1);

        //购物车商品数据
        if (SIGN == Constants.CART){
            List<ShoppingCart> carts = (List<ShoppingCart>) getIntent().getSerializableExtra("carts");
            wareOrderAdapter  = new WareOrderAdapter(this, carts);
            FullyLinearLayoutManager layoutManager = new FullyLinearLayoutManager(this);
            layoutManager.setOrientation(GridLayoutManager.HORIZONTAL);
            mRecyclerView.setAdapter(wareOrderAdapter);
            mRecyclerView.setLayoutManager(layoutManager);

            //我的订单再次购买按钮点击商品显示
        }else if (SIGN == Constants.ORDER){
            List<OrderItem> orderItems = (List<OrderItem>) getIntent().getSerializableExtra("order");
            orderItemAdapter = new OrderItemAdapter(this, orderItems);
            FullyLinearLayoutManager layoutManager = new FullyLinearLayoutManager(this);
            layoutManager.setOrientation(GridLayoutManager.HORIZONTAL);
            mRecyclerView.setLayoutManager(layoutManager);
            mRecyclerView.setAdapter(orderItemAdapter);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_new_order;
    }

    @Override
    public void setToolbar() {
        getToolbar().setTitle("订单确认");
        getToolbar().setleftButtonIcon(R.drawable.icon_back_32px);
    }

    @Override
    public void onClick(View view) {
        selectPayChannel(view.getTag().toString());
    }

    //提交订单
    public void postNewOrder(View view){
        List<WareItem> items = new ArrayList<>();

        //判断购物车订单还是再次购买订单
        if (SIGN == Constants.CART){
            postOrderByCart(items);
        }else if (SIGN == Constants.ORDER){
            postOrderByMyOrder(items);
        }

    }

    private void postOrderByCart(List<WareItem> items) {
        final List<ShoppingCart> carts = wareOrderAdapter.getDatas();
        for (ShoppingCart cart : carts){
            WareItem item = new WareItem(cart.getId(), (int) Float.parseFloat(cart.getPrice()));
            items.add(item);
        }
        String item_json = JSONUtil.toJson(items);

        String userId = MyApplication.getmApplication().getUser().getId() + "";

        ServiceGenerator.getRetrofit(this)
                .orderCreate(Long.parseLong(userId), item_json, (int) amount, 1, payChannel, MyApplication.getmApplication().getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SubscriberCallBack<CreateOrderRespMsg>(this, true) {
                    @Override
                    public void onSuccess(CreateOrderRespMsg result) {
                        mBtnOrder.setEnabled(true);

                        orderNum = result.getData().getOrderNum();

                        Charge charge = result.getData().getCharge();

                        openPaymentActivity(JSONUtil.toJson(charge));

                        //清空购物车已购买商品
                        if (SIGN == Constants.CART){
                            CartOperator cartOperator = CartOperator.getInstance(NewOrderActivity.this);
                            cartOperator.delete(carts);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mBtnOrder.setEnabled(true);
                    }

                });
    }

    //订单再次购买
    private void postOrderByMyOrder(List<WareItem> items) {
        List<OrderItem> orderItems = orderItemAdapter.getDatas();
        for (OrderItem orderItem : orderItems) {
            WareItem item = new WareItem(orderItem.getWares().getId(),
                    (int) Float.parseFloat(orderItem.getWares().getPrice()));
            items.add(item);
        }
        String item_json = JSONUtil.toJson(items);

        String userId = MyApplication.getmApplication().getUser().getId() + "";

        ServiceGenerator.getRetrofit(this)
                .orderCreate(Long.parseLong(userId), item_json, (int) amount, 1, payChannel, MyApplication.getmApplication().getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SubscriberCallBack<CreateOrderRespMsg>(this, true) {
                    @Override
                    public void onSuccess(CreateOrderRespMsg result) {
                        mBtnOrder.setEnabled(true);

                        orderNum = result.getData().getOrderNum();

                        Charge charge = result.getData().getCharge();

                        //打开支付页面
                        openPaymentActivity(JSONUtil.toJson(charge));

                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mBtnOrder.setEnabled(true);
                    }
                });

    }

    //显示模拟支付界面
    private void openPaymentActivity(String charge){
        Intent intent = new Intent();
        String packageName = getPackageName();
        ComponentName componentName = new ComponentName(packageName, packageName + ".wxapi.WXPayEntryActivity");
        intent.setComponent(componentName);
        intent.putExtra(PaymentActivity.EXTRA_CHARGE, charge);
        startActivityForResult(intent, Constants.REQUEST_CODE_PAYMENT);
    }

    //支付结果返回以及地址信息结果返回
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //显示默认地址
        showAddress();
        //支付结果返回
        payResult(requestCode, resultCode, data);
    }

    private void payResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_CODE_PAYMENT){
            if (resultCode == Activity.RESULT_OK){
                String result = data.getStringExtra("pay_result");
                if (result.equals("success")) {
                    changeOrderStatus(Constants.SUCCESS);
                } else if (result.equals("fail")) {
                    changeOrderStatus(Constants.FAIL);
                } else if (result.equals("cancel")) {
                    changeOrderStatus(Constants.CANCEL);
                } else {
                    changeOrderStatus(Constants.INVALID);
                }
            }
        }
    }

    private void changeOrderStatus(final int status) {
        Map<String, String> params = new HashMap<>(5);
        params.put("order_num", orderNum);
        params.put("status", status + "");

        ServiceGenerator.getRetrofit(this)
                .orderComplete(orderNum, status, MyApplication.getmApplication().getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SubscriberCallBack<CreateOrderRespMsg>(this, false) {
                    @Override
                    public void onSuccess(CreateOrderRespMsg result) {
                        //跳转到支付结果界面
                        toPayResultActivity(status);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);

                        //跳转到支付失败界面
                        toPayResultActivity(Constants.FAIL);
                    }
                });

    }

    private void toPayResultActivity(int status) {
        Intent intent = new Intent(this, PayResultActivity.class);
        intent.putExtra("status", status);
        startActivity(intent);
    }

    //选择支付渠道以及RadioButton单选
    private void selectPayChannel(String payChannel){

        for (Map.Entry<String, RadioButton> entry : channels.entrySet()){
            this.payChannel = payChannel;
            RadioButton radioButton = entry.getValue();

            if (entry.getKey().equals(payChannel)){
                //判断是否被选中
                boolean isChecked = radioButton.isChecked();
               if (!isChecked) {
                   radioButton.setChecked(true);
               }

            }else {
                //其他的都改为未选中
                radioButton.setChecked(false);
            }

        }

    }

    //显示默认地址
    private void showAddress(){
        //请求服务端获取地址
        String userId = MyApplication.getmApplication().getUser().getId() + "";

        if (!TextUtils.isEmpty(userId)){
            ServiceGenerator.getRetrofit(this)
                    .getAddrList(Long.parseLong(userId), MyApplication.getmApplication().getToken())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new SubscriberCallBack<List<Address>>(this, true) {
                        @Override
                        public void onSuccess(List<Address> result) {
                            for (Address address : result){
                                if (address.getIsDefault()){
                                    mTvName.setText(address.getConsignee());
                                    mTvAddr.setText(address.getAddr());
                                }
                            }
                        }
                    });
        }else {
            ToastUtils.show(this, "加载错误...");
        }
    }

    /**
     * 商品id和价格显示适配器
     */
    class WareItem {
        private Long ware_id;
        private int amount;

        public WareItem(Long ware_id, int amount) {
            this.ware_id = ware_id;
            this.amount = amount;
        }

        public Long getWare_id() {
            return ware_id;
        }

        public void setWare_id(Long ware_id) {
            this.ware_id = ware_id;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }
    }
}
