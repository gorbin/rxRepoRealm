package com.punicapp.rxreporealm

import android.content.Context

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.nio.charset.Charset
import java.security.SecureRandom

import io.realm.RealmConfiguration

class RealmKeyProvider(private val context: Context) {

    val bytesForRealm: ByteArray
        get() {
            val file = context.getFileStreamPath(REALM_KEY_FILE)
            if (file == null || !file.exists()) {
                initStore()
            }

            val key = fetchKey()
            return keyModification(REALM_KEY_FILE.toByteArray(Charset.forName("UTF-8")), key)
        }

    private fun fetchKey(): ByteArray {
        val generated = ByteArray(RealmConfiguration.KEY_LENGTH)
        try {
            val fileInputStream = context.openFileInput(REALM_KEY_FILE)
            fileInputStream.read(generated, 0, generated.size)
            fileInputStream.close()
        } catch (e: IOException) {
            throw RuntimeException(e)
        }

        return generated
    }

    private fun initStore() {
        val generated = ByteArray(RealmConfiguration.KEY_LENGTH)
        try {
            val rng = SecureRandom()
            rng.nextBytes(generated)
            val fileOutputStream = context.openFileOutput(REALM_KEY_FILE, Context.MODE_PRIVATE)

            fileOutputStream.write(generated)
            fileOutputStream.close()
        } catch (e: IOException) {
            throw RuntimeException(e)
        }

    }

    fun keyModification(teplate: ByteArray, source: ByteArray): ByteArray {
        val result = ByteArray(source.size)

        for (i in result.indices) {
            result[i] = (teplate[i % teplate.size].toInt() xor source[i].toInt()).toByte()
        }

        return result
    }

    companion object {
        private val REALM_KEY_FILE = "realm_key.data"
    }
}
