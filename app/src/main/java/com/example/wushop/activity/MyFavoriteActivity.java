package com.example.wushop.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.wushop.MyApplication;
import com.example.wushop.R;
import com.example.wushop.adapter.BaseAdapter;
import com.example.wushop.adapter.FavoriteAdapter;
import com.example.wushop.adapter.decoration.CardViewtemDecortion;
import com.example.wushop.bean.Favorite;
import com.example.wushop.msg.BaseResMsg;
import com.example.wushop.net.ServiceGenerator;
import com.example.wushop.net.SubscriberCallBack;
import com.example.wushop.widget.CustomDialog;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 收藏
 */

public class MyFavoriteActivity extends BaseActivity {

    @ViewInject(R.id.recycle_view)
    private RecyclerView mRecyclerView;

    private FavoriteAdapter mFavoriteAdapter;
    private CustomDialog mDialog;

    @Override
    public void init() {
        initFavorite();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_my_favorite;
    }

    @Override
    public void setToolbar() {
        getToolbar().setleftButtonIcon(R.drawable.icon_back_32px);
        getToolbar().setTitle("我的收藏");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        initFavorite();
    }

    private void initFavorite() {
        Long userId = MyApplication.getmApplication().getUser().getId();

        ServiceGenerator.getRetrofit(this)
                .favoriteList(userId, MyApplication.getmApplication().getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SubscriberCallBack<List<Favorite>>(this, true) {
                    @Override
                    public void onSuccess(List<Favorite> result) {
                        showFavorite(result);
                    }
                });
    }

    private void showFavorite(final List<Favorite> favorite) {
        if (mFavoriteAdapter == null){
            mFavoriteAdapter = new FavoriteAdapter(this, favorite, new FavoriteAdapter.FavoriteLisneter() {
                @Override
                public void onClickDelete(Favorite favorite) {
                    showDialog(favorite);
                }
            });
            mRecyclerView.setAdapter(mFavoriteAdapter);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            mRecyclerView.addItemDecoration(new CardViewtemDecortion());
            mFavoriteAdapter.setOnItemClickListenner(new BaseAdapter.OnItemClickListenner() {
                @Override
                public void onItemClick(View view, int positon) {
                    mFavoriteAdapter.showDetail(favorite.get(positon).getWares());
                }
            });
        }else {
            Log.e("length",favorite.size()+"");
            mFavoriteAdapter.refrenshData(favorite);
            mRecyclerView.setAdapter(mFavoriteAdapter);
        }
    }

    //显示删除对话框
    private void showDialog(final Favorite favorite) {
        CustomDialog.Builder builder = new CustomDialog.Builder(this);
        builder.setTitle("友情提示");
        builder.setMessage("您确定删除该商品吗?");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface anInterface, int i) {
                deleteFavorite(favorite);
                initFavorite();

                if (mDialog.isShowing())
                    mDialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface anInterface, int i) {
                if (mDialog.isShowing())
                    mDialog.dismiss();
            }
        });
        mDialog = builder.create();
        mDialog.show();
    }

    private void deleteFavorite(Favorite favorite) {
        ServiceGenerator.getRetrofit(this)
                .favoriteDel(favorite.getId(), MyApplication.getmApplication().getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SubscriberCallBack<BaseResMsg>(this, false) {
                    @Override
                    public void onSuccess(BaseResMsg result) {
                        if (result.getStatus() == result.STATUS_SUCCESS){
                            setResult(RESULT_OK);
                            if (mDialog.isShowing()){
                                mDialog.dismiss();
                            }
                        }
                    }
                });
    }
}
