package info.ryandorman.simplescheduler.dao;

import info.ryandorman.simplescheduler.model.Contact;

import java.util.List;

public interface ContactDao {
    List<Contact> getAll();
}
