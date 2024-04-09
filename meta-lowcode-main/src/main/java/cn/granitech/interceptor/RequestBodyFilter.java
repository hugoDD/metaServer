package cn.granitech.interceptor;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class RequestBodyFilter implements Filter {
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        ServletRequest requestWrapper = null;
        if (servletRequest instanceof HttpServletRequest) {
            requestWrapper = new BodyServletRequestWrapper((HttpServletRequest) servletRequest);
        }
        if (requestWrapper == null) {
            chain.doFilter(servletRequest, servletResponse);
        } else {
            chain.doFilter(requestWrapper, servletResponse);
        }
    }
}
