package com.example.wushop;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.example.wushop.bean.ShoppingCart;
import com.example.wushop.bean.Tab;
import com.example.wushop.fragment.CartFragment;
import com.example.wushop.fragment.CategoryFragment;
import com.example.wushop.fragment.HomeFragment;
import com.example.wushop.fragment.HotFragment;
import com.example.wushop.fragment.MineFragment;
import com.example.wushop.utils.CartOperator;
import com.example.wushop.utils.PreferencesUtils;
import com.example.wushop.widget.FragmentTabHost;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FragmentTabHost mTabHost;
    private LayoutInflater mInflater;
    private List<Tab> mTabs = new ArrayList<>();

    private CartFragment cartFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

    }

    public void init(){
        Tab tab_home = new Tab(R.drawable.selector_icon_home,R.string.home,HomeFragment.class);
        Tab tab_hot = new Tab(R.drawable.selector_icon_hot,R.string.hot, HotFragment.class);
        Tab tab_category = new Tab(R.drawable.selector__icon_category,R.string.catagory, CategoryFragment.class);
        Tab tab_cart = new Tab(R.drawable.selector_icon_cart,R.string.cart, CartFragment.class);
        Tab tab_mine = new Tab(R.drawable.selector_icon_mine,R.string.mine, MineFragment.class);

        mTabs.add(tab_home);
        mTabs.add(tab_hot);
        mTabs.add(tab_category);
        mTabs.add(tab_cart);
        mTabs.add(tab_mine);

        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this,getSupportFragmentManager(),R.id.realtabcontent);

        for (Tab tab:mTabs){
            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(getString(tab.getTitle()));
            tabSpec.setIndicator(buildIndicator(tab));
            mTabHost.addTab(tabSpec,tab.getFragment(),null);

        }

        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                if (tabId.equals(getString(R.string.cart))){
                    refreshCartData();
                }
            }
        });

        mTabHost.getTabWidget().setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);
        mTabHost.setCurrentTab(0);
    }

    private void refreshCartData() {
        if (cartFragment == null){
            //fragment只有在点击之后，才会添加进来
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(getString(R.string.cart));

            //判断当前fragmnet是否被点击，点击才存在
            if (fragment != null){
                cartFragment = (CartFragment) fragment;
                cartFragment.refreshData();
            }

        }else {
            cartFragment.refreshData();
        }
    }

    private View buildIndicator(Tab tab) {

        View view = mInflater.from(this).inflate(R.layout.tabspec_indicator,null);
        ImageView imageView = (ImageView) view.findViewById(R.id.tab_home);
        TextView textView = (TextView) view.findViewById(R.id.tab_text);
        imageView.setBackgroundResource(tab.getImage());
        textView.setText(tab.getTitle());
        return view;
    }

    private FragmentManager fmanager;
    private FragmentTransaction ftransaction;
    private void gotoMineFragment() {
        fmanager = getSupportFragmentManager();
        ftransaction = fmanager.beginTransaction();
        MineFragment  mineFragment = new MineFragment();
        ftransaction.replace(R.id.realtabcontent, mineFragment);
        ftransaction.commit();
    }

}
