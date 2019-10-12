package com.chanjet.architecture.api

/**
 * Created by zhangshaofang on 2018/11/22.
 */
interface ResponseInterceptor {
    fun response(statusCode: Long, result: String)
}
