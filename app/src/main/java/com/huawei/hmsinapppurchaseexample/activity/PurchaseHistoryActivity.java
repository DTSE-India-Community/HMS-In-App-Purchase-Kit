package com.huawei.hmsinapppurchaseexample.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.iap.Iap;
import com.huawei.hms.iap.IapApiException;
import com.huawei.hms.iap.IapClient;
import com.huawei.hms.iap.entity.OwnedPurchasesReq;
import com.huawei.hms.iap.entity.OwnedPurchasesResult;
import com.huawei.hmsinapppurchaseexample.R;

import java.util.ArrayList;
import java.util.List;

import com.huawei.hmsinapppurchaseexample.adapter.BillListAdapter;
import com.huawei.hmsinapppurchaseexample.util.CipherUtil;
import com.huawei.hmsinapppurchaseexample.util.Key;

public class PurchaseHistoryActivity extends AppCompatActivity {

    private String TAG = "PurchaseHistoryActivity";

    private ListView billListView;

    List<String> billList = new ArrayList<String>();

    private static String continuationToken = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_history);
        findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
        findViewById(R.id.bill_listview).setVisibility(View.GONE);
        billListView = findViewById(R.id.bill_listview);

    }

    @Override
    protected void onResume() {
        super.onResume();
        queryHistoryInterface();
    }

    // This API helps to find the list of CONSUMABLE purchased earlier ...
    private void queryHistoryInterface() {
        OwnedPurchasesReq req = new OwnedPurchasesReq();
        req.setPriceType(IapClient.PriceType.IN_APP_CONSUMABLE);
        Task<OwnedPurchasesResult> task = Iap.getIapClient(PurchaseHistoryActivity.this).obtainOwnedPurchaseRecord(req);
        task.addOnSuccessListener(new OnSuccessListener<OwnedPurchasesResult>() {
            @Override
            public void onSuccess(OwnedPurchasesResult result) {
                Log.i(TAG, "obtainOwnedPurchaseRecord, success");
                List<String> inAppPurchaseDataList = result.getInAppPurchaseDataList();
                List<String> signatureList = result.getInAppSignature();
                if (inAppPurchaseDataList == null) {
                    onFinish();
                    return;
                }
                Log.i(TAG, "list size: " + inAppPurchaseDataList.size());
                for (int i = 0; i < signatureList.size(); i++) {
                    boolean success = CipherUtil.doCheck(inAppPurchaseDataList.get(i), signatureList.get(i), Key.getPublicKey());
                    if (success) {
                        billList.add(inAppPurchaseDataList.get(i));
                    }
                }
                continuationToken = result.getContinuationToken();
                if (!TextUtils.isEmpty(continuationToken)) {
                    queryHistoryInterface();
                } else {
                    onFinish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                if (e instanceof IapApiException) {
                    IapApiException apiException = (IapApiException)e;
                    int returnCode = apiException.getStatusCode();
                } else {
                    // Other external errors
                }
            }
        });
    }

    private void onFinish() {
        findViewById(R.id.progressBar).setVisibility(View.GONE);
        findViewById(R.id.bill_listview).setVisibility(View.VISIBLE);
        Log.i(TAG, "onFinish");
        BillListAdapter billAdapter = new BillListAdapter(PurchaseHistoryActivity.this, billList);
        billListView.setAdapter(billAdapter);
    }

}

