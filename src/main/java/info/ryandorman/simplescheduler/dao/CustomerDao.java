package info.ryandorman.simplescheduler.dao;

/*
 *   Ryan Dorman
 *   ID: 001002824
 */

import info.ryandorman.simplescheduler.model.Customer;

import java.util.List;

/**
 * Allows easy access to persistent Customer data.
 */
public interface CustomerDao {
    /**
     * Gets a <code>java.util.List</code> of all Customers.
     * @return All Customers
     */
    List<Customer> getAll();

    /**
     * Gets a specific Customer based on their unique identifier.
     * @param id Unique identifier for Customer
     * @return Customer associated with identifier if any
     */
    Customer getById(int id);

    /**
     * Gets a <code>java.util.List</code> of all Customers with a first or last name like the name given. <em>Ignores
     * case when searching for like names.</em>
     * @param name Name to find Customers by
     * @return Customers with a first or last name like the name given
     */
    List<Customer> getByNameLike(String name);

    /**
     * Creates a new Customer record.
     * @param customer New Customer data to store
     * @return 1 or 0 to indicate number of records created
     */
    int create(Customer customer);

    /**
     * Updates an existing Customer record.
     * @param customer Updated Customer data to store
     * @return 1 or 0 to indicate number of records updated
     */
    int update(Customer customer);

    /**
     * Deletes an existing Customer record.
     * @param id Unique identifier of Customer to delete
     * @return 1 or 0 to indicate number of records deleted
     */
    int delete(int id);
}
