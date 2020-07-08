package com.huawei.hmsinapppurchaseexample.util;
/**
 * Name of the project QUIZ MANIA.
 * Created by Sanghati Mukherjee.
 * Huawei Technologies Co., Ltd.
 * sanghati.mukherjee@huawei.com
 */
public class Key {
    private static final String publicKey = "PUT YOUR PUBLIC KEY HERE ....";

     // get the publicKey of the application
     //During the encoding process, avoid storing the public key in clear text.

    public static String getPublicKey(){
        return publicKey;
    }
}
