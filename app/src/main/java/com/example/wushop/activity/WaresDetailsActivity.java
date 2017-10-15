package com.example.wushop.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.wushop.MyApplication;
import com.example.wushop.R;
import com.example.wushop.bean.User;
import com.example.wushop.bean.Wares;
import com.example.wushop.msg.BaseResMsg;
import com.example.wushop.net.ServiceGenerator;
import com.example.wushop.net.SubscriberCallBack;
import com.example.wushop.utils.CartOperator;
import com.example.wushop.utils.ToastUtils;
import com.example.wushop.widget.Constants;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.io.Serializable;

import dmax.dialog.SpotsDialog;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 商品详情activity
 */

public class WaresDetailsActivity extends BaseActivity {

    @ViewInject(R.id.webView)
    private WebView webView;

    private WebInterface mWebInterface;
    private WebClient mClient;

    private SpotsDialog mDialog;
    private Wares mWares;


    private CartOperator mCartOperator;

    @Override
    public void init() {
        Serializable serializable = getIntent().getSerializableExtra(Constants.WARES);

        if (serializable == null)
            this.finish();

        mDialog = new SpotsDialog(this,"loading...");
        mDialog.show();

        mWares = (Wares) serializable;

        mCartOperator = CartOperator.getInstance(this);

        initWebview();


    }

    /*
    *初始化WebView
    * 1.设置允许执行js脚本
    * webSettings.setJavaScriptEnabled(true);
    * 2.添加通信接口
    * webView.addJavascriptInterface(Interface,”InterfaceName”)
    * 3.JS调⽤Android
    * InterfaceName.MethodName
    * 4.android调⽤JS
    * webView.loadUrl("javascript:functionName()");
     */

    private void initWebview() {

        WebSettings webSettings = webView.getSettings();
        //1、设置允许执行Js脚本
        webSettings.setJavaScriptEnabled(true);
        //设置能加载背景图片
        webSettings.setBlockNetworkImage(false);
        //设置允许缓存
        webSettings.setAppCacheEnabled(true);

        webView.loadUrl(Constants.API.WARES_DETAILS);

        mWebInterface = new WebInterface(this);

        mClient = new WebClient();

        //2.添加通信接口 name和web页面名称一致
        webView.addJavascriptInterface(mWebInterface,"appInterface");

        webView.setWebViewClient(mClient);

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_wares_details;
    }

    @Override
    public void setToolbar() {
        getToolbar().setTitle(R.string.wares_details);
        getToolbar().setleftButtonIcon(R.mipmap.icon_back_32px);
        getToolbar().setRightButtonText(getString(R.string.share));
        getToolbar().setRightButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showShare();
            }
        });
    }

    //分享界面
    private void showShare() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");//文本方式分享
        shareIntent.putExtra(Intent.EXTRA_TEXT, Constants.API.WARES_DETAILS);
        startActivity(Intent.createChooser(shareIntent, "分享到"));//设置分享列表的标题

    }

    //定义接口进行通讯
    class WebInterface {

       private Context mContext;

        public WebInterface(Context context){
            this.mContext = context;
        }

        /**
         * 方法名和js代码中必须一致
         * 显示详情页
         */
        @JavascriptInterface
        private void showDetail(){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    webView.loadUrl("javascript:showDetail(" + mWares.getId() + ")");
                }
            });
        }

        //添加到购物车
        @JavascriptInterface
        public void buy(long id){
            mCartOperator.put(mWares);
            ToastUtils.show(mContext, R.string.has_add_cart);
        }

        //添加到收藏夹
        @JavascriptInterface
        public void addToCart(long id){
            addToFavorite();
        }


    }

    private void addToFavorite() {
        User user = MyApplication.getmApplication().getUser();
        if (user == null){
            startActivity(new Intent(this, LoginActivity.class));
        }

        String userId = MyApplication.getmApplication().getUser().getId() + "";
        if(!TextUtils.isEmpty(userId)){
            ServiceGenerator.getRetrofit(this)
                    .addFavorite(Long.parseLong(userId), mWares.getId(), MyApplication.getmApplication().getToken())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new SubscriberCallBack<BaseResMsg>(this,false) {
                        @Override
                        public void onSuccess(BaseResMsg result) {
                            ToastUtils.show(WaresDetailsActivity.this,getString(R.string.has_add_favorite));
                        }
                    });
        }else {
            ToastUtils.show(this, "加载错误...");
        }
    }

    private class WebClient extends WebViewClient{

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (mDialog!=null && mDialog.isShowing()){
                mDialog.dismiss();
                //显示详情
                mWebInterface.showDetail();
            }

        }
    }
}
