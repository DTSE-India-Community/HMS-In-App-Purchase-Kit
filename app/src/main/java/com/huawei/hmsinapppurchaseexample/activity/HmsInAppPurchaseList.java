package com.huawei.hmsinapppurchaseexample.activity;

/**
 * Name of the project QUIZ MANIA.
 * Created by Sanghati Mukherjee.
 * Huawei Technologies Co., Ltd.
 * sanghati.mukherjee@huawei.com
 */

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.iap.Iap;
import com.huawei.hms.iap.IapApiException;
import com.huawei.hms.iap.IapClient;
import com.huawei.hms.iap.entity.OrderStatusCode;
import com.huawei.hms.iap.entity.ProductInfoResult;
import com.huawei.hms.iap.entity.PurchaseResultInfo;
import com.huawei.hmsinapppurchaseexample.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import info.hoang8f.widget.FButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import com.huawei.hmsinapppurchaseexample.util.CipherUtil;
import com.huawei.hmsinapppurchaseexample.util.Key;
import com.huawei.hmsinapppurchaseexample.util.ProductPurchase;
import com.huawei.hmsinapppurchaseexample.util.RetrofitInterface;
import com.huawei.hmsinapppurchaseexample.util.Utility;

public class HmsInAppPurchaseList extends AppCompatActivity {

    public static final String TAG = "HmsInAppPurchaseList";

    public static final int REQ_CODE_BUY = 4002;

    private ListView listView;
    private String item_name = "NAME";
    private String item_price = "PRICE";
    private String item_desc = "DESC";
    private String item_productId = "PRODUCTID";
    private String item_image = "IMAGE";
    private String item_state = "STATE";
    private List<HashMap<String, Object>> products = new ArrayList<HashMap<String, Object>>();
    private ArrayList<String> productIds;
    private ProductPurchase productPurchase;
    int productType = 0;
    FButton btnCommon;
    LinearLayout llAlreadyPurchased;
    TextView txtTitle;
    String productId ="";
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL ="http://YOUR.IPV4.ADDRESS:PORT";
    boolean alreadyLoaded = false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_inapp);
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);

        btnCommon = findViewById(R.id.btnCommon);
        llAlreadyPurchased = findViewById(R.id.llAlreadyPurchased);
        listView = (ListView) findViewById(R.id.itemlist);

        /*
              CHECK WHETHER THE LIST IS ALREADY LOADED OR NOT...
        */
        if(!alreadyLoaded) {
            loadProductList();
            alreadyLoaded = true;
        }
        btnCommon.setOnClickListener(commonButtonFunction());
        btnCommon.setButtonColor(getResources().getColor(R.color.fbutton_color_asbestos));

    }
    private void loadProductList() {
        txtTitle = findViewById(R.id.txtHeader);
        productIds = new ArrayList<>();
        productIds.clear();
        String purchaseType = getIntent().getExtras().getString("type");

        if(purchaseType.equalsIgnoreCase("extra_life")){
            txtTitle.setText("Add Coins");
            productType = IapClient.PriceType.IN_APP_CONSUMABLE;
            // REPLACE IT WITH YOUR CONSUMABLE PRODUCT ID's
            productIds.add("CONSUMABLE_PRODUCT_ID");
            Drawable hour = HmsInAppPurchaseList.this.getResources().getDrawable(R.drawable.hour);
            btnCommon.setCompoundDrawablesWithIntrinsicBounds(hour,null,null,null);
            btnCommon.setText("PURCHASED");
            llAlreadyPurchased.setVisibility(View.GONE);

        }else if(purchaseType.equalsIgnoreCase("no_ads")){
            txtTitle.setText("No Ads Plan");
            productType = IapClient.PriceType.IN_APP_NONCONSUMABLE;
            // REPLACE IT WITH YOUR NON-CONSUMABLE PRODUCT ID's
            productIds.add("NON_CONSUMABLE_PRODUCT_ID");
            btnCommon.setVisibility(View.GONE);
        }else{
            productType = IapClient.PriceType.IN_APP_SUBSCRIPTION;
            // REPLACE IT WITH YOUR SUBSCRIPTION PRODUCT ID's
            productIds.add("SUBSCRIPTION_PRODUCT_ID_1");
            productIds.add("SUBSCRIPTION_PRODUCT_ID_2");
            productIds.add("SUBSCRIPTION_PRODUCT_ID_3");
            btnCommon.setText("SUBSCRIPTION");
            Drawable setting = HmsInAppPurchaseList.this.getResources().getDrawable(R.drawable.settings);
            btnCommon.setCompoundDrawablesWithIntrinsicBounds(setting,null,null,null);
            llAlreadyPurchased.setVisibility(View.GONE);
        }

         /* THIS CODE CHECKS THE PRODUCT LIST ALSO MAKE SURE YOU
            CALL THIS AT THE BEGINNING ( LET's PLAY BUTTON )    */
        productPurchase = new ProductPurchase(HmsInAppPurchaseList.this, "HmsInAppPurchaseList", productType, productIds);
        if(productType == IapClient.PriceType.IN_APP_NONCONSUMABLE) {
            //Un Comment this at DTSE Talk ..
          // productPurchase.checkUserOwnedTheProduct(this);
            // To Show the list ...
            listView.setVisibility(View.VISIBLE);
            llAlreadyPurchased.setVisibility(View.GONE);
            loadList();
        }else if(productType == 2) {
            listView.setVisibility(View.VISIBLE);
            llAlreadyPurchased.setVisibility(View.GONE);
            // This is actually checking whether the subscription is acitve or not at the beginning...
            productPurchase.refreshSubscription(false);
        }else {
            listView.setVisibility(View.VISIBLE);
            llAlreadyPurchased.setVisibility(View.GONE);
            loadList();
        }

    }

    public void loadList(){

        IapClient iapClient = Iap.getIapClient(HmsInAppPurchaseList.this);
        Task<ProductInfoResult> task = iapClient.obtainProductInfo(productPurchase.createProductInfoReq());
        task.addOnSuccessListener(new OnSuccessListener<ProductInfoResult>() {
            @Override
            public void onSuccess(final ProductInfoResult result) {
                if (result != null && !result.getProductInfoList().isEmpty()) {

                    // GET THE LIST OF PRODUCTS ...
                    products.clear();
                    products = productPurchase.getProducts(result.getProductInfoList());

                    final SimpleAdapter simAdapter = new SimpleAdapter(HmsInAppPurchaseList.this, products , R.layout.item_subscription,
                            new String[]{item_image, item_name, item_price,item_desc,item_state}, new int[]{
                            R.id.item_image, R.id.item_name, R.id.item_price,R.id.item_desc,R.id.item_status})
                    {
                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {
                            View view = convertView;
                            for(int i=0;i<products.size();i++) {
                                if (view == null) {
                                    LayoutInflater vi = getLayoutInflater();
                                    view = vi.inflate(R.layout.item_subscription, null);
                                    FButton btnStatus = view.findViewById(R.id.item_status);
                                    TextView txtName = view.findViewById(R.id.item_name);
                                    TextView txtDesc = view.findViewById(R.id.item_desc);
                                    TextView txtPrice = view.findViewById(R.id.item_price);
                                    ImageView img = view.findViewById(R.id.item_image);
                                    txtName.setText(products.get(position).get(item_name).toString());
                                    txtDesc.setText(products.get(position).get(item_desc).toString());
                                    txtPrice.setText(products.get(position).get(item_price).toString());
                                    img.setBackgroundResource(Integer.parseInt(products.get(position).get(item_image).toString()));
                                    if(productType == 0 || productType == 1) {
                                        btnStatus.setVisibility(View.INVISIBLE);
                                    }else{
                                        btnStatus.setVisibility(View.VISIBLE);
                                    }
                                    btnStatus.setText(products.get(position).get(item_state).toString());
                                    btnStatus.setTag((String) products.get(position).get(HmsInAppPurchaseList.this.item_productId));
                                    if(products.get(position).get(item_state).equals("ACTIVE PLAN")){
                                        btnStatus.setTextColor(getApplicationContext().getResources().getColor(R.color.fbutton_color_pomegranate));
                                        btnStatus.setButtonColor(getApplicationContext().getResources().getColor(R.color.fbutton_color_orange));
                                        btnStatus.setOnClickListener(getDetailSubscriptionPage());
                                    }else{
                                        btnStatus.setTextColor(getApplicationContext().getResources().getColor(R.color.fbutton_color_clouds));
                                        btnStatus.setOnClickListener(buySubscription());
                                    }
                                }
                            }
                            return view;
                        }
                    };
                    listView.setAdapter(simAdapter);
                    listView.requestLayout();
                    simAdapter.notifyDataSetChanged();
                    if(productType == 0 || productType == 1) {
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                                productId = (String) products.get(pos).get(HmsInAppPurchaseList.this.item_productId);
                                productPurchase.gotoPayment(HmsInAppPurchaseList.this, productId, productType);
                                alreadyLoaded = false;
                            }
                        });
                    }

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                Log.e(TAG, e.getMessage());
                if (e instanceof IapApiException) {
                    IapApiException iapApiException = (IapApiException) e;
                    int returnCode = iapApiException.getStatusCode();
                    if (returnCode == OrderStatusCode.ORDER_HWID_NOT_LOGIN) {
                        Toast.makeText(HmsInAppPurchaseList.this, "Please sign in to the app with a HUAWEI ID.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(HmsInAppPurchaseList.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(HmsInAppPurchaseList.this, "error", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_CODE_BUY) {
            if (data == null) {
                Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
                return;
            }
            PurchaseResultInfo purchaseResultInfo = Iap.getIapClient(this).parsePurchaseResultInfoFromIntent(data);
            switch (purchaseResultInfo.getReturnCode()) {
                case OrderStatusCode.ORDER_STATE_SUCCESS:
                    // verify signature of payment results.
                    boolean success = CipherUtil.doCheck(purchaseResultInfo.getInAppPurchaseData(), purchaseResultInfo.getInAppDataSignature(), Key.getPublicKey());
                    if (success) {
                        if(productType == IapClient.PriceType.IN_APP_SUBSCRIPTION) {

                            productPurchase.refreshSubscription(true);
                            loadProductList();
                            try {
                                JSONObject jsonObj = new JSONObject(purchaseResultInfo.getInAppPurchaseData());
                                System.out.println("purchaseToken >>>>" + jsonObj.getString("purchaseToken"));
                                System.out.println("purchaseToken >>>>" + jsonObj.getString("subscriptionId"));
                                HashMap<String, Object> subscriptionProduct = new HashMap<>();
                                subscriptionProduct.put("userId", "YOUR_MAIL_ID");
                                subscriptionProduct.put("purchaseToken", jsonObj.getString("purchaseToken"));
                                subscriptionProduct.put("subscriptionId", jsonObj.getString("subscriptionId"));
                                subscriptionProduct.put("subIsvalid", jsonObj.getBoolean("subIsvalid"));
                                subscriptionProduct.put("productId", jsonObj.getString("productId"));
                                subscriptionProduct.put("productName", jsonObj.getString("productName"));
                                subscriptionProduct.put("purchaseState", jsonObj.getInt("purchaseState"));
                                subscriptionProduct.put("currency", jsonObj.getString("currency"));
                                subscriptionProduct.put("price", jsonObj.getInt("price"));
                                subscriptionProduct.put("purchaseTime", jsonObj.getLong("purchaseTime"));
                                subscriptionProduct.put("country", jsonObj.getString("country"));
                                subscriptionProduct.put("expirationDate", jsonObj.getLong("expirationDate"));


                                Call<String> call = retrofitInterface.addSubscriptionProduct(subscriptionProduct);
                                call.enqueue(new Callback<String>() {
                                    @Override
                                    public void onResponse(Call<String> call, Response<String> response) {
                                        System.out.println("HERE >>" + response);
                                        if (response.code() == 200) {
                                            Toast.makeText(HmsInAppPurchaseList.this, "Subscription Product Send Successfully..", Toast.LENGTH_LONG).show();
                                        } else {
                                            Toast.makeText(HmsInAppPurchaseList.this, "Subscription Product Send Un-Successfully..", Toast.LENGTH_LONG).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<String> call, Throwable t) {
                                        Toast.makeText(HmsInAppPurchaseList.this, t.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                });

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }else if(productType == IapClient.PriceType.IN_APP_NONCONSUMABLE) {
                            showNonConsumedResult(true);
                        } else {
                          /*  Consumes a consumable after the consumable is delivered to a user who has completed payment.*/
                            productPurchase.consumeOwnedPurchase(this, purchaseResultInfo.getInAppPurchaseData(), productIds);
                        }
                    } else {
                        Toast.makeText(this, "Pay successful,sign failed", Toast.LENGTH_SHORT).show();
                    }
                    return;
                case OrderStatusCode.ORDER_STATE_CANCEL:
                    // The User cancels payment.
                    Toast.makeText(this, "user cancel", Toast.LENGTH_SHORT).show();
                    return;
                case OrderStatusCode.ORDER_PRODUCT_OWNED:
                    // The user has already owned the product.
                    if(productType == IapClient.PriceType.IN_APP_NONCONSUMABLE) {
                        Utility.commonStorage(this, productIds.get(0));
                        showNonConsumedResult(true);
                    }
                    Toast.makeText(this, "you have owned the product", Toast.LENGTH_SHORT).show();

                    return;
                case OrderStatusCode.ORDER_STATE_FAILED:
//                        productPurchase.checkUserOwnedTheProduct(this, -1);
                    break;

            }
            return;
        }else{
         //   loadProductList();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(HmsInAppPurchaseList.this, GameActivity.class));
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!alreadyLoaded) {
            loadProductList();
            alreadyLoaded = true;
        }
    }

    private View.OnClickListener getDetailSubscriptionPage() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object data = v.getTag();
                if (data instanceof String) {
                    String productId = (String) data;
                    showSubscription(productId);
                }
            }
        };
    }

    private View.OnClickListener buySubscription() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object data = v.getTag();
                if (data instanceof String) {
                    String productId = (String) data;
                    productPurchase.gotoPayment(HmsInAppPurchaseList.this, productId, productType);
                }
            }
        };
    }

    private View.OnClickListener commonButtonFunction() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(btnCommon.getText().toString().equalsIgnoreCase("SUBSCRIPTION")){
                   showSubscription("");
               }else if(btnCommon.getText().toString().equalsIgnoreCase("PURCHASED")){
                   startActivity(new Intent(HmsInAppPurchaseList.this, PurchaseHistoryActivity.class));
               }
            }
        };
    }

    public void showSubscription(String productId) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uri.parse("pay://com.huawei.hwid.external/subscriptions")
                .buildUpon()
                .appendQueryParameter("package", "com.huawei.hmsinapppurchaseexample")
                .appendQueryParameter("appid", "YOUR_APPID")
                .appendQueryParameter("sku", productId)
                .build();
        intent.setData(uri);
        startActivityForResult(intent,101);
    }

    public void showNonConsumedResult(boolean isPurchased){
        if(isPurchased) {
            llAlreadyPurchased.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        }else{
            llAlreadyPurchased.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            loadList();
        }

    }

}
