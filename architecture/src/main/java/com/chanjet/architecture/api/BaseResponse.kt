package com.chanjet.architecture.api


data class BaseResponse<T>(val code: String? = null ,   //返回码, 00：成功 其他参考错误码表
                           val message: String? = null   ,   //返回消息
                           val data: T? = null,
                           val sign: String? = null)//客户端公钥签名resData ）)