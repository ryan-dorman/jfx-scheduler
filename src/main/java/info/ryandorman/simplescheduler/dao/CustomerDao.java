package info.ryandorman.simplescheduler.dao;

import info.ryandorman.simplescheduler.model.Customer;

import java.util.List;

public interface CustomerDao {
    public List<Customer> getAll();

    public Customer getById(int id);

    public Customer create(Customer customer);

    public Customer update(Customer customer);

    public Customer delete(Customer customer);
}
