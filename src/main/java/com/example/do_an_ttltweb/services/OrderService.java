package com.example.do_an_ttltweb.services;

import com.example.do_an_ttltweb.model.cart.Cart;
import com.example.do_an_ttltweb.dao.OrderDao;
import com.example.do_an_ttltweb.model.Order;
import com.example.do_an_ttltweb.model.OrderAddress;
import com.example.do_an_ttltweb.model.OrderItem;

import java.util.List;

public class OrderService {
    private final OrderDao dao = new OrderDao();
    public boolean create(Order order, OrderAddress address, Cart cart) {
        return dao.createOrder(order,address, cart);
    }

    public List<Order> getAllOrders() {
        return dao.getAllOrders();
    }
    public List<OrderItem> getItemsByOrderId(int orderId) {
        return dao.getItemsByOrderId(orderId);
    }
    public boolean updateOrder(Order order) {
        return dao.updateOrderStatus(order);
    }
    public List<Order> getOrdersByUserId(int userId) {
        List<Order> orders = dao.getOrdersByUserId(userId);

        for (Order o : orders) {
            o.setItems(dao.getItemsByOrderId(o.getId()));
        }
        return orders;
    }
    public boolean cancelOrder(int orderId, int userId) {
        Order order = new Order();
        order.setId(orderId);
        order.setStatus("Đã hủy");
        return dao.cancelOrder(order, userId);
    }

    public int countOrders(String start, String end,String status) {
        return dao.countOrdersWithFilter(start, end,status);
    }

    public List<Order> getOrdersPagination(String start, String end,String status, int page, int pageSize ) {
        int offset = (page - 1) * pageSize;
        return dao.getOrdersWithFilter(start, end,status, pageSize, offset);
    }

    public List<Order> searchOrders(String keyword) {
        return dao.searchOrders(keyword);
    }

    public Order getOrderById(int orderId) {
        return dao.getOrderById(orderId);
    }

    public OrderAddress getAddressByOrderId(int orderId) {
        return dao.getAddressByOrderId(orderId);
    }

    public boolean updateOrderStatusAndGhn(int orderId, String status, String ghnCode) {
        return dao.updateOrderStatusAndGhn(orderId, status, ghnCode);
    }
}