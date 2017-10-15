package com.example.wushop.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.util.SparseArray;

import com.example.wushop.bean.ShoppingCart;
import com.example.wushop.bean.Wares;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * 购物车管理类
 */

public class CartOperator {

    private SparseArray<ShoppingCart> datas = null;

    private static Context context;
    public static final String CART_JSON = "cart_json";

    private static CartOperator operator;

    private CartOperator(Context context){
        this.context = context;
        datas = new SparseArray<>(10);
        listToSparse();
    }

    private void listToSparse() {
        List<ShoppingCart> carts = getDataFromLocal();

        if (carts != null && carts.size() > 0) {
            for (ShoppingCart cart : carts) {
                datas.put(cart.getId().intValue(), cart);
            }
        } else {
            datas.clear();
        }
    }

    public static CartOperator getInstance(Context context){

        if (operator == null){
            operator = new CartOperator(context);
        }

        return  operator;
    }

    public void put(Wares wares){
        ShoppingCart cart = convertData(wares);

        put(cart);
    }

    //存储SparseArray<ShoppingCart>数据，同时更新SharedPreferences的数据到本地
    public void put(ShoppingCart cart){

        //initValue():将Long型转换成int型
        ShoppingCart temp = datas.get(cart.getId().intValue());

        if (temp != null){
            temp.setCount(temp.getCount() + 1);
        }else {
            temp = cart;
            temp.setCount(1);
        }


        //将数据保存在SparseArray中
        datas.put(cart.getId().intValue(),temp);
        //将数据保存在SharedPerference中
        commit();
    }

    public void delete(ShoppingCart cart){
        datas.delete(cart.getId().intValue());
        commit();
    }

    //删除数据
    public void delete(List<ShoppingCart> carts) {
        if (carts != null && carts.size() > 0) {
            for (ShoppingCart cart : carts) {
                delete(cart);
            }
        }
    }

    //保存SparseArray<ShoppingCart>里的数据到本地
    private void commit() {
         List<ShoppingCart> carts = sparseToList();
         PreferencesUtils.putString(context, CART_JSON, JSONUtil.toJson(carts));
    }

    //将保存的数据转换成List<ShoppingCart>
    private List<ShoppingCart> sparseToList(){

        int size = datas.size();
        List<ShoppingCart> list = new ArrayList<>(size);
        for (int i = 0; i < size; i++){
            list.add(datas.valueAt(i));
        }
        return list;
    }

    //ShoppingCart子类不能强制转换成Wares父类，将Wares中数据添加到ShoppingCart
    public ShoppingCart convertData(Wares wares) {

        ShoppingCart cart = new ShoppingCart();
        cart.setId(wares.getId());
        cart.setDescription(wares.getDescription());
        cart.setName(wares.getName());
        cart.setImgUrl(wares.getImgUrl());
        cart.setPrice(wares.getPrice());

        return cart;

    }

    //从本地获取数据
    public List<ShoppingCart> getAll(){
        return getDataFromLocal();
    }

    //获取本地数据
    private List<ShoppingCart> getDataFromLocal() {

        String json = PreferencesUtils.getString(context, CART_JSON);

        List<ShoppingCart> carts = null;

        if (json != null){
            carts = JSONUtil.fromJson(json, new TypeToken<List<ShoppingCart>>(){}
            .getType());
        }
        return  carts;
    }

    //更新数据
    public void update(ShoppingCart shoppingCart){
        datas.put(shoppingCart.getId().intValue(),shoppingCart);
        commit();
    }
}
