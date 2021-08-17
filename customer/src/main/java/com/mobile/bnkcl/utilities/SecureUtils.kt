package com.mobile.bnkcl.utilities

import android.util.Base64
import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.nio.charset.StandardCharsets
import java.security.*
import java.util.*
import javax.crypto.Cipher
import javax.crypto.NoSuchPaddingException
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.DESKeySpec
import javax.crypto.spec.DESedeKeySpec
import kotlin.jvm.Throws
import kotlin.text.Charsets.UTF_16

object SecureUtils {

    private var key: Key? = null
    private const val DES_MODE = "DES/ECB/PKCS7Padding"
    private var cipher: Cipher? = null

    @Throws(Exception::class)
    fun encrypt(plainText: String): String {
        return encrypt(cipher, plainText, key)
    }

    @Throws(Exception::class)
    fun decrypt(encryptedText: String?): String {
        return decrypt(cipher, encryptedText, key)
    }
    /*public static String hex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte aByte : bytes) {
            result.append(String.format("%02x", aByte));
            // upper case
            // result.append(String.format("%02X", aByte));
        }
        return result.toString();
    }*/
    /**
     * 키값 * 24바이트인 경우 TripleDES 아니면 DES * @return * @throws Exception
     */
    @Throws(Exception::class)
    fun getKey(originalKey: String): Key {
        println("key(plain) : $originalKey")
        val digest = MessageDigest.getInstance("SHA-256")
        val key = digest.digest(
            originalKey.toByteArray(StandardCharsets.UTF_8)
        )
        val keyByte = Arrays.copyOfRange(key, 0, 8)
        //        System.out.println("\nkey(hex) : " + hex(keyByte));

//        System.out.println("키의 길이 : " + key().length);
        return if (keyByte.size == 24) getKey2(keyByte) else getKey1(
            keyByte
        )
    }

    /**
     * 지정된 비밀키를 가지고 오는 메서드 (DES) * require Key Size : 16 bytes * * @return Key 비밀키 클래스 * @exception Exception
     */
    @Throws(Exception::class)
    fun getKey1(keyByte: ByteArray?): Key {
        println("DES")
        val desKeySpec = DESKeySpec(keyByte)
        val keyFactory =
            SecretKeyFactory.getInstance("DES")
        return keyFactory.generateSecret(desKeySpec)
    }

    /**
     * 지정된 비밀키를 가지고 오는 메서드 (TripleDES) * require Key Size : 24 bytes * @return * @throws Exception
     */
    @Throws(Exception::class)
    fun getKey2(keyByte: ByteArray?): Key {
//        System.out.println("Triple DES");
        val desKeySpec = DESedeKeySpec(keyByte)
        val keyFactory =
            SecretKeyFactory.getInstance("DESede")
        return keyFactory.generateSecret(desKeySpec)
    }

    /**
     * 문자열 대칭 암호화 * * @param ID * 비밀키 암호화를 희망하는 문자열 * @return String 암호화된 ID * @exception Exception
     */
    @Throws(Exception::class)
    fun encrypt(
        cipher: Cipher?,
        ID: String,
        key: Key?
    ): String {
        cipher!!.init(Cipher.ENCRYPT_MODE, key)

        // 평문을 바이트화
        val inputBytes1 = ID.toByteArray(charset("UTF8"))

        // doFinal 메소드는 바이트한 평문을 암호화
        val outputBytes1 = cipher.doFinal(inputBytes1)
        //        System.out.println("\nciphertext(hex) : " + hex(outputBytes1));

        // 바이트를 Text화 = Base64 인코딩
        return String(Base64.encode(outputBytes1, Base64.DEFAULT))
    }

    /** * 문자열 대칭 복호화 * * @param codedID * 비밀키 복호화를 희망하는 문자열 * @return String 복호화된 ID * @exception Exception  */
    @Throws(Exception::class)
    fun decrypt(
        cipher: Cipher?,
        codedID: String?,
        key: Key?
    ): String {
        //복호화 모드로 Cipher 선언 암호화 했던 키 값 넣어줌
        cipher!!.init(Cipher.DECRYPT_MODE, key)

        //base64로 디코더
        val inputBytes1 =
            Base64.decode(codedID, Base64.DEFAULT)

        // 복호화 진행해서 바이트화
        val outputBytes2 = cipher.doFinal(inputBytes1)

        //바이트를 UTF-8로 평문으로 변경
        return String(outputBytes2, UTF_16)
    }

//    private fun String(bytes: ByteArray?, charset: String): String {
//        TODO("Not yet implemented")
//    }

    init {
        Security.addProvider(BouncyCastleProvider())
        try {
            cipher = Cipher.getInstance(DES_MODE, "BC")
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: NoSuchProviderException) {
            e.printStackTrace()
        } catch (e: NoSuchPaddingException) {
            e.printStackTrace()
        }
        try {
            key = getKey("bnkcmfi")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}