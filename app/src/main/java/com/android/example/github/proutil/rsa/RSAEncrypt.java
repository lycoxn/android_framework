package com.android.example.github.proutil.rsa;

import com.android.example.github.proutil.Config;
import com.android.example.github.proutil.StringUtils;
import com.google.gson.Gson;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * author:created by admin on 2018/12/13 17:06
 * detail:
 */
public class RSAEncrypt {
    private static final int MAX_ENCRYPT_BLOCK = 117;

    public static RSAPublicKey getPublicKeyByStr(String publicKeyStr)
            throws Exception {
        try {
            byte[] buffer = Base64rsa.decode(publicKeyStr);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此算法");
        } catch (InvalidKeySpecException e) {
            throw new Exception("公钥非法");
        } catch (NullPointerException e) {
            throw new Exception("公钥数据为空");
        }
    }


    public static RSAPrivateKey getPrivateKeyByStr(String privateKeyStr)
            throws Exception {
        try {
            byte[] buffer = Base64rsa.decode(privateKeyStr);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此算法");
        } catch (InvalidKeySpecException e) {
            throw new Exception("私钥非法");
        } catch (NullPointerException e) {
            throw new Exception("私钥数据为空");
        }
    }

    public static String encrypt(String plainTextData)
            throws Exception {
        RSAPublicKey publicKey = RSAEncrypt.getPublicKeyByStr(Config.PLATFORM_PUBLIC_KEY);
        if (publicKey == null) {
            throw new Exception("加密公钥为空, 请设置");
        }
        Cipher cipher = null;
        try {
            byte[] encryptInfo = plainTextData.getBytes();
            cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            int inputLen = encryptInfo.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段解密
            while (inputLen - offSet > 0) {
                if ((inputLen - offSet) > MAX_ENCRYPT_BLOCK) {
                    cache = cipher.doFinal(encryptInfo, offSet, MAX_ENCRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(encryptInfo, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_ENCRYPT_BLOCK;
            }
            encryptInfo = out.toByteArray();
            out.close();
            return Base64rsa.encode(encryptInfo);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此加密算法");
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
            return null;
        } catch (InvalidKeyException e) {
            throw new Exception("加密公钥非法,请检查");
        } catch (IllegalBlockSizeException e) {
            throw new Exception("明文长度非法");
        } catch (BadPaddingException e) {
            throw new Exception("明文数据已损坏");
        }
    }

    public static String encrypt(Map<String, String> map)
            throws Exception {
        Gson gson = new Gson();
        String plainTextData = gson.toJson(map);
        return encrypt(plainTextData);
    }

    public static String encrypt2(Map<String, Object> map)
            throws Exception {
        Gson gson = new Gson();
        String plainTextData = gson.toJson(map);
        return encrypt(plainTextData);
    }

    public static String decrypt(String private_key) throws Exception {
        //获取私钥
        if (StringUtils.isEmpty(Config.key)) {
//            String client_key = ShangFuTongApplication.mSharedPref.readString(AppConstants.SP_CLIENTKEY_VALUE, "");
//            Config.key = client_key;
        }
        RSAPrivateKey privateKey = RSAEncrypt.getPrivateKeyByStr(Config.key);
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        InputStream ins = new ByteArrayInputStream(Base64rsa.decode(private_key));
        ByteArrayOutputStream writer = new ByteArrayOutputStream();
        byte[] buf = new byte[128];
        int bufl;

        while ((bufl = ins.read(buf)) != -1) {
            byte[] block = null;

            if (buf.length == bufl) {
                block = buf;
            } else {
                block = new byte[bufl];
                for (int i = 0; i < bufl; i++) {
                    block[i] = buf[i];
                }
            }

            writer.write(cipher.doFinal(block));
        }

        return new String(writer.toByteArray());
    }
}
