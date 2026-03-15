package com.example.do_an_ttltweb.controller.account;

import com.example.do_an_ttltweb.model.Address;
import com.example.do_an_ttltweb.model.User;
import com.example.do_an_ttltweb.services.AccountService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(name = "AccountInfoServlet", urlPatterns = {"/info", "/update-info"})
public class AccountInfoServlet extends HttpServlet {

    private AccountService accountService = new AccountService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        User authUser = (User) session.getAttribute("user");

        if (authUser == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        User userDetail = accountService.getAccountInfo(authUser.getId());
        Address addressDetail = accountService.getUserAddress(authUser.getId());

        request.setAttribute("user", userDetail);
        request.setAttribute("addr", addressDetail);

        request.getRequestDispatcher("/info.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        User authUser = (User) session.getAttribute("user");

        if (authUser == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        String fullName = request.getParameter("fullname");
        String phone = request.getParameter("phone");
        String city = request.getParameter("city");
        String district = request.getParameter("district");
        String streetAddress = request.getParameter("address");
        String addressIdStr = request.getParameter("addressId");
        int addressId = (addressIdStr != null && !addressIdStr.isEmpty()) ? Integer.parseInt(addressIdStr) : 0;

        boolean isUpdated = accountService.updateUserInfo(authUser.getId(), fullName, phone, addressId, city, district, streetAddress);

        if (isUpdated) {
            authUser.setFull_name(fullName);
            authUser.setPhone(phone);
            session.setAttribute("user", authUser);
            request.setAttribute("message", "Cập nhật thành công!");
        } else {
            request.setAttribute("error", "Cập nhật thất bại!");
        }

        User userDetail = accountService.getAccountInfo(authUser.getId());
        Address addressDetail = accountService.getUserAddress(authUser.getId());
        request.setAttribute("user", userDetail);
        request.setAttribute("addr", addressDetail);

        request.getRequestDispatcher("/info.jsp").forward(request, response);
    }
}