package com.example.wushop.fragment;


import android.support.v7.widget.DefaultItemAnimator;
        import android.support.v7.widget.LinearLayoutManager;
        import android.support.v7.widget.RecyclerView;
        import android.util.Log;

        import android.view.View;

        import com.cjj.MaterialRefreshLayout;
        import com.cjj.MaterialRefreshListener;
        import com.example.wushop.R;
        import com.example.wushop.adapter.BaseAdapter;
        import com.example.wushop.adapter.HotWaresAdapter;
        import com.example.wushop.bean.Wares;
        import com.example.wushop.bean.Page;
        import com.example.wushop.net.ServiceGenerator;
        import com.example.wushop.net.SubscriberCallBack;
        import com.example.wushop.utils.ToastUtils;
        import com.lidroid.xutils.view.annotation.ViewInject;

        import java.util.ArrayList;
        import java.util.List;

        import rx.android.schedulers.AndroidSchedulers;
        import rx.schedulers.Schedulers;

/**
 * 热卖fragment
 */

public class HotFragment extends BaseFragment {

    private HotWaresAdapter mAdapter;

    @ViewInject(R.id.recyclerview_hot)
    private RecyclerView mRecyclerView;

    @ViewInject(R.id.refreshlayout_hot)
    private MaterialRefreshLayout mRefreshLayout;

    private List<Wares> datas = new ArrayList<>();

    private int curPage = 1;
    private int pageSize = 10;
    private int totalCount;
    private final int STATE_NORMAL = 0;
    private final int STATE_REFRESH = 1;
    private final int STATE_MORE = 2;
    private int state = STATE_NORMAL;


    @Override
    public void setToolbar() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_hot;
    }

    @Override
    public void init() {
        getData();
        initRefreshLayout();

    }

    private void getData(){
        ServiceGenerator.getRetrofit(getActivity())
                .getHotWares(curPage,pageSize)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SubscriberCallBack<Page<Wares>>(getActivity(),true) {
                    @Override
                    public void onSuccess(Page<Wares> result) {
                        datas = result.getList();
                        curPage = result.getCurrentPage();
                        pageSize = result.getPageSize();
                        totalCount = result.getTotalCount();
                        showData();

                    }
                });

    }

    private void showData() {
        switch (state) {
            case STATE_NORMAL:

                mAdapter = new HotWaresAdapter(getContext(),datas);
                mAdapter.setOnItemClickListenner(new BaseAdapter.OnItemClickListenner() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Wares wares = mAdapter.getItem(position);
                        mAdapter.showDetail(wares);
                    }
                });
                mRecyclerView.setAdapter(mAdapter);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                mRecyclerView.setItemAnimator(new DefaultItemAnimator());

                break;
            case STATE_MORE:
                mAdapter.addData(mAdapter.getDatas().size(), datas);
                mRecyclerView.scrollToPosition(mAdapter.getDatas().size());
                mRefreshLayout.finishRefreshLoadMore();
                break;
            case STATE_REFRESH:
                mAdapter.clearData();
                mAdapter.addData(datas);
                mRecyclerView.scrollToPosition(0);
                mRefreshLayout.finishRefresh();
                break;
        }
    }


    private void initRefreshLayout(){
        mRefreshLayout.setLoadMore(true);
        mRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                refreshData();
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                super.onRefreshLoadMore(materialRefreshLayout);
                if (curPage * pageSize < totalCount) {
                    loadMoreData();
                } else {
                    ToastUtils.show(getActivity(), "没有更多数据...");
                    mRefreshLayout.finishRefreshLoadMore();
                    mRefreshLayout.setLoadMore(false);
                }
            }
        });
    }

    /**
     * 刷新数据
     */
    private void refreshData() {
        curPage = 1;
        state = STATE_REFRESH;
        getData();
    }


    /**
     * 加载更多
     */
    private void loadMoreData() {
        curPage = ++curPage;
        state = STATE_MORE;
        getData();
    }
}
