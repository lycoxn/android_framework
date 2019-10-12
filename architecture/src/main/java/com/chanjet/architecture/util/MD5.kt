package com.chanjet.architecture.util

import java.io.UnsupportedEncodingException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

object MD5 {
    fun getMD5(string: String): String? {
        val hash: ByteArray

        try {
            hash = MessageDigest.getInstance("MD5").digest(string.toByteArray(charset("UTF-8")))
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
            return null
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
            return null
        }

        val hex = StringBuilder(hash.size * 2)
        for (b in hash) {
            if (b.toInt() and 0xFF < 0x10)
                hex.append("0")
            hex.append(Integer.toHexString(b.toInt() and 0xFF))
        }

        return hex.toString()
    }
}