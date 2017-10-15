package com.example.wushop.activity;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.example.wushop.MyApplication;
import com.example.wushop.R;
import com.example.wushop.bean.Address;
import com.example.wushop.bean.JsonBean;
import com.example.wushop.msg.BaseResMsg;
import com.example.wushop.net.ServiceGenerator;
import com.example.wushop.net.SubscriberCallBack;
import com.example.wushop.utils.GetJsonDataUtil;
import com.example.wushop.utils.ToastUtils;
import com.example.wushop.widget.ClearEditText;
import com.example.wushop.widget.Constants;
import com.google.gson.Gson;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 添加地址
 */

public class AddressAddActivity extends BaseActivity {

    private int tag;
    private ArrayList<JsonBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();


    @ViewInject(R.id.et_consignee)
    private ClearEditText mEtConsignee;

    @ViewInject(R.id.et_phone)
    private ClearEditText mEtPhone;

    @ViewInject(R.id.tv_address)
    private TextView mTvAddress;

    @ViewInject(R.id.et_add_des)
    private ClearEditText mEtAddDes;

    private Activity context;

    @Override
    public void init() {
        context = AddressAddActivity.this;

        //初始化省市数据
        initJsonData();
    }

    //省 - 省(1)=江西省 - 江西省(1)=南昌市 - 南昌市(1)=东湖区
    private void initJsonData() {
        String jsonData = new GetJsonDataUtil().getJson(this, "province.json");////获取assets目录下的json文件数据
        ArrayList<JsonBean> jsonBean = parseData(jsonData);//通过Gson转化为实体

        //添加省份数据
        options1Items = jsonBean;


        for (int i=0; i < jsonBean.size(); i++){//遍历省份

            ArrayList<String> cityList = new ArrayList<>();//该省的城市列表
            ArrayList<ArrayList<String>> province_AreaList = new ArrayList<>();//该省的所有区列表

            for (int j=0; j < jsonBean.get(i).getCityList().size(); j++){//遍历该省份的所有城市

                String cityName = jsonBean.get(i).getCityList().get(j).getName();//获取城市名
                cityList.add(cityName);//添加所有城市

                ArrayList<String> city_areaList = new ArrayList<>();//该诚市的所有区列表

                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                if (jsonBean.get(i).getCityList().get(j).getArea() == null
                    || jsonBean.get(i).getCityList().get(j).getArea().size() == 0){
                    city_areaList.add("");

                }else {
                    for (int z=0; z < jsonBean.get(i).getCityList().get(j).getArea().size(); z++){//遍历该市下的所有区

                        String areaName = jsonBean.get(i).getCityList().get(j).getArea().get(z);  //获取区名
                        city_areaList.add(areaName);//添加该城市所有地区


                    }
                }

                province_AreaList.add(city_areaList);
            }
            //添加城市
            options2Items.add(cityList);
            //添加地区
            options3Items.add(province_AreaList);

        }

    }

    private ArrayList<JsonBean> parseData(String data) {
        ArrayList<JsonBean> detail = new ArrayList<>();

        try {
            JSONArray jsonArray = new JSONArray(data);
            Gson gson = new Gson();
            for (int i=0; i < jsonArray.length(); i++){
                JsonBean jsonBean = gson.fromJson(jsonArray.optJSONObject(i).toString(), JsonBean.class);
                detail.add(jsonBean);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return detail;

    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_address_add;
    }

    @Override
    public void setToolbar() {
        //根据AddressListActivity传入的tag来显示编辑或按成按钮
        tag = getIntent().getIntExtra("tag",-1);

        final Address address = (Address) getIntent().getSerializableExtra("addressBean");

        if (tag == Constants.TAG_SAVE){
            getToolbar().setTitle("添加新地址");
            getToolbar().setleftButtonIcon(R.drawable.icon_back_32px);
            getToolbar().setRightButtonText("保存");
        }else if (tag == Constants.TAG_COMPLETE){
            getToolbar().setTitle("编辑地址");
            getToolbar().setleftButtonIcon(R.drawable.icon_back_32px);
            getToolbar().setRightButtonText("完成");
            showAddress(address);
        }

        getToolbar().setRightButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tag == Constants.TAG_SAVE){
                    //添加新地址
                    createAddress();
                }else if (tag == Constants.TAG_COMPLETE){
                    final Address address = (Address) getIntent().getExtras().getSerializable("addressBean");
                    //编辑地址
                    updateAddress(address);

                }
            }
        });


    }

    //编辑地址
    private void updateAddress(Address address) {
        check();

        String consignee = mEtConsignee.getText().toString();
        String phone = mEtPhone.getText().toString();
        String addr = mTvAddress.getText().toString() + "-" + mEtAddDes.getText().toString();

        ServiceGenerator.getRetrofit(this)
                .updateAddr(address.getId(), consignee, phone, addr, address.getZip_code(), address.getIsDefault(), MyApplication.getmApplication().getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SubscriberCallBack<BaseResMsg>(this, false) {
                    @Override
                    public void onSuccess(BaseResMsg result) {
                        if (result.getStatus() == result.STATUS_SUCCESS){
                            setResult(RESULT_OK);
                            finish();
                        }
                    }
                });

    }

    //添加地址
    private void createAddress() {
        check();

        String consignee = mEtConsignee.getText().toString().trim();
        String phone = mEtPhone.getText().toString().trim();
        String address = mTvAddress.getText().toString().trim() + "-" + mEtAddDes.getText().toString();

        if (check(phone)){
            String userId = MyApplication.getmApplication().getUser().getId() + "";
            String token = MyApplication.getmApplication().getToken();

            ServiceGenerator.getRetrofit(this)
                    .createAddr(Long.parseLong(userId),consignee,phone,address,"000000",token)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new SubscriberCallBack<BaseResMsg>(this, false) {
                        @Override
                        public void onSuccess(BaseResMsg result) {
                            if (result.getStatus() == result.STATUS_SUCCESS){
                                setResult(RESULT_OK);
                                finish();
                            }
                        }
                    });
        }
    }

    //检验手机号码
    private boolean check(String phone) {
        if (TextUtils.isEmpty(phone)){
            ToastUtils.show(this, "请输入手机号码");
            return false;
        }

        if (phone.length() != 11){
            ToastUtils.show(this, "手机号码长度不对");
            return false;
        }

        String rule = "^1(3|5|7|8|4)\\d{9}";
        Pattern p = Pattern.compile(rule);
        Matcher m = p.matcher(phone);

        if (!m.matches()){
            ToastUtils.show(this, "您输入的手机号码格式不正确");
            return false;
        }

        return true;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if (requestCode == Constants.ADDRESS_ADD){
                createAddress();//添加地址
            }else if (requestCode == Constants.ADDRESS_EDIT){
                Address address = (Address) getIntent().getExtras().getSerializable("addressBean");
                updateAddress(address);
            }
        }
    }

    //检查各项地址信息是否为空
    private void check() {
        String name = mEtConsignee.getText().toString().trim();
        String phone = mEtPhone.getText().toString().trim();
        String address = mTvAddress.getText().toString().trim();
        String address_des = mEtAddDes.getText().toString().trim();
        if (TextUtils.isEmpty(name)){
            ToastUtils.show(this,"请输入收件人姓名");
        }else if (TextUtils.isEmpty(phone)){
            ToastUtils.show(this,"请输入收件人电话");
        }else if (TextUtils.isEmpty(address)){
            ToastUtils.show(this,"请选择地区");
        }else if (TextUtils.isEmpty(address_des)){
            ToastUtils.show(this,"请输入具体地址");
        }
    }

    //将编辑的地址信息显示出来
    private void showAddress(Address address) {
        String[] addrArr = address.getAddr().split("-");
        mEtConsignee.setText(address.getConsignee());
        mEtPhone.setText(address.getPhone());
        mTvAddress.setText(addrArr[0] == null ? "" : addrArr[0]);
        mEtAddDes.setText(addrArr[1] == null ? "" : addrArr[1]);

    }

    @OnClick(R.id.ll_city_picker)
    public void showCityPickerView(View v){
        showPickerView();
    }

    //弹出省份选择器
    private void showPickerView() {

        //条件选择器
        OptionsPickerView pvOptions = new  OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3 ,View v) {
                //返回的分别是三个级别的选中位置
                String address = options1Items.get(options1).getPickerViewText()
                        + options2Items.get(options1).get(option2)
                        + options3Items.get(options1).get(option2).get(options3);
                mTvAddress.setText(address);
            }
        })
                .setTitleText("选择地址")
                .setDividerColor(R.color.black)
                .setTextColorCenter(R.color.black)
                .setContentTextSize(20)
                .setOutSideCancelable(false)
                .build();
        pvOptions.setPicker(options1Items, options2Items, options3Items);
        pvOptions.show();

    }
}
