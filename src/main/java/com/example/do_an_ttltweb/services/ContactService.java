package com.example.do_an_ttltweb.services;

import com.example.do_an_ttltweb.dao.ContactDao;
import com.example.do_an_ttltweb.model.Contact;

import java.util.List;

public class ContactService {

    private final ContactDao contactDao = new ContactDao();

    public void addContact(Contact contact) {
        contactDao.insertContact(contact);
    }

    public int getTotalContacts(String startDate, String endDate) {
        return contactDao.countContacts(startDate, endDate);
    }

    public List<Contact> getContactList(String startDate, String endDate, int limit, int offset) {
        return contactDao.getContacts(startDate, endDate, limit, offset);
    }
}