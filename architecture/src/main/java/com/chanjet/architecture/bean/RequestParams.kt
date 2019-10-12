package com.chanjet.architecture.bean

import com.google.gson.Gson

class RequestParams {
    var header: Map<String, Any>? = null
    var body: Map<String, Any>? = null
    override fun toString(): String {
        val gson = Gson()
        return gson.toJson(this)
    }
}
