package com.example.do_an_ttltweb.controller.cart;

import com.example.do_an_ttltweb.model.cart.Cart;
import com.example.do_an_ttltweb.model.cart.CartItem; // Import thêm CartItem
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter; // Import PrintWriter

@WebServlet(name = "UpdateCart", value = "/update-cart")
public class UpdateCart extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean isAjax = request.getParameter("ajax") != null;

        try {
            int pid = Integer.parseInt(request.getParameter("pid"));
            int quantity = Integer.parseInt(request.getParameter("q"));

            HttpSession session = request.getSession();
            Cart cart = (Cart) session.getAttribute("cart");

            if (cart != null) {
                cart.updateQuantity(pid, quantity);
                session.setAttribute("cart", cart);

                if (isAjax) {
                    CartItem item = cart.getItem(pid);
                    double newSubtotal = item != null ? item.getTotalPrice() : 0;
                    int totalCartQuantity = cart.getTotalQuantity();

                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    PrintWriter out = response.getWriter();

                    out.print("{\"status\":\"success\", \"newSubtotal\":" + newSubtotal + ", \"totalCartQuantity\":" + totalCartQuantity + "}");
                    out.flush();
                    return;
                }
            }
        } catch (NumberFormatException e) {
            if (isAjax) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
        }

        if (!isAjax) {
            response.sendRedirect(request.getContextPath() + "/cart");
        }
    }
}