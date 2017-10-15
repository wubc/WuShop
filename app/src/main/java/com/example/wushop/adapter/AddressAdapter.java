package com.example.wushop.adapter;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.wushop.R;
import com.example.wushop.bean.Address;

import java.util.List;

/**
 * Created by Administrator on 2017/10/6.
 */

public class AddressAdapter extends SimpleAdapter<Address> {

    private AddressListener mAddressListener;
    private TextView mTvEdit;
    private TextView mTvDelete;

    public AddressAdapter(Context context, List<Address> datas, AddressListener addressListener) {
        super(context, datas, R.layout.template_address);

        this.mAddressListener = addressListener;
    }

    public TextView getmTvEdit() {
        return mTvEdit;
    }

    public void setmTvEdit(TextView mTvEdit) {
        this.mTvEdit = mTvEdit;
    }

    public TextView getmTvDelete() {
        return mTvDelete;
    }

    public void setmTvDelete(TextView mTvDelete) {
        this.mTvDelete = mTvDelete;
    }

    @Override
    public void bindData(BaseViewHolder holder, final Address address) {
        holder.getTextView(R.id.tv_name).setText(address.getConsignee());
        holder.getTextView(R.id.tv_phone).setText(replacePhoneNum(address.getPhone()));
        holder.getTextView(R.id.tv_address).setText(address.getAddr());
        TextView tvEdit = holder.getTextView(R.id.tv_edit);
        TextView tvDel = holder.getTextView(R.id.tv_del);

        setmTvEdit(tvEdit);
        setmTvDelete(tvDel);

        CheckBox checkBox = (CheckBox) holder.getView(R.id.cb_is_defualt);
        boolean isDefault = address.getIsDefault();
        checkBox.setChecked(isDefault);

        if (isDefault){
            checkBox.setText("默认地址");
            checkBox.setClickable(false);
        }else {
            checkBox.setClickable(true);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton button, boolean b) {
                    if (b && mAddressListener != null) {
                        address.setIsDefault(b);
                        mAddressListener.setDefault(address);
                    }
                }
            });
        }

        tvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mAddressListener != null){
                    mAddressListener.onClickEdit(address);
                }
            }
        });

        tvDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mAddressListener != null){
                    mAddressListener.onClickDelete(address);
                }
            }
        });



    }

    //将电话号码中间四位用星号表示
    public String replacePhoneNum(String phone){
        return phone.substring(0,3) + "****" + phone.substring(7);
    }

    public interface AddressListener{

        void setDefault(Address address);
        void onClickEdit(Address address);
        void onClickDelete(Address address);

    }
}
