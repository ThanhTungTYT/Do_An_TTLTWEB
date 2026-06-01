package com.example.do_an_ttltweb.services;

import com.example.do_an_ttltweb.dao.ContactDao;
import com.example.do_an_ttltweb.model.Contact;

import java.util.List;

public class ContactService {

    private final ContactDao contactDao = new ContactDao();

    public void addContact(Contact contact) {
        contactDao.insertContact(contact);
    }

    public int getTotalContacts(String startDate, String endDate, String state) {
        return contactDao.countContacts(startDate, endDate, state);
    }

    public List<Contact> getContactList(String startDate, String endDate, String state, int limit, int offset) {
        return contactDao.getContacts(startDate, endDate, state, limit, offset);
    }

    public int countContactsByUserToday(int id) {
        return contactDao.countContactsByUserToday(id);
    }

    /** Chuyển trạng thái: PENDING → PROCESSING → DONE → PENDING */
    public void updateState(int id, String state) {
        contactDao.updateState(id, state);
    }
}