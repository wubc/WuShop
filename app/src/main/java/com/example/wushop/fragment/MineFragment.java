package com.example.wushop.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.wushop.MyApplication;
import com.example.wushop.R;
import com.example.wushop.activity.AddressListActivity;
import com.example.wushop.activity.LoginActivity;
import com.example.wushop.activity.MyFavoriteActivity;
import com.example.wushop.activity.MyOrderActivity;
import com.example.wushop.bean.User;
import com.example.wushop.utils.ToastUtils;
import com.example.wushop.widget.Constants;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 我的
 */

public class MineFragment extends Fragment {

    @ViewInject(R.id.profile_image)
    private CircleImageView mCircleImageView;

    @ViewInject(R.id.tv_username)
    private TextView tv_username;

    @ViewInject(R.id.tv_my_order)
    private TextView tv_order;

    @ViewInject(R.id.tv_favorite)
    private TextView tv_favorite;

    @ViewInject(R.id.tv_addr)
    private TextView tv_add;

    @ViewInject(R.id.btn_loginOut)
    private Button btn_loginOut;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine,container,false);
        ViewUtils.inject(this,view);

        initUser();
        return view;

    }
    //刚进入我的页面就要初始化用户数据
    private void initUser(){
        User user = MyApplication.getmApplication().getUser();
        showUser(user);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        initUser();

    }

    @OnClick(value = {R.id.tv_username,R.id.profile_image})
    public void toLogin(View view){
        //判断是否已登录，若是，则提示，否则，跳转
        User user = MyApplication.getmApplication().getUser();
        if (user != null) {
            ToastUtils.show(getContext(), "您已登录");
            mCircleImageView.setClickable(false);
            tv_username.setClickable(false);
        }else {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivityForResult(intent, Constants.REQUEST_CODE);
        }

    }

    @OnClick(R.id.tv_my_order)
    public void showMyOrder(View view){
        Intent intent = new Intent(getActivity(), MyOrderActivity.class);
        startActivity(intent, true);
    }

    @OnClick(R.id.tv_favorite)
    public void showMyFavorite(View view){
        Intent intent = new Intent(getActivity(), MyFavoriteActivity.class);
        startActivity(intent, true);
    }


    @OnClick(R.id.tv_addr)
    public void showMyAddr(View view){
        Intent intent = new Intent(getActivity(), AddressListActivity.class);
        startActivity(intent, true);
    }

    @OnClick(R.id.btn_loginOut)
    public void loginOut(View view){
        MyApplication.getmApplication().clearUser();
        showUser(null);
    }

    //显示用户数据
    private void showUser(User user) {
        if(user != null){
            tv_username.setText(user.getUsername());
            if (!TextUtils.isEmpty(user.getLogo_url())){
                Picasso.with(getActivity()).load(user.getLogo_url()).into(mCircleImageView);
            }
            btn_loginOut.setVisibility(View.VISIBLE);
        }else {
            tv_username.setText(R.string.to_login);
            btn_loginOut.setVisibility(View.INVISIBLE);
            tv_username.setClickable(true);
            mCircleImageView.setImageResource(R.drawable.default_head);
            mCircleImageView.setClickable(true);
        }
    }

    private void startActivity(Intent intent, boolean isNeedLogin){
        if (isNeedLogin){
            User user = MyApplication.getmApplication().getUser();

            if (user != null){
               super.startActivity(intent);
            }else {
                MyApplication.getmApplication().putIntent(intent);
                Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
                super.startActivityForResult(loginIntent, Constants.REQUEST_CODE);
            }
        }else {
            super.startActivity(intent);
        }
    }

}
