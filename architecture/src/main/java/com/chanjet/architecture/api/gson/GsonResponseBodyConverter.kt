/*
 * Copyright (C) 2015 Square, Inc.
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
package com.chanjet.architecture.api.gson

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import java.io.IOException
import java.io.Reader
import java.io.StringReader

import okhttp3.ResponseBody
import retrofit2.Converter
import timber.log.Timber
import java.util.Collections.replaceAll
import java.util.Collections.replaceAll


internal class GsonResponseBodyConverter<T>(private val gson: Gson, private val adapter: TypeAdapter<T>) : Converter<ResponseBody, T> {

    @Throws(IOException::class)
    override fun convert(value: ResponseBody): T? {
        val s = value.string()
        Timber.d("ResponseBodys: $s")
        val reader = StringReader(s)
        val jsonReader = gson.newJsonReader(reader)
        try {
            return adapter.read(jsonReader)
        } catch (e: Exception) {
            Timber.d("ResponseBody: " + "数据解析异常:" + javaClass.genericSuperclass)
            e.printStackTrace()
            return null
        } finally {
            value.close()
        }
    }
}
