package com.oauth.sample;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.text.ParseException;
import java.util.Date;

/**
 * @author jiangmb
 * @version 1.0.0
 * @date 2023-03-31 14:25
 */
public class HMACDemo {
    public static void main(String[] args) throws JOSEException, ParseException {
        // Create the JWT claims set
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject("alice")
                .issuer("https://c2id.com")
                .expirationTime(new Date(new Date().getTime() + 60 * 1000)).build();

         // Create the signed JWT
        SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);

        // 获取随机密钥
        SecretKey key = generateSecretKey();
        signedJWT.sign(new MACSigner(key));

        // 生成JWT
        String jwtString = signedJWT.serialize();
        System.out.println(jwtString);

        // 进行验签
        JWSVerifier verifier = new MACVerifier(key);
        System.out.println(signedJWT.verify(verifier));
        System.out.println(signedJWT.getJWTClaimsSet().getSubject());
        System.out.println( signedJWT.getJWTClaimsSet().getIssuer());
        System.out.println(new Date().before(signedJWT.getJWTClaimsSet().getExpirationTime()));


    }
    public static SecretKey generateSecretKey() {
        SecretKey hmacKey;
        try {
            hmacKey = KeyGenerator.getInstance("HmacSha256").generateKey();
            System.out.println(hmacKey.getFormat());
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
        return hmacKey;
    }
}
