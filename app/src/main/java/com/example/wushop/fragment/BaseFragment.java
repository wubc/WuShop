package com.example.wushop.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.wushop.MyApplication;
import com.example.wushop.R;
import com.example.wushop.activity.LoginActivity;
import com.example.wushop.bean.User;
import com.example.wushop.widget.ShopToolbar;
import com.lidroid.xutils.ViewUtils;

/**
 * Created by Administrator on 2017/9/15.
 */

public abstract class BaseFragment extends Fragment {

    private View mView;

    private void initToolbar(){
        if (getToolbar() != null){
            setToolbar();
        }
    }

    public abstract void setToolbar();

    public ShopToolbar getToolbar(){
        return (ShopToolbar) mView.findViewById(R.id.shop_Toolbar);
    }

    //布局id
    protected abstract int getLayoutId();

    public abstract void init();

    public void startActivity(Intent intent, boolean isNeedLogin){
        if (isNeedLogin){
            User user = MyApplication.getmApplication().getUser();

            if (user != null){
                startActivity(intent);
            }else {
                MyApplication.getmApplication().putIntent(intent);
                Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
                super.startActivity(loginIntent);
            }

        }else {
            startActivity(intent);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (getLayoutId() != 0) {
            mView = inflater.inflate(getLayoutId(), container, false);
        }
        ViewUtils.inject(this,mView);

        initToolbar();

        init();

        return mView;
    }
}
