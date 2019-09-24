package com.nnl.password.encrypt

import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.DESKeySpec
import javax.crypto.spec.DESedeKeySpec
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

abstract class AesEncryptor: AbstractEncryptor() {

    protected abstract fun getRawKey(): ByteArray
    /*
	加密
	 */
    override fun encrypt(input: ByteArray): ByteArray? {
        try {
            val rawKey = getRawKey()
            val skSpec = SecretKeySpec(rawKey, "AES")
            //val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            val cipher = Cipher.getInstance("AES")
            cipher.init(Cipher.ENCRYPT_MODE, skSpec)
            return cipher.doFinal(input)
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }

    }

    /*
	解密
	 */
    override fun decrypt(input: ByteArray): ByteArray? {
        try {
            val rawKey = getRawKey()
            val skSpec = SecretKeySpec(rawKey, "AES")
            //val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            val cipher = Cipher.getInstance("AES")
            cipher.init(Cipher.DECRYPT_MODE, skSpec)
            return cipher.doFinal(input)
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }

    }

}