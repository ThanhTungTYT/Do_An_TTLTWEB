package com.example.do_an_ttltweb.services;

import com.example.do_an_ttltweb.dao.ContactDao;
import com.example.do_an_ttltweb.model.Contact;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

    public int countContactsByUserToday(int id) {
        return contactDao.countContactsByUserToday(id);
    }

    public void deleteContact(int id) {
        contactDao.deleteContact(id);
    }

    public void deleteContacts(String[] ids) {
        if (ids == null || ids.length == 0) return;
        List<Integer> idList = Arrays.stream(ids)
                .map(Integer::parseInt)
                .collect(Collectors.toList());
        contactDao.deleteContacts(idList);
    }
}