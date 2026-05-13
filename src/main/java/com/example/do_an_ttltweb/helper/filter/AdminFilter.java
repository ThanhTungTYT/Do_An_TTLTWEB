package com.example.do_an_ttltweb.helper.filter;

import com.example.do_an_ttltweb.model.User;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public class AdminFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null) {
            res.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        String uri = req.getRequestURI();

        boolean isAllowed = false;

        if ("admin".equalsIgnoreCase(user.getRole())) {
            isAllowed = true;
        } else {
            if (uri.contains("/admin/products") && user.hasPermission("manage_product")) isAllowed = true;
            else if (uri.contains("/admin/orders") && user.hasPermission("manage_order")) isAllowed = true;
            else if (uri.contains("/admin/users") && user.hasPermission("manage_user")) isAllowed = true;
            else if (uri.contains("/admin/dashboard")) isAllowed = true;
        }
        if (isAllowed) {
            chain.doFilter(request, response);
        } else {
            String referer = req.getHeader("Referer");
            if (referer == null || referer.isEmpty()) {
                referer = req.getContextPath() + "/admin/dashboard";
            }
            res.sendRedirect(referer);
        }
    }
    
    @Override
    public void destroy() {
    }
}

