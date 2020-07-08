package com.huawei.hmsinapppurchaseexample.util;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RetrofitInterface {
    @POST("/addSubscriptionProduct")
    Call<String> addSubscriptionProduct(@Body HashMap<String, Object> map);
}
