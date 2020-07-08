package com.huawei.hmsinapppurchaseexample.util;

/**
 * Name of the project QUIZ MANIA.
 * Created by Sanghati Mukherjee.
 * Huawei Technologies Co., Ltd.
 * sanghati.mukherjee@huawei.com
 */

import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.iap.Iap;
import com.huawei.hms.iap.IapApiException;
import com.huawei.hms.iap.IapClient;
import com.huawei.hms.iap.entity.ConsumeOwnedPurchaseReq;
import com.huawei.hms.iap.entity.ConsumeOwnedPurchaseResult;
import com.huawei.hms.iap.entity.InAppPurchaseData;
import com.huawei.hms.iap.entity.OwnedPurchasesReq;
import com.huawei.hms.iap.entity.OwnedPurchasesResult;
import com.huawei.hms.iap.entity.ProductInfo;
import com.huawei.hms.iap.entity.ProductInfoReq;
import com.huawei.hms.iap.entity.PurchaseIntentReq;
import com.huawei.hms.iap.entity.PurchaseIntentResult;
import com.huawei.hms.support.api.client.Status;
import com.huawei.hmsinapppurchaseexample.R;
import com.huawei.hmsinapppurchaseexample.activity.HmsInAppPurchaseList;
import com.thecode.aestheticdialogs.AestheticDialog;

import org.json.JSONException;

import java.sql.Struct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.huawei.hmsinapppurchaseexample.util.CipherUtil;
import com.huawei.hmsinapppurchaseexample.util.Key;

public class ProductPurchase {
    public static final int REQ_CODE_BUY = 4002;
    public static String TAG;
    private Context context;
    private ListView listView;
    private String item_name = "NAME";
    private String item_price = "PRICE";
    private String item_desc = "DESC";
    private String item_productId = "PRODUCTID";
    private String item_image = "IMAGE";
    private String item_state = "STATE";
    private List<HashMap<String, Object>> products = new ArrayList<HashMap<String, Object>>();
    private int productType;
    private ArrayList<String> productIds = new ArrayList<>();
    private boolean purchaseStateValue;
    private OwnedPurchasesResult cacheOwnedPurchasesResult;

    public ProductPurchase(Context contxt, String tagName, int prodtType, ArrayList<String> prodtIds) {
        this.context = contxt;
        this.productType = prodtType;
        this.productIds = prodtIds;
        TAG = tagName;

    }

    public ProductInfoReq createProductInfoReq() {
        ProductInfoReq req = new ProductInfoReq();
        req.setPriceType(productType);
        req.setProductIds(productIds);
        return req;
    }

    // This will return the product list...
    public List<HashMap<String, Object>> getProducts(List<ProductInfo> productInfoList) {

        for (ProductInfo productInfo : productInfoList) {
            HashMap<String, Object> item = new HashMap<String, Object>();
            item.clear();
            item.put(item_name, productInfo.getProductName());
            item.put(item_price, productInfo.getPrice());
            item.put(item_desc, productInfo.getProductDesc());
            item.put(item_productId, productInfo.getProductId());
            if (productInfo.getPriceType() == IapClient.PriceType.IN_APP_SUBSCRIPTION) {
                // boolean productPurchaseState = checkProductActive(productInfo.getProductId());
                System.out.println("Pur " + checkActiveProduct(productInfo.getProductId()));

                if (checkActiveProduct(productInfo.getProductId())) {
                    item.put(item_state, "ACTIVE PLAN");
                    Utility.subscriptionSettings(productInfo.getProductId(),true,context);
                } else {
                    item.put(item_state, "BUY");
                    Utility.subscriptionSettings(productInfo.getProductId(),false,context);
                }

            } else {
                item.put(item_state, "BUY");
            }

            if (productInfo.getProductName().equalsIgnoreCase("Silver")) {
                item.put(item_image, R.drawable.silver);
            } else if (productInfo.getProductName().equalsIgnoreCase("Gold")) {
                item.put(item_image, R.drawable.gold);
            } else if (productInfo.getProductName().equalsIgnoreCase("Diamond")) {
                item.put(item_image, R.drawable.diamond);
            } else if (productInfo.getProductId().equalsIgnoreCase("NON_CONSUMABLE_PRODUCT_ID")) {
                item.put(item_image, R.drawable.ads);
            } else if (productInfo.getProductId().equalsIgnoreCase("CONSUMABLE_PRODUCT_ID")) {
                item.put(item_image, R.drawable.stackgold);
            }

            products.add(item);
        }
        return products;

    }

    // Payment is done here...
   /* Creates orders for PMS products, including consumables, non-consumables, and subscriptions.
    After creating an in-app product in AppGallery Connect, you can call this API to open the HUAWEI IAP checkout page
    and display the product, price, and payment method.
    Huawei can adjust in-app product prices by foreign exchange rate changes.*/

    public void gotoPayment(final Activity activity, String productId, int type) {
        Log.i(TAG, "call createPurchaseIntent");
        IapClient mClient = Iap.getIapClient(activity);
        Task<PurchaseIntentResult> task = mClient.createPurchaseIntent(createPurchaseIntentReq(type, productId));
        task.addOnSuccessListener(new OnSuccessListener<PurchaseIntentResult>() {
            @Override
            public void onSuccess(PurchaseIntentResult result) {
                Log.i(TAG, "createPurchaseIntent, onSuccess");
                if (result == null) {
                    Log.e(TAG, "result is null");
                    return;
                }
                Status status = result.getStatus();
                if (status == null) {
                    Log.e(TAG, "status is null");
                    return;
                }
                // Pull up the page to complete the payment process.
                if (status.hasResolution()) {
                    try {
                        status.startResolutionForResult(activity, REQ_CODE_BUY);
                    } catch (IntentSender.SendIntentException exp) {
                        Log.e(TAG, exp.getMessage());
                    }
                } else {
                    Log.e(TAG, "intent is null");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                Log.e(TAG, e.getMessage());
                Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
                if (e instanceof IapApiException) {
                    IapApiException apiException = (IapApiException) e;
                    int returnCode = apiException.getStatusCode();
                    Log.e(TAG, "createPurchaseIntent, returnCode: " + returnCode);
                    // handle error scenarios
                } else {
                    // Other external errors
                }

            }
        });
    }


    private PurchaseIntentReq createPurchaseIntentReq(int type, String productId) {
        PurchaseIntentReq req = new PurchaseIntentReq();
        req.setProductId(productId);
        req.setPriceType(type);
        req.setDeveloperPayload("test");
        return req;
    }


    public void consumeOwnedPurchase(final Context context, String inAppPurchaseData, ArrayList<String> productIds) {
        Log.i(TAG, "call consumeOwnedPurchase");
        IapClient mClient = Iap.getIapClient(context);
        Task<ConsumeOwnedPurchaseResult> task = mClient.consumeOwnedPurchase(createConsumeOwnedPurchaseReq(inAppPurchaseData));
        task.addOnSuccessListener(new OnSuccessListener<ConsumeOwnedPurchaseResult>() {
            @Override
            public void onSuccess(ConsumeOwnedPurchaseResult result) {
                // Consume success
                Log.i(TAG, "consumeOwnedPurchase success");
                for (int i = 0; i < productIds.size(); i++) {
                    Utility.commonStorage(context,productIds.get(i));
                }

                AestheticDialog.showRainbow((Activity) context, "Congratulation!", "Pay success, and the product has been delivered", AestheticDialog.SUCCESS);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                // Log.e(TAG, e.getMessage());
                //Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                if (e instanceof IapApiException) {
                    IapApiException apiException = (IapApiException) e;
                    int returnCode = apiException.getStatusCode();
                    // obtainOwnedPurchases Exception ...
                    if (returnCode == 60052 || returnCode == -1) {
                        checkUserOwnedTheProduct((Activity) context);
                    }
                    Log.e(TAG, "consumeOwnedPurchase fail,returnCode: " + returnCode);
                } else {
                    // Other external errors
                }

            }
        });
    }


    private ConsumeOwnedPurchaseReq createConsumeOwnedPurchaseReq(String purchaseData) {

        ConsumeOwnedPurchaseReq req = new ConsumeOwnedPurchaseReq();
        // Parse purchaseToken from InAppPurchaseData in JSON format.
        try {
            InAppPurchaseData inAppPurchaseData = new InAppPurchaseData(purchaseData);
            req.setPurchaseToken(inAppPurchaseData.getPurchaseToken());
        } catch (JSONException e) {
            Log.e(TAG, "createConsumeOwnedPurchaseReq JSONExeption");
        }
        return req;
    }

    // CHECK USER OWNED THE PRODUCT OR NOT..
    /*Queries information about all purchased in-app products, including consumables, non-consumables, and auto-renewable subscriptions.
● If the information about consumables is returned, the consumables might not be delivered due to some exceptions.
In this case, your app needs to check whether the consumables are delivered.
If not, the app needs to deliver them and calls the consumeOwnedPurchase API to consume them.
● If the information about non-consumables is returned, the non-consumables do not need to be consumed.
● If the information about subscriptions is returned, all existing subscription relationships of the user in the app are returned. */

    public void checkUserOwnedTheProduct(final Context context) {
        OwnedPurchasesReq ownedPurchasesReq = new OwnedPurchasesReq();
        ownedPurchasesReq.setPriceType(productType);
        Task<OwnedPurchasesResult> task = Iap.getIapClient(context).obtainOwnedPurchases(ownedPurchasesReq);
        task.addOnSuccessListener(new OnSuccessListener<OwnedPurchasesResult>() {
            @Override
            public void onSuccess(OwnedPurchasesResult result) {
                // Obtain the execution result.
                if (result != null && result.getInAppPurchaseDataList() != null) {
                    for (int i = 0; i < result.getInAppPurchaseDataList().size(); i++) {
                        String inAppPurchaseData = result.getInAppPurchaseDataList().get(i);
                        String inAppSignature = result.getInAppSignature().get(i);
                        boolean success = CipherUtil.doCheck(inAppPurchaseData, inAppSignature, Key.getPublicKey());
                        if (success) {
                            try {
                                InAppPurchaseData inAppPurchaseDataBean = new InAppPurchaseData(inAppPurchaseData);
                                int purchaseState = inAppPurchaseDataBean.getPurchaseState();
//                                int PURCHASED = 0;
//                                int CANCELED = 1;
//                                int REFUNDED = 2;

                                    if (purchaseState == 0) {
                                        if (inAppPurchaseDataBean.getProductId().equalsIgnoreCase("RemoveAd101")) {
                                            Utility.commonStorage(context, inAppPurchaseDataBean.getProductId());
                                            ((HmsInAppPurchaseList) context).showNonConsumedResult(true);
                                        }
                                    } else if (purchaseState == 1) {
                                        AestheticDialog.showFlatDark((Activity) context, "Hey!", "You Have Canceled " +
                                                inAppPurchaseDataBean.getProductName() + ". We are sorry for your inconvenient.", AestheticDialog.WARNING);
                                    } else {
                                        AestheticDialog.showFlatDark((Activity) context, "Hey!", "We Have Refunded  " +
                                                inAppPurchaseDataBean.getProductName() + ". We are sorry for your inconvenient.", AestheticDialog.WARNING);
                                    }

                            } catch (JSONException e) {
                            }
                        }
                    }
                } else {
                    if (productType == IapClient.PriceType.IN_APP_NONCONSUMABLE) {
                        ((HmsInAppPurchaseList) context).loadList();
                        return;
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                if (e instanceof IapApiException) {
                    IapApiException apiException = (IapApiException) e;
                    Status status = apiException.getStatus();
                    int returnCode = apiException.getStatusCode();
                } else {
                    // Other external errors
                }
            }
        });
    }


    // REFRESH SUBSCRIPTION FOR CHECKING ITS ACTIVENESS...
    public void refreshSubscription(boolean isPurchased) {
        OwnedPurchasesReq ownedPurchasesReq = new OwnedPurchasesReq();
        ownedPurchasesReq.setPriceType(productType);
        Task<OwnedPurchasesResult> task = Iap.getIapClient(context).obtainOwnedPurchases(ownedPurchasesReq);
        task.addOnSuccessListener(new OnSuccessListener<OwnedPurchasesResult>() {
            @Override
            public void onSuccess(OwnedPurchasesResult result) {

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor = preferences.edit();
                Gson gson = new Gson();
                String json = gson.toJson(result);
                editor.putString("cacheOwnedPurchasesResult", json);
                editor.apply();

                if(isPurchased) {
                    if (result != null && result.getInAppPurchaseDataList() != null) {
                        for (int i = 0; i < result.getInAppPurchaseDataList().size(); i++) {
                            String inAppPurchaseData = result.getInAppPurchaseDataList().get(i);
                            String inAppSignature = result.getInAppSignature().get(i);
                            boolean success = CipherUtil.doCheck(inAppPurchaseData, inAppSignature, Key.getPublicKey());
                            if (success) {
                                try {
                                    InAppPurchaseData inAppPurchaseDataBean = new InAppPurchaseData(inAppPurchaseData);
                                    int purchaseState = inAppPurchaseDataBean.getPurchaseState();
//                                int PURCHASED = 0;
//                                int CANCELED = 1;
//                                int REFUNDED = 2;
                                    if (purchaseState == 0) {

                                        AestheticDialog.showFlatDark((Activity) context, "Congratulation!", "You Have Purchased " +
                                                inAppPurchaseDataBean.getProductName() + " at " + inAppPurchaseDataBean.getCurrency() + " " + inAppPurchaseDataBean.getPrice(), AestheticDialog.SUCCESS);
                                    } else if (purchaseState == 1) {
                                        AestheticDialog.showFlatDark((Activity) context, "Hey!", "You Have Un-Subscribed " +
                                                inAppPurchaseDataBean.getProductName() + ". We are sorry for your inconvenient.", AestheticDialog.WARNING);
                                    } else {
                                        AestheticDialog.showFlatDark((Activity) context, "Hey!", "We Have Refunded  " +
                                                inAppPurchaseDataBean.getProductName() + ". We are sorry for your inconvenient.", AestheticDialog.WARNING);
                                    }
                                } catch (JSONException e) {
                                }
                            }
                        }
                    }
                }
                ((HmsInAppPurchaseList)context).loadList();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                if (e instanceof IapApiException) {
                    IapApiException apiException = (IapApiException) e;
                    Status status = apiException.getStatus();
                    int returnCode = apiException.getStatusCode();
                } else {
                    // Other external errors
                }
            }
        });
    }

    // TO CHECK ACTIVE AND IN-ACTIVE PRODUCTS ...
    public boolean checkActiveProduct(String productId) {
        Gson gson = new Gson();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        String json = preferences.getString("cacheOwnedPurchasesResult", "");
        OwnedPurchasesResult obj = gson.fromJson(json, OwnedPurchasesResult.class);
        if (null == obj) {
            Log.e(TAG, "OwnedPurchasesResult is null");
            return false;
        }

        List<String> inAppPurchaseDataList = obj.getInAppPurchaseDataList();
        for (String data : inAppPurchaseDataList) {
            try {
                InAppPurchaseData inAppPurchaseData = new InAppPurchaseData(data);
                if (productId.equals(inAppPurchaseData.getProductId())) {
                    int index = inAppPurchaseDataList.indexOf(data);
                    String signature = obj.getInAppSignature().get(index);
                    boolean credible = CipherUtil.doCheck(data, signature, Key.getPublicKey());
                    if (credible) {
                        if(inAppPurchaseData.isSubValid() && inAppPurchaseData.getPurchaseState()==0) {
                            return true;
                        }else{
                            return false;
                        }
                    } else {
                        Log.e(TAG, "check the data signature fail");
                        return false;
                    }
                }
            } catch (JSONException e) {
                Log.e(TAG, "parse InAppPurchaseData JSONException", e);
                return false;
            }
        }
        return false;
    }




}
