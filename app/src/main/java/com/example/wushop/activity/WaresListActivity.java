package com.example.wushop.activity;

import android.support.design.widget.TabLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.example.wushop.R;
import com.example.wushop.adapter.BaseAdapter;
import com.example.wushop.adapter.HotWaresAdapter;
import com.example.wushop.bean.Page;
import com.example.wushop.bean.Wares;
import com.example.wushop.net.ServiceGenerator;
import com.example.wushop.net.SubscriberCallBack;
import com.example.wushop.utils.ToastUtils;
import com.example.wushop.widget.Constants;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 商品列表
 */

public class WaresListActivity extends BaseActivity implements TabLayout.OnTabSelectedListener,View.OnClickListener{

    @ViewInject(R.id.tv_summary)
    private TextView tv_summary;

    @ViewInject(R.id.tab_layout)
    private TabLayout mTabLayout;

    @ViewInject(R.id.refresh_layout)
    private MaterialRefreshLayout mRefreshLayout;

    @ViewInject(R.id.recycle_view)
    private RecyclerView mRecyclerView;

    private HotWaresAdapter mWaresAdapter;

    private long goodsId = 0;
    private int orderBy = 0;

    private static final int TAG_DEFAULT = 0;
    private static final int TAG_SALE = 1;
    private static final int TAG_PRICE = 2;

    private static final int ACTION_LIST = 1;
    private static final int ACTION_GRID = 2;

    private int curPage = 1;
    private int pageSize = 10;
    private int totalCount;
    private final int STATE_NORMAL = 0;
    private final int STATE_REFRESH = 1;
    private final int STATE_MORE = 2;
    private int state = STATE_NORMAL;

    private List<Wares> datas = new ArrayList<>();

    @Override
    public void init() {
        goodsId = getIntent().getLongExtra(Constants.GOODS_ID,0);
        //初始化tab
        initTab();

        initRefrenshLayout();

        //获取数据
        getData();
    }

    private void initRefrenshLayout() {
        mRefreshLayout.setLoadMore(true);
        mRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                refreshData();
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                if (curPage * pageSize < totalCount){
                    loadMoreData();
                }else {
                    ToastUtils.show(WaresListActivity.this,"没有更多信息了");
                    mRefreshLayout.finishRefreshLoadMore();
                }
            }
        });
    }

    private void loadMoreData() {
        curPage = ++ curPage;
        state = STATE_MORE;
        getData();
    }

    private void refreshData() {
        curPage = 1;
        state = STATE_REFRESH;
        getData();
    }

    private void getData() {

        ServiceGenerator.getRetrofit(this)
                .goodsList(goodsId, orderBy, curPage, pageSize)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SubscriberCallBack<Page<Wares>>(this,true) {
                    @Override
                    public void onSuccess(Page<Wares> result) {
                        curPage = result.getCurrentPage();
                        pageSize = result.getPageSize();
                        totalCount = result.getTotalCount();
                        datas = result.getList();
                        showData();
                    }
                });

    }

    private void showData() {
        switch (state){
            case STATE_NORMAL:
                tv_summary.setText("共有"+totalCount+"件商品");
                mWaresAdapter = new HotWaresAdapter(this,datas);

                mWaresAdapter.setOnItemClickListenner(new BaseAdapter.OnItemClickListenner() {
                    @Override
                    public void onItemClick(View view, int positon) {
                        mWaresAdapter.showDetail(mWaresAdapter.getItem(positon));
                    }
                });

                mRecyclerView.setAdapter(mWaresAdapter);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                break;
            case STATE_REFRESH:
                mWaresAdapter.refrenshData(datas);
                mRecyclerView.scrollToPosition(0);
                mRefreshLayout.finishRefresh();
                break;
            case STATE_MORE:
                mWaresAdapter.loadMore(datas);
                mRecyclerView.scrollToPosition(0);
                mRefreshLayout.finishRefreshLoadMore();
                break;
        }
    }

    private void initTab() {
        TabLayout.Tab tab = mTabLayout.newTab();
        tab.setText(R.string.defaults);
        tab.setTag(TAG_DEFAULT);
        mTabLayout.addTab(tab);

        tab = mTabLayout.newTab();
        tab.setText(R.string.sales);
        tab.setTag(TAG_SALE);
        mTabLayout.addTab(tab);

        tab = mTabLayout.newTab();
        tab.setText(R.string.price);
        tab.setTag(TAG_PRICE);
        mTabLayout.addTab(tab);

        mTabLayout.addOnTabSelectedListener(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_wares_list;
    }

    @Override
    public void setToolbar() {
        getToolbar().setTitle(R.string.wares_list);
        getToolbar().getRightImgButton().setTag(ACTION_LIST);
        getToolbar().setRightButtonImgOnClickListener(this);
        getToolbar().setRightImgButtonIcon(R.drawable.icon_grid_32);
        getToolbar().setleftButtonIcon(R.mipmap.icon_back_32px);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        orderBy = (int) tab.getTag();
        curPage = 1;
        state = STATE_NORMAL;
        getData();
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onClick(View view) {
        int action = (int) view.getTag();
        Log.e("onclick","click");
        if (ACTION_LIST == action){
                getToolbar().setRightImgButtonIcon(R.drawable.icon_list_32);
                getToolbar().getRightImgButton().setTag(ACTION_GRID);
                mWaresAdapter.reSetLayout(R.layout.template_grid_wares);
                mRecyclerView.setLayoutManager(new GridLayoutManager(this,2));
                mRecyclerView.setAdapter(mWaresAdapter);
        }else if (ACTION_GRID == action) {
            getToolbar().setRightImgButtonIcon(R.drawable.icon_grid_32);
            getToolbar().getRightImgButton().setTag(ACTION_LIST);
            mWaresAdapter.reSetLayout(R.layout.template_hot_wares);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            mRecyclerView.setAdapter(mWaresAdapter);

        }
    }
}
