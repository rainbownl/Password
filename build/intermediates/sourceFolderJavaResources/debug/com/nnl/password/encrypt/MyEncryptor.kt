package com.nnl.password.encrypt

class MyEncryptor: AesEncryptor() {
    private var key = "com.niulei.passw"//ord.aes.key.2018_10_19"
    override fun getRawKey(): ByteArray {
        /*var byteArray = ByteArray(64)

        var keyArray = key.toByteArray(Charsets.UTF_8)
        keyArray.forEachIndexed { index, byte ->
            byteArray.set(index, byte)
        }
        return byteArray*/
        return key.toByteArray(Charsets.UTF_8)
    }
}