package com.android.example.github.proutil;

public class Config {

    public static final boolean ISDEBUGM = true;

//    public static UserBaseInfoBean userInfo;//登录状态
//    public static HsyServerProviderBaseInfoSel UserBean;//该商户基本信息
    public static final String RESPONSE_SUCCESSED_CODE = "00";
    public static String register_us = null;
    public static boolean is_LOGIN = false;

    private static String des_key = "";//主密钥
    //服务器路径 测试平台固定公钥
    public static final String httpURL = "https://cjdev-app-yc.chanpay.co:18888";
    public static final String PLATFORM_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCpLWBjWMuu6hrm8Hx+HDzg/z15RRPJDS0Tj0XkpUVEPzQ8Yx1KzoSk51Z6j/7CF1NUv4Fg4IUJRO2cFuaWMMgsfkXkp9WcploTX4g1FKjtbXKpdegoJgyq6fxUApQeosN4Fy9bvawc14ExRlRALgqQ5nQI8lCCY7SU36mrKvVovwIDAQAB";

    //生产平台固定公钥
//    public static final String httpURL = "https://qkapp-yc.chanpay.com";
//    public static final String PLATFORM_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCJ6fU01qXmbZ7cBKscT677TSmC8U9jFIPOcqvvW8xoAZUIJWdG0y9V0HNw8+2rlkakxn8lJlPjcpMzzKQ5JLKyESq5jnNmGzOGIn8Jqw2FQYSXJ7D5cOzn/znXIMGTJuuX9DBF1BAgXVTEu6pLm/9LwBf8qymduI4L128R0Dy68wIDAQAB";

    //平台固定私钥
    public static String key = "";


    public static String getDes_key() {
        return getKey(ISDEBUGM);
    }

    private static String getKey(boolean isDebug) {
        if (StringUtils.isEmpty(des_key)) {
            try {
                des_key = StringUtils.getKeys();
            } catch (Exception e) {
                return getKey(ISDEBUGM);
            }
        }
        return des_key;
    }

    public static String getMerchantSendHost() {
        if (ISDEBUGM) return "https://cjdev-app-dxy.chanpay.co:18888";
        else return "https://qkapp-dxy.chanpay.com";
    }
}