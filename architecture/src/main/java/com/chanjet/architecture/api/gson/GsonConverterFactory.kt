package com.chanjet.architecture.api.gson

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.NullPointerException
import java.lang.reflect.Type

/**
 * A { @linkplain Converter.Factory converter } which uses Gson for JSON.
 * <p>
 * Because Gson is so flexible in the types it supports, this converter assumes that it can handle
 * all types. If you are mixing JSON serialization with something else (such as protocol buffers),
 * you must { @linkplain Retrofit.Builder#addConverterFactory(Converter.Factory) add this instance }
 * last to allow the other converters a chance to see their types.
 */
class GsonConverterFactory private constructor(private var gson: Gson?) : Converter.Factory() {

    init {
        if (gson == null) throw NullPointerException("gson == null")
    }

    override fun responseBodyConverter(type: Type?, annotations: Array<Annotation>?,
                                       retrofit: Retrofit?): Converter<ResponseBody, *> {
        val adapter = gson?.getAdapter(TypeToken.get(type!!))
        return GsonResponseBodyConverter(gson!!, adapter!!)
    }

    override fun requestBodyConverter(type: Type?,
                                      parameterAnnotations: Array<Annotation>?, methodAnnotations: Array<Annotation>?, retrofit: Retrofit?): Converter<*, RequestBody> {
        val adapter = gson?.getAdapter(TypeToken.get(type!!))
        return GsonRequestBodyConverter(gson, adapter)
    }

    companion object {

        /**
         * Create an instance using `gson` for conversion. Encoding to JSON and
         * decoding from JSON (when no charset is specified by a header) will use UTF-8.
         */
        @JvmOverloads
        fun create(gson: Gson = Gson()): GsonConverterFactory {
            return GsonConverterFactory(gson)
        }
    }
}