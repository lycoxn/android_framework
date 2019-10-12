package com.chanjet.architecture.api

import android.content.Context

/**
 * 登陆信息处理（缓存）
 */

class GlobalToken( val context: Context)//    private static final String LOGIN_RESULT = "GLOBALTOKEN+LOGIN_RESULT";
//    /**
//     * 用户唯一标识
//     */
//    private long userId;
//    /**
//     * 登录认证信息
//     */
//    private String fwToken;
//
//    private LoginResult loginResult;
//    private long firstInvalidTime;
//
//    public GlobalToken(Context context) {
//        String loginResultStr = PrfUtils.getPrfparams(context, LOGIN_RESULT);
//        if (!TextUtils.isEmpty(loginResultStr)) {
//            try {
//                loginResult = deSerialization(loginResultStr);
//                if (loginResult != null) {
//                    setUserId(loginResult.getId());
//                    setFwToken(loginResult.getToken());
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (ClassNotFoundException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    public long getUserId() {
//        return userId;
//    }
//
//    public void setUserId(long userId) {
//        this.userId = userId;
//    }
//
//    public String getFwToken() {
//        return fwToken;
//    }
//
//    public void setFwToken(String fwToken) {
//        firstInvalidTime = 0;
//        this.fwToken = fwToken;
//    }
//
//    public void saveLoginResult(Context context, LoginResult loginResult) {
//        try {
//            this.loginResult = loginResult;
//            setUserId(loginResult.getId());
//            setFwToken(loginResult.getToken());
//            PrfUtils.savePrfparams(context, LOGIN_RESULT, serialize(loginResult));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public LoginResult getLoginResult() {
//        return loginResult;
//    }
//
//    public boolean isLogin() {
//        return userId != 0 && !TextUtils.isEmpty(fwToken);
//
//    }
//
//    public void logout(Context context) {
//        userId = 0;
//        fwToken = null;
//        PrfUtils.remove(context, LOGIN_RESULT);
//    }
//
//    /**
//     * 序列化对象
//     */
//    private String serialize(LoginResult person) throws IOException {
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        ObjectOutputStream objectOutputStream = new ObjectOutputStream(
//                byteArrayOutputStream);
//        objectOutputStream.writeObject(person);
//        String serStr = byteArrayOutputStream.toString("ISO-8859-1");
//        serStr = java.net.URLEncoder.encode(serStr, "UTF-8");
//        objectOutputStream.close();
//        byteArrayOutputStream.close();
//        return serStr;
//    }
//
//    /**
//     * 反序列化对象
//     *
//     * @param str
//     * @return
//     * @throws IOException
//     * @throws ClassNotFoundException
//     */
//    private LoginResult deSerialization(String str) throws IOException,
//            ClassNotFoundException {
//        String redStr = java.net.URLDecoder.decode(str, "UTF-8");
//        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
//                redStr.getBytes("ISO-8859-1"));
//        ObjectInputStream objectInputStream = new ObjectInputStream(
//                byteArrayInputStream);
//        LoginResult loginResult = (LoginResult) objectInputStream.readObject();
//        objectInputStream.close();
//        byteArrayInputStream.close();
//        return loginResult;
//    }
//
//    public void setFirstInvalidTime(long firstInvalidTime) {
//        this.firstInvalidTime = firstInvalidTime;
//    }
//
//    public long getFirstInvalidTime() {
//        return firstInvalidTime;
//    }
