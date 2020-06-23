package com.hys.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 *      jwt定义生成规则的token字符串，并将信息也在生成token字符串中保存
 */
public class JwtUtils {

    // 定义token生成后的过期时间
    public static final long EXPIRE = 1000 * 60 * 60 * 24;
    // 自定义生成的密钥（随便，用户自定义）
    public static final String APP_SECRET = "ukc8BDbRigUDaY6pZFfWus2jZWLPHO";

    /**
     * @param id
     * @param nickname
     *              用户登录传过来的参数
     * @return  返回jwt默认生成的token令牌
     */
    public static String getJwtToken(String id, String nickname){

        String JwtToken = Jwts.builder()
                // jwt生成的字符串的头信息，一般固定不变
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "HS256")

                // 设置随便一个参数
                .setSubject("hys-user")
                // 获取当前时间
                .setIssuedAt(new Date())
                // 将当前时间加上要设置的过期时间段；表明这个token多久过期
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE))

                // 用户登录传过来的参数，可以是多过
                .claim("id", id)
                .claim("nickname", nickname)

                // 加上自定义的密匙
                .signWith(SignatureAlgorithm.HS256, APP_SECRET)
                .compact();

        return JwtToken;
    }

    /**
     * 判断token是否存在与有效
     * @param jwtToken
     * @return
     */
    public static boolean checkToken(String jwtToken) {
        if(StringUtils.isEmpty(jwtToken)) return false;
        try {
            Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(jwtToken);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 判断token是否存在与有效
     * @param request
     * @return
     */
    public static boolean checkToken(HttpServletRequest request) {
        try {
            String jwtToken = request.getHeader("token");
            if(StringUtils.isEmpty(jwtToken)) return false;
            Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(jwtToken);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 根据token获取会员id
     * @param request
     * @return
     */
    public static String getMemberIdByJwtToken(HttpServletRequest request) {
        String jwtToken = request.getHeader("token");

        if(StringUtils.isEmpty(jwtToken)) return "";

        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(jwtToken);
        Claims claims = claimsJws.getBody();
        return (String)claims.get("id");
    }
}
