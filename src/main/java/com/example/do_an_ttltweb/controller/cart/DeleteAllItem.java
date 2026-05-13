package com.example.do_an_ttltweb.controller.cart;

import com.example.do_an_ttltweb.dao.CartDao;
import com.example.do_an_ttltweb.model.User;
import com.example.do_an_ttltweb.model.cart.Cart;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(name = "DeleteAllItem", value = "/remove-all")
public class DeleteAllItem extends HttpServlet {

    private final CartDao cartDao = new CartDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            Cart cart = (Cart) session.getAttribute("cart");
            if (cart != null) cart.deleteAll();

            User user = (User) session.getAttribute("user");
            if (user != null) cartDao.clearCart(user.getId());
        }
        response.sendRedirect(request.getContextPath() + "/cart");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}