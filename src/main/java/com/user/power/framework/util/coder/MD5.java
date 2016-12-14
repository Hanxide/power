package com.user.power.framework.util.coder;

import java.security.MessageDigest;

public class MD5 {

    /**
       获得字符串s的md5 hash值.
    */
    public static String getMD5(String s) {
        String md5sum = "NULL";
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] hash = md5.digest(s.getBytes());
            md5sum = dumpBytes(hash);
        } catch (Exception e) {}
        return md5sum;
    } // end getMD5()


    private static String dumpBytes(byte[] bytes) {
        int size = bytes.length;
        StringBuffer sb = new StringBuffer(size*2);
        String s;
        for (int i=0; i<size; i++) {
            s = Integer.toHexString(bytes[i]);
            if (s.length()==8)      // -128 <= bytes[i] < 0
                sb.append(s.substring(6));
            else if(s.length()==2)  // 16 <= bytes[i] < 128
                sb.append(s);
            else sb.append("0"+s);  // 0 <= bytes[i] < 16
        }
        return sb.toString();
    }
}