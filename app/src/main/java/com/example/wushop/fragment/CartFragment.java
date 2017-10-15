package com.example.wushop.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.wushop.R;
import com.example.wushop.activity.NewOrderActivity;
import com.example.wushop.adapter.CartAdapter;
import com.example.wushop.adapter.decoration.DividerItemDecortion;
import com.example.wushop.bean.ShoppingCart;
import com.example.wushop.utils.CartOperator;
import com.example.wushop.utils.PreferencesUtils;
import com.example.wushop.utils.ToastUtils;
import com.example.wushop.widget.Constants;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.io.Serializable;
import java.util.List;

/**
 * 购物车fragment
 */

public class CartFragment extends BaseFragment implements View.OnClickListener{

    @ViewInject(R.id.recyclerview_cart)
    private RecyclerView mRecyclerView;

    @ViewInject(R.id.checkbox_all)
    private CheckBox mCheckBox;

    @ViewInject(R.id.tv_total)
    private TextView mTVToal;

    @ViewInject(R.id.btn_order)
    private Button btn_order;

    @ViewInject(R.id.btn_del)
    private Button btn_del;

    private static final int ACTION_EDIT = 1;
    private static final int ACTION_COMPLETE = 2;

    private CartOperator mCartOperator;
    private CartAdapter mCartAdapter;

    @Override
    public void setToolbar() {
        getToolbar().hideSearchView();
        getToolbar().setRightButtonText(R.string.edit);
        getToolbar().setRightButtonOnClickListener(this);
        getToolbar().getRightButton().setTag(ACTION_EDIT);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_cart;
    }

    @Override
    public void init() {

        mCartOperator = CartOperator.getInstance(getContext());
        showData();
    }

    //显示购物车数据
    private void showData() {
        List<ShoppingCart> carts = mCartOperator.getAll();
        mCartAdapter = new CartAdapter(getContext(),carts,mCheckBox,mTVToal);
        mRecyclerView.setAdapter(mCartAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(new DividerItemDecortion(getContext(),DividerItemDecortion.VERTICAL_LIST));
    }

    //刷新数据
    public void refreshData(){
        mCartAdapter.clearData();
        List<ShoppingCart> carts = mCartOperator.getAll();
        mCartAdapter.addData(carts);
        mCartAdapter.showTotalPrice();
    }

    @Override
    public void onClick(View view) {
        int action = (int) view.getTag();

        if (ACTION_EDIT == action){//点击编辑转换到可删除
            showDelControl();
        }else if (ACTION_COMPLETE == action){//点击完成转换到不可删除
            hideDelControl();
        }

        //TODO
    }

    @OnClick(R.id.btn_del)
    public void delCart(View v){
        mCartAdapter.del_cart();
    }

    @OnClick(R.id.btn_order)
    public void toOrder(View view){
        if (view.getId() == R.id.btn_order){
            List<ShoppingCart> carts = mCartAdapter.getCheckData();
            if (carts.size() != 0 && carts != null) {
                Intent intent = new Intent(getActivity(), NewOrderActivity.class);
                intent.putExtra("carts", (Serializable) mCartAdapter.getCheckData());
                intent.putExtra("sign", Constants.CART);
                startActivity(intent, true);

            } else {
                ToastUtils.show(getContext(), "请选择要购买的商品");
            }
        }
    }

    private void hideDelControl() {
        getToolbar().getRightButton().setText("编辑");
        mTVToal.setVisibility(View.VISIBLE);
        btn_order.setVisibility(View.VISIBLE);
        btn_del.setVisibility(View.GONE);

        getToolbar().getRightButton().setTag(ACTION_EDIT);

        mCartAdapter.checkAll_None(true);
        mCheckBox.setChecked(true);
        mCartAdapter.showTotalPrice();
    }

    private void showDelControl() {
        getToolbar().getRightButton().setText("完成");
        mTVToal.setVisibility(View.GONE);
        btn_order.setVisibility(View.GONE);
        btn_del.setVisibility(View.VISIBLE);

        getToolbar().getRightButton().setTag(ACTION_COMPLETE);

        mCartAdapter.checkAll_None(false);
        mCheckBox.setChecked(false);

    }


}
