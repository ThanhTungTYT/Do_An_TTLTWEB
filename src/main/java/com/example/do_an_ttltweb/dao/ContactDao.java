package com.example.do_an_ttltweb.dao;

import com.example.do_an_ttltweb.model.Contact;
import com.example.do_an_ttltweb.helper.base.BaseDao;
import java.util.List;

public class ContactDao extends BaseDao {

    public void insertContact(Contact contact) {
        getJdbi().useHandle(handle ->
                handle.createUpdate(
                                "INSERT INTO contacts (user_id, full_name, email, message, sent_at, state) " +
                                        "VALUES (:userId, :fullName, :email, :message, NOW(), 'PENDING')"
                        )
                        .bind("userId", contact.getUser_id())
                        .bind("fullName", contact.getFull_name())
                        .bind("email", contact.getEmail())
                        .bind("message", contact.getMessage())
                        .execute()
        );
    }

    public int countContacts(String startDate, String endDate, String state) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM contacts WHERE 1=1 ");

        if (startDate != null && !startDate.isEmpty()) sql.append("AND DATE(sent_at) >= :start ");
        if (endDate != null && !endDate.isEmpty()) sql.append("AND DATE(sent_at) <= :end ");
        if (state != null && !state.isEmpty()) sql.append("AND state = :state ");

        return getJdbi().withHandle(handle -> {
            var query = handle.createQuery(sql.toString());
            if (startDate != null && !startDate.isEmpty()) query.bind("start", startDate);
            if (endDate != null && !endDate.isEmpty()) query.bind("end", endDate);
            if (state != null && !state.isEmpty()) query.bind("state", state);
            return query.mapTo(Integer.class).one();
        });
    }

    public List<Contact> getContacts(String startDate, String endDate, String state, int limit, int offset) {
        StringBuilder sql = new StringBuilder("SELECT id, user_id, full_name, email, message, sent_at, state FROM contacts WHERE 1=1 ");

        if (startDate != null && !startDate.isEmpty()) {
            sql.append("AND DATE(sent_at) >= :start ");
        }
        if (endDate != null && !endDate.isEmpty()) {
            sql.append("AND DATE(sent_at) <= :end ");
        }
        if (state != null && !state.isEmpty()){
            sql.append("AND state = :state ");
        }
        sql.append("ORDER BY sent_at DESC LIMIT :limit OFFSET :offset");

        return getJdbi().withHandle(handle -> {
            var query = handle.createQuery(sql.toString());

            if (startDate != null && !startDate.isEmpty()) {
                query.bind("start", startDate);
            }
            if (endDate != null && !endDate.isEmpty()) {
                query.bind("end", endDate);
            }
            if (state != null && !state.isEmpty()){
                query.bind("state", state);
            }
            query.bind("limit", limit);
            query.bind("offset", offset);

            return query.mapToBean(Contact.class).list();
        });
    }

    public int countContactsByUserToday(int userId) {
        return getJdbi().withHandle(handle ->
                handle.createQuery(
                                "SELECT COUNT(*) " +
                                        "FROM contacts " +
                                        "WHERE user_id = :userId " +
                                        "AND DATE(sent_at) = CURDATE()"
                        )
                        .bind("userId", userId)
                        .mapTo(Integer.class)
                        .one()
        );
    }

    public void updateState(int id, String state) {
        getJdbi().useHandle(handle ->
                handle.createUpdate("UPDATE contacts SET state = :state WHERE id = :id")
                        .bind("state", state)
                        .bind("id", id)
                        .execute()
        );
    }
}