package com.example.wushop.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.wushop.R;
import com.example.wushop.utils.ManifestUtil;
import com.example.wushop.utils.ToastUtils;
import com.example.wushop.widget.ClearEditText;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.mob.MobSDK;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.smssdk.DefaultOnSendMessageHandler;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.utils.SMSLog;

/**
 * 注册
 */

public class RegisterActivity extends BaseActivity {

    private static final String DEFAULT_COUNTRY_ID = "42";

    @ViewInject(R.id.et_phone)
    private ClearEditText et_phone;

    @ViewInject(R.id.et_pwd)
    private ClearEditText et_password;

    @ViewInject(R.id.tv_Country)
    private TextView mTvCountry;

    @ViewInject(R.id.tv_country_code)
    private TextView mTvCountryCode;

    private SMSEventHandler mEventHandler;
    private String Tag = "RegisterActivity";

    @Override
    public void init() {
        MobSDK.init(this, ManifestUtil.getMetaDataValue(this, "mob_sms_appKey")
                , ManifestUtil.getMetaDataValue(this, "mob_sms_appSectret"));
        mEventHandler = new SMSEventHandler();
        cn.smssdk.SMSSDK.registerEventHandler(mEventHandler);//注册回调接口

        //获取国家代码,[国名，区号，ID]
        String[] country = cn.smssdk.SMSSDK.getCountry(DEFAULT_COUNTRY_ID);
        if (country != null) {
            mTvCountry.setText(country[0]);
            mTvCountryCode.setText("+" + country[1]);
        }

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    public void setToolbar() {
        getToolbar().setTitle("用户注册(1/2)");
        getToolbar().setleftButtonIcon(R.drawable.icon_back_32px);
        getToolbar().setRightButtonText("下一步");
        getToolbar().setRightButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCode();
            }
        });
    }

    //获取验证码
    private void getCode() {
        String phoneNumber = et_phone.getText().toString().trim().replaceAll("\\s*", "");
        String countryCode = mTvCountryCode.getText().toString().trim();

        checkPhoneNum(phoneNumber, countryCode);

        //请求验证码，如果请求成功，则在EventHandler里面回调并跳转到下一个注册页面
        cn.smssdk.SMSSDK.getVerificationCode(countryCode, phoneNumber);
    }

    //验证手机号码合法性,空、长度、格式判断
    private void checkPhoneNum(String number, String code) {
        if (code.startsWith("+")) {
            code = code.substring(1);
        }

        if (TextUtils.isEmpty(number)) {
            ToastUtils.show(this, "请输入手机号码");
            return;
        }

        if (code == "86") {
            if (number.length() != 11) {
                ToastUtils.show(this, "手机号码长度不正确");
                return;
            }
        }

        String rule = "^1(3|5|7|8|4)\\d{9}";
        Pattern p = Pattern.compile(rule);
        Matcher matcher = p.matcher(number);

        if (!matcher.matches()) {
            ToastUtils.show(this, "您输入的手机号码格式不正确");
            return;
        }
    }

    class SMSEventHandler extends EventHandler {
        //event事件类型，
        //result操作结果，操作结果，为SMSSDK.RESULT_COMPLETE表示成功，为SMSSDK.RESULT_ERROR表示失败
        //data操作结果，如果result=SMSSDK.RESULT_ERROR，则类型为Throwable，如果result=SMSSDK.RESULT_COMPLETE需根据event判断
        @Override
        public void afterEvent(final int event, final int result, final Object data) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (result == cn.smssdk.SMSSDK.RESULT_COMPLETE) {//事件执行成功

                        if (event == cn.smssdk.SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {//返回支持的国家代码

                            onCountryListGot((ArrayList<HashMap<String, Object>>) data);

                        }else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
                            //请求验证后，跳转到验证码填写界面
                            afterVerificationCodeRequested((Boolean) data);
                        }else if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {

                        }
                    }else{
                        //根据服务器返回的网络错误，给toast提示
                        try {
                            ((Throwable) data).printStackTrace();
                            Throwable throwable = (Throwable) data;

                            JSONObject object = null;
                            object = new JSONObject(
                                    throwable.getMessage());
                            String des = object.optString("detail");
                            if (!TextUtils.isEmpty(des)) {
                                ToastUtils.show(RegisterActivity.this, des);
                                return;
                            }
                        } catch (Exception e) {
                            SMSLog.getInstance().w(e);
                        }
                    }

                }
            });

        }
    }

    /**
     * 传入国家代码、电话号码、密码并请求验证码，跳转到验证码填写界面
     * @param data
     */
    private void afterVerificationCodeRequested(Boolean data) {
        Log.e("reg",et_password.getText().toString());
        String phone = et_phone.getText().toString().trim().replaceAll("\\s*", "");
        String psd = et_password.getText().toString().trim();
        String countryCode = mTvCountryCode.getText().toString().trim();

        if (et_password.getText().toString().length() < 6 || et_password.getText().toString().length() > 20) {
            ToastUtils.show(this, "密码长度必须大于6位小于20位");
            return;
        }

        if (countryCode.startsWith("+")) {
            countryCode = countryCode.substring(1);
        }

        Intent intent = new Intent(RegisterActivity.this,Register2Activity.class);
        intent.putExtra("phone",phone);
        intent.putExtra("password",psd);
        intent.putExtra("countryCode",countryCode);
        startActivityForResult(intent,1);
        setResult(2);
    }

    //获取国家代码
    private void onCountryListGot(ArrayList<HashMap<String, Object>> countries) {
        for(HashMap<String, Object> country:countries){
            String code = (String) country.get("zone");
            String rule = (String) country.get("rule");

            if (TextUtils.isEmpty(code) || TextUtils.isEmpty(rule)){
                continue;
            }
            Log.d(Tag, "code=" + code + ",rule=" + rule);

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(mEventHandler);
    }
}
