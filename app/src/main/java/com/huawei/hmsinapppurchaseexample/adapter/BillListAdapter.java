/**
 * Name of the project QUIZ MANIA.
 * Created by Sanghati Mukherjee.
 * Huawei Technologies Co., Ltd.
 * sanghati.mukherjee@huawei.com
 */

package com.huawei.hmsinapppurchaseexample.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.huawei.hms.iap.entity.InAppPurchaseData;
import com.huawei.hmsinapppurchaseexample.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class BillListAdapter extends BaseAdapter {

    private static final String TAG = "BillListAdapter";

    private Context mContext;
    private List<String> mBillList;

    public BillListAdapter(Context context, List<String> billList) {
        mContext = context;
        mBillList = billList;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return mBillList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup rootView) {
        BillListViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.bill_list_item, null);
            holder = new BillListViewHolder();
            holder.orderStatus = (TextView) convertView.findViewById(R.id.bill_status);
            holder.productName = (TextView) convertView.findViewById(R.id.bill_product_name);
            holder.productPrice = (TextView) convertView.findViewById(R.id.bill_product_price);
            convertView.setTag(holder);
        } else {
            holder = (BillListViewHolder) convertView.getTag();
        }
        String billInfo = mBillList.get(position);
        try {
            JSONObject billInformation = new JSONObject(billInfo);
            String productName = billInformation.optString("productName");
            int productPrice = billInformation.optInt("price");
            String currency = billInformation.optString("currency");
            int orderStatus = billInformation.optInt("purchaseState");
            holder.productName.setText(productName);
            String productPriceNumber = productPrice / 100 + "." + productPrice % 100 + " " + currency;
            holder.productPrice.setText(productPriceNumber);
            switch (orderStatus) {
                case InAppPurchaseData.PurchaseState.PURCHASED:
                    holder.orderStatus.setText(R.string.success_state);
                    break;
                case InAppPurchaseData.PurchaseState.CANCELED:
                    holder.orderStatus.setText(R.string.cancel_state);
                    break;
                case InAppPurchaseData.PurchaseState.REFUNDED:
                    holder.orderStatus.setText(R.string.refund_state);
                    break;
                default:
                    holder.orderStatus.setText(R.string.cancel_state);
                    break;
            }
        } catch (JSONException e) {
            Log.e(TAG, "Json error occured!");
        }
        return convertView;
    }

    @Override
    public int getCount() {
        return mBillList.size();
    }

    public static class BillListViewHolder {
        TextView productName;
        TextView productPrice;
        TextView orderStatus;
    }

}
