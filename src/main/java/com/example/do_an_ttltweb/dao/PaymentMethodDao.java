package com.example.do_an_ttltweb.dao;

import com.example.do_an_ttltweb.helper.base.BaseDao;

public class PaymentMethodDao extends BaseDao {
    public Integer getIdByName(String name) {
        return getJdbi().withHandle(handle ->
                handle.createQuery(
                                "SELECT id FROM payment_method WHERE name = :name"
                        )
                        .bind("name", name)
                        .mapTo(Integer.class)
                        .findOne()
                        .orElse(null)
        );
    }

}
