package com.example.wushop.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.example.wushop.MyApplication;
import com.example.wushop.R;
import com.example.wushop.adapter.AddressAdapter;
import com.example.wushop.adapter.decoration.DividerItemDecortion;
import com.example.wushop.bean.Address;
import com.example.wushop.msg.BaseResMsg;
import com.example.wushop.net.ServiceGenerator;
import com.example.wushop.net.SubscriberCallBack;
import com.example.wushop.utils.ToastUtils;
import com.example.wushop.widget.Constants;
import com.example.wushop.widget.CustomDialog;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.Collections;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 地址列表
 */

public class AddressListActivity extends BaseActivity {
    
    @ViewInject(R.id.recycler_view)
    private RecyclerView mRecyclerView;
    
    private AddressAdapter mAddressAdapter;
    private CustomDialog mCustomDialog;
    
    @Override
    public void init() {

        initAddress();
    }

    //展示地址页面
    private void initAddress() {
        //判断用户是否登录
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

    private void showAddress(List<Address> result) {
        Collections.sort(result);

        if (mAddressAdapter == null){

            mAddressAdapter = new AddressAdapter(this, result, new AddressAdapter.AddressListener() {
                @Override
                public void setDefault(Address address) {
                    setResult(RESULT_OK);
                    updateAddress(address);
                }

                @Override
                public void onClickEdit(Address address) {
                    editAddress(address);
                }

                @Override
                public void onClickDelete(Address address) {
                    showDialog(address);
                    mCustomDialog.show();
                }
            });

            mRecyclerView.setAdapter(mAddressAdapter);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            mRecyclerView.addItemDecoration(new DividerItemDecortion(this, DividerItemDecortion.VERTICAL_LIST));

        }else {
            mAddressAdapter.refrenshData(result);
            mRecyclerView.setAdapter(mAddressAdapter);
        }
    }

    private void showDialog(final Address address) {
        CustomDialog.Builder builder = new CustomDialog.Builder(this);
        builder.setTitle("友情提示");
        builder.setMessage("您确定删除该地址吗？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface anInterface, int i) {
                deleteAddress(address);
                initAddress();

                if (mCustomDialog.isShowing()){
                    mCustomDialog.dismiss();
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface anInterface, int i) {
                if (mCustomDialog.isShowing()){
                    mCustomDialog.dismiss();
                }

            }
        });

        mCustomDialog = builder.create();
        mCustomDialog.show();
    }

    //删除地址
    public void deleteAddress(Address address) {
        ServiceGenerator.getRetrofit(this)
                .deleteAddr(address.getId(), MyApplication.getmApplication().getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SubscriberCallBack<BaseResMsg>(this, false) {
                    @Override
                    public void onSuccess(BaseResMsg result) {
                        if (result.getStatus() == result.STATUS_SUCCESS){
                            setResult(RESULT_OK);
                            if (mCustomDialog.isShowing()){
                                mCustomDialog.dismiss();
                            }
                        }
                    }
                });
    }

    //编辑地址
    public void editAddress(Address address) {
        Intent intent = new Intent(this, AddressAddActivity.class);
        intent.putExtra("tag", Constants.TAG_COMPLETE);
        intent.putExtra("addressBean", address);

        startActivityForResult(intent,Constants.ADDRESS_EDIT);
    }

    //更新地址，主要是更改address的默认勾选状态
    public  void updateAddress(Address address) {
        ServiceGenerator.getRetrofit(this)
                .updateAddr(address.getId(), address.getConsignee(),
                        address.getPhone(), address.getAddr(),
                        address.getZip_code(), address.getIsDefault(), MyApplication.getmApplication().getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SubscriberCallBack<BaseResMsg>(this, false) {
                    @Override
                    public void onSuccess(BaseResMsg result) {
                        if (result.getStatus() == result.STATUS_SUCCESS){
                            //从服务端更新数据
                            initAddress();
                        }

                    }
                });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_address_list;
    }

    @Override
    public void setToolbar() {
        getToolbar().setTitle("我的地址");
        getToolbar().setleftButtonIcon(R.drawable.icon_back_32px);
        getToolbar().setRightImgButtonIcon(R.drawable.icon_add_w);
        getToolbar().setRightButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toAddActivity();
            }
        });
    }

    /**
     * 跳转到添加地址页面
     * 点击右上角添加按钮，传入TAG_SAVE,更改添加地址页面toolbar显示
     */
    private void toAddActivity() {
        Intent intent = new Intent(this, AddressAddActivity.class);
        intent.putExtra("tag", Constants.TAG_SAVE);
        startActivityForResult(intent, Constants.ADDRESS_ADD);
    }

    //跳转AddressAddActivity页面结果处理
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        initAddress();
    }
}
