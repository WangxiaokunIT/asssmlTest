package com.xinshang.rest.modular.auth.filter;

import com.xinshang.core.base.tips.ErrorTip;
import com.xinshang.core.util.RenderUtil;
import com.xinshang.rest.common.enums.BizExceptionEnum;
import com.xinshang.rest.config.properties.JwtProperties;
import com.xinshang.rest.common.util.JwtTokenUtil;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 对客户端请求的jwt token验证过滤器
 *
 * @author fengshuonan
 * @Date 2017/8/24 14:04
 */
@Slf4j
public class AuthFilter extends OncePerRequestFilter {

    @Autowired
    private  JwtTokenUtil jwtTokenUtil;

    @Autowired
    private  JwtProperties jwtProperties;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        //是否在排除列表中
        if (matchPath(jwtProperties.getNoPath(), request.getServletPath())) {
            chain.doFilter(request, response);
            return;
        }


        //获取token
        final String requestHeader = request.getHeader(jwtProperties.getHeader());
        log.info(request.getServletPath()+"认证参数:{}",requestHeader);
        if (requestHeader != null && requestHeader.startsWith("Bearer ")) {
            String authToken = requestHeader.substring(7);

            //验证token是否过期,包含了验证jwt是否正确
            try {
                boolean flag = jwtTokenUtil.isTokenExpired(authToken);
                if (flag) {
                    RenderUtil.renderJson(response, new ErrorTip(BizExceptionEnum.TOKEN_EXPIRED.getCode(), BizExceptionEnum.TOKEN_EXPIRED.getMessage()));
                    return;
                }
            } catch (JwtException e) {
                //有异常就是token解析失败
                RenderUtil.renderJson(response, new ErrorTip(BizExceptionEnum.TOKEN_ERROR.getCode(), BizExceptionEnum.TOKEN_ERROR.getMessage()));
                return;
            }
        } else {
            //header没有带Bearer字段
            RenderUtil.renderJson(response, new ErrorTip(BizExceptionEnum.TOKEN_ERROR.getCode(), BizExceptionEnum.TOKEN_ERROR.getMessage()));
            return;
        }
        chain.doFilter(request, response);
    }

    private boolean matchPath(String[] paths,String realPath){
        for (String path : paths) {
            if(path.equals(realPath)){
                return true;
            }
            int index = path.indexOf("*");
            if(index!=-1){
                path = path.substring(0,index);
                if(realPath.startsWith(path)){
                    return true;
                }
            }

        }
        return false;
    }
}