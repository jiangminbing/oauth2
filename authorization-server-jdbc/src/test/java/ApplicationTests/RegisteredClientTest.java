package ApplicationTests;

import com.oauth.sample.SampleJdbcApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author jiangmb
 * @version 1.0.0
 * @date 2023-04-03 16:12
 */
@SpringBootTest(classes = SampleJdbcApplication.class)
public class RegisteredClientTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Test
    void testSaveClient() {
        /**
         *  这里的密码需要注意下，关键的两个类PasswordEncoderFactories,DelegatingPasswordEncoder
         *  在插入数据的时候，密码会根据配置对应的数据进加密生成加密串，默认算法是bcrypt
         *  密码：secret
         *  bcrypt算法 {bcrypt}$2a$10$iMz8sMVMiOgRgXRuREF/f.ChT/rpu2ZtitfkT5CkDbZpZlFhLxO3y
         *  MD5 算法：{MD5}{kew+Wt79PhCUfX7xVbMU14vgx5p4chaqg8D+C9dIGIs=}2991ff3c1388cf83e291803ef44d1fbd
         *  PDK 算法 ：{pbkdf2}cc409867e39f011f6332bbb6634f58e98d07be7fceefb4cc27e62501594d6ed0b271a25fd9f7fc2e
         *  DelegatingPasswordEncoder 是为了兼容系统多种密码加密算法，需要在在密码加下相关的前缀{}
         */
        RegisteredClient registeredClient = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId("messaging-client")
                .clientSecret("secret")
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .authorizationGrantType(AuthorizationGrantType.PASSWORD)
                .authorizationGrantType(AuthorizationGrantType.IMPLICIT)
                .redirectUri("http://127.0.0.1:8080/login/oauth2/code/messaging-client-oidc")
                .redirectUri("http://127.0.0.1:8080/authorized")
                .scope(OidcScopes.OPENID)
                .scope("message.read")
                .scope("message.write")
                .clientSecretExpiresAt(new Date(System.currentTimeMillis() + 50000).toInstant())
                .build();
        this.registeredClientRepository().save(registeredClient);
    }
    public static PasswordEncoder getDelegatingPasswordEncoder(String encodingId) {
        Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put("bcrypt", new BCryptPasswordEncoder());
        encoders.put("ldap", new org.springframework.security.crypto.password.LdapShaPasswordEncoder());
        encoders.put("MD4", new org.springframework.security.crypto.password.Md4PasswordEncoder());
        encoders.put("MD5", new org.springframework.security.crypto.password.MessageDigestPasswordEncoder("MD5"));
        encoders.put("noop", org.springframework.security.crypto.password.NoOpPasswordEncoder.getInstance());
        encoders.put("pbkdf2", new Pbkdf2PasswordEncoder());
        encoders.put("scrypt", new SCryptPasswordEncoder());
        encoders.put("SHA-1", new org.springframework.security.crypto.password.MessageDigestPasswordEncoder("SHA-1"));
        encoders.put("SHA-256",
                new org.springframework.security.crypto.password.MessageDigestPasswordEncoder("SHA-256"));
        encoders.put("sha256", new org.springframework.security.crypto.password.StandardPasswordEncoder());
        encoders.put("argon2", new Argon2PasswordEncoder());
        return new DelegatingPasswordEncoder(encodingId, encoders);
    }
    public PasswordEncoder passwordEncoder() {
        // 指定MD5算进行加密
        return this.getDelegatingPasswordEncoder("MD5");
    }
    public RegisteredClientRepository registeredClientRepository() {
        // 设置不同的算法，这里设置的是MD5，默认加密代码 bcrypt
        JdbcRegisteredClientRepository.RegisteredClientParametersMapper p = new JdbcRegisteredClientRepository.RegisteredClientParametersMapper();
        p.setPasswordEncoder(passwordEncoder());

        JdbcRegisteredClientRepository repository = new JdbcRegisteredClientRepository(jdbcTemplate);
        repository.setRegisteredClientParametersMapper(p);
        return repository;
    }
}
