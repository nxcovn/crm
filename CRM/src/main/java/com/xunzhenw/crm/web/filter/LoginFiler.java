package com.xunzhenw.crm.web.filter;

import com.xunzhenw.crm.settings.domain.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginFiler implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        System.out.println("进入到验证是否登录过滤器");

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        String path = request.getServletPath();
        if ("/login.jsp".equals(path) || "/settings/user/login.do".equals(path)){
            chain.doFilter(req,resp);
        }else{
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");

            //如果不为null说明登录过
            if (user!=null){
                chain.doFilter(req,resp);
            }else{
                //获取项目名，这里使用的是重定向，因为这样浏览器的访问地址和页面内容是相符的
                response.sendRedirect(request.getContextPath() + "/login.jsp");
            }
        }
        
    }

    @Override
    public void destroy() {

    }
}
