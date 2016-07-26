package com.bailibao.util;

import android.util.Base64;

import java.io.UnsupportedEncodingException;

/**
 * Created by Administrator on 2016/5/4.
 */
public class Base64Util {

    /**
     * 加密 ADN MD5
     * @param encode
     * @return
     */
    public static  String encodeAndMD5(String encode){
        byte[] decode = Base64.encode(MD5Util.encode(encode).getBytes(),Base64.DEFAULT);
        String str = null;
        try {
            str = new String(decode,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return  str;
    }

    /**
     * 加密
     */

    public static  String encode(String encode){
        byte[] decode = Base64.encode(encode.getBytes(),Base64.DEFAULT);
        String str1 = decode.toString();
        String result = null;
        try {
            String str  = new String(decode,"UTF-8");
            //包含“\n”
            result = str.substring(0,str.length() -1);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return  result;
    }

    /**
     * 解密
     * @param str
     * @return
     */
    public static  String decode(String str){
        byte[] decode = Base64.decode(str,Base64.DEFAULT);
        String result = null;
        try {
            result = new String(decode,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return  result;
    }

}
