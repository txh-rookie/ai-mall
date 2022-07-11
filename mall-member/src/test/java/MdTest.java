import org.apache.commons.codec.digest.Md5Crypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

/**
 * @author kevintam
 * @version 1.0
 * @title
 * @description
 * @createDate 2022/7/1
 */
public class MdTest {
    public static void main(String[] args) {
//        String s = Md5Crypt.md5Crypt("123456".getBytes(StandardCharsets.UTF_8), "$1$kevintam");
//        System.out.println(s);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encode = encoder.encode("123456");
        boolean matches = encoder.matches("123456", "$2a$10$Fy4T8uDwpTW6EfpURue.x.GemzWDv.7WVEYdsMcov2VH5vz0ZTl8S");
        System.out.println(encode+"=>"+matches);

    }
}
