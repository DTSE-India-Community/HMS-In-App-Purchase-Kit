package com.huawei.hmsinapppurchaseexample.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Name of the project QUIZ MANIA.
 * Created by Sanghati Mukherjee.
 * Huawei Technologies Co., Ltd.
 * sanghati.mukherjee@huawei.com
 */
public class Utility {

    public static void commonStorage(Context context, String productIds){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        System.out.println("product id >> " + productIds);
        if (productIds.equalsIgnoreCase("NON_CONSUMABLE_PRODUCT_ID")) {
            editor.putBoolean("removeads", true);
        } else if (productIds.equalsIgnoreCase("CONSUMABLE_PRODUCT_ID")) {
            editor.putBoolean("gamelife", true);
            int coinLeft = preferences.getInt("coinNow", 100);
            int coinValue = coinLeft + 300;
            editor.putInt("coinNow", coinValue);
        }
        editor.apply();
    }

    public static void subscriptionSettings(String productId,boolean boolValue,Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        if (productId.equalsIgnoreCase("SUBCRIPTION_PRODUCT_ID_1")) {
            editor.putBoolean("silver_sub", boolValue);
        } else if (productId.equalsIgnoreCase("SUBCRIPTION_PRODUCT_ID_2")) {
            editor.putBoolean("gold_sub", boolValue);
        } else if (productId.equalsIgnoreCase("SUBCRIPTION_PRODUCT_ID_3")) {
            editor.putBoolean("diamond_sub", boolValue);
        }
        editor.apply();
    }
}
