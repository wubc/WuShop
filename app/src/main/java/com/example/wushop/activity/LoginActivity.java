package com.example.wushop.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.wushop.MyApplication;
import com.example.wushop.R;
import com.example.wushop.bean.User;
import com.example.wushop.msg.LoginRespMsg;
import com.example.wushop.net.ServiceGenerator;
import com.example.wushop.net.SubscriberCallBack;
import com.example.wushop.utils.DESUtil;
import com.example.wushop.utils.ToastUtils;
import com.example.wushop.widget.ClearEditText;
import com.example.wushop.widget.Constants;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 登录
 */

public class LoginActivity extends BaseActivity{

    @ViewInject(R.id.et_phonenumber)
    private ClearEditText mClearEtPhone;

    @ViewInject(R.id.et_password)
    private ClearEditText mClearEtPwd;

    @ViewInject(R.id.btn_login)
    private Button mBtnLogin;

    @ViewInject(R.id.tv_register)
    private TextView mTvReg;

    @ViewInject(R.id.tv_forget_pwd)
    private TextView mTvForget;

    @Override
    public void init() {


    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void setToolbar() {

        getToolbar().setTitle("用户登录");
    }

    //跳转到注册界面
    @OnClick(R.id.tv_register)
    public void register(View view){
        Intent intent = new Intent(this,RegisterActivity.class);
        startActivityForResult(intent,1);
    }

    //登录
    @OnClick(R.id.btn_login)
    public void login(View view){
        String phone = mClearEtPhone.getText().toString().trim();
        String pwd = mClearEtPwd.getText().toString().trim();

        if (TextUtils.isEmpty(phone)) {
            ToastUtils.show(this, "请输入手机号码");
            return;
        }

        if (TextUtils.isEmpty(pwd)) {
            ToastUtils.show(this, "请输入登录密码");
            return;
        }

        ServiceGenerator.getRetrofit(this)
                .login(phone, DESUtil.enCode(Constants.DES_KEY, pwd))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SubscriberCallBack<LoginRespMsg<User>>(this,false) {
                    @Override
                    public void onSuccess(LoginRespMsg<User> result) {
                        MyApplication myApplication = MyApplication.getmApplication();
                        myApplication.putUser(result.getData(), result.getToken());

                        //根据登录意图判断是否登录
                        if ( result.getData() != null && result.getToken() != null){
                            setResult(Constants.REQUEST_CODE);

                            ToastUtils.show(LoginActivity.this, "登录成功");

                            finish();
                        }else {
                            ToastUtils.show(LoginActivity.this, "登录失败");

                        }

                    }
                });
    }
}
