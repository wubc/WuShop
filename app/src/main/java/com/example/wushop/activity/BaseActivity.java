package com.example.wushop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.wushop.MyApplication;
import com.example.wushop.R;
import com.example.wushop.bean.User;
import com.example.wushop.widget.ShopToolbar;
import com.lidroid.xutils.ViewUtils;

/**
 * BaseActivity封装
 */

public abstract class BaseActivity extends AppCompatActivity{

    private void initToolbar(){
        if (getToolbar() != null ){
            setToolbar();
            getToolbar().setLeftButtonOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });

        }
    }

    public ShopToolbar getToolbar() {
        return (ShopToolbar) findViewById(R.id.activity_toolbar);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        ViewUtils.inject(this);

        initToolbar();

        init();
    }

    public abstract void init();

    public abstract int getLayoutId();

    public abstract void setToolbar();

    public void startActivity(Intent intent, boolean isNeedLogin) {

        if (isNeedLogin) {
            User user = MyApplication.getmApplication().getUser();

            if (user != null) {
                super.startActivity(intent);
            } else {
                MyApplication.getmApplication().putIntent(intent);
                Intent loginIntent = new Intent(this, LoginActivity.class);
                super.startActivity(loginIntent);
            }
        } else {
            super.startActivity(intent);
        }
    }


}
