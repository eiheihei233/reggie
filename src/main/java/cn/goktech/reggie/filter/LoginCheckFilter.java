package cn.goktech.reggie.filter;

import cn.goktech.reggie.common.BaseContext;
import cn.goktech.reggie.common.R;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录过滤
 */
@WebFilter( filterName = "LoginCheckFilter",urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {

    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String requestURI = request.getRequestURI();

        log.info("拦截到请求{}",requestURI);

//        定义不需要处理的请求路径
        String[] urls = new String[]{
            "/employee/login",
            "/employee/logout",
            "/backend/**",
            "/front/**"
        };

        boolean check = check(urls, requestURI);

        if (check){
            //如果是不需要处理的路径 直接放行
            log.info("本次请求{}不需要处理",requestURI);
            filterChain.doFilter(request,response);
            return;
        }

        if (request.getSession().getAttribute("employee") != null){
            // 如果用户已经登录，放行
            log.info("用户已登录");
            Long empId = (Long) request.getSession().getAttribute("employee");
            BaseContext.setCurrentId(empId);
            filterChain.doFilter(request,response);
            return;
        }
        log.info("用户未登录");
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;
    }

    /**
     * 路径匹配，检查本次请求是否要放行
     * @param requestURI
     * @return
     */
    public boolean check(String[] urls,String requestURI){
        for (String url:urls) {
            boolean match = PATH_MATCHER.match(url, requestURI);
            if (match){
                //匹配成功，直接放行
                return true;
            }
        }
        return false;
    }
}
