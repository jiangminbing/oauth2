package ApplicationTests;

import com.oauth.sample.SampleJdbcApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.MessageDigestPasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;

/**
 * @author jiangmb
 * @version 1.0.0
 * @date 2023-04-03 15:22
 */

@SpringBootTest(classes = SampleJdbcApplication.class)
public class ApplicationTests {
    /**
     * 初始化客户端信息
     */
    @Autowired
    private UserDetailsManager userDetailsManager;

    /**
     * 创建用户信息
     */
    @Test
    public void testSaveUser() {
        UserDetails userDetails = User.builder().passwordEncoder(s ->   "{MD5}" + new MessageDigestPasswordEncoder("MD5").encode(s))
                .username("user1")
                .password("password")
                .roles("ADMIN")
                .build();
        userDetailsManager.createUser(userDetails);
    }
}
