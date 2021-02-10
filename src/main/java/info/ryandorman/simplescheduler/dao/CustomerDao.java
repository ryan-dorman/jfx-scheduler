package info.ryandorman.simplescheduler.dao;

import info.ryandorman.simplescheduler.model.Customer;

import java.util.List;

public interface CustomerDao {
    List<Customer> getAll();

    Customer getById(int id);

    List<Customer> getByNameLike(String name);

    int create(Customer customer);

    int update(Customer customer);

    int delete(int id);
}
