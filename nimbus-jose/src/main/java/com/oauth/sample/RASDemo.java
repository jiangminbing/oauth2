package com.oauth.sample;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.rsa.crypto.KeyStoreKeyFactory;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.util.Date;
import java.util.UUID;

/**
 * @author jiangmb
 * @version 1.0.0
 * @date 2023-03-31 17:28
 */
public class RASDemo {
    /**
     * KeyPairGenerator.getInstance("RSA") 和 KeyStoreKeyFactory 这两个代码片段有很大的区别。
     *
     * KeyPairGenerator.getInstance("RSA") 是用于生成密钥对的工具类，它可以根据指定的算法（例如"RSA"）生成公钥和私钥对。使用KeyPairGenerator类生成的密钥对可以用于加密、解密、签名和验证等操作。
     *
     * KeyStoreKeyFactory 则是一个用于在Java Keystore中查找和获取密钥的工具类。它需要一个Keystore文件和密码来初始化，并提供了获取KeyStore中已经存在的密钥对的方法。
     *
     * 在这个特定的代码中，KeyStoreKeyFactory类用于从一个指定的Keystore文件中获取密钥对。new ClassPathResource("jwt.jks")用于获取类路径中的文件资源，即指定Keystore文件的路径，"123456".toCharArray() 则是Keystore文件的密码。
     *
     * 总的来说，KeyPairGenerator用于生成密钥对，而KeyStoreKeyFactory用于从Keystore文件中获取密钥对，这是两个不同的工具类，它们的作用和使用场景也不同。
     * @param args
     */
    public static void main(String[] args) throws Exception, ParseException {

        RSAKey rsaJWK = generateRsaKey();
        // 用私钥创建一个签名器
        JWSSigner signer = new RSASSASigner(rsaJWK);
        // Prepare JWT with claims set
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject("alice")
                .issuer("https://c2id.com")
                .expirationTime(new Date(new Date().getTime() + 60 * 1000))
                .build();

        SignedJWT signedJWT = new SignedJWT(
                new JWSHeader.Builder(JWSAlgorithm.RS256).keyID(rsaJWK.getKeyID()).build(),
                claimsSet);

        // 构建签名
        signedJWT.sign(signer);

        String s = signedJWT.serialize();
        System.out.println(s);


        verify(s);




    }
    /**
     * 验签
     */
    public static void verify(String token) throws Exception{
        // JWT/JWS 字符串转 JWSObject 对象
        JWSObject jwsObject = JWSObject.parse(token);
        // 根据公要生成验证器
        RSAKey rsaKey = generateRsaKey();
        RSAKey publicRsaKey = rsaKey.toPublicJWK();
        System.out.println(publicRsaKey);   // show 公钥
        JWSVerifier jwsVerifier = new RSASSAVerifier(publicRsaKey);

        // 使用校验器校验 JWSObject 对象的合法性
        if (!jwsObject.verify(jwsVerifier)) {
            throw new RuntimeException("token签名不合法！");
        }

        // 拆解 JWT/JWS，获得荷载中的内容
        String payload = jwsObject.getPayload().toString();
        System.out.println(payload);    // show 荷载
    }

    public static RSAKey generateRsaKey() {
        // 从 classpath 下获取 RSA 秘钥对
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new ClassPathResource("jwt.jks"), "123456".toCharArray());
        KeyPair keyPair = keyStoreKeyFactory.getKeyPair("jwt", "123456".toCharArray());
        // 获取 RSA 公钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        // 获取 RSA 私钥
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        RSAKey rsaKey = new RSAKey.Builder(publicKey).privateKey(privateKey).build();

        return rsaKey;
    }
    public static RSAKey generateRsaKey2(){
        KeyPair keyPair;
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            keyPair = keyPairGenerator.generateKeyPair();
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        // @formatter:off
        return new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID(UUID.randomUUID().toString())
                .build();
    }

}
