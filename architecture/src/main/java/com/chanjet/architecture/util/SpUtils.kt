package com.chanjet.architecture.util

import android.content.Context
import android.content.SharedPreferences
import android.text.TextUtils

import com.google.gson.Gson

import java.lang.reflect.Type

/**
 * Created by liuyicen on 2019/3/9 5:41 PM.
 */

object SpUtils {
    private val DEFAULT_SP_NAME = "default_sp"

    //存放基本类型的数据
    fun put(context: Context, key: String, value: Any?) {
        if (value == null) return
        val type = value.javaClass.simpleName
        val sp = context.getSharedPreferences(DEFAULT_SP_NAME, Context.MODE_PRIVATE)
        val editor = sp.edit()
        if ("String" == type) {
            editor.putString(key, value as String?)
        } else if ("Integer" == type) {
            editor.putInt(key, (value as Int?)!!)
        } else if ("Boolean" == type) {
            editor.putBoolean(key, (value as Boolean?)!!)
        } else if ("Float" == type) {
            editor.putFloat(key, (value as Float?)!!)
        } else if ("Long" == type) {
            editor.putLong(key, (value as Long?)!!)
        }
        editor.commit()
    }

    //获取基本类型的数据
    operator fun get(context: Context, key: String, defaultObject: Any?): Any? {
        if (defaultObject == null) return null
        val type = defaultObject.javaClass.simpleName
        val sp = context.getSharedPreferences(DEFAULT_SP_NAME, Context.MODE_PRIVATE)
        if ("String" == type) {
            return sp.getString(key, defaultObject as String?)
        } else if ("Integer" == type) {
            return sp.getInt(key, (defaultObject as Int?)!!)
        } else if ("Boolean" == type) {
            return sp.getBoolean(key, (defaultObject as Boolean?)!!)
        } else if ("Float" == type) {
            return sp.getFloat(key, (defaultObject as Float?)!!)
        } else if ("Long" == type) {
            return sp.getLong(key, (defaultObject as Long?)!!)
        }
        return null
    }

    // 通过类名字去获取一个对象
    fun <T> getObject(context: Context, clazz: Class<T>): T? {
        val key = getKey(clazz)
        val json = getString(context, key, null)
        if (TextUtils.isEmpty(json)) {
            return null
        }
        try {
            val gson = Gson()
            return gson.fromJson(json, clazz)
        } catch (e: Exception) {
            return null
        }

    }

    // 通过Type去获取一个泛型对象
    fun <T> getObject(context: Context, type: Type): T? {
        val key = getKey(type)
        val json = getString(context, key, null)
        if (TextUtils.isEmpty(json)) {
            return null
        }
        try {
            val gson = Gson()
            return gson.fromJson<T>(json, type)
        } catch (e: Exception) {
            return null
        }

    }

    /**
     * 保存一个对象，object必须是普通类，而不是泛型，如果是泛型,请使用 [SpUtils.putObject]
     *
     * @param context
     * @param object
     */
    fun putObject(context: Context, `object`: Any) {
        val key = getKey(`object`.javaClass)
        val gson = Gson()
        val json = gson.toJson(`object`)
        putString(context, key, json)
    }

    /**
     * 保存一个泛型对象
     *
     * @param context
     * @param object
     * @param type    如果你要保存 List<Person> 这个类, type应该 传入 new TypeToken<List></List><Person>>() {}.getType()
    </Person></Person> */
    fun putObject(context: Context, `object`: Any, type: Type) {
        val key = getKey(type)
        val gson = Gson()
        val json = gson.toJson(`object`)
        putString(context, key, json)
    }

    fun removeObject(context: Context, clazz: Class<*>) {
        remove(context, getKey(clazz))
    }

    fun removeObject(context: Context, type: Type) {
        remove(context, getKey(type))
    }

    fun getKey(clazz: Class<*>): String {
        return clazz.name
    }

    fun getKey(type: Type): String {
        return type.toString()
    }

    fun remove(context: Context, key: String) {
        val sp = context.getSharedPreferences(DEFAULT_SP_NAME, Context.MODE_PRIVATE)
        val edit = sp.edit()
        edit.remove(key)
        edit.commit()
    }

    fun putString(context: Context, key: String, value: String) {
        val sp = context.getSharedPreferences(DEFAULT_SP_NAME, Context.MODE_PRIVATE)
        val edit = sp.edit()
        edit.putString(key, value)
        edit.commit()
    }

    fun getString(context: Context, key: String, defValue: String?): String? {
        val sp = context.getSharedPreferences(DEFAULT_SP_NAME, Context.MODE_PRIVATE)
        return sp.getString(key, defValue)
    }
}
