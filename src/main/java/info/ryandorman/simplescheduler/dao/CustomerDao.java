package info.ryandorman.simplescheduler.dao;

import info.ryandorman.simplescheduler.model.Customer;

import java.util.List;

public interface CustomerDao {
    List<Customer> getAll();

    Customer getById(int id);

    List<Customer> getByNameLike(String name);

    void create(Customer customer);

    void update(Customer customer);

    void delete(int id);
}
