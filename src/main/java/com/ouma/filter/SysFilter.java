package com.ouma.filter;

import com.ouma.pojo.User;
import com.ouma.utils.Constants;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter(filterName="sysFilter",urlPatterns={"/jsp/*"})
public class SysFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        User user =  (User) request.getSession().getAttribute(Constants.USER_SESSION);
        if(user == null){
            System.out.println("user doesn't exist");
            response.sendRedirect(request.getContextPath() + "/error.jsp");
        }else {
            System.out.println("user exist");
            filterChain.doFilter(servletRequest,servletResponse);
        }
    }

    @Override
    public void destroy() {
    }
}
