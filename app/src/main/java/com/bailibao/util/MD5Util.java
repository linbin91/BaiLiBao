package com.bailibao.util;

import java.security.MessageDigest;

/**
 * Created by Administrator on 2016/5/4.
 */
public class MD5Util {

    public static String encode(String str) {
        String md5Key;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(str.getBytes());
            md5Key = bytesToHexString(mDigest.digest());
        } catch (Exception e) {
            return null;
        }
        return md5Key;
    }



    public static String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

}
