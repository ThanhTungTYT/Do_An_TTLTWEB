package com.example.do_an_ttltweb.services;

import com.example.do_an_ttltweb.dao.AccountDao;
import com.example.do_an_ttltweb.dao.OrderDao;
import com.example.do_an_ttltweb.model.Order;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class AdminPage1Service {

    private OrderDao orderDao = new OrderDao();
    private AccountDao accountDao = new AccountDao();

    // 0 = lấy toàn bộ sản phẩm (không giới hạn); danh sách có thanh cuộn ở giao diện
    private static final int PRODUCT_LIST_LIMIT = 0;

    public Timestamp getStartTime(String filter) {
        Calendar cal = Calendar.getInstance();

        if ("week".equals(filter)) {
            cal.add(Calendar.DAY_OF_MONTH, -7);
        } else if ("month".equals(filter)) {
            cal.add(Calendar.MONTH, -1);
        } else if ("quarter".equals(filter)) {
            cal.add(Calendar.MONTH, -3);
        } else if ("all".equals(filter)) {
            cal.add(Calendar.YEAR, -100);
        } else {
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
        }
        return new Timestamp(cal.getTimeInMillis());
    }

    public Timestamp getEndTime() {
        return new Timestamp(System.currentTimeMillis());
    }

    public double getTotalRevenue(String filter) {
        return orderDao.getTotalRevenue(getStartTime(filter), getEndTime());
    }

    public int getTotalOrders(String filter) {
        return orderDao.countOrders(getStartTime(filter), getEndTime());
    }

    public int getPendingOrders(String filter) {
        return orderDao.countPendingOrders(getStartTime(filter), getEndTime());
    }

    public int getNewCustomers(String filter) {
        return accountDao.countNewCustomers(getStartTime(filter), getEndTime());
    }

    public List<Map<String, Object>> getTopProducts(String filter) {
        return orderDao.getTopProducts(getStartTime(filter), getEndTime(), "all".equals(filter), PRODUCT_LIST_LIMIT);
    }

    public List<Map<String, Object>> getWorstProducts(String filter) {
        return orderDao.getWorstProducts(getStartTime(filter), getEndTime(), "all".equals(filter), PRODUCT_LIST_LIMIT);
    }

    public List<Order> getOrders(String filter) {
        return orderDao.getOrdersByDate(getStartTime(filter), getEndTime());
    }

    public double getTotalRevenue(Timestamp start, Timestamp end) {
        return orderDao.getTotalRevenue(start, end);
    }

    public int getTotalOrders(Timestamp start, Timestamp end) {
        return orderDao.countOrders(start, end);
    }

    public int getPendingOrders(Timestamp start, Timestamp end) {
        return orderDao.countPendingOrders(start, end);
    }

    public int getNewCustomers(Timestamp start, Timestamp end) {
        return accountDao.countNewCustomers(start, end);
    }

    public List<Map<String, Object>> getTopProducts(Timestamp start, Timestamp end) {
        return orderDao.getTopProducts(start, end, false, PRODUCT_LIST_LIMIT);
    }

    public List<Map<String, Object>> getWorstProducts(Timestamp start, Timestamp end) {
        return orderDao.getWorstProducts(start, end, false, PRODUCT_LIST_LIMIT);
    }

    public List<Order> getOrders(Timestamp start, Timestamp end) {
        return orderDao.getOrdersByDate(start, end);
    }
}
