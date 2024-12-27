package info.ryandorman.simplescheduler.dao;

import info.ryandorman.simplescheduler.model.Contact;

import java.util.List;


/**
 * Allows predictable access to persistent Contact data.
 */
public interface ContactDao {
    /**
     * Gets a <code>java.util.List</code> of all Contacts.
     *
     * @return All Contacts
     */
    List<Contact> getAll();
}
