package com.example.wushop.widget;

import android.os.CountDownTimer;
import android.widget.TextView;

import com.example.wushop.R;

/**
 * 倒计时
 */

public class CountTimerView extends CountDownTimer {

    public static  final int COUNT_TIME = 61000;
    private TextView btn;//显示剩余秒数的view
    private int endMill;////剩余秒数

    //@param millisInFuture    倒计时总时间
    //@param countDownInterval 渐变时间
    public CountTimerView(long millisInFuture, long countDownInterval, TextView btn, int endStr) {
        super(millisInFuture, countDownInterval);
        this.btn = btn;
        this.endMill = endStr;
    }

    public CountTimerView(TextView textView, int endMill){
        super(COUNT_TIME, 1000);
        this.btn = textView;
        this.endMill = endMill;
    }

    public CountTimerView(TextView btn){
        super(COUNT_TIME, 1000);
        this.btn = btn;
        this.endMill = R.string.smssdk_resend_identify_code;
    }

    //计时过程
    @Override
    public void onTick(long l) {
        btn.setEnabled(false);
        btn.setText(l/1000 + "秒后可以重新发送");
    }

    //倒计时完毕
    @Override
    public void onFinish() {
        btn.setEnabled(true);
        btn.setText(endMill);
    }
}
