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

package com.android.example.github.api

import androidx.lifecycle.LiveData
import com.android.example.github.dto.GetClientPrivateKey
import com.android.example.github.dto.GetClientPrivateKeyBean
import com.android.example.github.vo.Contributor
import com.android.example.github.vo.Repo
import com.android.example.github.vo.User
import com.chanjet.architecture.api.ApiResponse
import com.chanjet.architecture.api.BaseResponse
import com.chanjet.architecture.api.CommonObserver
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

/**
 * REST API access points
 */
interface YunService {
    companion object {
        const val SERVER_URL: String = "https://cjdev-app-yc.chanpay.co:18888"
        const val PLATFORM_PUBLIC_KEY: String = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCpLWBjWMuu6hrm8Hx+HDzg/z15RRPJDS0Tj0XkpUVEPzQ8Yx1KzoSk51Z6j/7CF1NUv4Fg4IUJRO2cFuaWMMgsfkXkp9WcploTX4g1FKjtbXKpdegoJgyq6fxUApQeosN4Fy9bvawc14ExRlRALgqQ5nQI8lCCY7SU36mrKvVovwIDAQAB"
    }

    @GET("users/{login}")
    fun getUser(@Path("login") login: String): LiveData<ApiResponse<User>>

    @GET("users/{login}/repos")
    fun getRepos(@Path("login") login: String): LiveData<ApiResponse<List<Repo>>>

    @GET("repos/{owner}/{name}")
    fun getRepo(
            @Path("owner") owner: String,
            @Path("name") name: String
    ): LiveData<ApiResponse<Repo>>

    @GET("repos/{owner}/{name}/contributors")
    fun getContributors(
            @Path("owner") owner: String,
            @Path("name") name: String
    ): LiveData<ApiResponse<List<Contributor>>>

    @GET("search/repositories")
    fun searchRepos(@Query("q") query: String): LiveData<ApiResponse<RepoSearchResponse>>

    @GET("search/repositories")
    fun searchRepos(@Query("q") query: String, @Query("page") page: Int): Call<RepoSearchResponse>

    @JvmSuppressWildcards
    @Headers("Content-Type: application/json", "Accept: application/json")
    @POST("/v4/yc/getClientPrivateKey")
    fun getClientPrivateKey(@Body body: Map<String, Any>?): LiveData<ApiResponse<BaseResponse<GetClientPrivateKey>>>

    @Headers("Content-Type: application/json", "Accept: application/json")
    @POST("/v3/getToken")
    fun getToken(@Body body: Map<String, Any>?): LiveData<ApiResponse<RepoSearchResponse>>
}
