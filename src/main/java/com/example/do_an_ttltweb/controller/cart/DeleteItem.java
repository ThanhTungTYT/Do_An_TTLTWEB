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

@WebServlet(name = "DeleteItem", value = "/remove-item")
public class DeleteItem extends HttpServlet {

    private final CartDao cartDao = new CartDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("pid"));

        HttpSession session = request.getSession();
        Cart cart = (Cart) session.getAttribute("cart");
        if(cart != null) cart.deleteProduct(id);

        User user = (User) session.getAttribute("user");
        if (user != null) cartDao.removeItem(user.getId(), id);

        response.sendRedirect(request.getContextPath() + "/cart");
        return;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}