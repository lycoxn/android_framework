/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.chanjet.architecture.di


import android.content.Context
import com.chanjet.architecture.api.CommonParamsInterceptor
import com.chanjet.architecture.api.GlobalToken
import com.chanjet.architecture.api.LiveDataCallAdapterFactory
import com.chanjet.architecture.api.ResponseInterceptor
import com.chanjet.architecture.api.gson.GsonConverterFactory
import com.chanjet.architecture.bean.DeviceInfo
import com.chanjet.architecture.bean.HeaderParams
import com.chanjet.architecture.util.DeviceUtils
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class ApiModule(private val mContext: Context) {

    private var mResponseInterceptor: ResponseInterceptor? = null

    constructor(context: Context, responseInterceptor: ResponseInterceptor) : this(context) {
        mResponseInterceptor = responseInterceptor
    }

//    @Provides
//    @Singleton
//    fun provideCacheCommonService(restAdapter: Retrofit, cacheDatabase: CacheDatabase, appExecutors: AppExecutors): CommonService {
//        val commonService = restAdapter.create(CommonService::class.java!!)
//        val handler = DynamicProxy(commonService, cacheDatabase, mContext, appExecutors)
//        return Proxy.newProxyInstance(handler.javaClass.getClassLoader(), commonService
//                .getClass().getInterfaces(), handler) as CommonService
//    }

//    @Provides
//    fun provideCacheDatabase(): CacheDatabase {
//        return Room.databaseBuilder(mContext,
//                CacheDatabase::class.java!!, "hivescm_cache").build()
//    }

    @Singleton
    @Provides
    fun provideGlobalToken(): GlobalToken {
        return GlobalToken(mContext)
    }

    @Singleton
    @Provides
    fun provideDeviceInfo(): DeviceInfo {
        return DeviceUtils.getDeviceInfo(mContext)
    }

    @Singleton
    @Provides
    fun provideHeader(baseUrl: String): HeaderParams {
        val headerParams = HeaderParams()
        headerParams.baseHost = baseUrl
        return headerParams
    }

    @Singleton
    @Provides
    fun provideCommonParamsInterceptor(globalToken: GlobalToken, deviceInfo: DeviceInfo, header: HeaderParams): CommonParamsInterceptor {
        return CommonParamsInterceptor(mContext, globalToken, deviceInfo, header, mResponseInterceptor)
    }

//    @Singleton
//    @Provides
//    fun provideApiGeneratoryFactory(commonService: CommonService): ApiGeneratoryFactory {
//        return ApiGeneratoryFactory(commonService)
//    }


    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient, baseUrl: String): Retrofit {
        return Retrofit.Builder().client(okHttpClient)
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
//                .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
                .addCallAdapterFactory(LiveDataCallAdapterFactory())
                .build()
    }


    @Provides
    @Singleton
    fun provideOkHttpClient(commonParamsInterceptor: CommonParamsInterceptor): OkHttpClient {
        val builder = OkHttpClient.Builder()
        builder.connectTimeout((15 * 1000).toLong(), TimeUnit.MILLISECONDS)
                .readTimeout((15 * 1000).toLong(), TimeUnit.MILLISECONDS)
        builder.addInterceptor(commonParamsInterceptor)
        return builder.build()
    }
}
