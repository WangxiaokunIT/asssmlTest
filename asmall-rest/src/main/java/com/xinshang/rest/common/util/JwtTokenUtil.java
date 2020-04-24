package com.xinshang.rest.common.util;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.xinshang.core.util.ToolUtil;
import com.xinshang.core.util.ToolUtil;
import com.xinshang.rest.common.enums.BizExceptionEnum;
import com.xinshang.rest.config.properties.JwtProperties;
import com.xinshang.rest.config.properties.JwtProperties;
import com.xinshang.rest.modular.asmall.model.Member;
import com.xinshang.rest.modular.asmall.service.IMemberService;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * <p>jwt token工具类</p>
 * <pre>
 *     jwt的claim里一般包含以下几种数据:
 *         1. iss -- token的发行者
 *         2. sub -- 该JWT所面向的用户
 *         3. aud -- 接收该JWT的一方
 *         4. exp -- token的失效时间
 *         5. nbf -- 在此时间段之前,不会被处理
 *         6. iat -- jwt发布时间
 *         7. jti -- jwt唯一标识,防止重复使用
 * </pre>
 *
 * @author fengshuonan
 * @Date 2017/8/25 10:59
 */
@Component
public class JwtTokenUtil {

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private IMemberService iMemberService;

    /**
     * 获取手机号从token中
     */
    public String getPhoneNoFromToken(String token) {
        return getClaimFromToken(token).getSubject();
    }

    /**
     * 获取当前登录用户从token中
     */
    public Member getMemberFromToken(String token) {

        Member member = iMemberService.selectOne(new EntityWrapper<Member>().eq("openid", getClaimFromToken(token).getSubject()));
        if(member==null){
            member = iMemberService.selectOne(new EntityWrapper<Member>().eq("username", getClaimFromToken(token).getSubject()));
        }
        return member;
    }

    /**
     * 从httpServletRequest获取当前登录用户
     */
    public Member getMemberFromRequest(HttpServletRequest request) {
        return getMemberFromToken(request.getHeader("Authorization").substring(7));
    }

    /**
     * 获取jwt发布时间
     */
    public Date getIssuedAtDateFromToken(String token) {
        return getClaimFromToken(token).getIssuedAt();
    }

    /**
     * 获取jwt失效时间
     */
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token).getExpiration();
    }

    /**
     * 获取jwt接收者
     */
    public String getAudienceFromToken(String token) {
        return getClaimFromToken(token).getAudience();
    }

    /**
     * 获取私有的jwt claim
     */
    public String getPrivateClaimFromToken(String token, String key) {
        return getClaimFromToken(token).get(key).toString();
    }

    /**
     * 获取md5 key从token中
     */
    public String getMd5KeyFromToken(String token) {
        return getPrivateClaimFromToken(token, jwtProperties.getMd5Key());
    }

    /**
     * 获取jwt的payload部分
     */
    public Claims getClaimFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(jwtProperties.getSecret())
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 解析token是否正确,不正确会报异常<br>
     */
    public void parseToken(String token) throws JwtException {
        Jwts.parser().setSigningKey(jwtProperties.getSecret()).parseClaimsJws(token).getBody();
    }

    /**
     * <pre>
     *  验证token是否失效
     *  true:过期   false:没过期
     * </pre>
     */
    public Boolean isTokenExpired(String token) {
        try {
            final Date expiration = getExpirationDateFromToken(token);
            return expiration.before(new Date());
        } catch (Exception e) {
            return true;
        }
    }



    /**
     * 生成token(通过用户名和签名时候用的随机数)
     */
    public String generateToken(String userName, String randomKey) {
        Map<String, Object> claims = new HashMap(1);
        claims.put(jwtProperties.getMd5Key(), randomKey);
        return doGenerateToken(claims, userName);
    }

    /**
     * 生成token
     */
    private String doGenerateToken(Map<String, Object> claims, String subject) {
        final Date createdDate = new Date();
        final Date expirationDate = new Date(createdDate.getTime() + jwtProperties.getExpiration() * 1000);

        return Jwts.builder()
                .setId(UUID.randomUUID().toString())
                .setClaims(claims)
                //说明
                .setSubject(subject)
                //签发者信息
                .setIssuer("asmall-rest")
                //接收用户
                .setAudience("custom")
                .setIssuedAt(createdDate)
                .setExpiration(expirationDate)
                //数据压缩方式
                .compressWith(CompressionCodecs.GZIP)
                .signWith(SignatureAlgorithm.HS512, jwtProperties.getSecret())
                .compact();
    }

    /**
     * 获取混淆MD5签名用的随机字符串
     */
    public String getRandomKey() {
        return ToolUtil.getRandomString(6);
    }
}