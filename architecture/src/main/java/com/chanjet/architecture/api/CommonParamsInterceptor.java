package com.chanjet.architecture.api;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.chanjet.architecture.bean.DeviceInfo;
import com.chanjet.architecture.bean.HeaderParams;
import com.chanjet.architecture.bean.RequestParams;
import com.chanjet.architecture.util.LogUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import timber.log.Timber;

/**
 * *公共参数
 * 1) Header
 * 2) Query Param
 * 3) POST Param form-data
 * 4) POST Param x-www-form-urlencoded
 */

public class CommonParamsInterceptor implements Interceptor {

    private Context mContext;
    private DeviceInfo mDeviceInfo;
    private GlobalToken mGlobalToken;
    private ResponseInterceptor mResponseInterceptor;
    private HeaderParams mHeader;

    public CommonParamsInterceptor() {
    }

    public CommonParamsInterceptor(Context context, GlobalToken globalToken, DeviceInfo deviceInfo, HeaderParams header, ResponseInterceptor responseInterceptor) {
        mContext = context;
        mGlobalToken = globalToken;
        mDeviceInfo = deviceInfo;
        mHeader = header;
        mResponseInterceptor = responseInterceptor;
    }

    private static Map<String, String> commonParams;

    public synchronized static void setCommonParam(Map<String, String> commonParams) {
        if (commonParams != null) {
            if (CommonParamsInterceptor.commonParams != null) {
                CommonParamsInterceptor.commonParams.clear();
            } else {
                CommonParamsInterceptor.commonParams = new HashMap<>();
            }
            for (String paramKey : commonParams.keySet()) {
                CommonParamsInterceptor.commonParams.put(paramKey, commonParams.get(paramKey));
            }
        }
    }

    public synchronized static void updateOrInsertCommonParam(@NonNull String paramKey, @NonNull String paramValue) {
        if (commonParams == null) {
            commonParams = new HashMap<>();
        }
        commonParams.put(paramKey, paramValue);
    }

    @Override
    public synchronized Response intercept(Chain chain) throws IOException {
        Request request = rebuildRequest(chain.request());
        Response response = chain.proceed(request);
        // 输出返回结果
        try {
            ResponseBody responseBody = response.peekBody(Long.MAX_VALUE);
            byte[] bytes = responseBody.string().getBytes();
            loginFilter(bytes, responseBody.contentType().charset());
            LogUtils.Companion.dd("commonResponse", "Request: " + chain.request() + "Headers:" + bodyToString(request.headers()) + "----Params:" + bodyToString(request.body()));
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.Companion.dd("commonResponse", e.toString());
        }
//        LogUtils.Companion.dd("commonResponse", "Request: " + chain.request() + "Headers:" + bodyToString(request.headers()) + "----Params:" + bodyToString(request.body()));
        //        saveCookies(response, request.url().toString());
        return response;
    }

    private Request rebuildRequest(Request request) throws IOException {
        Request newRequest;
        if ("POST".equals(request.method())) {
            newRequest = rebuildPostRequest(request);
        } else if ("GET".equals(request.method())) {
            newRequest = rebuildGetRequest(request);
        } else {
            newRequest = request;
        }
//        LogUtils.Companion.dd("requestUrl:", "requestUrl: " + newRequest.url().toString());
        return newRequest;
    }

    /**
     * 对post请求添加统一参数
     */
    private Request rebuildPostRequest(Request request) {
//        if (commonParams == null || commonParams.size() == 0) {
//            return request;
//        }
        Map<String, String> signParams = new HashMap<>(); // 假设你的项目需要对参数进行签名
        RequestBody originalRequestBody = request.body();
        assert originalRequestBody != null;
        RequestBody newRequestBody;
        if (originalRequestBody instanceof FormBody) { // 传统表单
            FormBody.Builder builder = new FormBody.Builder();
            FormBody requestBody = (FormBody) request.body();
            int fieldSize = requestBody == null ? 0 : requestBody.size();
            for (int i = 0; i < fieldSize; i++) {
                builder.add(requestBody.name(i), requestBody.value(i));
                signParams.put(requestBody.name(i), requestBody.value(i));
            }
            if (commonParams != null && commonParams.size() > 0) {
                signParams.putAll(commonParams);
                for (String paramKey : commonParams.keySet()) {
                    builder.add(paramKey, commonParams.get(paramKey));
                }
            }
            // ToDo 此处可对参数做签名处理 signParams
            /**
             * String sign = SignUtil.sign(signParams);
             * builder.add("sign", sign);
             */
            newRequestBody = builder.build();
        } else if (originalRequestBody instanceof MultipartBody) { // 文件
            MultipartBody requestBody = (MultipartBody) request.body();
            MultipartBody.Builder multipartBodybuilder = new MultipartBody.Builder();
            if (requestBody != null) {
                for (int i = 0; i < requestBody.size(); i++) {
                    MultipartBody.Part part = requestBody.part(i);
                    multipartBodybuilder.addPart(part);

                    /*
                     上传文件时，请求方法接收的参数类型为RequestBody或MultipartBody.Part参见ApiService文件中uploadFile方法
                     RequestBody作为普通参数载体，封装了普通参数的value; MultipartBody.Part即可作为普通参数载体也可作为文件参数载体
                     当RequestBody作为参数传入时，框架内部仍然会做相关处理，进一步封装成MultipartBody.Part，因此在拦截器内部，
                     拦截的参数都是MultipartBody.Part类型
                     */

                    /*
                     1.若MultipartBody.Part作为文件参数载体传入，则构造MultipartBody.Part实例时，
                     需使用MultipartBody.Part.createFormData(String name, @Nullable String filename, RequestBody body)方法，
                     其中name参数可作为key使用(因为你可能一次上传多个文件，服务端可以此作为区分)且不能为null，
                     body参数封装了包括MimeType在内的文件信息，其实例创建方法为RequestBody.create(final @Nullable MediaType contentType, final File file)
                     MediaType获取方式如下：
                     String fileType = FileUtil.getMimeType(file.getAbsolutePath());
                     MediaType mediaType = MediaType.parse(fileType);

                     2.若MultipartBody.Part作为普通参数载体，建议使用MultipartBody.Part.createFormData(String name, String value)方法创建Part实例
                       name可作为key使用，name不能为null,通过这种方式创建的实例，其RequestBody属性的MediaType为null；当然也可以使用其他方法创建
                     */

                    /*
                      提取非文件参数时,以RequestBody的MediaType为判断依据.
                      此处提取方式简单暴力。默认part实例的RequestBody成员变量的MediaType为null时，part为非文件参数
                      前提是:
                      a.构造RequestBody实例参数时，将MediaType设置为null
                      b.构造MultipartBody.Part实例参数时,推荐使用MultipartBody.Part.createFormData(String name, String value)方法，或使用以下方法
                        b1.MultipartBody.Part.create(RequestBody body)
                        b2.MultipartBody.Part.create(@Nullable Headers headers, RequestBody body)
                        若使用方法b1或b2，则要求

                      备注：
                      您也可根据需求修改RequestBody的MediaType，但尽量保持外部传入参数的MediaType与拦截器内部添加参数的MediaType一致，方便统一处理
                     */

                    MediaType mediaType = part.body().contentType();
                    if (mediaType == null) {
                        String normalParamKey;
                        String normalParamValue;
                        try {
                            normalParamValue = getParamContent(requestBody.part(i).body());
                            Headers headers = part.headers();
                            if (!TextUtils.isEmpty(normalParamValue) && headers != null) {
                                for (String name : headers.names()) {
                                    String headerContent = headers.get(name);
                                    if (!TextUtils.isEmpty(headerContent)) {
                                        String[] normalParamKeyContainer = headerContent.split("name=\"");
                                        if (normalParamKeyContainer.length == 2) {
                                            normalParamKey = normalParamKeyContainer[1].split("\"")[0];
                                            signParams.put(normalParamKey, normalParamValue);
                                            break;
                                        }
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            if (commonParams != null && commonParams.size() > 0) {
                signParams.putAll(commonParams);
                for (String paramKey : commonParams.keySet()) {
                    // 两种方式添加公共参数
                    // method 1
                    multipartBodybuilder.addFormDataPart(paramKey, commonParams.get(paramKey));
                    // method 2
//                    MultipartBody.Part part = MultipartBody.Part.createFormData(paramKey, commonParams.get(paramKey));
//                    multipartBodybuilder.addPart(part);
                }
            }
            // ToDo 此处可对参数做签名处理 signParams
            /**
             * String sign = SignUtil.sign(signParams);
             * multipartBodybuilder.addFormDataPart("sign", sign);
             */
            newRequestBody = multipartBodybuilder.build();
        } else {
            try {
                JSONObject jsonObject;
                if (originalRequestBody.contentLength() == 0) {
                    jsonObject = new JSONObject();
                } else {
                    jsonObject = new JSONObject(getParamContent(originalRequestBody));
                }
                if (commonParams != null && commonParams.size() > 0) {
                    for (String commonParamKey : commonParams.keySet()) {
                        jsonObject.put(commonParamKey, commonParams.get(commonParamKey));
                    }
                }
                // ToDo 此处可对参数做签名处理
                /**
                 * String sign = SignUtil.sign(signParams);
                 * jsonObject.put("sign", sign);
                 */
                newRequestBody = RequestBody.create(originalRequestBody.contentType(), jsonObject.toString());
                LogUtils.Companion.dd("signParams", getParamContent(newRequestBody));

            } catch (Exception e) {
                newRequestBody = originalRequestBody;
                e.printStackTrace();
            }
        }
//        可根据需求添加或修改header,此处制作示意
//       return request.newBuilder()
//                .addHeader("header1", "header1")
//                .addHeader("header2", "header2")
//                .method(request.method(), newRequestBody)
//                .build();
        return request.newBuilder().method(request.method(), newRequestBody).build();
    }

    /**
     * 获取常规post请求参数
     */
    private String getParamContent(RequestBody body) throws IOException {
        Buffer buffer = new Buffer();
        body.writeTo(buffer);
        return buffer.readUtf8();
    }

    /**
     * 对get请求做统一参数处理
     */
    private Request rebuildGetRequest(Request request) {
        if (commonParams == null || commonParams.size() == 0) {
            return request;
        }
        String url = request.url().toString();
        int separatorIndex = url.lastIndexOf("?");
        StringBuilder sb = new StringBuilder(url);
        if (separatorIndex == -1) {
            sb.append("?");
        }
        for (String commonParamKey : commonParams.keySet()) {
            sb.append("&").append(commonParamKey).append("=").append(commonParams.get(commonParamKey));
        }
        Request.Builder requestBuilder = request.newBuilder();
        return requestBuilder.url(sb.toString()).build();
    }


    //拦截登录
    private void loginFilter(byte[] bytes, Charset charset) {
        try {
            String data = null;
            if (charset == null) {
                data = new String(bytes);
            } else {
                data = new String(bytes, charset);
            }
            JSONObject jsonObject = new JSONObject(data);
//            JSONObject statusObject = jsonObject.getJSONObject("status");
            long statusCode = jsonObject.getLong("code");
            String statusCodeStr = String.valueOf(statusCode);
            boolean expired = statusCodeStr.endsWith("03000452");
            if (expired && mContext != null) {
                Intent intent = new Intent("android.intent.action.common.LoginReceiver");
                mContext.sendBroadcast(intent);
            } else if (mResponseInterceptor != null) {
                mResponseInterceptor.response(statusCode, jsonObject.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String bodyToString(final RequestBody request) {
        try {
            final RequestBody copy = request;
            final Buffer buffer = new Buffer();
            if (copy != null) {
                copy.writeTo(buffer);
            } else {
                return "";
            }
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }

    private static String bodyToString(final Headers headers) {
        Map<String, List<String>> params = headers.toMultimap();
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, List<String>> entry : params.entrySet()) {
            stringBuilder.append(entry.getKey()).append(":").append(entry.getValue());
        }
        return stringBuilder.toString();
    }

    public Map<String, String> getHeaderMap() {
        return mHeader.getParams();
    }


    public Map<String, Object> getHeaderParams() {
        Map<String, Object> commonParams = new HashMap<>();
//        commonParams.put("userId", mGlobalToken.getUserId());//用户ID
//        commonParams.put("fwToken", mGlobalToken.getFwToken());//用户登录后获取到的用户token
//        commonParams.put("brand", mDeviceInfo.getBrand()); // 手机品牌
//        commonParams.put("mid", mDeviceInfo.getModel());//手机型号
//        commonParams.put("w", mDeviceInfo.getScreenWidth());// 手机屏幕宽度
//        commonParams.put("h", mDeviceInfo.getScreenHeight());// 手机屏幕高度
//        commonParams.put("channel", mDeviceInfo.getChannel());//渠道
//        commonParams.put("IMEI", mDeviceInfo.getIMEI());//IMEI
//        commonParams.put("mac", mDeviceInfo.getMac());//MAC地址
//        commonParams.put("udid", mDeviceInfo.getUdid());//UDID
//        commonParams.put("platform", "android");//平台:android,iphone,ipad
//        commonParams.put("os", mDeviceInfo.getSdk());   // 系统版本
//        commonParams.put("version", mDeviceInfo.getVersionName()); // 客户端版本名称,版本号格式：[1~9]+.[0~9].[0~9]
//        commonParams.put("version_code", mDeviceInfo.getVersionCode());   // 客户端版本号
//        commonParams.put("network", NetworkHelpers.getNetworkType(mContext));//客户端当前使用网络环境 例如:android,iphone,ipad,WIFI,unknown
//        commonParams.put("Accept-Encoding", mDeviceInfo.getAcceptEncoding());  //1 压缩,0 不压缩 //控制返回结果是否启用压缩
//        commonParams.put("encrypt", mDeviceInfo.getEncrypt());// 1加密, 0 非加密，返回结果是否启用加密,加密类型与当前客户端请求加密方式及加密版本保持一致
//        commonParams.put("appid", mDeviceInfo.getPackageName());  //手机APPID（客户端包名）
//        commonParams.put("guid", mDeviceInfo.getDeviceId());  //客户端唯一ID
//        commonParams.put("appType", mDeviceInfo.getAppType()); //应用类型 例如：OMS,TMS,WMS...y
        commonParams.put("Content-Type", "application/json");
        commonParams.put("Accept", "application/json");
        Map<String, String> headerMap = getHeaderMap();
        if (headerMap != null) {
            for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                if (!TextUtils.isEmpty(entry.getValue())) {
                    commonParams.put(entry.getKey(), entry.getValue());
                }
            }
        }

        return commonParams;
    }
}
