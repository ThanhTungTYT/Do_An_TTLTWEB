package com.example.do_an_ttltweb.services;

import com.example.do_an_ttltweb.dao.PaymentMethodDao;

public class PaymentMethodService {

    private static PaymentMethodService instance;
    private final PaymentMethodDao paymentMethodDao = new PaymentMethodDao();

    public static PaymentMethodService getInstance() {
        if (instance == null) {
            instance = new PaymentMethodService();
        }
        return instance;
    }

    public int getPaymentMethodId(String name) {
        Integer id = paymentMethodDao.getIdByName(name);
        if (id == null) {
            throw new RuntimeException("Payment method không hợp lệ: " + name);
        }
        return id;
    }
}
