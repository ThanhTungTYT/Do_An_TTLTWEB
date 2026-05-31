package com.example.do_an_ttltweb.controller.adminPage4;

import com.example.do_an_ttltweb.model.User;
import com.example.do_an_ttltweb.services.AccountService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "DeleteUser", value = "/delete-user")
public class DeleteUser extends HttpServlet {

    private AccountService account = new AccountService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        int uid = Integer.parseInt(request.getParameter("uid"));
        User user = account.getAccountInfo(uid);

        if(user.getStatus().equals("active")){
            if(account.banUser(uid)){
                request.getSession().setAttribute("success", "Đã khóa tài khoản!");
            } else {
                request.getSession().setAttribute("error", "Khóa tài khoản thất bại!");
            }
        }else {
            if(account.unBanUser(uid)){
                request.getSession().setAttribute("success", "Đã mở khóa tài khoản!");
            } else {
                request.getSession().setAttribute("error", "Mở khóa tài khoản thất bại!");
            }
        }
        response.sendRedirect(request.getContextPath() + "/admin/users");
    }
}